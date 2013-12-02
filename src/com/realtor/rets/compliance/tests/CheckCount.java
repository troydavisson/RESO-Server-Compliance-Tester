/*
 * CheckCount.java
 *
 *
 */
package com.realtor.rets.compliance.tests;

import org.realtor.rets.retsapi.RETSSearchTransaction;
import org.realtor.rets.retsapi.RETSTransaction;

import com.realtor.rets.compliance.TestResult;


/**
 * Checks the results of search transactions.  This class currently evaluates
 * the COUNT tag and the Records Attribute
 *
 * @author pobrien
 */
public class CheckCount extends BaseEvaluator {
  /**
   * Creates a new instance of CheckCount
   */
  public CheckCount() {
  }

  	protected TestResult processResults(String transName,RETSTransaction t) {
		if (t instanceof RETSSearchTransaction){
			return	processSearchResults(transName,(RETSSearchTransaction) t);
		}
		return null;
	}

  /**
   * Process a search transaction, pull out the response body
   * check for the presence of the COUNT tag and Records attribute.
   *
   * @param transName transaction name
   * @param st Search transaction
   *
   * @return test result (were count and records found)
   */
  protected TestResult processSearchResults(String transName,RETSSearchTransaction st) {
    //String body = (String) st.getResponseMap().get("body");

    	
		if (st.getCountPresent()==false) { // could just not add the TestResult

		  return reportResult(transName+" CheckCount",
							  "Check for the COUNT TAG", "Failure",
							  "The search request contained Count=1 but the response did not contain a COUNT TAG");
		}

		int count = 0;
		try {
			System.out.println("===records "+st.getRecords());
			count=Integer.parseInt(st.getRecords());
		} catch (Exception e) {
			System.out.println("===error ");
			e.printStackTrace();
			return reportResult(transName+" CheckCount",
					  "Check records attribute", "Failure",
					  "The COUNT tag was present but the Records attribute is not present or is not a valid number "+st.getRecords());

		}
		if (count >=0) {

			return reportResult(transName+" CheckCount",
							  "Count is present and Records attribute returned valid numerical data: Records=" +count, "Success",
							  "The search request contained Count=1 and the response contained a COUNT TAG");

		} else {


			return reportResult(transName+" CheckCount",
									   "Count is present but Records attribute returned non-numeric data or was not present", "Failure",
									  "The search request contained Count=1 but the response contained an invalid Records attribute");



		}
	}

}