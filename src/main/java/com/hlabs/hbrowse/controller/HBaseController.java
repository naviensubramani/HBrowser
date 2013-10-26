package com.hlabs.hbrowse.controller;

import com.hlabs.hbrowse.config.HbaseConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONObject;

import java.io.IOException;

public class HBaseController {

	public static Configuration configuration;
	private static HTablePool pool;

	static {  
		HbaseConfig hbaseConfig = new HbaseConfig();
		configuration = HBaseConfiguration.create();  
		configuration.set("hbase.zookeeper.property.clientPort", hbaseConfig.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());
		configuration.set("hbase.zookeeper.quorum", hbaseConfig.getHBASE_ZOOKEEPER_QUORUM());
		//  configuration.set("hbase.zookeeper.property.clientPort", "2181");
		//configuration.set("hbase.zookeeper.quorum", "localhost");

		// Without pooling, the connection to a table will be reinitialized.
		// Creating a new connection to a table might take up to 5-10 seconds!
		pool = new HTablePool(configuration, 10);
	}  





	public static JSONObject scanTables(String TableName, String columnFamilyName) throws IOException{

		HTableInterface table = pool.getTable(TableName);

		JSONObject rowObj = new JSONObject();
		JSONObject colObj = new JSONObject();

		try {
			Scan scan = new Scan();

			//        scan.setCaching(NUMBER_OF_ROWS_TO_CACHE);
			//If you want to get data for all families then do not add any family.
			scan.addFamily(Bytes.toBytes(columnFamilyName));
			ResultScanner scanner = table.getScanner(scan);

			// For each row
			for (Result result : scanner) {
				colObj.clear();
				for (KeyValue kv : result.raw()) {
					colObj.put(Bytes.toString(kv.getQualifier()), Bytes.toString(kv.getValue()));
				}
				rowObj.put(Bytes.toString(result.getRow()),colObj.clone());
			}
			System.out.println(rowObj);
			scanner.close();
		}
		catch (IOException e){
			System.out.print(e);
		}
		finally {
			table.close();
		}
		return rowObj;


	}

}

