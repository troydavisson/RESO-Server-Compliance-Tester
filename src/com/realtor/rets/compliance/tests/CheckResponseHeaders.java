/*
 * CheckResponse Headers
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
public class CheckResponseHeaders extends BaseEvaluator {
  /** fields required for the response headers */
  String[] requiredFields = {
    "date", "cache-control", "content-type", "rets-version"
  };

  /** Optional response header fields */
  String[] optionalFields = { "content-length", "transfer-encoding", "server" };

  /**
   * Creates a new instance of CheckResponseHeaders
   */
  public CheckResponseHeaders() {
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
        return checkResponseHeaders(transName, responseHeaders);

        // Don't really need to report on optional stuff
        // checkOptionalResponseHeaders(testReport, transName, responseHeaders);
      }

  }

  /**
   * Checks to make sure response headers are in a transaction response
   *
   * @param transactionName Name of the Transaction for reporting
   * @param map response headers for the Transaction
   */
  protected TestResult checkResponseHeaders(String transactionName,
                                      Map map) {
    Document document = null;
    String status = "Success";
    String notes = "Fields found : ";
    String missingRequiredFields = "";

    if (map == null) {
      return reportResult("checkResponseHeaders",
                                    "There was no response map found",
                                    "Failure",
                                    "No fields found in transaction : " + transactionName);


    }

    int missingCount = 0;
    int foundCount = 0;

    Map headerKeys=CollectionUtils.copyLowerCaseMap(map);


    for (int i = 0; i < requiredFields.length; i++) {
      if (headerKeys.get(requiredFields[i]) == null) {
        if (missingCount++ == 0) {
          missingRequiredFields = requiredFields[i];
        } else {
          missingRequiredFields = missingRequiredFields + ", "
                                  + requiredFields[i];
        }
      } else {
        if (foundCount++ > 0) {
          notes = notes + ", " + requiredFields[i];
        } else {
          notes = notes + requiredFields[i];
        }
      }
    }

    if (missingCount > 0) {
      return reportResult("checkResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", the response is missing the following fields :"
                                      + missingRequiredFields, "Failure", notes);
    } else {
      return reportResult("checkResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" All required response fields found"
                                      + missingRequiredFields, "Success", notes);
    }
  }

  /**
   * Checks optional response headers, only reporting status of Info if any are
   * missing
   *
   * @param tr TestReport object to add TestResult object to
   * @param transactionName Name of the transaction for reporting
   * @param map Map of the Response headers.
   */
  protected void checkOptionalResponseHeaders(TestReport tr,
                                              String transactionName,
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
      tr.addTestResult(reportResult("checkResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", the response is missing the following OPTIONAL field(s) :"
                                      + missingOptionalFields, "Info", notes));
    } else {
      tr.addTestResult(reportResult("checkResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" All optional response fields found"
                                      + missingOptionalFields, "Success", notes));
    }
  }



}