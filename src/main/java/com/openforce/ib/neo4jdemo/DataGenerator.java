package com.openforce.ib.neo4jdemo;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 *
 * @author Ismail Bay <ismail.bay@openforce.com>
 */
public class DataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DataGenerator.class);

    public static void generate(GraphDatabaseService graphDb) {
        LOG.info("starting to generate the data ...");

        Random random = new Random(System.currentTimeMillis());


        // used for cypher QL
        ExecutionEngine engine = new ExecutionEngine(graphDb);

        Transaction tx = graphDb.beginTx();

        // delete all nodes
        engine.execute("start n=node(*) match n-[r?]->() delete n, r");


        try {
            ////////////////////////////
            // create nodes
            ////////////////////////////
            for (String p : persons) {
                Node n = graphDb.createNode();
                n.setProperty("entity_type", "person");
                n.setProperty("name", p);

                LOG.info("created new person {}", p);
            }

            for (String t : technologies) {
                Node n = graphDb.createNode();
                n.setProperty("entity_type", "technology");
                n.setProperty("name", t);

                LOG.info("created new technology {}", t);
            }

            ////////////////////////////
            // create relations between persons and technologies
            ////////////////////////////
            ExecutionResult resultPersons = engine.execute("start n=node(*) where n.entity_type = 'person' return n");

            for (Map<String, Object> row : resultPersons) {
                for (Map.Entry<String, Object> p : row.entrySet()) {
                    Node person = (Node) p.getValue();

                    ExecutionResult resultTechs = engine.execute("start n=node(*) where n.entity_type = 'technology' return n");
                    for (Map<String, Object> row2 : resultTechs) {
                        for (Map.Entry<String, Object> t : row2.entrySet()) {
                            Node tech = (Node) t.getValue();

                            if (random.nextInt() % 4 != 0) {
                                int grade = random.nextInt(71) + 30;
                                LOG.info("{} knows {}, grade: {}", new Object[] { person.getProperty("name"), tech.getProperty("name"), grade});

                                Relationship rel = person.createRelationshipTo(tech, RelTypes.KNOWS);
                                rel.setProperty("grade", grade);
                            }
                        }
                    }
                }
            }

            ////////////////////////////
            // create relations between persons
            ////////////////////////////

            resultPersons = engine.execute("start n=node(*) where n.entity_type = 'person' return n");

            for (Map<String, Object> row : resultPersons) {
                for (Map.Entry<String, Object> p : row.entrySet()) {
                    Node person = (Node) p.getValue();

                    ExecutionResult otherPersonsResult = engine.execute("start n=node(*) where n.entity_type = 'person' and not(n.name = '" + person.getProperty("name") + "') return n");
                    for (Map<String, Object> row2 : otherPersonsResult) {
                        for (Map.Entry<String, Object> o : row2.entrySet()) {
                            Node other = (Node) o.getValue();
                            int workEfficiency = random.nextInt(51) + 50;
                            LOG.info("work efficiency between {} and {} is {}",
                                    new Object[] {person.getProperty("name"), other.getProperty("name"), workEfficiency});

                            Relationship rel = person.createRelationshipTo(other, RelTypes.WORKS_EFFICIENTLY);
                            rel.setProperty("efficiency", workEfficiency);
                        }

                    }
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }

        LOG.info("generating finished");
    }


    private static String[] persons = new String[] {"Ismail", "Velko", "Michael", "Rene", "Bernhard", "Gerhard", "Pierre", "Matthias", "Martin", "Pawel", "Christian", "Alex"};
    private static String[] technologies = new String[] {"Java", "Scala", "Play", "NoSQL", "SQL", "Unix"};



}
