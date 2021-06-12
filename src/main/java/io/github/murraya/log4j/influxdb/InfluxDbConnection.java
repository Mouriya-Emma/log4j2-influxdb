package io.github.murraya.log4j.influxdb;

import com.google.gson.Gson;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.nosql.AbstractNoSqlConnection;
import org.apache.logging.log4j.core.appender.nosql.NoSqlObject;
import java.time.Instant;
import java.util.Map;


/**
 * The InfluxDB implementation of {@link AbstractNoSqlConnection}.
 */
public final class InfluxDbConnection extends AbstractNoSqlConnection<Map<String, Object>, InfluxDbObject> {

	private final InfluxDBClient influxDBClient;
	private final WriteApi writeApi;


	public InfluxDbConnection(final InfluxDBClient influxDBClient, final WriteOptions writeOptions) {
		this.influxDBClient = influxDBClient;
		writeApi = influxDBClient.getWriteApi(writeOptions);

	}

	@Override
	public InfluxDbObject createObject() {
		return new InfluxDbObject();
	}

	@Override
	public InfluxDbObject[] createList(int length) {
		return new InfluxDbObject[length];
	}


	@Override
	public void insertObject(NoSqlObject<Map<String, Object>> noSqlObject) {
		int i = 0;
		Map<String, Object> kv = noSqlObject.unwrap();

		String measurement = "log";
		if (kv.containsKey("measurement")){
			measurement = kv.get("measurement").toString();
			kv.remove("measurement");
		}
		Point point = Point.measurement(measurement);
		if (kv.containsKey("tags")){
			Object tags = kv.get("tags");
			if (tags instanceof Map){
				kv.remove("tags");
				point.addTags((Map<String, String>) tags);
			}
		}
		if (kv.containsKey("level")){
			i = ((Level) kv.get("level")).intLevel();
			point.addTag("level", String.valueOf(i));
//			kv.remove("level");
		}

		if (kv.containsKey("loggerName")){
			Object loggerName = kv.get("loggerName");
			if (loggerName == null) {
				loggerName = "NULL";
			}
			point.addTag("loggerName", loggerName.toString());
			kv.remove("loggerName");
		}

		if (kv.containsKey("millis")){
			point.time((Long) kv.get("millis"),WritePrecision.MS);
		} else {
			point.time(Instant.now().toEpochMilli(), WritePrecision.MS);
		}

		if (kv.containsKey("message")){
			point.addTag("message", kv.get("message").toString());
			kv.remove("message");
		}

//		if (kv.containsKey("thrown")){
//			Optional.of(kv.get("thrown")).ifPresentOrElse(
//					(t) -> {
//						Throwable throwable = (Throwable) t;
//						point.addTag("thrown", throwable.toString());
//					},
//					() ->{
//						point.addTag("thrown", "NULL");
//					});
//		}

		point.addField(RandomNum.getInstance().next256String(), new Gson().toJson(kv));
		writeApi.writePoint(point);
		if (i <=200){
			writeApi.flush();
		}
	}

	@Override
	protected void closeImpl() {
		influxDBClient.close();
	}

}
