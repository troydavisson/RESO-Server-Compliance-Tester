/*
 * CheckSearchResults.java
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.*;

import java.util.*;

import org.realtor.rets.retsapi.*;


/**
 * Checks the results of search transactions.  This class currently compares
 * requested fields with those returned.  ASSUMES COMPACT OR COMPACT-DECDED
 * FORMAT USED
 *
 * @author pobrien
 */
public class CheckSearchResults extends BaseEvaluator {
  /**
   * Creates a new instance of CheckSearchResults
   */
  public CheckSearchResults() {
  }

  	protected TestResult processResults(String transName,RETSTransaction t) {
		if (t instanceof RETSSearchTransaction){
			return	processSearchResults(transName,(RETSSearchTransaction) t);
		}
		return null;
	}

  /**
   * Process a search transaction, pull out the response body and call
   * compareSElectResults passing the body, selected search fields, and the
   * delimiter used (assumes compact result)
   *
   * @param st Search transaction
   *
   * @return test result (were all the fields found)
   */
  protected TestResult processSearchResults(String transName,RETSSearchTransaction st) {
    String body = (String) st.getResponseMap().get("body");

    if (st.getSearchSelect() == null) { // could just not add the TestResult

      return reportResult(transName+" CheckSearchResults",
                          "Compare requested fields to response fields", "Info",
                          "No fields were requested in the search transaction, nothing to do here");
    }

    return compareSelectResults(transName, st, (String) body, st.getSearchSelect(),
                                st.getSearchDelimiter());
  }

  /**
   * Compares a Search Transaction response columns (compact format) to the
   * requested select fields from the request.  Reports any missing response
   * fields.
   *
   * @param trans Transaction to compare search results, mostly passed for reporting
   * @param body body of the search transaction
   * @param searchSelect requested fields
   * @param delim delimiter used in compact format
   *
   * @return result of the test
   */
  protected TestResult compareSelectResults(String transName,RETSTransaction trans, String body,
                                            String searchSelect, String delim) {
    if ((body == null) || (body.length() < 1)) {
      return reportResult(trans, transName+" Null Search Results",
                          "Search transaction body was null, unable to evaluate",
                          "Info");
    }

    int start = body.indexOf("<COLUMNS>");
    int end = body.indexOf("</COLUMNS>");


    String cols = body.substring(start, end);
    char sep = (char) Integer.parseInt(delim.trim(), 16);
    StringTokenizer st = new StringTokenizer(cols, "" + sep);
    StringTokenizer st2 = new StringTokenizer(searchSelect, ",");

    // Start the compare now
    ArrayList<String> al = new ArrayList<String>();

    while (st.hasMoreTokens()) {
      al.add(st.nextToken());
    }

    String succesfulTokens = "";
    String missingTokens = "";

    while (st2.hasMoreTokens()) {
      String tok = st2.nextToken();

      if (al.contains(tok)) {
        succesfulTokens = succesfulTokens + "  " + tok;
      } else {
        missingTokens = missingTokens + "  " + tok;
      }
    }

    if (missingTokens.length() > 1) {
      return reportResult(transName + " CheckSearchResults",
                          "Compare requested fields to response fields",
                          "Failure",
                          "The following search fields were not in the response : "
                          + missingTokens + "\n\n" + body);
    } else {
      return reportResult(transName+" CheckSearchResults",
                          "Compare requested fields to response fields",
                          "Success",
                          "All requested search fields were found in the response \n\n"
                          + body);
    }
  }
}