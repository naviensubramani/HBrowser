package com.hlabs.hbrowse.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/26/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

public class HBaseManager {

    public static Configuration hbaseConf = null;
    public static HBaseAdmin hbaseAdmin = null;

    public HBaseManager(String newConf) {

        hbaseConf = getConfiguration();

    }

    public HBaseManager() {

        if (hbaseConf == null) {
            hbaseConf = getConfiguration();
        }
    }

    /**
     * @return new HBaseConfiguration
     */
    private Configuration getConfiguration() {

        Configuration config = HBaseConfiguration.create();
        config.clear();

        config.set("hbase.zookeeper.quorum", HbaseConfig.getHBASE_ZOOKEEPER_QUORUM());
        config.set("hbase.zookeeper.property.clientPort", HbaseConfig.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());
        config.set("hbase.master", HbaseConfig.getHBASE_MASTER());

        try {
            hbaseAdmin = new HBaseAdmin(config);
        }
        catch (MasterNotRunningException e) {

            HbaseConfig.SERVER_ERROR = true;

        }
        catch (ZooKeeperConnectionException e) {
            HbaseConfig.SERVER_ERROR = true;
        }
        catch (Exception e) {
            HbaseConfig.SERVER_ERROR = true;
        }
        return config;

    }

    public static Configuration getHbaseConf() {

        return hbaseConf;
    }

    public static HBaseAdmin getHbaseAdmin() {

        return hbaseAdmin;

    }

    public static void setHbaseConf(HBaseConfiguration hbaseConf) {

        HBaseManager.hbaseConf = hbaseConf;
    }

}