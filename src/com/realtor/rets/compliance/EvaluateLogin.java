/*
 * EvaluateLogin.java
 *
 * @author pobrien
 */
package com.realtor.rets.compliance;

import com.realtor.rets.compliance.gui.ReportForm;
import com.realtor.rets.compliance.tests.util.CollectionUtils;
import com.realtor.rets.compliance.tests.util.DateTimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtor.rets.retsapi.RETSLoginTransaction;

import java.util.*;

/**
 * Evaluates the login transaction
 * pobrien 
  */
public class EvaluateLogin {

    private static Log log = LogFactory.getLog(ReportForm.class);

    private boolean isRETS10 = false;
    private boolean isRETS172=false;

    private String[] optionalCapabilityURL18 = { "Action", "ChangePassword",
            "GetObject", "LoginComplete", "Logout", "Update","PostObject","GetPayloadList" };

    private String[] optionalCapabilityURL172 = { "Action", "ChangePassword",
                "GetObject", "LoginComplete", "Logout", "Update","ServerInformation" };

    private String[] optResponseKeys = { "Info", "Broker", "MemberName","MetadataVersion","MetadataTimestamp","MinMetadataTimestamp",
            "User" };

    private String[] optResponseArgs = { "Info", "Broker", "MemberName","MetadataVersion","MetadataTimestamp","MinMetadataTimestamp",
    "User" };

    private String[] requiredCapabilityURL = { "Login", "Search", "GetMetadata" };

    private String[] requiredResponseArgs18 = { "Info=BROKERCODE", "Info=BROKERBRANCH","Info=METADATAID","Info=METADATAVERSION","Info=METADATATIMESTAMP","Info=MINMETADATATIMESTAMP",
            "Info=USER","Info=MEMBERNAME","Info=USERID","Info=USERCLASS","Info=USERLEVEL" };

    private String[] requiredResponseArgs172 = { "BROKERCODE", "BROKERBRANCH","METADATAID","METADATAVERSION","METADATATIMESTAMP","MINMETADATATIMESTAMP",
            "USER","MEMBERNAME","USERID","USERCLASS","USERLEVEL" };

    /**
     * Creates a new instance of EvaluateLogin
     */
    public EvaluateLogin() {
    }

    /**
     * Evaluates a RETSLoginTransaction, makes sure all required capabilities,
     * required headers and authentication methods supported.
     * 
     * @param trans
     *            login transaction
     * 
     * @return test results
     */
    public TestReport execute(RETSLoginTransaction trans) {
        
        Calendar gmtCalendar = DateTimeUtils.getGMTDateTimeNow();
        
        TestReport testReport = new TestReport();
        testReport.setName("EvaluateLogin");
        testReport.setDescription("Validating the login response.");
        testReport.setTestConfigFile("Login");
        Map respMap = CollectionUtils.copyLowerCaseMap(trans.getResponseHeaderMap());

        TestResult retsVersionTestResult = getVersionTestResult(respMap);

        testReport.addTestResult(checkCapabilityURLs(trans, requiredCapabilityURL, true));
        if (isRETS172){
            testReport.addTestResult(checkOptionalCapabilityURLs(trans, optionalCapabilityURL172, false));
            testReport.addTestResult(checkOptionalCapabilityURLsUnsupported(trans, optionalCapabilityURL172, false));
            checkResponseArgs(trans,testReport);
        } else {
            testReport.addTestResult(checkOptionalCapabilityURLs(trans, optionalCapabilityURL18, false));
            testReport.addTestResult(checkOptionalCapabilityURLsUnsupported(trans, optionalCapabilityURL18, false));

        }
        testReport.addTestResult(retsVersionTestResult);

        if (! isRETS10) {
            checkResponseTags(trans, testReport);
        }

        checkResponseKeysOpt(trans, testReport);

        TestResult gmtDateTestResult = doGMTDateTest(respMap, gmtCalendar);
        testReport.addTestResult(gmtDateTestResult);

        return testReport;
    }

    /**
     * Checks authentication method(s)
     * 
     * @param auth
     *            collection of authentication methods
     * 
     * @return test result
     */
    public TestResult checkAuthenticate(Collection auth) {
        Iterator itr = auth.iterator();
        boolean basic = false;
        boolean digest = false;

        while (itr.hasNext()) {
            String str = (String) itr.next();

            if (str.startsWith("Basic")) {
                basic = true;
            }

            if (str.startsWith("Digest")) {
                digest = true;
            }
        }

        String description = "Supported Authentication methods include ";

        if (basic) {
            description = description + "[Basic] ";
        }

        if (digest) {
            description = description + "[Digest] ";
        }

        TestResult testResult = new TestResult("EvaluateLogin", description);
        testResult.setStatus("Info");
        testResult.setNotes("");

        return testResult;
    }

