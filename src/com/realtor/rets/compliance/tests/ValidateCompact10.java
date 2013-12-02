/*
 * Test1.java
 *
 */
package com.realtor.rets.compliance.tests;

import java.util.Map;

import org.realtor.rets.retsapi.RETSTransaction;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;

/**
 * Extends the BaseEvaluator Interface to validate against a DTD
 *
 * @author pobrien
 */
public class ValidateCompact10 extends ValidateCompact {

  public ValidateCompact10() {
    super();
    specReference = "";
  }

private String notes="";
private String docTypeLocation="";
boolean error=false;
private String errorList="";
  /**
   * Evaluate a set of transactions. This method is called by the TestExecuter
   * Checks the XML response against the RETS DTD
   *
   * @param trans Hash containing all transactions defined in a test script
   * @param testReport Report to which to add individual testResult objects.
   *
   * @return just returns an empty string for now.
   */
  protected TestResult processResults(String transName, RETSTransaction t) {
      if (t == null) {
        return null;
      }

      Map resp = t.getResponseMap();
      String cName = t.getClass().getName();

      Map m = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
	  boolean isText = CollectionUtils.hasValue(m,"content-type","text/plain");
      int cnt=CollectionUtils.keyCount(m,"content-type");


        if (cnt > 1)
        {
            String mContent = "Multiple Content-Type's are reported the response for transaction : "+
                               transName;
            String note = "Some clients may fail or be confused if multiple 'Content-Type' values returned ";
            return reportResult("Validate Compact10", mContent,"Info",note,"n/a");

        }




        if(!isText){
    	    notes+= "Transaction " + cName
            + " does not include a Content-Type of 'text/plain'";
		}
         String body = (String) resp.get("body");
		 return validateCompact(transName,body);




  }



}
