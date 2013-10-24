package com.hlabs.hbrowse.handler;

/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/2/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */

import com.hlabs.hbrowse.config.AppConfig;
import com.hlabs.hbrowse.controller.HBaseController;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import static spark.Spark.*;

/**
 *
 */
public class App {
    private final Configuration cfg;

    public static void main(String[] args) throws IOException {
        new App();
    }

    public App() throws IOException {
        cfg = createFreemarkerConfiguration();
        setPort(8082);
        initializeRoutes();
    }

    abstract class FreemarkerBasedRoute extends Route {
        final Template template;

        /**
         * Constructor
         *
         * @param path The route path which is used for matching. (e.g. /login, users/:name)
         */
        protected FreemarkerBasedRoute(final String path, final String templateName) throws IOException {
            super(path);
            template = cfg.getTemplate(templateName);
        }

        @Override
        public Object handle(Request request, Response response) {
            StringWriter writer = new StringWriter();
            try {
                doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        protected abstract void doHandle(final Request request, final Response response, final Writer writer)
                throws IOException, TemplateException;

    }

    private void initializeRoutes() throws IOException {
        // this is the HBrowser home page
        get(new FreemarkerBasedRoute("/", "home.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                HashMap<String, String> root = new HashMap<String, String>();

                template.process(root, writer);
            }
        });

        // used to process internal errors
        get(new FreemarkerBasedRoute("/internal_error", "error_template.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("error", "System has encountered an error.");
                template.process(root, writer);
            }
        });


        // used to query user tables
        post(new Route("/listTablesNames") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                JSONParser parser = new JSONParser();

                try {

                    Object obj = parser.parse(data);

                    JSONObject dataObject = (JSONObject) obj;

                    JSONObject conn = (JSONObject) dataObject.get("conn");
                    AppConfig appCfg = configureHBase(conn);

                    HBaseController hr = new HBaseController();

                    JSONObject tObj = hr.getAllTableNames();

                    return  tObj;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "Unable to list tables ";
                }
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


    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(App.class, "/templates");
        return retVal;
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
}
