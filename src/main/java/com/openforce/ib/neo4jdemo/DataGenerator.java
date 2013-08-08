package com.openforce.ib.neo4jdemo;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ismail
 * Date: 8/8/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DataGenerator.class);

    public static void generate(GraphDatabaseService graphDb) {
        LOG.info("starting to generate the data ...");


        Transaction tx = graphDb.beginTx();

        try {
            for (String p : persons) {
                Node n = graphDb.createNode();
                n.setProperty("entity_type", "person");
                n.setProperty("name", p);

                LOG.info("created new person {}", n);
            }

            for (String t : technologies) {
                Node n = graphDb.createNode();
                n.setProperty("entity_type", "technology");
                n.setProperty("name", t);

                LOG.info("created new technology {}", t);
            }


            tx.success();
        } finally {
            tx.finish();
        }

        LOG.info("generating finished");

    }


    private static String[] persons = new String[] {"Ismail", "Velko", "Michael", "Rene", "Bernhard", "Gerhard", "Pierre", "Agatha"};
    private static String[] technologies = new String[] {"Java", "Scala", "Play", "NoSQL", "SQL", "Unix", "Office"};

    private static enum RelTypes {
        KNOWS,
        GETS_ALONG
    }

}
