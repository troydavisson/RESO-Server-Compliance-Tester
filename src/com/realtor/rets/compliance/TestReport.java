/*
 * TestReport.java
 *
 */
package com.realtor.rets.compliance;

import org.realtor.rets.retsapi.RETSConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * The TestReport object is intended to contain a collection of related TestResult
 * Objects.  This structure should make it easy to output to any type of report
 * format desired.
 *
 */
public class TestReport {
  /** Holds value of property name. */
  private String name;

  /** Holds value of property description. */
  private String description;

  /** Holds value of property testConfigFile. */
  private String testConfigFile;

  /** Collection of TestResult objects */
  private ArrayList testResults;

private String sessionId;

private String userAgent;

private String password;

private String userId;

private String timestamp;

private String serverProfileUrl;

  /**
   * Creates a new instance of TestReport
   */
  public TestReport() {
    testResults = new ArrayList();
  }

  /**
   * Creates a new instance of TestReport
   * @param name Name of the test report
   * @param description Description of the test report
   * @param configFile Test Script used to generate the test report
   */
  public TestReport(String name, String description, String configFile) {
    testResults = new ArrayList();
    setName(name);
    setDescription(description);
    setTestConfigFile(configFile);
  }

  /**
   * Getter for property name.
   *
   * @return Value of property name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for property name.
   *
   * @param name New value of property name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for property description.
   *
   * @return Value of property description.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Setter for property description.
   *
   * @param description New value of property description.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Getter for property testConfigFile.
   *
   * @return Value of property testConfigFile.
   */
  public String getTestConfigFile() {
    return this.testConfigFile;
  }

  /**
   * Setter for property testConfigFile.
   *
   * @param testConfigFile New value of property testConfigFile.
   */
  public void setTestConfigFile(String testConfigFile) {
    this.testConfigFile = testConfigFile;
  }

  /**
   * Adds a TestResult object to the report
   *
   * @param tr test result to add
   */
  public void addTestResult(TestResult tr) {
    if (tr!=null)
    {
		testResults.add(tr);
	}
  }

  /**
   * Returns the collection of TestResult objects
   *
   * @return the collection of test results in this report
   */
  public Collection getTestResults() {
    return testResults;
  }

  public String generateXML() {
      StringBuffer stringBuffer = new StringBuffer();

//      stringBuffer.append("<TestReport>\n");
      stringBuffer.append("  <TestSuite name=\"" + getName() +
              "\" description=\"" + getDescription() +
              "\" config=\"" + getTestConfigFile() + "\">\n");
      stringBuffer.append("      <timestamp>" +getTimestamp() + "</timestamp>\n");
      stringBuffer.append("      <userId>" + getUserId()+ "</userId>\n");
      stringBuffer.append("      <password>" + getPassword()+ "</password>\n");
      stringBuffer.append("      <userAgent>" + getUserAgent()+ "</userAgent>\n");
      stringBuffer.append("      <sessionId>" + getSessionId()+ "</sessionId>\n");
      stringBuffer.append("      <serverProfileUrl>" +getServerProfileUrl()+ "</serverProfileUrl>\n");
      TestResult test = null;
      Iterator iterator = getTestResults().iterator();
      while ( iterator.hasNext() ) {
          test = (TestResult) iterator.next();
          stringBuffer.append("    <Test>\n");
          stringBuffer.append("      <name>" + test.getName() + "</name>\n");
          stringBuffer.append("      <retsStatus>" + test.getRetsReplyCode() + "</retsStatus>\n");
          stringBuffer.append("      <description><![CDATA[" + test.getDescription() + "]]></description>\n");
          stringBuffer.append("      <status>" + test.getStatus()+ "</status>\n");
          stringBuffer.append("      <notes>\n");
          stringBuffer.append("       <![CDATA[" + test.getNotes() + "]]>");
          stringBuffer.append("      </notes>\n");
          
          stringBuffer.append("    </Test>\n");
      }
      
      stringBuffer.append("  </TestSuite>\n");
//      stringBuffer.append("</TestReport>\n");
      return stringBuffer.toString();

// JAXB - do it later
//      com.realtor.rets.compliance.jaxb.TestReportType testReport =
//             new com.realtor.rets.compliance.jaxb.impl.TestReportImpl();
//
//      com.realtor.rets.compliance.jaxb.Test
//      testReport.TestReport(getName(), getDescription(), getTestConfigFile());
//      TestResult testResult = null;
//
//      Iterator iterator = getTestResults().iterator();
//      while ( iterator.hasNext() ) {
//          TestResult test = (TestResult) iterator.next();
//
//      }

  }
  public String getSessionId() {
	return sessionId;
}

public TestReport setSessionId(String sessionId) {
	this.sessionId = sessionId;
	
	return this;
}

public String getUserAgent() {
	return userAgent;
}

public TestReport setUserAgent(String userAgent) {
	this.userAgent = userAgent;
	return this;
}

public String getPassword() {
	return password;
}

public TestReport setPassword(String password) {
	this.password = password;
	return this;
}

public String getUserId() {
	return userId;

}

public TestReport setUserId(String userId) {
	this.userId = userId;
	return this;
}

public String getTimestamp() {
	return timestamp;
}

public TestReport setTimestamp(String timestamp) {
	this.timestamp = timestamp;
	return this;
}

public String getServerProfileUrl() {
	return serverProfileUrl;
}

public TestReport setServerProfileUrl(String serverProfileUrl) {
	this.serverProfileUrl = serverProfileUrl;
	return this;
}

public TestReport setTestResults(ArrayList testResults) {
	this.testResults = testResults;
	return this;
}

public TestReport setValuesFromRETSConnection(RETSConnection conn){
	setServerProfileUrl(conn.getServerUrl());
	setSessionId(conn.getSessionId());
	setUserAgent(conn.getUserAgent());
	setUserId(conn.getUsername());
	setPassword(conn.getPassword());
	setTimestamp(new java.util.Date().toString());
	return this;
}

public String toString() {


      StringBuffer sb = new StringBuffer();

      sb.append("Test name : ");
      sb.append(getName());
      sb.append("\n");
      sb.append("Test description : ");
      sb.append(getDescription());
      sb.append("\n");
      sb.append("Test configuration file : ");
      sb.append(getTestConfigFile());
      sb.append("\n");

      Collection results = getTestResults();
      Iterator itr = results.iterator();

      while (itr.hasNext()) {
        TestResult test = (TestResult) itr.next();
        sb.append("   Test Name : [");
        sb.append(test.getName());
        sb.append("]\n  Status : [");
        sb.append(test.getStatus());
        sb.append("]\n  Description : [");
        sb.append(test.getDescription());
        sb.append("]\n  Notes : [");
        sb.append(test.getNotes());
        sb.append("]\n  Java Class : [");
        sb.append(test.getEvaluatorClass());
        sb.append("]\n");
      }

      return sb.toString();
    }
}
