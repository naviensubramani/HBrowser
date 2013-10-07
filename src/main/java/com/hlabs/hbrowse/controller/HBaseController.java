package com.hlabs.hbrowse.controller;

import com.hlabs.hbrowse.config.AppConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

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


    public static void drop_Table(String tableName) {
        System.out.println("Dropping table ......"+tableName);
        try {
            HBaseAdmin admin = new HBaseAdmin(configuration);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" Successfully Dropped table ......"+tableName);

    }

    public static String list_Tables() {
        System.out.println("List table ......");
        String tableList = "";
        try {
            HBaseAdmin admin = new HBaseAdmin(configuration);
            tableList =  Arrays.toString(admin.listTables());
//            admin.listTables();
            System.out.println(tableList);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" Successfully Listed table ......");
        return (tableList);
    }


}

