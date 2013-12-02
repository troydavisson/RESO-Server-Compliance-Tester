/* $Header$
 * Created on Nov 19, 2004
 */
package com.realtor.rets.compliance.tests.dmql;

import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.metadata.MetadataUtils;
import org.apache.commons.jxpath.xml.JDOMParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.realtor.rets.retsapi.RETSSearchTransaction;

import java.io.InputStream;
import java.util.Properties;

/**
 * Base class for DMQL Result Evaluator classes will evaluate each of the standard
 * DMQL search (dynamically created) test transacions
 * <p/>
 *
 * @author pobrien
 */
public abstract class DMQLResultsStandard extends DMQLResults {

    private static Log log = LogFactory.getLog(DMQLResultsStandard.class);

    private static final String [] DTD_ELEMENTS = new String [] {"RETS-20021015.dtd", "RETS-20041001.dtd"};
//    private final static String DTD_ELEMENT = "<!DOCTYPE RETS SYSTEM \"RETS-20021015.dtd\">";

    private Properties strdNamesToXPathProps;

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
        TestResult dmqlTestResult = null;

        String xPathmappingFile = "retsXPath.properties";
        strdNamesToXPathProps = new Properties();
        try {
            strdNamesToXPathProps = PropertyManager.getInstance(xPathmappingFile).getProperties();
        } catch (Exception e) {
            log.error("ERROR: could not load XPath mapping file! " + e);
        }

        String responseBody = (String) searchTrans.getResponseMap().get("body");
        String testSearchSelect = (String) searchTrans.getSearchQuery();
        if (log.isDebugEnabled()) {
            String testSearchType = (String) searchTrans.getSearchType();
            log.debug("From request SearchSelect: " + testSearchSelect);
            log.debug("searchType: " + testSearchType);
            log.debug("transName: " + transName);
        }

        Document queryResponseDoc = null;

        String respBodyNoDTD = responseBody;
        for (int i = 0; i < DTD_ELEMENTS.length; i++) {
            respBodyNoDTD = respBodyNoDTD.replaceAll("<!DOCTYPE RETS SYSTEM \"" + DTD_ELEMENTS[i]+ "\">", "");
        }
        JDOMParser domParser = new JDOMParser();
        InputStream thisInputStream = MetadataUtils.InputStreamFromString(respBodyNoDTD);
        //todo: put next line into Try-Catch block & report ERROR back if hit catch
        queryResponseDoc = (Document) domParser.parseXML(thisInputStream);

        if (searchTrans.getSearchQueryType() == null) { // could just not add the TestResult

            dmqlTestResult = reportResult(transName + " DMQLTestResults",
                    "Compare requested fields to response fields", "Failure",
                    "No fields were requested in the search transaction, nothing to do here");
        } else {
            dmqlTestResult = compareDMQLResults(transName, responseBody, queryResponseDoc);
        }

        return dmqlTestResult;
    }

    /**
     * Compare a DMQL Search Transaction response to the requested select
     * fields from the request.  Reports any missing response fields.
     *
     * @param transName        name of this DMQL search transaction
     * @param responseBody     body of the search transaction
     * @param queryResponseDoc JDOM document object loaded into a response Object
     * @return TestResult object - result of the test
     */
    protected abstract TestResult compareDMQLResults(String transName,
                                                     String responseBody,
                                                     Document queryResponseDoc);
    
    /**
     * @param responseBody
     * @param fieldStrdName
     * @param xPathQuery
     * @param e
     */
    protected void setResultVarsXPathException(String responseBody, String fieldStrdName, String xPathQuery, Exception e) {
        testResultStatus = "INFO";
        testResultNotes = "ERROR: The following XPath query " + xPathQuery
                + " did not work on field named " + fieldStrdName
                + " for the XML response: " + '\n' + responseBody;
    }

    /**
     * Get the XPath query for the given standard name.
     *
     * @param strdName The standard name of the field for which to fetch the XPath query.
     * @return The XPath query for the given standard name.
     */
    protected String getXPathQuery(String fieldStandardName) {
        String xPathQuery = strdNamesToXPathProps.getProperty(fieldStandardName);
        if (log.isDebugEnabled()) {
            log.debug("XPath Query: " + xPathQuery);
        }
        return xPathQuery;
    }

    /**
        Get the DMQL property prefix.
    */
    protected String getDMQLPropertyPrefix() {
        return "DMQL.Standard.";
    }
}
