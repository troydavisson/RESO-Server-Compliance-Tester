/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
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
public class DMQLStandardCompactCharResultsOR extends DMQLResultsSystem {

    private final static String FAILURE_NOTES =
        "One or more Search fields had an incorrect Character OR Numeric value in the response: ";

    private static Log log = LogFactory.getLog(DMQLStandardCompactCharResultsOR.class);

    protected void compareDMQLResults(String transName, String responseBody,
   		CompactFormatData compactFormatData) {

        String andOrField = getDMQLProperty("DMQL.Standard",DMQL_CHARACTER_ANDOR_FIELD);
        String andOrValue = getDMQLProperty("DMQL.Standard",DMQL_CHARACTER_ANDOR_VALUE);
        String decimalMaximumField = getDMQLProperty("DMQL.Standard",DMQL_DECIMAL_MAXIMUM_FIELD);
        String decimalMaximumValue = getDMQLProperty("DMQL.Standard",DMQL_DECIMAL_MAXIMUM_VALUE);

        testResultDesc =
            "Compare value of requested field " + andOrField +
            " to the field Value " + andOrValue + "* *OR* ensure the numeric field "
            + decimalMaximumField + " is <= to the value " + decimalMaximumValue;

        List andOrDataList = compactFormatData.getDataForColumnAsList(andOrField);
        List decimalMaximumDataList = compactFormatData.getDataForColumnAsList(decimalMaximumField);

        if (andOrDataList.isEmpty() || decimalMaximumDataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT",
                        "querying XML response - NO results returned from query!");
            log.error("DMQLStandardCompactCharResultsAND Failed - " + testResultNotes);
        } else {
        	Iterator andOrIterator = andOrDataList.iterator();
        	Iterator decimalMaximumIterator = decimalMaximumDataList.iterator();
        	String testFailValue = null;
        	String testTwoFailValue = null;

            while (andOrIterator.hasNext()) {
                String queryResult = (String) andOrIterator.next();
                testFailValue = determineTestResult(andOrField, queryResult);

                queryResult = (String) decimalMaximumIterator.next();
                testTwoFailValue = determineTestResult(decimalMaximumField, queryResult);
            }

            if (testResultStatus == null) {
                if (testFailValue == null || testTwoFailValue == null) {     //we failed this test
                    testResultStatus = "SUCCESS";
                    testResultNotes = "All requested search fields had the correct Numeric values in the response \n\n"
                            + responseBody;
                } else {
                    testResultStatus = "FAILURE";
                    testResultNotes = FAILURE_NOTES + testFailValue
                            + "\n\n" + responseBody;

                }

            }
        }
    }

    /**
     * @param fieldStrdName
     * @return
     */
    private String determineTestResult(String fieldStrdName, String queryResult) {
        BigDecimal bdNumericResult  = null;
        String testFailValue        = null;
        String resultLowerCase      = null;

        if (log.isDebugEnabled()) {
            log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
        }

        String characters = getDMQLProperty(DMQL_CHARACTER_ANDOR_VALUE);
        long threshold = Long.parseLong(getDMQLProperty(DMQL_DECIMAL_MAXIMUM_VALUE));

        Pattern charPattern = Pattern.compile("[a-zA-Z]");
        Matcher patternMatcher = charPattern.matcher(queryResult);
        if (queryResult != null && queryResult.length() > 0) {
            if (patternMatcher.find()) {
                log.debug("Matched alhpa-ONLY Chars in queryResult");
                resultLowerCase = queryResult.toLowerCase();
                if (!resultLowerCase.startsWith(characters.toLowerCase())) {
                    testFailValue = queryResult;
                }
            } else {
                log.debug("queryResult datatype IS Decimal");
                try {
                    bdNumericResult = new BigDecimal(queryResult);
                } catch (NumberFormatException ne) {
                    setResultVarsErr(queryResult,
                            "Could NOT convert numeric Result String to a number!");
                    log.error("ERROR converting numericResult String to a BigDecimal value " + ne);
                }
                if (bdNumericResult.doubleValue() > threshold) {
                    testFailValue = queryResult;
                }
            }
        }
        return testFailValue;
    }
}
