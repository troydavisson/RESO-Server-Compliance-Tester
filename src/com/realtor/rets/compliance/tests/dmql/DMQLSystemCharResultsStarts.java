/* $Header$ 
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Iterator;
import java.util.List;

/**
 *   DMQL test Evaluator class - Tests DMQL query language by using 
 * user's selections in TestParameters
 * 
 * @author pobrien
 */
public class DMQLSystemCharResultsStarts extends DMQLResultsSystem {
	
    private static Log log = LogFactory.getLog(DMQLSystemCharResultsStarts.class);   
    
    private final static String FAILURE_NOTES =
        "One or more Search fields had an incorrect 'Character value' in the response: ";
    
    protected void compareDMQLResults(String transName, String responseBody, 
   		CompactFormatData compactFormatData) {
    	
        String fieldName = getDMQLProperty(DMQL_CHARACTER_STARTSWITH_FIELD);
        String fieldValue = getDMQLProperty(DMQL_CHARACTER_STARTSWITH_VALUE);
        
        testResultDesc =
            "Compare value of requested field " + fieldName +
            "; Value must Start With the String " + fieldValue;
        
        List dataList = compactFormatData.getDataForColumnAsList(fieldName);
        
        if (dataList.isEmpty()) {
            setResultVarsErr("COMPACT-FORMAT", "NO results returned from query!");
            log.error("DMQLSystemCharResultsStarts Failed - " + testResultNotes);
        }
        else {
            String testFailResult = null;
	        Iterator iterator = dataList.iterator();
	        
	        while (iterator.hasNext()) {
	        	String testValue = (String) iterator.next();
	        	if (! testValue.startsWith(fieldValue)) {
	        		testFailResult = testValue;
	        		break;
	        	}
	        }
	        
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
}
