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
 * field's value, ensuring it is in a range specified by today's date,
 * msf_dtToday
 * <p/>
 *
 * @author pobrien
 */
public class DMQLStandardDateResultsToday extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardDateResultsToday.class);

    private final static String msf_FAILURE_NOTES =
            "One or more Search fields had an incorrect Date value in the response: ";
    private static Calendar msf_calToday;
    private final static String msf_SUCCESS_NOTES =
            "All requested search fields had the correct Date values in the response: \n\n";
    private final static SimpleDateFormat msf_dqmlDateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final static int msf_CAL_HOUR_VAL = 0;
    private final static int msf_CAL_MINUTE_VAL = 0;
    private final static int msf_CAL_SECOND_VAL = 0;
    private final static int msf_CAL_MILLISECOND_VAL = 0;

    /**
     * 
     */
    public DMQLStandardDateResultsToday() {

        super();

        msf_calToday = Calendar.getInstance();
        msf_calToday.set(Calendar.HOUR, msf_CAL_HOUR_VAL);
        msf_calToday.set(Calendar.MINUTE, msf_CAL_MINUTE_VAL);
        msf_calToday.set(Calendar.SECOND, msf_CAL_SECOND_VAL);
        msf_calToday.set(Calendar.MILLISECOND, msf_CAL_MILLISECOND_VAL);
        msf_calToday.set(Calendar.AM_PM, Calendar.PM);

        if (log.isDebugEnabled()) {
            log.debug("Comparison Date Value LOADed: " + msf_calToday);
        }

    }

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_DATE_TODAY_FIELD);
        log.debug("field is:"+DMQL_DATE_TODAY_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult DMQLTestResult = null;
        boolean isXmlLoaded = false;
        Iterator xPathResultsIt = null;
        testResultStatus = null;

        testResultDesc = "Compare value of requested field " + fieldStrdName +
                "to the specified today's Date; date field Value must be THE SAME AS the value "
                + msf_calToday;

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
            log.error("DMQLDateResultsToday Failed - " + testResultNotes);
        } else {
            while (xPathResultsIt.hasNext()) {
                queryResult = (String) xPathResultsIt.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldStrdName);
                }

                if (queryResult != null && queryResult.length() > 0) {

                    try {
                        queryResultDate = msf_dqmlDateFormat.parse(queryResult);
                        queryResultCal.setTime(queryResultDate);
                        queryResultCal.set(Calendar.HOUR, msf_CAL_HOUR_VAL);
                        queryResultCal.set(Calendar.MINUTE, msf_CAL_MINUTE_VAL);
                        queryResultCal.set(Calendar.SECOND, msf_CAL_SECOND_VAL);
                        queryResultCal.set(Calendar.MILLISECOND, msf_CAL_MILLISECOND_VAL);
                        queryResultCal.set(Calendar.AM_PM, Calendar.PM);

                        if (log.isDebugEnabled()) {
                            log.debug("Loaded QueryResult DATE val: " + queryResultCal.getTime());
                        }
                        if (queryResultCal.after(msf_calToday)) {
                            isTestFailed = true;
                            dateFailedValue = queryResultDate.toString();
                        } else if (queryResultCal.before(msf_calToday)) {
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

