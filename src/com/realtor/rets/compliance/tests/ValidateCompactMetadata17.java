package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateCompact to validate metadata responses
 * @author pobrien
 */
public class ValidateCompactMetadata17 extends ValidateCompact {
  /**
   * Creates a new instance of ValidateCompact
   */
  public ValidateCompactMetadata17() {
     super();
    setDocTypeLocation("RETS-COMPACT-METADATA-1_7.dtd");
  }

}