    /**
     * Checks for the required capability urls.
     * 
     * @param trans
     *            login transaction to check
     * @param keys
     *            keys passed
     * @param required
     *            are these required capabilities? If not only set Info status
     *            on missing URLs
     * 
     * @return test results
     */
    public TestResult checkOptionalCapabilityURLs(RETSLoginTransaction trans,
            String[] keys, boolean required) {
        String good = "";
        int goodCnt = 0;
        String bad = "";
        int badCnt = 0;

        for (int i = 0; i < keys.length; i++) {
            String val = trans.getCapabilityUrl(keys[i]);

            if (val == null) {
                if (badCnt++ == 0) {
                    bad = keys[i];
                } else {
                    bad = bad + ", " + keys[i];
                }
            } else {
                if (goodCnt++ == 0) {
                    good = keys[i];
                } else {
                    good = good + ", " + keys[i];
                }
            }
        }

        TestResult tr = new TestResult("EvaluateLogin", "Check for supported optional capability URLs");
        tr.setEvaluatorClass(this.getClass().getName());

        if (goodCnt > 0) {
                tr.setStatus("Info");
                tr.setNotes("Login transaction reported support for the following OPTIONAL capability URLS :"
                                + good);
            }


        return tr;
    }

        /**
     * Checks for the required capability urls.
     *
     * @param trans
     *            login transaction to check
     * @param keys
     *            keys passed
     * @param required
     *            are these required capabilities? If not only set Info status
     *            on missing URLs
     *
     * @return test results
     */
    public TestResult checkOptionalCapabilityURLsUnsupported(RETSLoginTransaction trans,
            String[] keys, boolean required) {
        String good = "";
        int goodCnt = 0;
        String bad = "";
        int badCnt = 0;

        for (int i = 0; i < keys.length; i++) {
            String val = trans.getCapabilityUrl(keys[i]);

            if (val == null) {
                if (badCnt++ == 0) {
                    bad = keys[i];
                } else {
                    bad = bad + ", " + keys[i];
                }
            } else {
                if (goodCnt++ == 0) {
                    good = keys[i];
                } else {
                    good = good + ", " + keys[i];
                }
            }
        }

        TestResult tr = new TestResult("EvaluateLogin", "Check for non-supported optional capability URLs");
        tr.setEvaluatorClass(this.getClass().getName());

        if (badCnt > 0) {
                tr.setStatus("Info");
                tr.setNotes("Login transaction does not support the following OPTIONAL capability URLS :"
                                + bad);
            }


        return tr;
    }

    /**
     * Checks for the required capability urls.
     *
     * @param trans
     *            login transaction to check
     * @param keys
     *            keys passed
     * @param required
     *            are these required capabilities? If not only set Info status
     *            on missing URLs
     *
     * @return test results
     */
    public TestResult checkCapabilityURLs(RETSLoginTransaction trans,
            String[] keys, boolean required) {
        String good = "";
        int goodCnt = 0;
        String bad = "";
        int badCnt = 0;

        for (int i = 0; i < keys.length; i++) {
            String val = trans.getCapabilityUrl(keys[i]);

            if (val == null) {
                if (badCnt++ == 0) {
                    bad = keys[i];
                } else {
                    bad = bad + ", " + keys[i];
                }
            } else {
                if (goodCnt++ == 0) {
                    good = keys[i];
                } else {
                    good = good + ", " + keys[i];
                }
            }
        }

        TestResult tr = new TestResult("EvaluateLogin", "Check capability URLs");
        tr.setEvaluatorClass(this.getClass().getName());

        if (badCnt > 0) {
            if (required) {
                tr.setStatus("Failure");
                if (goodCnt==0){
                tr.setNotes("Login transaction did not report support for the following REQUIRED capability URLS : "
                                + bad);
                } else {
                    tr.setNotes("Login transaction did not report support for the following REQUIRED capability URLS : "
                                                    + bad+ "but does support the following: "+good);

                }
            } else {
                tr.setStatus("Info");
                tr.setNotes("Login transaction did not report support for the following OPTIONAL capability URLS : "
                                + bad);
            }
        } else {
            tr.setStatus("Success");

            if (required) {
                tr.setNotes("Login transaction reported support for the following REQUIRED capability URLS :"
                                + good);
            } else {
                tr.setNotes("Login transaction reported support for the following OPTIONAL capability URLS :"
                                + good);
            }
        }

        tr.setSpecificationReference("4.13 page 32");

        return tr;
    }
    
