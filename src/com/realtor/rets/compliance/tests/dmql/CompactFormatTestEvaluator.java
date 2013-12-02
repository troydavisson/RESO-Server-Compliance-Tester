/*$Header: 
**/ 
package com.realtor.rets.compliance.tests.dmql;

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSTransaction;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.BaseEvaluator;

/**
 * A test evaluator to print a COMPACT-FORMAT response to the console.
  * @author pobrien
 */
public class CompactFormatTestEvaluator extends BaseEvaluator {
    
    private static Log log = LogFactory.getLog(CompactFormatTestEvaluator.class);   
    
    public CompactFormatTestEvaluator() {
        super();
        specReference = "";
    }
    
    /**
     * Evaluate a set of transactions. This method is called by the TestExecuter
     * Checks the XML response against the RETS DTD
     *
     * @param trans Hash containing all transactions defined in a test script
     * @param testReport Report to which to add individual testResult objects.
     *
     * @return just returns an empty string for now.
     */
	protected TestResult processResults(String transName, RETSTransaction t) {

        if (t == null) {
            return null;
        }
        
        Map response = t.getResponseMap();
    	String status = "Success";
    	String notes = "Dump complete";
        try {
        	CompactFormatData cfd = CompactFormatData.parse((String) response.get("body"));
        	if (log.isDebugEnabled()) {
        		log.debug(cfd);
        	}
        }
        catch (IOException e) {
        	status = "Error";
        	notes = e.getMessage();
        }
        TestResult thisTestResult = reportResult("CompactFormatTestEvaluator",
                "Dumps COMPACT-FORMAT data to console",
                status, notes);
        
        return thisTestResult;
	}
}