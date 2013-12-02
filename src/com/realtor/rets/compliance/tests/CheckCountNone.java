/*
 * CheckCountNone.java
 *
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.*;

import java.util.*;


import org.realtor.rets.retsapi.*;


/**
 * Checks the results of search transactions.  This class currently evaluates
 * the COUNT tag and the Records Attribute
 *
 * @author pobrien
 */
public class CheckCountNone extends BaseEvaluator {
  /**
   * Creates a new instance of CheckCountNone
   */
  public CheckCountNone() {
  }

  	protected TestResult processResults(String transName,RETSTransaction t) {
		if (t instanceof RETSSearchTransaction){
			return	processSearchResults(transName,(RETSSearchTransaction) t);
		}
		return null;
	}

  /**
   * Process a search transaction, pull out the response body and
   * check to make certain the count tag is not present
   *
   * @param transName transaction name
   * @param st Search transaction
   *
   * @return test result (was the count tag absent)
   */
  protected TestResult processSearchResults(String transName,RETSSearchTransaction st) {
    String body = (String) st.getResponseMap().get("body");
   
    if (st.getCountPresent() == true) { // could just not add the TestResult

      return reportResult(transName+" CheckCountNone",
                          "Check for the COUNT TAG", "Failure",
                          "The search request contained Count=0 but the response contains a COUNT tag");
    } else {
		return reportResult(transName+" CheckCountNone",
						   "COUNT tag is not present.", "Success",
						   "The search request contained Count=0 and the response does not contain a COUNT tag");

	}

  }

}