    /**
     * Checks Login Response Arguments
     * 
     * @param trans
     *            Login Transaction
     * @param tr
     *            TestReport to which to add results of the test.
     */
    protected void checkResponseArgs(RETSLoginTransaction trans, TestReport tr) {
        Map map = trans.getResponseMap();
        
        TestResult result = new TestResult("EvaluateLogin",
                "Checking Required Response Arguments");
       
        String notes = "Fields found : ";
        String info = "";
        String missingRequiredFields = "";

        int missingCount = 0;
        int foundCount = 0;
        int badCaseCount = 0;
        String key = null;

            for (int i = 0; i < requiredResponseArgs172.length; i++) {
                if (map.get(requiredResponseArgs172[i]) == null) {
                    key = checkResponseCaseless(requiredResponseArgs172[i], map);

                    if (key == null) {
                        if (missingCount++ == 0) {
                            missingRequiredFields = requiredResponseArgs172[i];
                        } else {
                            missingRequiredFields = missingRequiredFields + ", "
                                    + requiredResponseArgs172[i];
                        }
                    } else {
                        badCaseCount++;

                        info = info + "\t Looking for \"" + requiredResponseArgs172[i]
                                + "\" found \"" + key + "\"\n";
                    }
                } else {
                    if (foundCount++ > 0) {
                        notes = notes + ", " + requiredResponseArgs172[i];
                    } else {
                        notes = notes + requiredResponseArgs172[i];
                    }
                }
            }

        if (missingCount > 0) {
            result.setStatus("Failure");
            result
                    .setNotes("Login transaction missing required response arguments:\n "
                            + missingRequiredFields);
            tr.addTestResult(result);
        } else if (badCaseCount > 0) {
            result.setStatus("Failure");
            result
                    .setNotes("Login transaction found the following required response arguments:\n  "
                            + notes
                            + " \n\n The following fields were found, but with an incorrect case: \n"
                            + info);
            tr.addTestResult(result);
        } else {
            result.setStatus("Success");
            result
                    .setNotes("Login transaction found all required response arguments:\n  "
                            + notes);
            tr.addTestResult(result);
        }
    }

    /**
     * Optional login response arguments to check
     * 
     * @param trans
     *            Transaction to check
     * @param tr
     *            TestReport to add results to
     */
    protected void checkResponseArgsOpt(RETSLoginTransaction trans,
            TestReport tr) {
        Map map = trans.getResponseMap();
        TestResult result = new TestResult("EvaluateLogin",
                "Checking Optional Response Tags");

        String notes = "Fields found : ";
        String info = "";
        String missingOptFields = "";

        int missingCount = 0;
        int foundCount = 0;
        int badCaseCount = 0;
        String key = null;

        for (int i = 0; i < optResponseArgs.length; i++) {
            if (map.get(optResponseArgs[i]) == null) {
                key = checkResponseCaseless(optResponseArgs[i], map);

                if (key == null) {
                    if (missingCount++ == 0) {
                        missingOptFields = optResponseArgs[i];
                    } else {
                        missingOptFields = missingOptFields + ", "
                                + optResponseArgs[i];
                    }
                } else {
                    badCaseCount++;

                    info = info + "\t Looking for \"" + optResponseArgs[i]
                            + "\" found \"" + key + "\"\n";
                }
            } else {
                if (foundCount++ > 0) {
                    notes = notes + ", " + optResponseArgs[i];
                } else {
                    notes = notes + optResponseArgs[i];
                }
            }
        }

        String display = "";

        if (missingCount > 0) {
            result.setStatus("Success");
            display += ("Login transaction does not support the following optional response arguments:\n "
                    + missingOptFields + "\n\n");
        }

        if (badCaseCount > 0) {
            result.setStatus("Failure");
            display += ("Login transaction supports the following optional response arguments, but with an incorrect case: \n"
                    + info + "\n\n");
        }

        if (foundCount > 0) {
            result.setStatus("Success");
            display += ("Login transaction supports the following optional response arguments:\n  "
                    + notes + " \n\n ");
        }

        result.setNotes(display);
        tr.addTestResult(result);
    }

