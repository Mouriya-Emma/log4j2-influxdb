package io.github.murraya.log4j.influxdb;

import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteOptions;
import io.reactivex.BackpressureOverflowStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.nosql.NoSqlProvider;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.util.Strings;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The InfluxDB implementation of {@link NoSqlProvider}.
 */
@Plugin(name = "InfluxDb", category = "Core", printObject = true)
public final class InfluxDbProvider implements NoSqlProvider<InfluxDbConnection> {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final WriteOptions DEFAULT_WRITE_OPTIONS;
	private final String description;

	private static String url;
	private static String org;
	private static String bucket;
	private static Integer batchSize = 10;
	private static char[] token;

	static {
		DEFAULT_WRITE_OPTIONS = WriteOptions.builder()
				.batchSize(batchSize)
				.backpressureStrategy(BackpressureOverflowStrategy.ERROR)
				.build();
	}

	public InfluxDbProvider(String url, String org, String bucket, String token, String description) {
		InfluxDbProvider.url = url;
		InfluxDbProvider.org = org;
		InfluxDbProvider.bucket = bucket;
		InfluxDbProvider.token = token.toCharArray();
		this.description = "mongoDb{ " + description + " }";
	}

	@Override
	public InfluxDbConnection getConnection() {
		return new InfluxDbConnection(InfluxDBClientFactory.create(url,token,org,bucket),DEFAULT_WRITE_OPTIONS);
	}

	@Override
	public String toString() {
		return this.description;
	}

	@PluginFactory
	public static InfluxDbProvider createNoSqlProvider(
			@PluginAttribute("url")String url,
			@PluginAttribute("org")String org,
			@PluginAttribute("bucket")String bucket,
			@PluginAttribute("token")String token,
			@PluginAttribute("batchSize")Integer batchSize
			) {
		LOGGER.info("createInfluxDbProvider");
		String description = "";

		if (Strings.isNotEmpty(url) && Strings.isNotEmpty(org) && Strings.isNotEmpty(bucket) && Strings.isNotEmpty(token)){
			if (!isValidURL(url)){
				InfluxDbProvider.LOGGER.error("The URL is not valid!!");
				return null;
			}

			if (batchSize == null || batchSize == 0){
				InfluxDbProvider.LOGGER.warn("BatchSize in InfluxDB client write option is not set, the default value is "+batchSize+" in this appender, the log will write every 10 as a batch");
			} else {
				InfluxDbProvider.batchSize = batchSize;
			}
			description = "url=" + url;
			description += ", org=" + org;
			description += ", bucket=" + bucket;
			description += ", token=" + token;
			description += ", batchSize=" + batchSize;
			description +=  "]";

//				Other parameters can be added
			return new InfluxDbProvider(url,org,bucket,token,description);
		} else {
			InfluxDbProvider.LOGGER.error("----   Log4j2 InfluxDB connect error    ----");
			if (!Strings.isNotEmpty(url)){
				InfluxDbProvider.LOGGER.error("Cannot connect to InfluxDB, url does not exist");
				return null;}
			if (!Strings.isNotEmpty(org)){
				InfluxDbProvider.LOGGER.error("Cannot connect to InfluxDB, org does not exist");
				return null;}
			if (!Strings.isNotEmpty(bucket)){
				InfluxDbProvider.LOGGER.error("Cannot connect to InfluxDB, bucket does not exist");
				return null;}
			if (!Strings.isNotEmpty(token)){
				InfluxDbProvider.LOGGER.error("Cannot connect to InfluxDB, token does not exist");
				return null;}
			InfluxDbProvider.LOGGER.error("----  Log4j2 InfluxDB connect error end  ----");
		}
		return new InfluxDbProvider(url,org,bucket,token,description);
	}

	protected static boolean isValidURL(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			LOGGER.error(e);
		}
		try {
			assert u != null;
			u.toURI();
		} catch (URISyntaxException e) {
			LOGGER.error(e);
		}
		return true;
	}

}
