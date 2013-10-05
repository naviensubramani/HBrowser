package com.hlabs.hbrowse.handler;

/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/2/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */

import freemarker.template.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import static spark.Spark.*;

import com.hlabs.hbrowse.controller.HBaseController;
import com.hlabs.hbrowse.config.AppConfig;



public class App {

    public static void main(String[] args) throws IOException {
    	
        setPort(8080);
        final Configuration cfg = configureFreemarker();
        

        get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {

                //freemarker needs a Writer to render the final Html code
                StringWriter sw = new StringWriter();

                //params used in the template files
                //passed the sublayout filename and the title page
                HashMap params = getPageParams("home.ftl", "Home page");
                try {

                    //template engine processing
                    cfg.getTemplate("main.ftl").process(params, sw);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //return the rendered html code
                return sw.toString();
            }
        });

        post(new Route("/get_result") {
            @Override
            public Object handle(Request request, Response response) {
                String userQuery = request.queryParams("qu");

                return "Hello World: " +  userQuery;
            }
        });


        post(new Route("/createTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                JSONParser parser = new JSONParser();
                try {

                    Object obj = parser.parse(data);

                    JSONObject dataObject = (JSONObject) obj;

                    JSONObject conn = (JSONObject) dataObject.get("conn");
                    AppConfig appCfg = configureHBase(conn);

                    String tableName = (String) dataObject.get("table_name");
                    System.out.println(tableName);

                    JSONArray columnFamily = (JSONArray) dataObject.get("column_family");

                    HBaseController hr = new HBaseController();
                    hr.create_Table(tableName,columnFamily);

                    return "Successfully created table "+tableName;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "Unable to create table";
                }
            }
        });


        post(new Route("/dropTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                JSONParser parser = new JSONParser();
                try {

                    Object obj = parser.parse(data);

                    JSONObject dataObject = (JSONObject) obj;

                    JSONObject conn = (JSONObject) dataObject.get("conn");
                    AppConfig appCfg = configureHBase(conn);

                    String tableName = (String) dataObject.get("table_name");
                    System.out.println(tableName);

                    HBaseController hr = new HBaseController();

                    hr.drop_Table(tableName);

                    return "Successfully Deleted table "+tableName;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "Unable to drop table";
                }
            }
        });


    }

    private static Configuration configureFreemarker() {
        Configuration cfg = new Configuration();

        try {

            //indicates the templates directory to freemarker
            cfg.setDirectoryForTemplateLoading(new File("templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfg;
    }

    private static AppConfig configureHBase(JSONObject conn) {
        AppConfig appConfig = new AppConfig();

        try {

            appConfig.setHBASE_ZOOKEEPER_QUORUM((String) conn.get("zkQuorum"));
            appConfig.setHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT((String) conn.get("zkPort"));
            appConfig.setHBASE_MASTER("localhost");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return appConfig;
    }



    //uses to create a Hashmap with specific keys
    private static HashMap getPageParams(String page, String title) {
        HashMap params = new HashMap();

        //page and title from main.ftl
        params.put("page", "pages/" + page);
        params.put("title", title);
        return params;
    }

}

