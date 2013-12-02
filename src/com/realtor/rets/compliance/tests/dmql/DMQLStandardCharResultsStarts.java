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

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a Character-based
 * field's value that starts with (*) msf_CHAR_VALUE_MATCH
 * <p/>
 *
 * @author pobrien
 */
public class DMQLStandardCharResultsStarts extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardCharResultsStarts.class);

    private final static String FAILURE_NOTES =
            "One or more Search fields had an incorrect 'Character value' in the response: ";

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_CHARACTER_STARTSWITH_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult dmqlTestResult = null;        
        Iterator queryResultsIt = null;

        String characters = getDMQLProperty(DMQL_CHARACTER_STARTSWITH_VALUE);
        testResultDesc =
                "Compare value of requested field " + fieldStrdName +
                "; Value must Start With the String " + characters;

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
        String resultLowCase = null;
        String testFailResult = null;

        if (queryResultsIt == null) {
            setResultVarsErr(xPathQuery,
                    "querying XML response - NO results returned from query!");
            log.error("DMQLCharResultsStarts Failed - " + testResultNotes);
        } else {
            while (queryResultsIt.hasNext()) {
                queryResult = (String) queryResultsIt.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
                }
                if (queryResult != null && queryResult.length() > 0) {
                    resultLowCase = queryResult.toLowerCase();
                    if (!resultLowCase.startsWith(characters.toLowerCase())) {
                        testFailResult = queryResult;
                    }
                }
            }

            if (testResultStatus == null) {
                if (testFailResult == null) {     //we passed this test
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
