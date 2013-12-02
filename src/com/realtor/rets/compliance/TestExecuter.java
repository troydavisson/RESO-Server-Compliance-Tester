/* $Header$
 
 */
package com.realtor.rets.compliance;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSConnection;
import org.realtor.rets.retsapi.RETSLoginTransaction;
import org.realtor.rets.retsapi.RETSTransaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The test Executer executes RETS Compliance tests defined in XML documents
 * Calls RETSTransactions defined in the XML documents and evaluates the
 * results of these transactions using the Evaluator classes referenced in the
 * XML. A precondition to using this class is a successful RETS login
 * transaction.
 * <p/>
 * <p/>

 */
public class TestExecuter {
    /**
     * for testing only, this connects to my local RETS server
     */
    HashMap<String, RETSTransaction> transHash = null;

    /**
     * This hash table contains a list of transactions that failed to execute
     */
    HashMap transErrorHash = null;

    /**
     * Name of the xml test file used
     */
    String testFileName = null;

    /**
     * Report generated for the login transaction
     */
    TestReport loginReport = null;

    /**
     * Rets connection object
     */
    private RETSConnection localConn = null;

    private static Log log = LogFactory.getLog(TestExecuter.class);

    /**
     * Creates a new instance of TestExecuter
     */
    public TestExecuter() {
    }

