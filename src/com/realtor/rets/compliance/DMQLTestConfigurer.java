/* $Header$
 * @author pobrien
 */
package com.realtor.rets.compliance;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSConnection;

import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.metadata.MetadataFacade;

/**
 *  "Controller" Class for having the user run the
 *  setup for the Advanced DMQL tests
 *
 * Copyright 2004, Avantia inc.
 * @author $Author: pobrien $
 * @version $Revision: 1800 $
 * Check-In Date: $Date: 2007-04-15 15:37:23 -0400 (Sun, 15 Apr 2007) $
 */
public class DMQLTestConfigurer {

    private static Log log = LogFactory.getLog(DMQLTestConfigurer.class);

    private String logDir;
    private String password;
    private final String retsVer = "RETS/1.5";
    private String serverUrl;
    private HashMap m_testResultMap = new HashMap();
    private static final String msf_propsFileName = "C:/aopenden/development/deploy/retsCompliance/config/TestParameters.properties";
    private String userAgent;
    private String uaPassword;
    private String username;

    public DMQLTestConfigurer(String username, String password, String url,
                    String logDir, String userAgent, String uaPassword) {
        super();
        this.username = username;
        this.password = password;
        this.logDir = logDir;
        this.serverUrl = url;
        this.userAgent = userAgent;
        this.uaPassword = uaPassword;
    }

    public MetadataFacade setupDMQLTests() throws Exception {
        MetadataFacade mdFacade = new MetadataFacade();
        TestExecuter te = new TestExecuter();

        if (log.isDebugEnabled()) {
            log.debug("running Test with username: " + username);
        }
        RETSConnection RETSconn = te.loginAction(username, password, serverUrl, retsVer,
                                userAgent, logDir, uaPassword);

        String xmlFileName = PropertyManager.getConfigDirectory() + File.separator + "GetMetadataDMQLTest.xml";

        TestReport mdTestReport = te.execute(RETSconn, xmlFileName, msf_propsFileName);

        String testResponseBody =  null;
        testResponseBody = getTestResultRespBody(mdTestReport);
        boolean isMdXMLLoaded = false;
        try {
            isMdXMLLoaded = mdFacade.setMetaDataXML(testResponseBody);
        } catch (Exception e) {
            mdFacade = null;
            throw e;
        }


        return mdFacade;
    }

    /**
     * @param mdTestReport
     */
    private String getTestResultRespBody(TestReport mdTestReport) {
        String testResponseBody = null;

        ArrayList allTestResults = (ArrayList)mdTestReport.getTestResults();
        for(int i = 0; i < allTestResults.size(); i++) {
            TestResult mdTestResult = (TestResult)allTestResults.get(i);
            if (mdTestResult != null && mdTestResult.getName().startsWith("DMQL-MetadataSystem")) {
                testResponseBody = mdTestResult.getResponseBody();
            }

            if (log.isDebugEnabled()) {
                if (testResponseBody != null && testResponseBody.length() > 0) {
                    log.debug("Test Result: " + mdTestResult.getName() +
                            " has RespBody: " + testResponseBody.substring(0,300));
                } else {
                    log.error("ERROR: incorrect TestResult Response Body - CANNOT proceed with test ");
                }

            }
        }
        return testResponseBody;
    }

//Getters for class members
    /**
     * @return Returns the logDir.
     */
    public String getLogDir() {
        return logDir;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @return Returns the serverUrl.
     */
    public String getServerUrl() {
        return serverUrl;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
}
