/* $Header$
 */
package com.realtor.rets.compliance.junit;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.realtor.rets.retsapi.RETSConnection;

import com.realtor.rets.compliance.TestExecuter;
import com.realtor.rets.compliance.TestReport;
import com.realtor.rets.compliance.TestResult;

/**
 *  JUnit generic testCase class for testing ServerCompliance Evaluator classes
 *
 * @author pobrien
 */
public class EvaluatorTest extends TestCase {

    private static Log log = LogFactory.getLog(EvaluatorTest.class);

    private final static String password = "password";
    protected RETSConnection RetsConn = null;
    private String serverUrl = "http://localhost:8080/rets/server/login";
    private String transLogDir = "C:/tmp/ComplianceLogs";
    private String userAgent = "";
    private String username = "266123";
    private String uaPassword;
    protected String appPath = System.getProperty("user.dir");

    protected TestExecuter executer;

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        String[] appArgs = new String[4];
        appArgs[0] = "-p";
        appArgs[1] = "TestParameters.properties";
        appArgs[2] = "-u";
        appArgs[3] = "C:/aopenden/development/deploy/retsCompliance/config/TestClient.properties";

        TestExecuter.readCommandLineOptions(appArgs);

        executer = new TestExecuter();
        RetsConn = executer.loginAction(username, password, serverUrl, null,
                userAgent, transLogDir,uaPassword);

        assertNotNull("Rets Connection is null", RetsConn);

        TestReport testReport = executer.getLoginReport();
        assertNotNull("The Test Report from the LoginAction is null", testReport);
        Collection col = testReport.getTestResults();
        assertNotNull("The Test RESULTS from the LoginAction are null", testReport);
        Iterator itr = col.iterator();

        while (itr.hasNext())
        {
            TestResult test = (TestResult) itr.next();
            String loginTestResult = test.getName() + " has a status of: " + test.getStatus()
                        + "; and a description of: " + test.getDescription();
            log.debug(loginTestResult);
        }
    }

    /**
     * Constructor for EvaluatorTest.
     * @param arg0
     */
    public EvaluatorTest(String arg0) {
        super(arg0);
    }

}
