/*
 * CheckGetObjectOptionalResponse Headers
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.TestReport;

import com.realtor.rets.compliance.TestResult;

import com.realtor.rets.compliance.tests.util.*;

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
public class CheckRetsVersion18 extends BaseEvaluator {

  
  /**
   * Creates a new instance of CheckRetsVersion
   */
  public CheckRetsVersion18() {
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
        return checkVersion(transName, responseHeaders);

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
  protected TestResult checkVersion(String transactionName,
                                              Map map) {
    Document document = null;
    String status = "Success";
    String notes = "RETS version : ";
    

    Map headerKeys=CollectionUtils.copyLowerCaseMap(map);
    String version = headerKeys.get("rets-version").toString();
    notes = notes+version;
        if (version.equals("RETS/1.8")||version.equals("RETS/1.8.0")) {
      return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", The response contains the right RETS version :"
                                      + version, "Success", notes);
    } else {
      return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" Incorrect RETS version: "
                                      + version, "Failure", notes);
    }
  }



}