    /**
     * Optional login response arguments to check
     * 
     * @param trans
     *            Transaction to check
     * @param tr
     *            TestReport to add results to
     */
    protected void checkResponseKeysOpt(RETSLoginTransaction trans,
            TestReport tr) {
        Map map = trans.getResponseMap();
        TestResult result = new TestResult("EvaluateLogin",
                "Checking Optional Response Keys");

        String notes = "Fields found : ";
        String info = "";
        String missingOptFields = "";

        int missingCount = 0;
        int foundCount = 0;
        int badCaseCount = 0;
        String key = null;

        for (int i = 0; i < optResponseKeys.length; i++) {
            if (map.get(optResponseKeys[i]) == null) {
                key = checkResponseCaseless(optResponseKeys[i], map);

                if (key == null) {
                    if (missingCount++ == 0) {
                        missingOptFields = optResponseKeys[i];
                    } else {
                        missingOptFields = missingOptFields + ", "
                                + optResponseKeys[i];
                    }
                } else {
                    badCaseCount++;

                    info = info + "\t Looking for \"" + optResponseKeys[i]
                            + "\" found \"" + key + "\"\n";
                }
            } else {
                if (foundCount++ > 0) {
                    notes = notes + ", " + optResponseKeys[i];
                } else {
                    notes = notes + optResponseKeys[i];
                }
            }
        }

        String display = "";

        if (missingCount > 0) {
            result.setStatus("Success");
            display += ("Login transaction does not support the following optional response arguments:\n "
                    + missingOptFields + "\n\n");
        }

        if (badCaseCount > 0) {
            result.setStatus("Failure");
            display += ("Login transaction supports the following optional response arguments, but with an incorrect case: \n"
                    + info + "\n\n");
        }

        if (foundCount > 0) {
            result.setStatus("Success");
            display += ("Login transaction supports the following optional response arguments:\n  "
                    + notes + " \n\n ");
        }

        result.setNotes(display);
        tr.addTestResult(result);
    }


    /**
     * Checks for RETS-RESPONSE tags
     * 
     * @param trans
     *            Login Transaction
     * @param tr
     *            TestReport to which to add results of the test.
     */
    protected void checkResponseTags(RETSLoginTransaction trans, TestReport tr) {
        Map map = trans.getResponseMap();
        TestResult result = new TestResult("EvaluateLogin",
                "Checking for RETS-RESPONSE Tags");

        String body = (String) map.get("body");

        if ((body != null) && (body.length() > 0)) {
            if ((body.indexOf("<RETS-RESPONSE>") == -1)
                    && (body.indexOf("</RETS-RESPONSE>") == -1)) {
                result.setStatus("Failure");
                result
                        .setNotes("Login transaction is missing both RETS-RESPONSE tags:"
                                + body);
                tr.addTestResult(result);
            } else if ((body.indexOf("<RETS-RESPONSE>") == -1)
                    || (body.indexOf("</RETS-RESPONSE>") == -1)) {
                result.setStatus("Failure");
                result
                        .setNotes("Login transaction is missing one of the RETS-RESPONSE tags:"
                                + body);
                tr.addTestResult(result);

            } else if ((body.indexOf("<RETS-RESPONSE>") >= 0)
                    && (body.indexOf("</RETS-RESPONSE>") >= 0)) {
                result.setStatus("Success");
                result
                        .setNotes("Login transaction has the required RETS-RESPONSE tags:"
                                + body);
                tr.addTestResult(result);
            }
        }
    }

    /**
     * Check Response, ignore case. Maybe the case was not the same as expected.
     * 
     * @param reqField
     *            required filed
     * @param map
     *            values to compare against
     * 
     * @return return found value
     */
    protected String checkResponseCaseless(String reqField, Map map) {
        Set respKeySet = map.keySet();
        Iterator iter = null;
        String key = null;

        iter = respKeySet.iterator();

        while (iter.hasNext()) {
            key = (String) iter.next();

            if (reqField.equalsIgnoreCase(key)) {
                return key;
            }
        }

        return null;
    }

    protected TestResult getVersionTestResult(Map respMap) {
        Collection versionHeaders;
        boolean isValidRETSVersion = false;
        String retsVersion = "";
        TestResult versionTestResult;

        versionHeaders = (Collection) respMap.get("rets-version");
        if (versionHeaders.size() > 1) {
            retsVersion = "MORE THAN ONE RETS_VERSION HEADER RETURNED";
        } else {
            Iterator headerIterator = versionHeaders.iterator();
            retsVersion = (String) headerIterator.next();
        }

        if (log.isDebugEnabled()) {
            log.debug("From Header retsVersion: " + retsVersion);
        }

    	if (retsVersion.equalsIgnoreCase("RETS/1.0")) {
    	    isRETS10 = true;
    	    isValidRETSVersion = true;
    	}
        else if (retsVersion.equalsIgnoreCase("RETS/1.5")) {
            isValidRETSVersion = true;
        } else if (retsVersion.equalsIgnoreCase("RETS/1.7")) {
            isValidRETSVersion = true;
        
        }
        else if (retsVersion.equalsIgnoreCase("RETS/1.7.2")) {
            isValidRETSVersion = true;
            isRETS172=true;
        }
        else if (retsVersion.equalsIgnoreCase("RETS/1.8")) {
            isValidRETSVersion = true;
        
        }
        else if (retsVersion.equalsIgnoreCase("RETS/1.8.0")) {
            isValidRETSVersion = true;
        
        }
        else {
            isValidRETSVersion = false;
        }
        versionTestResult = setVersionTestResult(isValidRETSVersion, retsVersion);

        return versionTestResult;
    }

