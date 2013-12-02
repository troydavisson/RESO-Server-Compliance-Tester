package com.realtor.rets.compliance.tests;


/**
 * Extends the ValidateServerInfoResponse and validates against the 1.7 RETS ServerInfo DTD
 * @author pobrien
 */
public class ValidateServerInfoResponse17 extends ValidateServerInfoResponse {
  /**
   * Creates a new instance of ValidateServerInfo
   */
  public ValidateServerInfoResponse17() {
     super();
    setDocTypeLocation("RETS-SERVERINFO-1_7.dtd");
  }

}