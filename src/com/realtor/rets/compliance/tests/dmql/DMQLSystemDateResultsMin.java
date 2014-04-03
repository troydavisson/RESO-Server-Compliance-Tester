/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using
 * user's selections in TestParameters
 *
 * @author $Author: pobrien $
 */
public class DMQLSystemDateResultsMin extends DMQLResultsSystem {

    private static Log log = LogFactory.getLog(DMQLSystemDateResultsMin.class);

    private final static String FAILURE_NOTES =
        "One or more Search fields had an incorrect Date value in the response: ";
    private final static String SUCCESS_NOTES =
        "All requested search fields had the correct Date values in the response: \n\n";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	protected void compareDMQLResults(String transName, String responseBody,
   		CompactFormatData compactFormatData) {

		String decimalMinimumField = getDMQLProperty(DMQL_DATE_MINIMUM_FIELD);
        log.debug("field is:"+DMQL_DATE_MINIMUM_FIELD);
        List decimalMinimumDataList = compactFormatData.getDataForColumnAsList(decimalMinimumField);

        testResultStatus = null;
        Date thresholdDate = null;

        try {
            thresholdDate = DATE_FORMAT.parse(getDMQLProperty(DMQL_DATE_MINIMUM_VALUE));
        } catch (Exception e) {
            testResultStatus = "INFO";
            testResultNotes = "ERROR: Error in query Could Not Initialize Comparison Date Value: "
                    + e;
            log.error(testResultNotes);
        }
        if (log.isDebugEnabled()) {
            log.debug("Comparison Date Value LOADed: " + thresholdDate);
        }

        testResultDesc = "Compare value of requested field " + decimalMinimumField +
                " to the specified range; date field Value must be AFTER " + thresholdDate;

        String dateFailedValue = null;
        boolean isTestFailed = false;
        String queryResult = null;
        Date queryResultDate = null;


        if (decimalMinimumDataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT",
                    "querying XML response - NO results returned from query!");
            log.error("DMQLDateResultsGT Failed - " + testResultNotes);
        } else {
        	Iterator iterator = decimalMinimumDataList.iterator();
            while (iterator.hasNext()) {
                queryResult = (String) iterator.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + decimalMinimumField);
                }

                if (queryResult != null && queryResult.length() > 0) {

                    try {
                        queryResultDate = DATE_FORMAT.parse(queryResult);
                        if (log.isDebugEnabled()) {
                            log.debug("Loaded QueryResult DATE val: " + queryResultDate);
                        }
                        log.error("--------------------------> " + new Date(queryResultDate.getTime()) +
                                " ::: " + thresholdDate);
                        if ( (!queryResultDate.after(thresholdDate))&&(!queryResultDate.equals(thresholdDate)) ) {
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
                    testResultNotes = FAILURE_NOTES + dateFailedValue
                            + "\n\n" + responseBody;

                } else {
                    testResultStatus = "SUCCESS";
                    testResultNotes = SUCCESS_NOTES + responseBody;
                }

            }
        }

    }
}
