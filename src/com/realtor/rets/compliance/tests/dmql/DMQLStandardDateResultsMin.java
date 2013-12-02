/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import com.realtor.rets.compliance.TestResult;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a Date
 * field's value, ensuring it is in a range specified by msf_GREATER_THAN_VAL
 * <p/>
 * <p/>
 *
 * @author $Author: pobrien $
 */
public class DMQLStandardDateResultsMin extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardDateResultsMin.class);

    private final static String msf_FAILURE_NOTES =
            "One or more Search fields had an incorrect Date value in the response: ";
    private final static String msf_SUCCESS_NOTES =
            "All requested search fields had the correct Date values in the response: \n\n";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *
     */
    public DMQLStandardDateResultsMin() {
        super();

    }

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_DATE_MINIMUM_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult DMQLTestResult = null;
        boolean isXmlLoaded = false;
        Iterator xPathResultsIt = null;
        testResultStatus = null;
        Date thresholdDate = null;

        try {
            thresholdDate = dateFormat.parse(getDMQLProperty(DMQL_DATE_MINIMUM_VALUE));
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
                " to the specified range; date field Value must be AFTER " + thresholdDate;

        if (queryResponseDoc != null) {
            JXPathContext docContext = JXPathContext.newContext(queryResponseDoc);
            try {
                isXmlLoaded = true;
                xPathResultsIt = (Iterator) docContext.iterate(xPathQuery);
            } catch (JXPathException je) {
                log.error("JXPath ERROR: " + je);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, je);
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, e);
            }
        }

        String dateFailedValue = null;
        boolean isTestFailed = false;
        String queryResult = null;
        Calendar queryResultCal = null;
        Date queryResultDate = null;

        queryResultCal = Calendar.getInstance();

        if (xPathResultsIt == null) {
            setResultVarsErr(xPathQuery,
                    "querying XML response - NO results returned from query!");
            log.error("DMQLDateResultsGT Failed - " + testResultNotes);
        } else {
            while (xPathResultsIt.hasNext()) {
                queryResult = (String) xPathResultsIt.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
                }

                if (queryResult != null && queryResult.length() > 0) {

                    try {
                        queryResultDate = dateFormat.parse(queryResult);
                        if (log.isDebugEnabled()) {
                            log.debug("Loaded QueryResult DATE val: " + queryResultDate);
                        }
                        log.error("--------------------------> " + new Date(queryResultDate.getTime()) +
                                " ::: " + thresholdDate);
                        if ( (!queryResultDate.after(thresholdDate))&&(!queryResultDate.equals(thresholdDate)) ){
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
                if (isTestFailed == true) {     //we failed this test
                    testResultStatus = "FAILURE";
                    testResultNotes = msf_FAILURE_NOTES + dateFailedValue.toString()
                            + "\n\n" + responseBody;

                } else {
                    testResultStatus = "SUCCESS";
                    testResultNotes = msf_SUCCESS_NOTES + responseBody;
                }

            }
        }

        DMQLTestResult = reportResult(transName, testResultDesc,
                testResultStatus, testResultNotes);

        return DMQLTestResult;
    }
}
