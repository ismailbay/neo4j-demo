package com.openforce.ib.neo4jdemo;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 * @author Ismail Bay <ismail.bay@openforce.com>
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("starting the neo4-demo");

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("neo4j-demo.db");
        registerShutdownHook(graphDb);

        DataGenerator.generate(graphDb);

        // all person knowing SQL, Java with work efficiency > 70
        Transaction tx = graphDb.beginTx();

        ExecutionEngine engine = new ExecutionEngine(graphDb);

        LOG.info("People knowing SQL > 30 and Java > 60:");
        ExecutionResult result = engine.execute(QUERY1);
        for (Map<String, Object> row : result) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                Node person = (Node) entry.getValue();
                LOG.info("\t{}", person.getProperty("name"));
            }
        }

        LOG.info("Best coworkers (> 80):");
        result = engine.execute(QUERY2);
        LOG.info(result.dumpToString());

        tx.success();
        tx.finish();


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

    private static final String QUERY1 =
            "START n = node(*) " +
            " MATCH p=java<-[r1:KNOWS]-n-[r2:KNOWS]->sql" +
            " WHERE r1.grade > 60 AND r2.grade > 30 AND sql.name = 'SQL' AND java.name = 'Java'" +
            " RETURN n";

    private static final String QUERY2 =
            "START n = node(*)" +
            " MATCH p=java<-[r1:KNOWS]-n-[r2:KNOWS]->sql, n-[r3:WORKS_EFFICIENTLY]-m" +
            " WHERE r1.grade >= 60 AND r2.grade >= 30 AND sql.name = 'SQL' AND java.name = 'Java' AND r3.efficiency > 80" +
            " RETURN DISTINCT n.name as worker, collect(m.name) as coworkers";


}
