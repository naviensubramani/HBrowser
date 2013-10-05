package com.hlabs.hbrowse.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class AppConfig {

	private static String SPARK_SERVER_PORT = "8080";
	private static String HBASE_ZOOKEEPER_QUORUM;
	private static String HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;
	private static String HBASE_MASTER; 
    
	

	public static String getSPARK_SERVER_PORT() {
		return SPARK_SERVER_PORT;
	}

	public static void setSPARK_SERVER_PORT(String spark_server_port) {
		SPARK_SERVER_PORT = spark_server_port;
	}

	public static String getHBASE_ZOOKEEPER_QUORUM() {
		return HBASE_ZOOKEEPER_QUORUM;
	}

	public static void setHBASE_ZOOKEEPER_QUORUM(String hbase_zookeeper_quorum) {
		HBASE_ZOOKEEPER_QUORUM = hbase_zookeeper_quorum;
	}

	public static String getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT() {
		return HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;
	}

	public static void setHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT(
			String hbase_zookeeper_property_client_port) {
		HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT = hbase_zookeeper_property_client_port;
	}

	public static String getHBASE_MASTER() {
		return HBASE_MASTER;
	}

	public static void setHBASE_MASTER(String hbase_master) {
		HBASE_MASTER = hbase_master;
	}
}
