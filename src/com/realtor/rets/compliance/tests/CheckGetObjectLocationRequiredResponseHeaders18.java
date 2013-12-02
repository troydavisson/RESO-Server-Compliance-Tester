/*
 * CheckGetObjectOptionalResponse Headers
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.TestReport;
import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.*;

import java.net.URL;
import java.util.*;

import org.realtor.rets.retsapi.*;
import org.w3c.dom.*;

/**
 * Extends the TestEvaluator Interface (extends BaseEvaluator) Check for
 * require response headers, may be extended to check for  optional response
 * headers.
 *
 * @author pobrien
 */
public class CheckGetObjectLocationRequiredResponseHeaders18 extends BaseEvaluator {

  /** Optional response header fields */

    	

  /**
   * Creates a new instance of CheckResponseHeaders
   */
  public CheckGetObjectLocationRequiredResponseHeaders18() {
  }


protected TestResult processResults(String transName,RETSTransaction t) {
      if (t == null) {
        return reportResult("checkResponseHeaders",
                                              "Transaction \"" + transName
                                                + "\" is null, check the transaction definition",
                                              "Failure",
                                              "Transaction could not be constructed");
      } else {
        HashMap responseHeaders = t.getResponseHeaderMap();
        return checkOptionalResponseHeaders(transName, responseHeaders);

      }

  }

  /**
   * Checks optional response headers, only reporting status of Info if any are
   * missing
   *
   *
   * @param transactionName Name of the transaction for reporting
   * @param map Map of the Response headers.
   */
  protected TestResult checkOptionalResponseHeaders(String transactionName,
                                              Map map) {
    Document document = null;
    String status = "Success";
    String notes = "Fields found : ";
    String missingOptionalFields = "";

    int missingCount = 0;
    int foundCount = 0;


    Map headerKeys=CollectionUtils.copyLowerCaseMap(map);
    String location = headerKeys.get("location").toString();

      if (location == null) {
              return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", the server supports location but response is missing the location header",
                                       "Failure", notes);
      } 
      else if (location != null) {
    	  try {
    		  URL aURL = new URL(location);
    	  } catch (Exception e){
    		  return reportResult("checkOptionalResponseHeaders",
                      "Transaction \"" + transactionName
                        + "\", the location header is present but is not a proper URL "+location,
                         "Failure", notes);
    	  }
      
      }return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" Location found and is a correct URL: "+location,
                                      "Success", notes);
    
  }



}