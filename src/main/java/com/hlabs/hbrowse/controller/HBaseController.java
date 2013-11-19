package com.hlabs.hbrowse.controller;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
		String RowKey = HBaseController.get_Value(data,"row_key").toString();
		data = HBaseController.get_Value(data,"values").toString();
		
		System.out.println(tableName + ":" + columnFamily + ":" + RowKey + ":" + data);
		
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper om = new ObjectMapper();
		
		try {
			map = om.readValue(data, new TypeReference<Map<String, String>>(){});
			System.out.println(map);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int i=0;
		String[] columnQualifier = new String[map.size()];
		String[] RowValue = new String[map.size()];
		for(Entry<String, String> e : map.entrySet()){
			columnQualifier[i] = e.getKey();
			RowValue[i] = e.getValue();
			i++;
		}

		try {
			HBaseTableScanner.addRecord(tableName, columnFamily, columnQualifier, RowKey, RowValue);
		}
		catch (IOException e) {
			e.printStackTrace();
			return "Unable to insert into table";
		}
		return "Sucessfully Inserted into table "+tableName;
	}

    public static String delete_rec(String data){
        System.out.println("delete record");
        String tableName = HBaseController.get_Value(data,"table_name").toString();
        String rowKey = HBaseController.get_Value(data,"row_key").toString();
        try {
            HBaseTableScanner.delRecord(tableName,rowKey);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Unable to delete record";
        }
        return "Sucessfully deleted "+rowKey;
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

