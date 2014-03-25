/* $Header$ 
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using 
 * user's selections in TestParameters
 * @author pobrien 
 */
public class DMQLStandardCompactNumericResultsMin extends DMQLResultsSystem {
	
    private static Log log = LogFactory.getLog(DMQLStandardCompactNumericResultsMin.class);
    
    private final static String FAILURE_NOTES =
        "One or more Search field values had an incorrect numeric value in the response: ";
    
    protected void compareDMQLResults(String transName, String responseBody, 
   		CompactFormatData compactFormatData) {
        
        if (log.isDebugEnabled()){
            log.debug("* In DMQLStandardCompactNumericResultsMin evaluator class");
        }
        
        String fieldName        = getDMQLProperty("DMQL.Standard",DMQL_DECIMAL_MINIMUM_FIELD);
        String fieldValue       = getDMQLProperty("DMQL.Standard",DMQL_DECIMAL_MINIMUM_VALUE);
        long threshold          = Long.MIN_VALUE;
        String testFailResult   = null;
    	
        try {
            threshold = Long.parseLong(fieldValue);
        } catch(NumberFormatException ne) {
            setResultVarsErr(fieldValue, "Could NOT convert decimal PROPERTY String to a number for field: " + fieldName);
            log.error("ERROR converting decimal PROPERTY String to a Long value " + ne);
        }

        testResultDesc =
            "Compare value of requested field " + fieldName +
            " to " + threshold + "; field Value must be >= " + threshold;
        
        List dataList = compactFormatData.getDataForColumnAsList(fieldName);
        if (dataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT",
                             "NO results returned from DMQL query!");
            log.error("DMQLStandardCompactNumericResultsMax Failed - " + testResultNotes);
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
                log.error("DMQLStandardCompactNumericResultsMax Failed - " + testResultNotes);
            }
            try {
                bdValueToTest = new BigDecimal(valueToTest);
            } catch (NumberFormatException ne) {
                setResultVarsErr(valueToTest,
                       "Could NOT convert numeric Result String to a number!");
                log.error("ERROR converting numericResult String to a BigDecimal value " + ne);
            }
            
            if (bdValueToTest.doubleValue() < threshold) {
                testFailResult = bdValueToTest.toString();
                break;
            }
        }  //end while

        if (testResultStatus == null) {
            if (testFailResult == null) {
                testResultStatus = "SUCCESS";
                testResultNotes = "All requested search fields had the correct Numeric values in the response \n\n"
                        + responseBody;
            } else { //we failed this test
                testResultStatus = "FAILURE";
                testResultNotes = FAILURE_NOTES + bdValueToTest.toString()
                        + "\n\n" + responseBody;

            }
        }
    }
}
