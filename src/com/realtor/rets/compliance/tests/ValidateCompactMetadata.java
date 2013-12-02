package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate metadata responses
 * @author pobrien
 */
public class ValidateCompactMetadata extends ValidateCompact {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactMetadata() {
     super();
    setDocTypeLocation("RETSCOMPACTMETA-20030710.dtd");
  }

}