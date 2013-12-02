/*
 * BaseEvaluator.java
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.*;

import java.util.*;

import org.realtor.rets.retsapi.RETSTransaction;

/**
 * Base class that can be extended -- adds methods that could be used often by
 * TestEvaluators
 *
 * @author pobrien
 */
public abstract class BaseEvaluator implements TestEvaluator {
  /** Text describing relationship to the RETS Spec */
  protected String specReference = "";

  /**
   * Creates a new instance of BaseEvaluator
   */
  public BaseEvaluator() {
  }

/**
   * Class called by the TestExecuter to evaluate tranactions
   *
   * @param trans hashmap containing all transaction in a test script
   * @param testReport report to which to add test results
   *
   * @return empty string at this point
   */
  public String evaluate(HashMap trans, TestReport testReport) {
    Iterator itr = trans.keySet().iterator();
    int i = 1;
//add comment to test subversion
    while (itr.hasNext()) {
	 String transName=itr.next().toString();
	 System.out.println("inside evaluate transName is " + transName);
      RETSTransaction t = (RETSTransaction) trans.get(transName);

      if (t == null) {
        return null;
      }


        testReport.addTestResult(processResults(transName,t));

    }

    return "";
  }




  /**
   * Abstract method evaluate must be implemented by all TestEvaluators
   *
   * @param t RETSTransaction to be evaulated
   *
   *
   * @return TestResult passed back to the testExecuter.
   */
  protected abstract TestResult processResults(String transName,RETSTransaction t);

  /**
   * This method builds a test result object
   *
   * @param name name of the test performed
   * @param description describes the tests
   * @param status Success, Failure or Info
   * @param notes Notes about the test
   *
   * @return test result object to be returned
   */
  protected TestResult reportResult(String name, String description,
                                    String status, String notes) {
    TestResult result = new TestResult(name, description);
    result.setStatus(status);
    result.setNotes(notes);
    result.setEvaluatorClass(this.getClass().getName());
    result.setSpecificationReference(specReference);

    return result;
  }

  /**
   * This method builds a test result object
   *
   * @param name name of the test performed
   * @param description describes the tests
   * @param status Success, Failure or Info
   * @param notes Notes about the test
   * @param specRef describe how this test relates to the RETS Specification
   *
   * @return test result object to be returned
   */
  protected TestResult reportResult(String name, String description,
                                    String status, String notes, String specRef) {
    TestResult result = new TestResult(name, description);
    result.setStatus(status);
    result.setNotes(notes);
    result.setEvaluatorClass(this.getClass().getName());
    result.setSpecificationReference(specRef);

    return result;
  }

  /**
   * Report Results to
   * @param name Name of the test
   * @param description Describe the test
   * @param status Status of the test (Failure, Info, Success)
   * @param notes Any notes about the test
   * @param jException Java Exception thrown
   * @param specRef Reference to the RETS Spec.
   *
   * @return result object to be added to a test report
   */
  protected TestResult reportResult(String name, String description,
                                    String status, String notes,
                                    String jException, String specRef) {
    TestResult result = new TestResult(name, description);
    result.setStatus(status);
    result.setNotes(notes);
    result.setJavaException(jException);
    result.setEvaluatorClass(this.getClass().getName());
    result.setSpecificationReference(specRef);

    return result;
  }

  /**
   * This method builds a test result object
   *
   * @param trans RETS Transaction reporting the error
   * @param name name of the test performed
   * @param description describes the tests
   * @param status Success, Failure or Info
   *
   * @return test result object to be returned
   */
  protected TestResult reportResult(RETSTransaction trans, String name,
                                    String description, String status) {
    TestResult testResult = new TestResult(name, description);
    testResult.setJavaException("");
    testResult.setStatus(status);

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

    return testResult;
  }
}