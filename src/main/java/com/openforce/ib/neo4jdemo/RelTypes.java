package com.openforce.ib.neo4jdemo;

import org.neo4j.graphdb.RelationshipType;

/**
 * @author Ismail Bay <ismail.bay@openforce.com>
 */
public enum RelTypes implements RelationshipType {
    KNOWS,
    WORKS_EFFICIENTLY
}
