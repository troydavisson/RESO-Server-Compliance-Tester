/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import com.realtor.rets.compliance.TestResult;

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a Date
 * field's value, ensuring it is in a range specified by msf_LESS_THAN_VAL
 * <p/>
 *
 * @author pobrien
 */
public class DMQLStandardDateResultsMax extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardDateResultsMax.class);

    private final static String FAILURE_NOTES =
            "One or more Search fields had an incorrect Date value in the response: ";
    private final static String SUCCESS_NOTES =
            "All requested search fields had the correct Date values in the response: \n\n";

    private final static SimpleDateFormat RETS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_DATE_MAXIMUM_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult dmqlTestResult   = null;
        Date thresholdDate          = null;
        Iterator xPathResultsIt     = null;

        try {
            thresholdDate = RETS_DATE_FORMAT.parse(getDMQLProperty(DMQL_DATE_MAXIMUM_VALUE));
        } catch (Exception e) {
            testResultStatus = "INFO";
            testResultNotes = "ERROR: Error in query Could Not Initialize Comparison Date Value: "
                    + e;
            log.error(testResultNotes);
        }
        if (log.isDebugEnabled()) {
            log.debug("Comparison Date Value LOADed: " + thresholdDate);
        }

        testResultDesc = "Compare value of requested field " + fieldStrdName +
                " to the specified range; date field Value must be BEFORE the value " + thresholdDate;

        if (queryResponseDoc != null) {
            JXPathContext docContext = JXPathContext.newContext(queryResponseDoc);
            try {
                xPathResultsIt = (Iterator) docContext.iterate(xPathQuery);
            } catch (JXPathException je) {
                log.error("JXPath ERROR: " + je);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, je);
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, e);
            }
        }

        String dateFailedValue  = null;
        boolean isTestFailed    = false;
        String queryResult      = null;
        Date queryResultDate    = null;

        if (xPathResultsIt == null) {
            setResultVarsErr(xPathQuery,
                    "querying XML response - NO results returned from query!");
            log.error("DMQLDateResultsLT Failed - " + testResultNotes);
        } else {
            while (xPathResultsIt.hasNext()) {
                queryResult = (String) xPathResultsIt.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
                }

                if (queryResult != null && queryResult.length() > 0) {

                    try {
                        queryResultDate = RETS_DATE_FORMAT.parse(queryResult);
                        if (log.isDebugEnabled()) {
                            log.debug("Loaded QueryResult DATE val: " + queryResultDate);
                        }
                        if (queryResultDate.after(thresholdDate)) {
                            isTestFailed = true;
                            dateFailedValue = queryResultDate.toString();
                        }
                    } catch (ParseException ne) {
                        testResultStatus = "INFO";
                        testResultNotes = "ERROR: could not parse QueryResult Date: "
                                + queryResult + "\n\n " + responseBody;
                        log.error(testResultNotes);
                    }
                }
            }

            if (testResultStatus == null) {
                if (isTestFailed) {     //we failed this test
                    testResultStatus = "FAILURE";
                    testResultNotes = FAILURE_NOTES + dateFailedValue + "\n\n" + responseBody;

                } else {
                    testResultStatus = "SUCCESS";
                    testResultNotes = SUCCESS_NOTES + responseBody;
                }
            }

        }

        dmqlTestResult = reportResult(transName, testResultDesc,
                testResultStatus, testResultNotes);

        return dmqlTestResult;
    }
}
