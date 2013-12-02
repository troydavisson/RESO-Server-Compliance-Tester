/* $Header$ 
 */
package com.realtor.rets.compliance.tests.dmql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using 
 * user's selections in TestParameters
 * 
 * @author pobrien
 */
public class DMQLSystemDateResultsToday extends DMQLResultsSystem {
	
    private static Log log = LogFactory.getLog(DMQLSystemDateResultsToday.class);   
    
    private final static String FAILURE_NOTES =
        "One or more Search fields had an incorrect Date value in the response: ";
	private final static String SUCCESS_NOTES =
        "All requested search fields had the correct Date values in the response: \n\n";
    private final static int CAL_HOUR_VAL = 0;
    private final static int CAL_MINUTE_VAL = 0;
    private final static int CAL_SECOND_VAL = 0;
    private final static int CAL_MILLISECOND_VAL = 0;
    private final static SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");
    
    private static Calendar todayCalendar;
    
    static {
    	todayCalendar = Calendar.getInstance();
    	todayCalendar.set(Calendar.HOUR, CAL_HOUR_VAL);
    	todayCalendar.set(Calendar.MINUTE, CAL_MINUTE_VAL);
        todayCalendar.set(Calendar.SECOND, CAL_SECOND_VAL);
        todayCalendar.set(Calendar.MILLISECOND, CAL_MILLISECOND_VAL);
        todayCalendar.set(Calendar.AM_PM, Calendar.PM);

        if (log.isDebugEnabled()) {
            log.debug("Comparison Date Value LOADed: " + todayCalendar);
        }
    }
    
    protected void compareDMQLResults(String transName, String responseBody, 
   		CompactFormatData compactFormatData) {
   	
   		String fieldName = getDMQLProperty(DMQL_DATE_TODAY_FIELD);
    
   		testResultDesc = "Compare value of requested field " + fieldName +
			" to the specified today's Date; date field Value must be THE SAME AS the value "
			+ todayCalendar;
    	
        String dateFailedValue = null;
        boolean isTestFailed = false;
        Date queryResultDate = null;
        String queryResult;

        Calendar queryResultCal = Calendar.getInstance();

        List dateList = compactFormatData.getDataForColumnAsList(fieldName);
        
        if (dateList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT",
                    "querying XML response - NO results returned from query!");
            log.error("DMQLDateResultsToday Failed - " + testResultNotes);
        } else {
        	Iterator dateIterator = dateList.iterator();
            while (dateIterator.hasNext()) {
            	queryResult = (String) dateIterator.next();
                if (log.isDebugEnabled()) {
                    log.debug("success - FOUND the value " + queryResult + " for Field: " + fieldName);
                }

                if (queryResult != null && queryResult.length() > 0) {

                    try {
                        queryResultDate = DATE_FORMAT.parse(queryResult);
                        queryResultCal.setTime(queryResultDate);
                        queryResultCal.set(Calendar.HOUR, CAL_HOUR_VAL);
                        queryResultCal.set(Calendar.MINUTE, CAL_MINUTE_VAL);
                        queryResultCal.set(Calendar.SECOND, CAL_SECOND_VAL);
                        queryResultCal.set(Calendar.MILLISECOND, CAL_MILLISECOND_VAL);
                        queryResultCal.set(Calendar.AM_PM, Calendar.PM);

                        if (log.isDebugEnabled()) {
                            log.debug("Loaded QueryResult DATE val: " + queryResultCal.getTime());
                        }
                        if (queryResultCal.after(todayCalendar)) {
                            isTestFailed = true;
                            dateFailedValue = queryResultDate.toString();
                        } else if (queryResultCal.before(todayCalendar)) {
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
