/* $Header$ 
 */
package com.realtor.rets.compliance.tests.dmql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSTransaction;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;
import com.realtor.rets.compliance.tests.BaseEvaluator;

/**
 *  Evaluator class for ONLY the metadata request sent to RETS server;
 * This class will be a "skeleton" evaluator class & ONLY do a basic
 * check that the returned metadata is valid
 * 
  * @author pobrien
 */
public class DMQLEvalMetadata extends BaseEvaluator {
    
    private static Log log = LogFactory.getLog(DMQLEvalMetadata.class);
    
    /**
     * 
     */
    public DMQLEvalMetadata() {
        super();
        specReference = "";
    }

    /**
     * Evaluate ONLY the metadata transactions at a very basic validation level.
     * FOR the DMQL multi-part test     * 
     *
     * @param trans Hash containing all transactions defined in a test script
     * @param testReport Report to which to add individual testResult objects.
     *
     * @return just returns an empty string for now.
     */
    protected TestResult processResults(String transName, RETSTransaction t) {
        String notes;
        TestResult thisTestResult;
        
        if (t == null) {
            thisTestResult = null;
        }
        
        Map response = t.getResponseMap();
        String className = t.getClass().getName();
        if (log.isDebugEnabled()) {
            log.debug("ClassName of trans: " + className);            
        }
        
        Map m = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
        boolean isXml = CollectionUtils.hasValue(m, "content-type", "text/xml");
        
        if (isXml) {
            String body = (String) response.get("body");
            thisTestResult = evaluateMetaDataResponse(transName, body);
            thisTestResult.setResponseBody(body);
        } else {
            notes = "Transaction " + className
                    + " does not include a Content-Type of 'text/xml'";
            thisTestResult = reportResult("MetadataSystem*",
                    "Checks to see if the depth and breadth are correct",
                    "Info", notes);
        }
        
        return thisTestResult;
    }

    /**
     * This is the method that does the actual check of a "metaData" transaction body for a valid
     *  STANDARD-XML data response.  In this case, we ONLY check for a couple of key tags:
     * <METADATA-RESOURCE> and if we find that, we return success.  
     *
     * @param transactionName Name of the Transaction checked (from the test script)
     * @param responseBody transaction body which should be valid compact
     *
     * @return results of the test.
     */    
    private TestResult evaluateMetaDataResponse(String transactionName,
                                    String responseBody) {

        String jException = "";
        String status = "Failure";
        TestResult testResult;
        boolean valid = true;

        if ((responseBody != null) && (responseBody.length() > 0)) {

            boolean foundTagStart = false;
            if (responseBody.indexOf("<METADATA-RESOURCE") >= 0
                    || responseBody.indexOf("<METADATA-SYSTEM") >= 0) {
                foundTagStart = true;
                if (log.isDebugEnabled()) {
                    log.debug("found a correct Tag Starting position");
                }
            }

            if (foundTagStart == true) {
                status = "Success";
            }
        }

        testResult = reportResult(
                "DMQL-MetadataSystem*:  " + transactionName,
                "Checks to see if the the transaction has valid breadth and depth",
                status, responseBody, jException, "n/a");
        if (log.isDebugEnabled()) {
            log.debug("reportResult Status = " + status);
        }

        return testResult;

    }
}
