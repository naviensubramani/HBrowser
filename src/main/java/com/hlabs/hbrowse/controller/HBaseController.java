package com.hlabs.hbrowse.controller;

import com.hlabs.hbrowse.config.AppConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class HBaseController {
	
	public static Configuration configuration;
    private static HTablePool pool;

    static {  
    	AppConfig appConfig = new AppConfig();
        configuration = HBaseConfiguration.create();  
        configuration.set("hbase.zookeeper.property.clientPort",appConfig.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());
        configuration.set("hbase.zookeeper.quorum", appConfig.getHBASE_ZOOKEEPER_QUORUM());
      //  configuration.set("hbase.zookeeper.property.clientPort", "2181");
        //configuration.set("hbase.zookeeper.quorum", "localhost");

        // Without pooling, the connection to a table will be reinitialized.
        // Creating a new connection to a table might take up to 5-10 seconds!
        pool = new HTablePool(configuration, 10);
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

    public static JSONObject getAllTableNames() {

        JSONObject tblObj = new JSONObject();
        JSONArray tblAry = new JSONArray();

        try {
            HBaseAdmin admin = new HBaseAdmin(configuration);
            HTableDescriptor[] tables = admin.listTables();
            int i = 0;
            for (HTableDescriptor tbl : tables) {
                tblAry.add(tbl.getNameAsString());
                i++;
            }
        }
        catch (IOException e) {
            System.out.print(e);
        }
        System.out.println(tblAry);
        tblObj.put("TableNames",tblAry);
        return tblObj;
    }

    public String[] getColFamilies(String TableName) {
        String[] families = null;
        try {
            HTable userTable = new HTable(configuration, TableName);


            Set<byte[]> familySet = userTable.getTableDescriptor().getFamiliesKeys();

            Object[] allFamiles = familySet.toArray();
            families = new String[allFamiles.length];
            int i = 0;
            for (Object family2 : allFamiles) {
                byte[] family = (byte[]) family2;
                // userTable.getTableDescriptor().getFamily(family).getMaxVersions();
                families[i] = Bytes.toString(family);
                System.out.println(families[i]);
                i++;
            }

        }
        catch (IOException e) {
            System.out.println("Exception :" + e);
        }

        return families;
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

