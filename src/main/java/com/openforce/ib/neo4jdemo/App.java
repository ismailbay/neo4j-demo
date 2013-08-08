package com.openforce.ib.neo4jdemo;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ismail
 * Date: 8/8/13
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("starting the neo4-demo");


        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("neo4j-demo.db");
        registerShutdownHook(graphDb);

        DataGenerator.generate(graphDb);




    }

    /**
     * register a shutdown hook for nicely shutting down the neo4j when the VM exists
     *
     * @param graphDb
     */
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
