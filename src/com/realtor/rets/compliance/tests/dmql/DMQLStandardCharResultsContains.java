/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import com.realtor.rets.compliance.TestResult;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a Character-based
 * field's value that CONTAINS the string value msf_CHAR_VALUE_MATCH
 * <p/>
 * <p/>
 *
 * @author $Author: pobrien $
 */
public class DMQLStandardCharResultsContains extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardCharResultsContains.class);

    private final static String FAILURE_NOTES =
            "One or more Search fields had an incorrect 'Character value' in the response: ";

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_CHARACTER_CONTAINS_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult dmqlTestResult   = null;
        Iterator queryResultsIt     = null;

        String containsCharacters = getDMQLProperty(DMQL_CHARACTER_CONTAINS_VALUE).toLowerCase();

        testResultDesc =
                "Compare value of requested field " + fieldStrdName +
                "; Value contains the String: " + containsCharacters;

        if (queryResponseDoc != null) {
            JXPathContext docContext = JXPathContext.newContext(queryResponseDoc);
            try {
                queryResultsIt = (Iterator) docContext.iterate(xPathQuery);
            } catch (JXPathException je) {
                log.error("JXPath ERROR: " + je);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, je);
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, e);
            }
        }

        String queryResult = null;
        String testFailResult = null;

        Pattern charPattern = null;
        Matcher patternMatcher = null;
        charPattern = Pattern.compile(containsCharacters);

        if (queryResultsIt == null) {
            setResultVarsErr(xPathQuery, "querying XML response - NO results returned from query!");
            log.error("DMQLCharResultsContains Failed - " + testResultNotes);
        } else {
            while (queryResultsIt.hasNext()) {
                queryResult = (String) queryResultsIt.next();
                patternMatcher = charPattern.matcher(queryResult.toLowerCase());

                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
                }

                if (queryResult != null && queryResult.length() > 0) {
                    if (!patternMatcher.find()) {
                        log.debug(queryResult + " Did not MATCH " + containsCharacters);
                        testFailResult = queryResult;
                    }
                }
            }

            if (testResultStatus == null) {
                if (testFailResult == null) {     //we PASSSED this test
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

        dmqlTestResult = reportResult(transName, testResultDesc,
                testResultStatus, testResultNotes);
        return dmqlTestResult;
    }
}
