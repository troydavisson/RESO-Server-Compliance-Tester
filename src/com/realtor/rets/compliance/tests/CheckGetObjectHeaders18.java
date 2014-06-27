/*
 * CheckResponse Headers
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;
import org.realtor.rets.retsapi.RETSTransaction;
import org.w3c.dom.Document;

import java.util.Map;

/**
 * Extends the TestEvaluator Interface (extends BaseEvaluator) Check for
 * required response headers specific to GetObject
 *
 * @author pobrien
 */
public class CheckGetObjectHeaders18 extends BaseEvaluator {
  /** fields required for the response headers */

  /* String[] requiredFields = {
    "content-id", "object-id", "mime-version"
  };
  */

	String[] requiredFields = {
    "content-type","mime-version","content-id","object-id"
  };



  /**
   * Creates a new instance of CheckResponseHeaders
   */
  public CheckGetObjectHeaders18() {
  }


protected TestResult processResults(String transName,RETSTransaction t) {
      if (t == null) {
        return reportResult("CheckGetObjectHeaders",
                                              "Transaction \"" + transName
                                                + "\" is null, check the transaction definition",
                                              "Failure",
                                              "Transaction could not be constructed");
      } else {
        Map responseHeaders = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
        return checkResponseHeaders(transName, responseHeaders);

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
      return reportResult("CheckGetObjectResponseHeaders",
                                    "There was no response map found",
                                    "Failure",
                                    "No fields found in transaction : " + transactionName);


    }

    int missingCount = 0;
    int foundCount = 0;

    for (int i = 0; i < requiredFields.length; i++) {
      if (map.get(requiredFields[i]) == null) {

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
      return reportResult("CheckGetObjectResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\", the response is missing the following required header fields :"
                                      + missingRequiredFields, "Failure", notes);
    } else {
      return reportResult("CheckGetObjectResponseHeaders",
                                    "Transaction \"" + transactionName
                                      + "\" All required response header fields found"
                                      + "", "Success", notes);
    }
  }



}
