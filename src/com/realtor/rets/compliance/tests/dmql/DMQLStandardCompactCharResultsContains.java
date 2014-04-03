/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters
 *
 * @author $Author: pobrien $
 */
public class DMQLStandardCompactCharResultsContains extends DMQLResultsSystem {

    private static Log log = LogFactory.getLog(DMQLStandardCompactCharResultsContains.class);

    private final static String FAILURE_NOTES =
        "One or more Search fields had an incorrect 'Character value' in the response: ";

    protected void compareDMQLResults(String transName, String responseBody,

   		CompactFormatData compactFormatData) {

        //String fieldName = getDMQLProperty(DMQL_CHARACTER_CONTAINS_FIELD).toLowerCase();
        String fieldName = getDMQLProperty("DMQL.Standard.",DMQL_CHARACTER_CONTAINS_FIELD);
        String containsCharacters = getDMQLProperty("DMQL.Standard.",DMQL_CHARACTER_CONTAINS_VALUE);

        testResultDesc =
            "Compare value of requested field " + fieldName +
            "; Value contains the String: " + containsCharacters;

		System.out.println("fieldName is"+ fieldName);
        List dataList = compactFormatData.getDataForColumnAsList(fieldName);

        if (dataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT", "NO results returned from query!");
            log.error("DMQLStandardCompactCharResultsContains Failed - " + testResultNotes);
        }
        else {
            Pattern charPattern = null;
            Matcher patternMatcher = null;
            charPattern = Pattern.compile(containsCharacters.toLowerCase());

	        Iterator iterator = dataList.iterator();
            String testFailResult = null;

	        while (iterator.hasNext()) {
            	String queryResult = (String) iterator.next();
            	patternMatcher = charPattern.matcher(queryResult.toLowerCase());

            	if (log.isDebugEnabled()) {
            		log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldName);
            	}

            	if (queryResult != null && queryResult.length() > 0) {
            		if (!patternMatcher.find()) {
            			log.debug(queryResult + " Did not MATCH " + containsCharacters);
            			testFailResult = queryResult;
            			break;
            		}
            	}
            }

            if (testResultStatus == null) {
            	if (testFailResult == null) {
            		testResultStatus = "SUCCESS";
            		testResultNotes = "All requested search fields had the correct Character values in the response \n\n"
            			+ responseBody;
            	} else {
            		testResultStatus = "FAILURE";
            		testResultNotes = FAILURE_NOTES + testFailResult
						+ "\n\n" + responseBody;

            	}
            }

        }
    }
}
