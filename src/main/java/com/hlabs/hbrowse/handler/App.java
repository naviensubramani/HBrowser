package com.hlabs.hbrowse.handler;

/**
  * User: naveen
 * Date: 10/2/13
 * Time: 11:02 AM
 */
import com.hlabs.hbrowse.config.HBaseManager;
import com.hlabs.hbrowse.config.HbaseConfig;
import com.hlabs.hbrowse.controller.HBaseController;
import com.hlabs.hbrowse.controller.HbaseTableManager;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.json.simple.JSONObject;
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
    private static HBaseManager hBaseConfigManager;

    public static void main(String[] args) throws IOException {
        new App(args);
    }

    public App (String[] args) throws IOException {
        cfg = createFreemarkerConfiguration();
        staticFileLocation("content");
        int serverPort = 8082;
        if (args.length > 0) {
            try {
                serverPort = Integer.parseInt(args[0]);
                System.out.println("Setting Spark Server port ::: "+serverPort);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + " must be an integer");
                System.exit(1);
            }
        }
        setPort(serverPort);
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


        // Save Hbase Configuration Details
        post(new Route("/saveConfig") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                JSONObject conn = (JSONObject) HBaseController.get_Value(data,"conn");
                App.setConfig(conn);

                return  "Sucessfully Saved Configuration";
            }
        });

        // used to query user tables
        post(new Route("/listTablesNames") {
            @Override
            public Object handle(Request request, Response response) {

                return HbaseTableManager.getAllTableNames();

            }
        });


        post(new Route("/createTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.create(data);
            }
        });


        post(new Route("/dropTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.drop(data);
            }
        });


        post(new Route("/scanTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.scan(data);
            }
        });

        post(new Route("/insertTable") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.insert(data);
            }
        });
//
        // used to query user tables
        post(new Route("/getCF") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.get_ColumnFamilies(data);

            }
        });

        post(new Route("/deleteRec") {
            @Override
            public Object handle(Request request, Response response) {
                String data = request.queryParams("data");

                return HBaseController.delete_rec(data);
            }
        });

    }


    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(App.class, "/templates");
        return retVal;
    }


    private static void setConfig(JSONObject conn) {
        clearConfig();
        System.out.println("Save the config details");
        try {
            HbaseConfig.setHBASE_ZOOKEEPER_QUORUM((String) conn.get("zkQuorum"));
            HbaseConfig.setHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT((String) conn.get("zkPort"));
            HbaseConfig.setHBASE_MASTER("localhost");

        if (hBaseConfigManager == null) {
            System.out.println("null creating config object");
            hBaseConfigManager = new HBaseManager();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clearConfig() {
        try {
            if(hBaseConfigManager != null){
            HbaseConfig.clearAllConfigs();
            hBaseConfigManager = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
