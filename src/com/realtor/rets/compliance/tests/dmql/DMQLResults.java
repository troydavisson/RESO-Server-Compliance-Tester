/* $Header $
 */
package com.realtor.rets.compliance.tests.dmql;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSSearchTransaction;
import org.realtor.rets.retsapi.RETSTransaction;
import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.BaseEvaluator;

/**
 * Base class for DMQL Result Evaluator classes will evaluate each of the
 * DMQL search (dynamically created) test transacions
 * <p/>
 *
 * @author pobrien
 */
public abstract class DMQLResults extends BaseEvaluator {
	
    private static Log log = LogFactory.getLog(DMQLResults.class);
    
    // Suffixes
    protected static final String DMQL_CHARACTER_ANDOR_FIELD = "Character.AndOr.Field";
    protected static final String DMQL_CHARACTER_ANDOR_VALUE = "Character.AndOr.Value";
    protected static final String DMQL_CHARACTER_CONTAINS_FIELD = "Character.Contains.Field";
    protected static final String DMQL_CHARACTER_CONTAINS_VALUE = "Character.Contains.Value";
    protected static final String DMQL_CHARACTER_STARTSWITH_FIELD = "Character.StartsWith.Field";
    protected static final String DMQL_CHARACTER_STARTSWITH_VALUE = "Character.StartsWith.Value";
    protected static final String DMQL_DECIMAL_RANGELOW_FIELD = "Decimal.RangeLow.Field";
    protected static final String DMQL_DECIMAL_RANGELOW_VALUE = "Decimal.RangeLow.Value";
    protected static final String DMQL_DECIMAL_RANGEHIGH_VALUE = "Decimal.RangeHigh.Value";
    protected static final String DMQL_DECIMAL_MAXIMUM_FIELD = "Decimal.Maximum.Field";
    protected static final String DMQL_DECIMAL_MAXIMUM_VALUE = "Decimal.Maximum.Value";
    protected static final String DMQL_DECIMAL_MINIMUM_FIELD = "Decimal.Minimum.Field";
    protected static final String DMQL_DECIMAL_MINIMUM_VALUE = "Decimal.Minimum.Value";
    protected static final String DMQL_DATE_MAXIMUM_FIELD = "Date.Maximum.Field";
    protected static final String DMQL_DATE_MAXIMUM_VALUE = "Date.Maximum.Value";
    protected static final String DMQL_DATE_MINIMUM_FIELD = "Date.Minimum.Field";
    protected static final String DMQL_DATE_MINIMUM_VALUE = "Date.Minimum.Value";
    protected static final String DMQL_DATE_TODAY_FIELD = "Date.Today.Field";
    
    private static Properties testParams = null;
    
    protected String testResultStatus = null;
    protected String testResultDesc = null;       //testResult Description
    protected String testResultNotes = null;

    /**
     * @param notesExtraInfo
     * @param failureDescription
     */
    protected void setResultVarsErr(String notesExtraInfo, String failureDescription) {
        testResultStatus = "INFO";
        testResultNotes = "ERROR: Error in query or results returned: "
                + failureDescription + " - " + notesExtraInfo;
    }

    /**
     *  Initialize the test parameters.
     */
    private void initializeTestParams() {
    	testParams = PropertyManager.getParametersPropertyManager().getProperties();
    }
    

    /**
     * Get the field's standard name from the properties.
     *
     * @param keyBase The base "portion" from the key that comes after the designation of 
     *  "system" or "standard," which defines the dataType, the TestType and whether the
     *  prperty is a "field" or "that field's value".
     * @return The field's standard name.
     */
    protected String getDMQLProperty(String keyBase) {
    	if (testParams == null) {
    		initializeTestParams();
    	}
        String propertyValue = testParams.getProperty(getDMQLPropertyPrefix() + keyBase);
        if (log.isDebugEnabled()) {
            log.debug("Returned Property Value for " + keyBase + ": " + propertyValue);
        }
        return propertyValue;
    }
    
    /**
     * Get the field's standard name from the properties.
     *
     * @param keyBase The base "portion" from the key that comes after the designation of 
     *  "system" or "standard," which defines the dataType, the TestType and whether the
     *  prperty is a "field" or "that field's value".
     * @return The field's standard name.
     */
    protected String getDMQLProperty(String prefix,String keyBase) {
    	if (testParams == null) {
    		initializeTestParams();
    	}
        String propertyValue = testParams.getProperty(prefix + keyBase);
        if (log.isDebugEnabled()) {
            log.debug("Returned Property Value for " + keyBase + ": " + propertyValue);
        }
        return propertyValue;
    }
    /**
        Get the DMQL property prefix.
     */
    protected abstract String getDMQLPropertyPrefix();

    protected TestResult processResults(String transName, RETSTransaction t) {
        TestResult testResult = null;
        if (t instanceof RETSSearchTransaction) {
            RETSSearchTransaction searchTrans = (RETSSearchTransaction) t;
            testResult = processDMQLResults(transName, searchTrans);
        }

        return testResult;
    }
    
    /**
     * Process a DMQL search transaction, pull out the response body and call
     * compareDMQLResults() passing the body, selected search fields
     *
     * @param transName
     * @param searchTrans
     * @return
     */
    protected abstract TestResult processDMQLResults(String transName, 
    												 RETSSearchTransaction searchTrans);
}
