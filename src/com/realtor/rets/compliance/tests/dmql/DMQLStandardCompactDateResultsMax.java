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
import java.util.Locale;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using 
 * user's selections in TestParameters.  This evaluator tests the each value returned by
 * the DMQL query to make sure that it exists in time BEFORE the date specified in
 * the parameter defined in TestParameters as...
 * 
 * @author pobrien
 */
public class DMQLStandardCompactDateResultsMax extends DMQLResultsSystem {
	
    private static Log log = LogFactory.getLog(DMQLStandardCompactDateResultsMax.class);
    
    private final static SimpleDateFormat RETS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", new Locale("English"));
    private final static String FAILURE_NOTES =
        "One or more Search field values had an incorrect numeric value in the response: ";
        
    protected void compareDMQLResults(String transName, String responseBody, 
   		CompactFormatData compactFormatData) {
        
        String fieldName        = getDMQLProperty("DMQL.Standard.",DMQL_DATE_MAXIMUM_FIELD);
        String fieldValue       = getDMQLProperty("DMQL.Standard.",DMQL_DATE_MAXIMUM_VALUE);
        Date thresholdDate      = null;
        String testFailResult   = null;
        
        try {
            thresholdDate = RETS_DATE_FORMAT.parse(fieldValue);
        } catch(ParseException pe) {
            setResultVarsErr(fieldValue, "Could NOT convert decimal PROPERTY String to a number for field: " + fieldName);
            log.error("ERROR converting decimal PROPERTY String to a Long value " + pe);
        }
        
        testResultDesc =
            "Compare value of requested field " + fieldName +
            "'s date value to " + fieldValue + "; returned date Value must be <= " + fieldValue;
        
        List dataList = compactFormatData.getDataForColumnAsList(fieldName);
        if (dataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT", "NO results returned from DMQL query!");
            log.error("DMQLStandardCompactDateResultsMax Failed - " + testResultNotes);
        }

        Iterator iterator = dataList.iterator();
        Date dtValueToTest    = null;
        
        while (iterator.hasNext()) {
            String valueToTest = (String) iterator.next();
            if (log.isDebugEnabled()) {
                log.debug("success - FOUND the value " + valueToTest
                        + " for Field: " + fieldName);
            }
            if (valueToTest == null || valueToTest.length() == 0) {
                setResultVarsErr(fieldName, "This field did not have a Value");
                log.error("DMQLStandardCompactNumericResultsMax Failed - " + testResultNotes);
            }
            try {
                dtValueToTest = RETS_DATE_FORMAT.parse(valueToTest);
                if (log.isDebugEnabled()) {
                    log.debug("Loaded QueryResult DATE val: " + dtValueToTest);
                }
            } catch (ParseException pe) {
                setResultVarsErr(valueToTest,
                       "Could NOT convert numeric Result String to a number!");
                log.error("ERROR converting numericResult String to a BigDecimal value " + pe);
            }

            if (dtValueToTest.after(thresholdDate)) {
                testFailResult = dtValueToTest.toLocaleString();
                break;
            }
        }  //end while
        
        if (testResultStatus == null) {
            if (testFailResult == null) {
                testResultStatus = "SUCCESS";
                testResultNotes = "All requested search fields had the correct Date values in the response \n\n"
                        + responseBody;
            } else { //we failed this test
                testResultStatus = "FAILURE";
                testResultNotes = FAILURE_NOTES + dtValueToTest.toLocaleString()
                        + "\n\n" + responseBody;

            }
        }
            	
    }
    
}
