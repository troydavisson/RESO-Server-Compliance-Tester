/*
 * CheckLogoutOptArgs
 *
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
public class CheckLogoutOptArgs18 extends BaseEvaluator {
  /** fields required for the response headers */
  String[] optResponseArgs={ "ConnectTime", "Billing", 
            "SignOffMessage" };

  /**
   * Creates a new instance of CheckLogoutOptArgs
   */
  public CheckLogoutOptArgs18() {
  }


protected TestResult processResults(String transName,RETSTransaction t) {
      if (t == null) {
        return reportResult("checkOptionalResponseArguments",
                                              "Transaction \"" + transName
                                                + "\" is null, check the transaction definition",
                                              "Failure",
                                              "Transaction could not be constructed");
      } else {
        Map responseMap = t.getResponseMap();
        return checkResponseArgsOpt(transName, responseMap);


      }

  }

/**
     * Optional logout response arguments to check
     *
     * @param transactionName
     *            Transaction name
     * @param map
     *            responseMap
     */
	 protected TestResult checkResponseArgsOpt(String transactionName, Map map) {


		TestResult result = new TestResult("Logout",
			"Checking Optional Response Arguments");

		String notes = "Fields found : ";
		String info = "";
		String missingOptFields = "";

		int missingCount = 0;
		int foundCount = 0;
		int badCaseCount = 0;
		String key = null;

		for (int i = 0; i < optResponseArgs.length; i++) {
		    if (map.get(optResponseArgs[i]) == null) {
			key = checkResponseCaseless(optResponseArgs[i], map);

			if (key == null) {
			    if (missingCount++ == 0) {
				missingOptFields = optResponseArgs[i];
			    } else {
				missingOptFields = missingOptFields + ", "
					+ optResponseArgs[i];
			    }
			} else {
			    badCaseCount++;

			    info = info + "\t Looking for \"" + optResponseArgs[i]
				    + "\" found \"" + key + "\"\n";
			}
		    } else {
			if (foundCount++ > 0) {
			    notes = notes + ", " + optResponseArgs[i];
			} else {
			    notes = notes + optResponseArgs[i];
			}
		    }
		}

		String display = "";

		if (missingCount > 0) {
		    result.setStatus("Info");
		    display += ("Logout transaction does not support the following optional response arguments:\n "
			    + missingOptFields + "\n\n");
		}

		if (badCaseCount > 0) {
		    result.setStatus("Failure");
		    display += ("Logout transaction supports the following optional response arguments, but with an incorrect case: \n"
			    + info + "\n\n");
		}

		if (foundCount > 0) {
		    result.setStatus("Success");
		    display += ("Logout transaction supports the following optional response arguments:\n  "
			    + notes + " \n\n ");
		}

		result.setNotes(display);
		return result;
	  }


	    /**
	       * Check Response, ignore case. Maybe the case was not the same as expected.
	       *
	       * @param reqField
	       *            required filed
	       * @param map
	       *            values to compare against
	       *
	       * @return return found value
	       */
	      protected String checkResponseCaseless(String reqField, Map map) {
	          Set respKeySet = map.keySet();
	          Iterator iter = null;
	          String key = null;

	          iter = respKeySet.iterator();

	          while (iter.hasNext()) {
	              key = (String) iter.next();

	              if (reqField.equalsIgnoreCase(key)) {
	                  return key;
	              }
	          }

	          return null;
    }
  }