    /**
     * Finds the first instance of TestClient.properties in the class path and
     * uses it's rets server properties to execute test scripts.   Test scripts
     * executed are all .xml files found in the "TestScripts" directory (first
     * "TestScripts" directory found in the classpath)
     *
     * @param args No arguments are read in
     */
    public static void main(String[] args) {
        TestExecuter te = new TestExecuter();
        String username = null;
        String password = null;
        String loginurl = null;
        String useragent = null;
        String uaPassword = null;
        String logDir = null;
        String retsVer = null;

        readCommandLineOptions(args);

        try {
            Properties clientProps = PropertyManager.getClientPropertyManager().getProperties();
            username = clientProps.getProperty("username");
            password = clientProps.getProperty("password");
            loginurl = clientProps.getProperty("loginurl");
            useragent = clientProps.getProperty("uaPassword");
            uaPassword = clientProps.getProperty("useragent");
            logDir = clientProps.getProperty("transactionlogdirectory");
            retsVer = clientProps.getProperty("retsVersion");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RETSConnection conn = te.loginAction(username, password, loginurl,
                retsVer, useragent, logDir,uaPassword);

        // really need to get rid of this using System environment to pass variables
        File testFile = null;
        String testFileName = PropertyManager.getTestFile();
        if ( testFileName != null && (testFile = new File(testFileName)).exists() ) {
            TestReport report = te.execute(conn, testFile.getAbsolutePath());
            te.printReport(report);
        } else {
            // Otherwise, pull all test scripts from specified directory
            File[] files = te.getTestScriptList(PropertyManager.getTestDirectory());

            for (int i = 0; i < files.length; i++) {
                TestReport report = te.execute(conn, files[i].getAbsolutePath());
                te.printReport(report);
            }
        }
    }

    /**
     * Gets list of test script files ending in .xml from dirName directory
     *
     * @param dirName name of directory containing scripts
     */
    private File[] getTestScriptList(String dirName) {

        URL aURL = getClass().getClassLoader().getResource(dirName);

        File f = new File(aURL.getFile());

        java.io.FilenameFilter ff = new java.io.FilenameFilter() {
            public boolean accept(File dir, String name) {
                String upperCaseName = name.toUpperCase();

                return (upperCaseName.endsWith(".XML"));
            }
        };

        return f.listFiles(ff);
    }

    /**
     * Login to a rets server
     *
     * @param user     RETS server user id
     * @param password rets server password
     * @param retsVer  RETS-Version
     * @param loginUrl login URL
     * @return rets connection object
     */
    public RETSConnection loginAction(String user, String password,
                                      String loginUrl, String retsVer) {

        if ((retsVer == null) || (retsVer.trim().length() == 0)) {
            try {
                Properties clientProps = PropertyManager.getInstance("TestClient.properties").getProperties();
                retsVer = clientProps.getProperty("retsVersion");
            } catch (Exception e) {
                retsVer = "RETS/1.0";
            }
        }

        return loginAction(user, password, loginUrl, retsVer, null, null,null);
    }

    /**
     * Connect to a rets server, use a given userAgent string
     *
     * @param user        rets user
     * @param password    user's password to the rets server
     * @param loginUrl    server login url
     * @param retsVer     RETS-Version
     * @param userAgent   user agent string (see rets spec)
     * @param transLogDir directory to which to write output
     * @return Connection to the rets server
     */
    public RETSConnection loginAction(String user, String password,
                                      String loginUrl, String retsVer, String userAgent,
                                      String transLogDir, String uaPassword) {
        RETSConnection conn = new RETSConnection();
        conn.setTransactionLogDirectory("/tmp");

        if ((retsVer == null) || (retsVer.trim().length() == 0)) {
            try {
                Properties clientProps = PropertyManager.getClientPropertyManager().getProperties();
                retsVer = clientProps.getProperty("retsVersion");
                if (log.isDebugEnabled()) {
                    log.debug("setting retsver to " + retsVer);
                }
            } catch (Exception e) {
                retsVer = "RETS/1.0";
                log.debug("exception loading props, setting retsver to " + retsVer);
            }
        }

        conn.setRetsVersion(retsVer);


        RETSLoginTransaction trans = new RETSLoginTransaction();

        trans.getResponseStream();
        trans.setPassword(password);
        trans.setUsername(user);
        trans.setUrl(loginUrl);


        if (userAgent != null) {
            conn.setUserAgent(userAgent);
        }

		if (uaPassword != null) {
            conn.setUAPassword(uaPassword);
            conn.setUAHeader(null);
        }


        if (transLogDir != null) {
            conn.setTransactionLogDirectory(transLogDir);
        }

        conn.execute(trans);

        EvaluateLogin el = new EvaluateLogin();
        loginReport = el.execute(trans);
        loginReport.setValuesFromRETSConnection(conn);

        return conn;
    }

    /**
     * Returns a TestReport object with the results of the login transaction
     *
     * @return login results
     */
    public TestReport getLoginReport() {
        return loginReport;
    }

    /**
     * Prints the results of a test to standard out
     *
     * @param report test report to print
     */
    public void printReport(TestReport report) {

        if (report != null) {
            System.out.println(report.toString());
        }
    }

    /**
     * Executes a test report, logging into to a RETS server first.
     *
     * @param userName    rets user
     * @param password    user's password to the rets server
     * @param url         server login url
     * @param xmlFileName test file
     * @return Test report for the tests defined in xmlFileName
     */
    public TestReport execute(String userName, String password, String url, String retsVer, String xmlFileName) {
        if (localConn == null) {
            localConn = loginAction(userName, password, url, retsVer);
        }

        return execute(localConn, xmlFileName);
    }

    /**
     * Executes a test as defined in an xml file
     *
     * @param conn        An active connection to a RETS server (login transaction
     *                    already performed)
     * @param xmlFileName name of the xml file to load
     * @return test report for the tests defined in xmlFileName
     */
    public TestReport execute(RETSConnection conn, String xmlFileName) {
        Document document = null;
        File xmlFile = new File(xmlFileName);
        if (log.isDebugEnabled()) {
            if (xmlFile.exists()) {
                log.debug("XMLfilePath: " + xmlFile.getAbsolutePath());
            } else {
                log.debug("XMLfile: " + xmlFileName + " does not exist.");
            }

        }
        testFileName = xmlFileName;

        Properties params = PropertyManager.getParametersPropertyManager().getProperties();
        String outStr = null;
        try {
            outStr = Template.fillIn(xmlFile, params);
        } catch (TemplateException e) {
            e.printStackTrace();
            System.out.println(e + xmlFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e + xmlFileName);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(outStr)));
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            System.out.println(e + xmlFileName);
        } catch (org.xml.sax.SAXException e) {
            System.out.println(e + xmlFileName);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println(e + xmlFileName);
        }

