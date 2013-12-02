package com.realtor.rets.compliance.tests.util;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * @author pobrien
 */
public class HNode
{
    private String name = null;
    private String type = null;
    private String qualifiedClass = null;

    private Collection children = null;

    public HNode() {
        super();
    }

    public HNode(String name) {
        this();
        this.setName(name);
    }

    public void addChild(Object object) {
        Collection collection = getChildren();
        if ( collection == null ) {
            collection = new ArrayList();
            setChildren(collection);
        }
        collection.add(object);
    }

    public void removeChild(Object object) {
        Collection collection = getChildren();
        if ( collection != null && object != null ) {
            collection.remove(object);
        }
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQualifiedClass() {
        return qualifiedClass;
    }

    public void setQualifiedClass(String qualifiedClass) {
        this.qualifiedClass = qualifiedClass;
    }

    public String toString() {
        if ( getChildren() != null ) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(name + " (" + type + ")\n");
            Iterator iterator = getChildren().iterator();
            while ( iterator.hasNext() ) {
                stringBuffer.append(iterator.next().toString() + "\n");
            }
            return stringBuffer.toString();

        } else {
            return name + " (" + type + ")";
        }
    }
}
