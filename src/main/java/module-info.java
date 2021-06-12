module log4j.influxdb {
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires influxdb.client.java;
    requires gson;
    requires io.reactivex.rxjava2;
}