        if (document == null) {
            return null;
        } else {
            return execute(conn, document);
        }
    }

    /**
     * Executes a test as defined in an xml file
     *
     * @param conn        An active connection to a RETS server (login transaction
     *                    already performed)
     * @param xmlFileName name of the xml file to load
     * @param props       TestParametersproperties fileName to load
     * @return test report for the tests defined in xmlFileName
     */
    public TestReport execute(RETSConnection conn, String xmlFileName, String propertiesFile) {
        Document document = null;
        File xmlFile = new File(xmlFileName);
        if (log.isDebugEnabled()) {
            if (xmlFile.exists()) {
                log.debug("XMLfilePath: " + xmlFile.getAbsolutePath());
            } else {
                log.debug("XMLfile: " + xmlFileName + " does not exist.");
            }

        }
        testFileName = xmlFileName;

        Properties params = null;
        try {
//            params = getPropertiesFromFile(propertiesFile);
            params = PropertyManager.getInstance(propertiesFile).getProperties();
        } catch (Exception e) {
            log.error("ERROR loading Properties file: " + propertiesFile);
            log.error(e);
        }

        String outStr = null;
        try {
            outStr = Template.fillIn(xmlFile, params);
        } catch (TemplateException e) {
            System.out.println(e + xmlFileName);
        } catch (IOException e) {
            System.out.println(e + xmlFileName);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(outStr)));
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            System.out.println(e + xmlFileName);
        } catch (org.xml.sax.SAXException e) {
            System.out.println(e + xmlFileName);
        } catch (java.io.IOException e) {
            System.out.println(e + xmlFileName);
        }

        if (document == null) {
            return null;
        } else {
            return execute(conn, document);
        }
    }


    /**
     * Executes a test as defined in an xml DOM document.
     * This method :
     * 1. Builds transaction objects from the XML document
     * 2. Executes the transactions
     * 3. Uses reflection to create instances of test evaluators
     * defined for the test
     * 4. Executes the test evaluators passing an array of
     * object containing transaction results.
     *
     * @param conn An active connection to a RETS server (login transaction
     *             already performed)
     * @param doc  DOM document defining a test.
     * @return Test report for the tests defined in doc
     */
    public TestReport execute(RETSConnection conn, Document doc) {
        TestReport testReport = new TestReport();
        testReport.setValuesFromRETSConnection(conn);
        
        String result = null;
        Element ele = doc.getDocumentElement();
        NodeList nl = ele.getChildNodes();
        RETSTransaction tmpTrans = null;

        // list of all transactions for processing
        transHash = new HashMap<String, RETSTransaction>();
        transErrorHash = new HashMap();

        //list of all evaluators to process the transactions
        ArrayList<String> evalList = new ArrayList<String>();

        // parse the child nodes of the root element (looking for transactions and
        // java classes
        testReport.setTestConfigFile(testFileName);

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);

            // if the node is a transaction, add it to the list of transactions
            if (node.getNodeName().equalsIgnoreCase("TestName")) {
                testReport.setName(getFirstChildValue(node));
            } else if (node.getNodeName().equalsIgnoreCase("TestDescription")) {
                testReport.setDescription(getFirstChildValue(node));
            } else if (node.getNodeName().equalsIgnoreCase("TestEvaluators")) {
                // if the node is a test evaluator java class, add it to the list
                // of test evaluators
                NodeList evals = node.getChildNodes();

                for (int k = 0; k < evals.getLength(); k++) {
                    Node n = evals.item(k);
                    Node firstChild = n.getFirstChild();

                    if (n.getNodeName().equals("JavaClassName") && (firstChild != null)) {
                        //                       System.out.println("Java class : "+firstChild.getNodeValue());
                        evalList.add(firstChild.getNodeValue());
                    }
                }
            } else if (node.getNodeName().charAt(0) != '#') {
                TestTransaction testTrans = buildTestTransaction(node);
                executeTransaction(conn, testTrans, testReport);
            }


            //  this is for debugging,
            //        if you add more things to the schema/xml file, uncomment this to
            //            help in debugging
            //        else
            //            System.out.println("unparsed :"+node.getNodeName());
        }

        executeTests(transHash, evalList, testReport);
        return testReport;
    }


    private void executeTransaction(RETSConnection conn, TestTransaction trans,
                                    TestReport testReport) {
        if (executeTransaction(conn, trans.getTransaction(), testReport)) {
            HashMap<String, RETSTransaction> transMap = new HashMap<String, RETSTransaction>();
            
            testReport.setValuesFromRETSConnection(conn);
            transMap.put(trans.getName(), trans.getTransaction());
            
            executeTests(transMap, trans.getTestList(), testReport);
        }
    }

    private void executeTests(HashMap<String, RETSTransaction> transMap, List<String> testList, TestReport testReport) {

        // here we get the evaluators
        try {
            Iterator<String> itr = testList.iterator();

            while (itr.hasNext()) {
                String name = itr.next();
                System.out.println("inside executeTests test name is " + name);
                // get new instance of a test evaluator using reflection
                TestEvaluator te = (TestEvaluator) Class.forName(name).newInstance();


                // call the evaluate method on the test evaluator passing
                // all the transaction object we just created
                te.evaluate(transMap, testReport);
            }
        } catch (Exception e) {
            e.printStackTrace();
            testReport.addTestResult(reportException(e, "Exception thrown"));
        }
    }


    /**
     * Executes an individual RETS Transaction (trans)
     *
     * @param conn  an active rets connection (login performed)
     * @param trans transaction to execute
     * @return returns true on sucessful execution of a RETS transaction
     * @throws Exception client API might run into an exception
     */
    private boolean executeTransaction(RETSConnection conn, RETSTransaction trans,
                                       TestReport testReport) {
        /*if (trans.getRequestType().equalsIgnoreCase("GetMetadata")) {
          RETSGetMetadataTransaction gmt = (RETSGetMetadataTransaction) trans;

          if (gmt.getVersion() != null) {
            conn.setRequestHeaderField("RETS-Version", gmt.getVersion());

            //System.out.println("Setting header field :"+gmt.getVersion());
          }
        }*/
        try {
            conn.execute(trans);
        } catch (Exception e) {
            testReport.addTestResult(reportTransactionException(e, trans));
            e.printStackTrace();
            return false;
        }

        if (trans.getResponseStatus() != null)
            if (!trans.getResponseStatus().equalsIgnoreCase("0")) {
                TestResult testResult = new TestResult("Response Status " +
                        trans.getResponseStatus(),
                        "Transaction " + trans.getClass().getName() +
                        " return a response Status of " +
                        trans.getResponseStatus());
                testResult.setJavaException("");
                testResult.setStatus("Info");

                StringBuffer sb = new StringBuffer();
                sb.append("Transaction Name : " + trans.getClass().getName());
                sb.append("\nRequest Type : " + trans.getRequestType());
                sb.append("\nTransaction arguments : \n");
                Map rMap = trans.getRequestMap();
                if (rMap != null) {
                    Iterator itr = rMap.keySet().iterator();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        sb.append("\n\t" + key + "=" + (String) rMap.get(key));
                    }
                }
                testResult.setNotes(sb.toString());
                testReport.addTestResult(testResult);
            }
        return true;
    }


    /**
     * Executes an external method call as defined in an xml document conforming
     * to the schema "RETSCompliance.xsd".  The schema defines an external
     * method call as follows : the param qnl is the root node of an
     * ExternalMethodCall tag, in the example above, the would be the
     * DynamicQueryGenerationMethod node.
     * <p/>
     * <p/>
     * <xs:complexType name="ExternalMethodCall">
     * <xs:sequence>
     * <xs:element name="JavaClassName" type="xs:string"/>
     * <xs:element name="MethodName" type="xs:string"/>
     * <xs:element name="TransactionPassed" type="xs:string" minOccurs="0" maxOccurs="1"/>
     * </xs:sequence>
     * </xs:complexType>
     * <p/>
     * In "RETSCompilance.xsd", the DynamicQueryGenerationMethod tag in the
     * definition of SearchTransaction is of the ExternalMethodCall so instances
     * of DynamicQueryGenerationMethod will be required to have child tags :
     * JavaClassName, MethodName and ond optionally TransactionPassed. Below is an
     * example :
     * <p/>
     * <p/>
     * <DynamicQueryGenerationMethod>
     * <JavaClassName>com.realtor.rets.compliance.tests.util.QueryBuilders</JavaClassName>
     * <MethodName>propertyQueryFromMetadata</MethodName>
     * <TransactionPassed>Metadata Property Search</TransactionPassed>
     * </DynamicQueryGenerationMethod>
     *
     * @param qnl root ExternalMethodCall node.
     * @return string returned from the external method call.
     */
    private String ExternalMethodCall(NodeList qnl) {
        String qClass = null;
        String qMethod = null;
        List<String> qTransaction = new ArrayList<String>();

        for (int q = 0; q < qnl.getLength(); q++) {
            Node qnode = qnl.item(q);
            String qname = qnode.getNodeName();

            if (qname.equalsIgnoreCase("JavaClassName")) {
                qClass = getFirstChildValue(qnode);
            } else if (qname.equalsIgnoreCase("MethodName")) {
                qMethod = getFirstChildValue(qnode);
            } else if (qname.equalsIgnoreCase("TransactionPassed")) {
                qTransaction.add(getFirstChildValue(qnode));
            }
        }

        try {
            Class c = Class.forName(qClass);
            Class[] ca = new Class[1];


            //ca[0] = RETSTransaction[].class;
            ca[0] = RETSTransaction.class;

            Method m = c.getMethod(qMethod, ca);
            RETSTransaction[] rts = new RETSTransaction[qTransaction.size()];

            for (int z = 0; z < qTransaction.size(); z++) {
                RETSTransaction trans = transHash.get(qTransaction.get(z));

                if (trans != null) {
                    rts[z] = trans;
                } else {
                }
            }

            String query = (String) m.invoke(null, rts);

            return query;
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("The class you are looking for was not found: "
                    + qClass);

            e.printStackTrace();
        } catch (java.lang.NoSuchMethodException nsme) {
            System.out.println("The method you are looking for was not found: "
                    + qMethod);
            nsme.printStackTrace();
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return null;
    }

    /**
     * Helper method to get child text values of a node
     *
     * @param node object to get child text value from
     * @return text value of the first child of the current node
     */
    private String getFirstChildValue(Node node) {
        Node firstChild = node.getFirstChild();

        if (firstChild != null) {
            return firstChild.getNodeValue();
        }

        return null;
    }


    /**
     * Builds  Test Transactions based on an XML definition.
     *
     * @param node defining the parameters for a
     *             transaction
     * @return transaction built
     */
    private TestTransaction buildTestTransaction(Node node) {

        RETSTransaction trans = null;
        String tranType = "org.realtor.rets.retsapi.RETS" + node.getNodeName();

        try {
            Class c = Class.forName(tranType);
            trans = (RETSTransaction) c.newInstance();

        } catch (Exception e) {
            System.out.println("Error creating instance of class:" + node.getNodeName() + " " + e);

        }

        NodeList nl = node.getChildNodes();
        String transLabel = null;
        List<String> transTestList = new ArrayList<String>();


        for (int i = 0; i < nl.getLength(); i++) {
            Node childNode = nl.item(i);

            String methodName = childNode.getNodeName();

            if (methodName.equalsIgnoreCase("TransactionLabel")) {
                transLabel = getFirstChildValue(childNode);
            } else if (methodName.equalsIgnoreCase("TestEvaluators")) {
                transTestList = getEvalList(childNode);
            } else if (methodName.charAt(0) != '#') {
                methodName = "set" + methodName;
                System.out.println("testTransaction method is " + methodName);
                try {
                    Method mSet = trans.getClass().getMethod(methodName, new Class[]{String.class});
                    mSet.invoke(trans, new Object[]{getFirstChildValue(childNode)});
                } catch (NoSuchMethodException e) {
                    System.out.println("no such method found " + methodName);
                } catch (Exception e) {
                    System.out.println("exception found in " + methodName + " " + e);
                }
            }


        }

        transHash.put(transLabel, trans);

        return new TestTransaction(transLabel, trans, transTestList);
    }


    /**
     * Reports an excepption in a testResult object
     *
     * @param e       Exception to report
     * @param message Additional message used for notes
     * @return test Result object
     */
    public static TestResult reportException(Exception e, String message) {
        TestResult testResult = new TestResult("Exception Thrown", message);
        testResult.setJavaException(e.toString());
        testResult.setStatus("Exception");
        testResult.setNotes(message);

        return testResult;
    }

    /**
     * Reports an excepption in a testResult object
     *
     * @param e       Exception to report
     * @param message Additional message used for notes
     * @return test Result object
     */
    public static TestResult reportTransactionException(Exception e, RETSTransaction trans) {
        TestResult testResult = new TestResult("Exception Thrown", e.toString());
        testResult.setJavaException(e.toString());
        testResult.setStatus("Exception");

        StringBuffer sb = new StringBuffer();
        sb.append("Transaction Name : " + trans.getClass().getName());
        sb.append("\nRequest Type : " + trans.getRequestType());
        sb.append("\nTransaction arguments : \n");
        Map rMap = trans.getRequestMap();
        if (rMap != null) {
            Iterator itr = rMap.keySet().iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                sb.append("\t" + key + "=" + (String) rMap.get(key));
            }
        }
        testResult.setNotes(sb.toString());

        return testResult;
    }

    /**
     	Read the command line options.
    */
    public static void readCommandLineOptions(String[] args) {
    	for (int i = 0; i < args.length; i++) {
    		String arg = args[i];

			if (arg.equals("-t")) {
				PropertyManager.setTestDirectory(args[++i]);
	        }
			else if (arg.equals("-c")) {
				PropertyManager.setConfigDirectory(args[++i]);
			}
			else if (arg.equals("-f")) {
				PropertyManager.setTestFile(args[++i]);
	        }
    	}
    }

    private List<String> getEvalList(Node node) {
        ArrayList<String> eList = new ArrayList<String>();
        NodeList evals = node.getChildNodes();
        for (int k = 0; k < evals.getLength(); k++) {

            Node n = evals.item(k);
            Node firstChild = n.getFirstChild();

            if (n.getNodeName().equals("JavaClassName") && (firstChild != null)) {
                eList.add(firstChild.getNodeValue());
            }
        }
        return eList;
    }

    class TestTransaction {
        RETSTransaction trans;
        List<String> testList;
        String name;

        TestTransaction(String aName, RETSTransaction aTrans, List<String> aTestList) {
            name = aName;
            trans = aTrans;
            testList = aTestList;
        }

        RETSTransaction getTransaction() {
            return trans;
        }

        List<String> getTestList() {
            return testList;
        }

        String getName() {
            return name;
        }
    }

}