    private TestResult setVersionTestResult(boolean isValidRETSVersion,
            String retsVersion) {
        TestResult versionTestResult = null;

        String testDescription = "Check RETS version Header";
        String testName = "EvaluateLogin";
        String testNotes;
        String testStatus;

        if (isValidRETSVersion == true) {
            testStatus = "Success";
            testNotes = "Login transaction Contains a correct RETS Version Header: "
                    + retsVersion;
        } else {
            testStatus = "Failure";
            testNotes = "Login transaction Contains an INCORRECT RETS Version in that header: "
                    + retsVersion;
        }

        versionTestResult = new TestResult(testName, testDescription);
        versionTestResult.setEvaluatorClass(this.getClass().getName());
        versionTestResult.setStatus(testStatus);
        versionTestResult.setNotes(testNotes);

        return versionTestResult;

    }

    protected TestResult doGMTDateTest(Map respMap, Calendar gmtCalendarNow) {
        TestResult GMTtestResult = null;

        Calendar calDateHeader  = null;
        Collection dateHeaders;
        String dateHeaderString = "";
        boolean isValidDate     = true;
        String testNotes;
        String testStatus       = "success";   
        
        dateHeaderString = getDateHeaderString(respMap);
        if (dateHeaderString.length() <= 0 ) {
            testNotes = "ERROR: " + dateHeaderString;
            testStatus = "failure";
            GMTtestResult = createDateHeaderTestResult(testNotes, testStatus);
        } else {            
            try {
                calDateHeader = DateTimeUtils.getGMTTimeFromString(dateHeaderString);
            } catch (Exception e) {                
                testStatus = "failure";
                testNotes = "Invalid Date Format.  Could Not Parse Date String in Header: "
                            + dateHeaderString;
                isValidDate = false;
            }         
        }        
        if (isValidDate) {     //we compare the 2 dates & determine if it is valid
            if (log.isDebugEnabled()){
                log.debug("Parsed Date: " + calDateHeader.getTime());
            }
                
            isValidDate = DateTimeUtils.CompareRETSTimeToNow(calDateHeader, gmtCalendarNow);
        }
        
        if (!isValidDate && testStatus.equalsIgnoreCase("success")) {
            testStatus = "failure";
            testNotes = "Login transaction Contains an INCORRECT Date - RETS Header Date is NOT in GMT: " 
                            + dateHeaderString;  
        } else {
            testNotes = "Login transaction Contains the correct RETS Date Header using GMT: " 
                            + dateHeaderString;                
        }
        
        GMTtestResult = createDateHeaderTestResult(testNotes, testStatus);
        return GMTtestResult;
    }

    /**
     * @param testNotes
     * @param testStatus
     * @return
     */
    private TestResult createDateHeaderTestResult(String testNotes, String testStatus) {
        TestResult GMTtestResult;
        
        String testDescription  = "Check Date Header";
        String testName         = "EvaluateLogin";
        
        GMTtestResult = new TestResult(testName, testDescription);
        GMTtestResult.setEvaluatorClass(this.getClass().getName());
        GMTtestResult.setStatus(testStatus);
        GMTtestResult.setNotes(testNotes);

        return GMTtestResult;
    }

    /**
     * @param respMap
     * @return
     */
    private String getDateHeaderString(Map respMap) {
        Collection dateHeaders;
        String dateHeaderString;
        
        dateHeaders = (Collection) respMap.get("date");
        
        if (dateHeaders == null) {
            dateHeaderString = "NO Date Header RETURNED";            
        } else if (dateHeaders.size() > 1){        
            dateHeaderString = "MORE than One Date Header RETURNED";
        } else {
            Iterator headerIterator = dateHeaders.iterator();
            dateHeaderString = (String) headerIterator.next();            
        }
        if (log.isDebugEnabled()) {
            log.debug("Date-Time STRING from Date Header: " + dateHeaderString);
        }
        return dateHeaderString;
    }
    
}
