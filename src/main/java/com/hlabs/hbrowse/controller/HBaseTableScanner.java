package com.hlabs.hbrowse.controller;

import com.hlabs.hbrowse.config.HBaseManager;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseTableScanner {

	private static HTablePool pool;

	static {  
		pool = new HTablePool(HBaseManager.hbaseConf, 10);
	}  


	public static JSONObject scanTables(String TableName, String columnFamilyName) throws IOException{

		HTableInterface table = pool.getTable(TableName);

		JSONObject rowObj = new JSONObject();
		JSONObject colObj = new JSONObject();

		try {
			Scan scan = new Scan();
//            Scan.setMaxResultSize(5);

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

	public static void insert_into_Table(String tableName, String coln_family, String coln_qualifier, String row_key, String coln_value) throws IOException{

        HTable table = new HTable(HBaseManager.hbaseConf,tableName);

        //Check whether the Column name is present
        if (!table.getTableDescriptor().hasFamily(Bytes.toBytes(coln_family)) ){
            System.out.println(coln_family + "(ColumnFamily) is not available in Table:" + tableName);
        }

        Put put = new Put(Bytes.toBytes(row_key));
        put.add(Bytes.toBytes(coln_family), Bytes.toBytes(coln_qualifier), Bytes.toBytes(coln_value));
        table.put(put);
        table.close();
    }

    /**
     * Delete a row
     */
    public static void delRecord(String tableName, String rowKey)
            throws IOException {
        HTable table = new HTable(HBaseManager.hbaseConf,tableName);
        List<Delete> list = new ArrayList<Delete>();
        Delete del = new Delete(rowKey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("del recored " + rowKey + " ok.");
    }


}

