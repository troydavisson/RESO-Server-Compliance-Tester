/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import com.realtor.rets.compliance.TestResult;

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a Character-based
 * field's value, ensuring that this field's value is = to msf_CHAR_MATCH_VAL
 * OR a numeric field's values msf_LESS_THAN_VALUE
 * <p/>
 *
 * @author $Author: pobrien $
 */
public class DMQLStandardCharResultsOR extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardCharResultsOR.class);

    private final static String FAILURE_NOTES =
            "One or more Search fields had an incorrect Character OR Numeric value in the response: ";

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_CHARACTER_ANDOR_FIELD);
        String secondFieldStrdName = getDMQLProperty(DMQL_DECIMAL_MAXIMUM_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);
        String xPathQuery2 = getXPathQuery(secondFieldStrdName);

        TestResult dmqlTestResult = null;
        Iterator resultsIt = null;
        Iterator resultsNumericIt = null;

        String characters = getDMQLProperty(DMQL_CHARACTER_ANDOR_VALUE);
        long threshold = Long.parseLong(getDMQLProperty(DMQL_DECIMAL_MAXIMUM_VALUE));

        testResultDesc =
                "Compare value of requested field " + fieldStrdName +
                " to the field Value " + characters + "* *OR* ensure the numeric field "
                + secondFieldStrdName + " is <= to the value " + threshold;

        if (queryResponseDoc != null) {
            JXPathContext docContext = JXPathContext.newContext(queryResponseDoc);
            try {
                resultsIt = (Iterator) docContext.iterate(xPathQuery);
                resultsNumericIt = (Iterator) docContext.iterate(xPathQuery2);
            } catch (JXPathException je) {
                log.error("JXPath ERROR: " + je);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, je);
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, e);
            }
        }

        String testFailValue = null;
        String testTwoFailValue = null;
        String queryResult = null;

        if (resultsIt == null || resultsNumericIt == null) {
            setResultVarsErr(xPathQuery,
                    "querying XML response - NO results returned from query!");
            log.error("DMQLCharResultsOR Failed - " + testResultNotes);
        } else {
            while (resultsIt.hasNext()) {
                queryResult = (String) resultsIt.next();
                testFailValue = determineTestResult(fieldStrdName, queryResult);

                queryResult = (String) resultsNumericIt.next();
                testTwoFailValue = determineTestResult(secondFieldStrdName, queryResult);
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

        dmqlTestResult = reportResult(transName, testResultDesc,
                testResultStatus, testResultNotes);
        return dmqlTestResult;
    }

    /**
     * @param fieldStrdName
     * @param resultsIt
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
