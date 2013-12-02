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
public class CheckGetObjectOptionalResponseHeaders extends BaseEvaluator {

  /** Optional response header fields */

    	String[] optionalFields = {
        "location","description"
  };

  /**
   * Creates a new instance of CheckResponseHeaders
   */
  public CheckGetObjectOptionalResponseHeaders() {
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


    for (int i = 0; i < optionalFields.length; i++) {
      if (headerKeys.get(optionalFields[i]) == null) {
        if (missingCount++ == 0) {
          missingOptionalFields = optionalFields[i];
        } else {
          missingOptionalFields = missingOptionalFields + ", "
                                  + optionalFields[i];
        }
      } else {
        if (foundCount++ > 0) {
          notes = notes + ", " + optionalFields[i];
        } else {
          notes = notes + optionalFields[i];
        }
      }
    }

    if (missingCount > 0) {
      return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", the response is missing the following OPTIONAL field(s) :"
                                      + missingOptionalFields, "Info", notes);
    } else {
      return reportResult("checkOptionalResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" All optional response fields found"
                                      + missingOptionalFields, "Success", notes);
    }
  }



}