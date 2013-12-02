/*
 * CheckCountOnly.java
 *
 *
 */
package com.realtor.rets.compliance.tests;

import org.realtor.rets.retsapi.RETSSearchTransaction;
import org.realtor.rets.retsapi.RETSTransaction;

import com.realtor.rets.compliance.TestResult;

/**
 * Checks the results of search transactions. This class currently evaluates the
 * COUNT tag and the Records Attribute
 * 
 * @author pobrien
 */
public class CheckCountOnly extends BaseEvaluator {
	/**
	 * Creates a new instance of CheckCountOnly
	 */
	public CheckCountOnly() {
	}

	protected TestResult processResults(String transName, RETSTransaction t) {
		if (t instanceof RETSSearchTransaction) {
			return processSearchResults(transName, (RETSSearchTransaction) t);
		}
		return null;
	}

	/**
	 * Process a search transaction, pull out the response body check for the
	 * presence of the COUNT tag and Records attribute. Make certain there are
	 * no COLUMNS or DATA present.
	 * 
	 * @param transName
	 *            transaction name
	 * @param st
	 *            Search transaction
	 * 
	 * @return test result (were count and records found)
	 */
	protected TestResult processSearchResults(String transName,
			RETSSearchTransaction st) {
		String body = (String) st.getResponseMap().get("body");
		int start = body.indexOf("<DATA>");
		int end = body.indexOf("</DATA>");

		
		if (st.getCountPresent()==false) {

			return reportResult(
					transName + " CheckCountOnly",
					"Check for the COUNT TAG",
					"Failure",
					"The search request contained Count=2 but the response did not contain a COUNT TAG");
		}
		int count=0;
		try {
			count = Integer.parseInt(st.getRecords());
		} catch (Exception e){
			return reportResult(
					transName + " CheckCountOnly",
					"Count is present but Records attribute returned non-numeric data or was not present "+st.getRecords(),
					"Failure",
					"The search request contained Count=2 but the response contained an invalid Records attribute");

		}


		if (start >= 0) {

			return reportResult(
					transName + " CheckCountOnly",
					"Data is present in a count-only request: "
							+ body.substring(start, end), "Failure",
					"The search request contained Count=2 but the response contained data");

		}

		return reportResult(
				transName + " CheckCountOnly",
				"COUNT tag and Records attribute are present and there is no data in the response,",
				"Success",
				"The search request contained Count=2 and response contained no data.");

	}

}