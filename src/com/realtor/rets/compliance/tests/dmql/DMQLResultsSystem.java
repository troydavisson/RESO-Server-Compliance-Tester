/* $Header$
 */
package com.realtor.rets.compliance.tests.dmql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSSearchTransaction;
import com.realtor.rets.compliance.TestResult;
import java.io.IOException;

/**
 * Base class for DMQL Result Evaluator classes will evaluate each of the system
 * DMQL search (dynamically created) test transacions
 * <p/>
  *
 * @author pobrien
  */
public abstract class DMQLResultsSystem extends DMQLResults {
	
    private static Log log = LogFactory.getLog(DMQLResultsSystem.class);
    
    /**
       Get the DMQL property prefix.
	*/
	protected String getDMQLPropertyPrefix() {
	    return "DMQL.System.";
	}


    /**
     * Compare a DMQL Search Transaction response to the requested select
     * fields from the request.  Reports any missing response fields.
     *
     * @param transName        name of this DMQL search transaction
     * @param responseBody     body of the search transaction
     * @param compactFormatData CompactFormatData object
     */
    protected abstract void compareDMQLResults(String transName,
                                                     String responseBody,
													 CompactFormatData compactFormatData);
    
    /**
     * Process a DMQL search transaction, pull out the response body and call
     * compareDMQLResults() passing the body, selected search fields
     *
     * @param transName
     * @param searchTrans
     * @return
     */
	protected TestResult processDMQLResults(String transName,
			RETSSearchTransaction searchTrans) {		
        
        String responseBody = (String) searchTrans.getResponseMap().get("body");
    	CompactFormatData compactFormatData = null;
        try {
        	compactFormatData = CompactFormatData.parse(responseBody);
        }
        catch (IOException e) {
            log.error("ERROR: could not parse compact format data: " + e);
        }
		
        TestResult dmqlTestResult = null;
        if (searchTrans.getSearchQueryType() == null) { // could just not add the TestResult
            dmqlTestResult = reportResult(transName + " DMQLTestResults",
                    "Compare requested fields to response fields", "Failure",
                    "No fields were requested in the search transaction, nothing to do here");
        } else {
            compareDMQLResults(transName, responseBody, compactFormatData);            
            dmqlTestResult = reportResult(transName, testResultDesc,
                                          testResultStatus, testResultNotes);
        }

        return dmqlTestResult;
	}
}
