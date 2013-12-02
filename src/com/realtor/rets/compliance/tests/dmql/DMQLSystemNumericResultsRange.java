/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters
 *
  * @author $Author: pobrien $
 */
public class DMQLSystemNumericResultsRange extends DMQLResultsSystem {

    private static Log log = LogFactory.getLog(DMQLSystemNumericResultsRange.class);

    private final static String FAILURE_NOTES =
        "One or more Search field values had an incorrect numeric value in the response: ";

    protected void compareDMQLResults(String transName, String responseBody,
   		CompactFormatData compactFormatData) {

        if (log.isDebugEnabled()){
            log.debug("* In DMQLSystemNumericResultsRange evaluator class");
        }

        String fieldName        = getDMQLProperty(DMQL_DECIMAL_RANGELOW_FIELD);
        String fieldValueLow    = getDMQLProperty(DMQL_DECIMAL_RANGELOW_VALUE);
        String fieldValueHigh   = getDMQLProperty(DMQL_DECIMAL_RANGEHIGH_VALUE);
        long lowThreshold       = Long.MIN_VALUE;
        long highThreshold      = Long.MIN_VALUE;
        String testFailResult   = null;

        try {
            lowThreshold = Long.parseLong(fieldValueLow);
            highThreshold = Long.parseLong(fieldValueHigh);
        } catch(NumberFormatException ne) {
            setResultVarsErr(ne.toString(), "Could NOT convert decimal PROPERTY String to a number for field: "
                             + fieldName );
            log.error("ERROR converting decimal PROPERTY String to a Long value " + ne);
        }

        testResultDesc =
            "Compare value of requested field " + fieldName +
            " which must be in the range of " + fieldValueLow + " to " + fieldValueHigh +
            "to pass this test.";

        List dataList = compactFormatData.getDataForColumnAsList(fieldName);
        if (dataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT",
                             "NO results returned from DMQL query!");
            log.error("DMQLSystemNumericResultsRange Failed - " + testResultNotes);
        }

        Iterator iterator = dataList.iterator();
        BigDecimal bdValueToTest = null;

        while (iterator.hasNext()) {
            String valueToTest = (String) iterator.next();
            if (log.isDebugEnabled()) {
                log.debug("success - FOUND the value " + valueToTest + " for Field: " + fieldName);
            }
            if (valueToTest == null || valueToTest.length() == 0) {
                setResultVarsErr(fieldName, "This field did not have a Value");
                log.error("DMQLSystemNumericResultsRange Failed - " + testResultNotes);
            }
            try {
                bdValueToTest = new BigDecimal(valueToTest);
            } catch (NumberFormatException ne) {
                setResultVarsErr(valueToTest,
                       "Could NOT convert numeric Result String to a number!");
                log.error("ERROR converting numericResult String to a BigDecimal value " + ne);
            }

            if (bdValueToTest.doubleValue() > highThreshold ||
                    bdValueToTest.doubleValue() < lowThreshold) {
                testFailResult = bdValueToTest.toString();
                break;
            }
        }  //end while

        if (testResultStatus == null) {
            if (testFailResult == null) {
                testResultStatus = "SUCCESS";
                testResultNotes = "All requested search fields had a Numeric value within the correct range in the response \n\n"
                        + responseBody;
            } else { //we failed this test
                testResultStatus = "FAILURE";
                testResultNotes = FAILURE_NOTES + bdValueToTest.toString()
                        + "\n\n" + responseBody;

            }
        }

    }
}
