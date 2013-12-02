/*
 * QueryBuilders.java
 *
 */
package com.realtor.rets.compliance.tests.util;

import java.io.*;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.realtor.rets.retsapi.*;

import org.w3c.dom.*;

/**
 * This class contains methods used to build queries and field selection lists
 *
 * @author pobrien
 */
public class QueryBuilders {
  /**
   *  Builds a property query from metadata transaction object passed as argument
   * @param trans Transaction passed
   *
   * @return DMQL query
   */
  public static String propertyQueryFromMetadata(RETSTransaction trans) {
    if ((trans == null) || (trans.getClass() != RETSGetMetadataTransaction.class)) {
      System.out.println(
          "[ERROR] propertyQueryFromMetadata : Get Metadata Transaction required ");
    }

    return "(ListPrice=500000+),(Beds=5+)";
  }

  /**
   * Returns a list of select fields from a metadata table transaction result
   * passed.  This is use to build Select lists <FieldSelectionMethod> for
   * Search transactions.
   *
   * @param trans transaction passed in, should be a getMetadataTransaction
   *
   * @return Select list of fields defined in the getMetadata Transaction
   */
  public static String getSearchQueryFieldsFromMetadata(RETSTransaction trans) {
    if ((trans == null) || (trans.getClass() != RETSGetMetadataTransaction.class)) {
      System.out.println(
          "[Failure] getSearchQueryFieldsFromMetadata : Get Metadata Transaction required ");

      return ""; // get everything
    }

    String retString = null;
    RETSGetMetadataTransaction gmt = (RETSGetMetadataTransaction) trans;
    String responseBody = (String) gmt.getResponseMap().get("body");

    return getFieldsFromMetadataString(responseBody);
  }

  /**
   * Private method that builds a DOM document from the body of a getMetadaTransaction
   * and returns system names from the metadata seperated by commas ",".
   *
   * @param mdString body of a get metadata table transaction
   *
   * @return comma seperated list of system names
   */
  private static String getFieldsFromMetadataString(String mdString) {
    String retString = null;
    HashMap fieldHash = new HashMap();

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      ByteArrayInputStream bais = new ByteArrayInputStream(mdString.getBytes());

      Document doc = builder.parse(bais);

      NodeList nl = doc.getElementsByTagName("Field");

      for (int i = 0; i < nl.getLength(); i++) {
        NodeList childNodes = nl.item(i).getChildNodes();
        String systemName = "";
        String dataType = "";

        for (int k = 0; k < childNodes.getLength(); k++) {
          Node node = childNodes.item(k);

          // if the node is a transaction, add it to the list of transactions
          if (node.getNodeName().equalsIgnoreCase("SystemName")) {
            systemName = getFirstChildValue(node);
          } else if (node.getNodeName().equalsIgnoreCase("DataType")) {
            dataType = getFirstChildValue(node);
          }
        }

        fieldHash.put(systemName, dataType);
      }
    } catch (Exception e) {
      //e.printStackTrace();
      return null;
    }

    Iterator itr = fieldHash.keySet().iterator();

    while (itr.hasNext()) {
      if (retString == null) {
        retString = (String) itr.next();
      } else {
        retString = retString + "," + (String) itr.next();
      }
    }

    return retString;
  }

  /**
   * Helper method to get values out of DOM documents
   *
   * @param node xml node you want the first text value from
   *
   * @return text value of the first child of the node passed
   */
  private static String getFirstChildValue(Node node) {
    Node firstChild = node.getFirstChild();

    if (firstChild != null) {
      return firstChild.getNodeValue();
    }

    return null;
  }
}