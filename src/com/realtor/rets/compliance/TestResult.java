/*
 * TestResult.java
 *
  */
package com.realtor.rets.compliance;

/**
 * This class provides a means of storing the results of a RETS Compliance Test
 * 
 * 
 * @author pobrien
 */
public class TestResult {
  /** Creates a new instance of TestResult */
    
  String description = null;
  private String evaluatorClass;
  private String javaException = null;
  String name = null;
  private String notes;
  private String specificationReference;
  private String status = null;
  private String responseBody = null;

  /**
   * Creates a new TestResult object.
   */
  public TestResult() {
  }

  /**
   * Creates a new TestResult object.
   *
   * @param testName Name of the test performed
   * @param description Description of the test performed
   */
  public TestResult(String testName, String description) {
    name = testName;
    this.description = description;
  }

  /**
   * Returns the name of the test
   *
   * @return test name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the description of the test 
   *
   * @return test Description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Getter for property javaException.
   * 
   * @return Value of property javaException.
   */
  public String getJavaException() {
    return this.javaException;
  }

  /**
   * Setter for property javaException.
   * 
   * @param javaException New value of property javaException.
   */
  public void setJavaException(String javaException) {
    this.javaException = javaException;
  }

  /**
   * Getter for property status.
   * 
   * @return Value of property status.
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Setter for property status.
   * 
   * @param status New value of property status.
   */
  public void setStatus(String status) {
    this.status = status;
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
   * Setter for property name.
   * 
   * @param name New value of property name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for property notes.
   * 
   * @return Value of property notes.
   */
  public String getNotes() {
    return this.notes;
  }

  /**
   * Setter for property notes.
   * 
   * @param notes New value of property notes.
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Getter for property evaluatorClass.
   * 
   * @return Value of property evaluatorClass.
   */
  public String getEvaluatorClass() {
    return this.evaluatorClass;
  }

  /**
   * Setter for property evaluatorClass.
   * 
   * @param evaluatorClass New value of property evaluatorClass.
   */
  public void setEvaluatorClass(String evaluatorClass) {
    this.evaluatorClass = evaluatorClass;
  }

  /**
   * Getter for property specificationReference.
   * 
   * @return Value of property specificationReference.
   */
  public String getSpecificationReference() {
    return this.specificationReference;
  }

  /**
   * Setter for property specificationReference.
   * 
   * @param specificationReference New value of property
   *        specificationReference.
   */
  public void setSpecificationReference(String specificationReference) {
    this.specificationReference = specificationReference;
  }
    /**
     * @return Returns ONLY used now for special case DMQL test
     */
    public String getResponseBody() {
        return responseBody;
    }
    /**
     * @param responseBody ONLY used now for special case DMQL test to set value
     *      for the DMQLTestConfigurer class
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}