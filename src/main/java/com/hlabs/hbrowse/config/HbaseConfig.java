package com.hlabs.hbrowse.config;

public final class HbaseConfig {

	private static String SPARK_SERVER_PORT;
	private static String HBASE_ZOOKEEPER_QUORUM;
	private static String HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;
	private static String HBASE_MASTER;

    public static boolean SERVER_ERROR = false;
    public static boolean SERVER_NOT_CONNECTED = true;
	

	public static String getSPARK_SERVER_PORT() {
		return HbaseConfig.SPARK_SERVER_PORT;
	}

	public static void setSPARK_SERVER_PORT(String spark_server_port) {
		HbaseConfig.SPARK_SERVER_PORT = spark_server_port;
	}

	public static String getHBASE_ZOOKEEPER_QUORUM() {
		return HbaseConfig.HBASE_ZOOKEEPER_QUORUM;
	}

	public static void setHBASE_ZOOKEEPER_QUORUM(String hbase_zookeeper_quorum) {
        HbaseConfig.HBASE_ZOOKEEPER_QUORUM = hbase_zookeeper_quorum;
	}

	public static String getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT() {
		return HbaseConfig.HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;
	}

	public static void setHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT(
			String hbase_zookeeper_property_client_port) {
        HbaseConfig.HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT = hbase_zookeeper_property_client_port;
	}

	public static String getHBASE_MASTER() {
		return HbaseConfig.HBASE_MASTER;
	}

	public static void setHBASE_MASTER(String hbase_master) {
        HbaseConfig.HBASE_MASTER = hbase_master;
	}

    public static void clearAllConfigs() {

        setHBASE_MASTER("");
        setHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT("");
        setHBASE_ZOOKEEPER_QUORUM("");
    }
}
