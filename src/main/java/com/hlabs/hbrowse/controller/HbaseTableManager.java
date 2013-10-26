package com.hlabs.hbrowse.controller;

import com.hlabs.hbrowse.config.HBaseManager;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class HbaseTableManager {

//    private static HBaseManager hBaseConfigManager;

	public static JSONObject getAllTableNames() {

		JSONObject tblObj = new JSONObject();
		JSONArray tblAry = new JSONArray();

		try {
			HTableDescriptor[] tables = HBaseManager.getHbaseAdmin().listTables();
			int i = 0;
			for (HTableDescriptor tbl : tables) {
				tblAry.add(tbl.getNameAsString());
				i++;
			}
		}
		catch (IOException e) {
			System.out.print(e);
		}
		tblObj.put("TableNames",tblAry);
		return tblObj;
	}


    @SuppressWarnings("unchecked")
    public static JSONArray getColFamilies(String TableName) throws IOException {

        HTable table = new HTable(HBaseManager.hbaseConf,TableName);

        JSONArray coln_array = new JSONArray();

        HColumnDescriptor[] coln_desc = table.getTableDescriptor().getColumnFamilies();

        for (HColumnDescriptor HCD : coln_desc ){
            coln_array.add(HCD.getNameAsString());
        }
        return coln_array;

    }

    @SuppressWarnings("unchecked")
    public static String create_Table(String tableName,JSONArray columnFamily) {
        System.out.println("start create table ......");
        try {
            HBaseAdmin hBaseAdmin = HBaseManager.getHbaseAdmin();
            if (hBaseAdmin.tableExists(tableName)) {
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
                System.out.println(tableName + "already exist,deleting ....");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            Iterator<String> iterator = columnFamily.iterator();
            while (iterator.hasNext()) {
                String colName = iterator.next();
                tableDescriptor.addFamily(new HColumnDescriptor(colName));
            }

            hBaseAdmin.createTable(tableDescriptor);
            return "Sucessfully Created Table";
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Create table ......"+tableName);
        return "Unable to Created Table";
    }


    public static void drop_Table(String tableName) {
        System.out.println("Dropping table ......"+tableName);
        try {
            HBaseAdmin admin = HBaseManager.getHbaseAdmin();
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

}

