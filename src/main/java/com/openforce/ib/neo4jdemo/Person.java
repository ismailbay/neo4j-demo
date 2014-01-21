package com.openforce.ib.neo4jdemo;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 * @author Ismail Bay <ismail.bay@openforce.com>
 */
public class Person {

    private Node underlyingNode;

    public Person(Node underlyingNode) {
        this.underlyingNode = underlyingNode;
    }

    public String getName() {
        return underlyingNode.getProperty("name").toString();
    }

    public void setName(String name) {
        underlyingNode.setProperty("name", name);
    }

    public Relationship relateTo(Person other, RelationshipType relType) {
        return null;
    }

    public Iterable<Person> getBestCoWorkers() {
        // query, order by efficiency, return
        return null;
    }
}
