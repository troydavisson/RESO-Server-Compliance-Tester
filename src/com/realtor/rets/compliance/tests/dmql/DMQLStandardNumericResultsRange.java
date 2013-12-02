/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import com.realtor.rets.compliance.TestResult;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters; this class tests a numeric
 * field's value, ensuring it is in a range specified by msf_GREATER_THAN_VAL &
 * msf_LESS_THAN_VALUE
 * <p/>
 *
 * @author $Author: pobrien $
 */
public class DMQLStandardNumericResultsRange extends DMQLResultsStandard {

    private static Log log = LogFactory.getLog(DMQLStandardNumericResultsRange.class);

    private final static String FAILURE_NOTES =
            "One or more Search fields had an incorrect numeric value in the response: ";

    protected TestResult compareDMQLResults(String transName, String responseBody,
                                            Document queryResponseDoc) {

        String fieldStrdName = getDMQLProperty(DMQL_DECIMAL_RANGELOW_FIELD);
        String xPathQuery = getXPathQuery(fieldStrdName);

        TestResult dmqlTestResult = null;
        Iterator numericResultsIt = null;

        long lowThreshold = Long.parseLong(getDMQLProperty(DMQL_DECIMAL_RANGELOW_VALUE));
        long highThreshold = Long.parseLong(getDMQLProperty(DMQL_DECIMAL_RANGEHIGH_VALUE));

        testResultDesc =
                "Compare value of requested field " + fieldStrdName +
                " to the specified range; field Value must be <= " + highThreshold +
                " AND >= " + lowThreshold;

        if (queryResponseDoc != null) {
            JXPathContext docContext = JXPathContext.newContext(queryResponseDoc);
            try {
                numericResultsIt = (Iterator) docContext.iterate(xPathQuery);
            } catch (JXPathException je) {
                log.error("JXPath ERROR: " + je);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, je);
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e);
                setResultVarsXPathException(responseBody, fieldStrdName, xPathQuery, e);
            }
        }

        BigDecimal bdNumericResult = null;
        BigDecimal bdFailureValue = null;
        boolean isTestFailed = false;
        String numericResult = null;

        if (numericResultsIt == null) {
            setResultVarsErr(xPathQuery,
                    "querying XML response - NO results returned from query!");
            log.error("DMQLNumericResultsRange Failed - " + testResultNotes);
        } else {
            while (numericResultsIt.hasNext()) {
                numericResult = (String) numericResultsIt.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + numericResult + " for Field: " + fieldStrdName);
                }
                if (numericResult != null && numericResult.length() > 0) {
                    try {
                        bdNumericResult = new BigDecimal(numericResult);
                    } catch (NumberFormatException ne) {
                        setResultVarsErr(numericResult,
                                "Could NOT convert numeric Result String to a number!");
                        log.error("ERROR converting numericResult String to a BigDecimal value " + ne);
                    }
                    if (bdNumericResult.doubleValue() > highThreshold ||
                            bdNumericResult.doubleValue() < lowThreshold)
                    {
                        isTestFailed = true;
                        bdFailureValue = bdNumericResult;
                        log.info("TEST DMQLNumericResultsRange Failed: actual value for field "
                                 + fieldStrdName + " is " + bdNumericResult.doubleValue()
                                 + " which is NOT in the range of: " + lowThreshold + " to "
                                 + highThreshold);

                    }
                }
            }

            if (testResultStatus == null) {
                if (isTestFailed) {     //we failed this test
                    testResultStatus = "FAILURE";
                    testResultNotes = FAILURE_NOTES + bdFailureValue.toString()
                            + "\n\n" + responseBody;
                } else {
                    testResultStatus = "SUCCESS";
                    testResultNotes = "All requested search fields had the correct Numeric values in the response \n\n"
                            + responseBody;
                }

            }

        }

        dmqlTestResult = reportResult(transName, testResultDesc,
                testResultStatus, testResultNotes);

        return dmqlTestResult;
    }

}
