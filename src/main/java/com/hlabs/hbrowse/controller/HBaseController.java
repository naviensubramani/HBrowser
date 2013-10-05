package com.hlabs.hbrowse.controller;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.json.simple.JSONArray;

import com.hlabs.hbrowse.config.AppConfig;

public class HBaseController {
	
	public static Configuration configuration;  
    static {  
    	AppConfig appConfig = new AppConfig();
        configuration = HBaseConfiguration.create();  
        configuration.set("hbase.zookeeper.property.clientPort",appConfig.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());
        configuration.set("hbase.zookeeper.quorum", appConfig.getHBASE_ZOOKEEPER_QUORUM());
      //  configuration.set("hbase.zookeeper.property.clientPort", "2181");  
        //configuration.set("hbase.zookeeper.quorum", "localhost");  
    }  


    public static void create_Table(String tableName,JSONArray columnFamily) {
        System.out.println("start create table ......");
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
            if (hBaseAdmin.tableExists(tableName)) {
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + "already exist,deleting ....");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            Iterator<String> iterator = columnFamily.iterator();
            while (iterator.hasNext()) {
                String colName = iterator.next();
                System.out.println(colName);
                tableDescriptor.addFamily(new HColumnDescriptor(colName));
            }

            hBaseAdmin.createTable(tableDescriptor);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Create table ......"+tableName);
    }


}

