package com.hlabs.hbrowse.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class HBaseController {


	public static Object scan(String data){
		System.out.println("scan tbl");

		try {
			String tableName = HBaseController.get_Value(data,"table_name").toString();
			String columnFamily = HBaseController.get_Value(data,"column_family").toString();

			return HBaseTableScanner.scanTables(tableName, columnFamily);
		}
		catch (IOException e) {
			e.printStackTrace();
			return "Unable to scan table";
		}

	}

	public static String create(String data){
		System.out.println("create tbl");
		String tableName = HBaseController.get_Value(data,"table_name").toString();
		JSONArray columnFamily = (JSONArray) HBaseController.get_Value(data,"column_family");
		HbaseTableManager.create_Table(tableName, columnFamily);
		return "Sucessfully created table "+tableName;
	}

	public static String insert(String data){
		System.out.println("Insert in to tbl");
		String tableName = HBaseController.get_Value(data,"table_name").toString();
		String columnFamily = HBaseController.get_Value(data,"column_family").toString();
		String columnQualifier = HBaseController.get_Value(data,"column_qualifier").toString();
		String RowKey = HBaseController.get_Value(data,"row_key").toString();
		String RowValue = HBaseController.get_Value(data,"column_value").toString();

		try {
			HBaseTableScanner.insert_into_Table(tableName,columnFamily,columnQualifier,RowKey,RowValue);
		}
		catch (IOException e) {
			e.printStackTrace();
			return "Unable to insert into table";
		}
		return "Sucessfully Inserted into table "+tableName;
	}

	public static String drop(String data){
		System.out.println("drop tbl");
		String tableName = HBaseController.get_Value(data,"table_name").toString();
		HbaseTableManager.drop_Table(tableName);
		return "Sucessfully dropped table "+tableName;
	}

	public static Object get_ColumnFamilies(String data){
		System.out.println("List Column Families");
		String tableName = HBaseController.get_Value(data,"table_name").toString();

		try {
			return HbaseTableManager.getColFamilies(tableName);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return "Unable to get column families for "+tableName;
	}

	public static Object get_Value(String jsonData,String key){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(jsonData);
			JSONObject dataObject = (JSONObject) obj;
			return dataObject.get(key);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return "Unable to parse given data "+jsonData;
		}
	}

	//    For Unit Testing

	//    public static void main(String args[]){
	////        String data = "{\"conn\":{\"zkQuorum\":\"localhost\",\"zkPort\":\"2181\"},\"table_name\":\"employee\",\"column_family\":\"personal1\"}";
	//        String data = "{\"conn\":{\"zkQuorum\":\"localhost\",\"zkPort\":\"2181\"},\"table_name\":\"employee\",\"column_family\":[\"personal1\",\"medical\"]}";
	//        Object obj = HBaseController.create(data);
	//
	//        System.out.println(obj.toString());
	//
	//    }


}

