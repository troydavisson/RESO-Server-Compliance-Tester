/*
 * Test1.java
 *
 */
package com.realtor.rets.compliance.tests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.realtor.rets.retsapi.RETSTransaction;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;

/**
 * Extends the BaseEvaluator Interface to validate against a DTD
 *
 * @author pobrien
 */
public class ValidateLogoutResponse extends BaseEvaluator {

  public ValidateLogoutResponse() {
    super();
    specReference = "";
  }

private String notes="";
private String docTypeLocation="";
boolean error=false;
private String errorList="";
  /**
   * Evaluate a set of transactions. This method is called by the TestExecuter
   * Checks the XML response against the RETS DTD
   *
   * @param trans Hash containing all transactions defined in a test script
   * @param testReport Report to which to add individual testResult objects.
   *
   * @return just returns an empty string for now.
   */
  protected TestResult processResults(String transName, RETSTransaction t) {
      if (t == null) {
        return null;
      }

      Map resp = t.getResponseMap();
      String cName = t.getClass().getName();

      Map m = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
	  boolean isXml = CollectionUtils.hasValue(m,"content-type","text/xml");
      int cnt=CollectionUtils.keyCount(m,"content-type");


        if (cnt > 1)
        {
            String mContent = "Multiple Content-Type's are reported the response for transaction : "+
                               transName;
            String note = "Some clients may fail or be confused if multiple 'Content-Type' values returned ";
            return reportResult("Validate Compact", mContent,"Info",note,"n/a");

        }




        if(!isXml){
    	    notes+= "Transaction " + cName
            + " does not include a Content-Type of 'text/xml'";
		}
         String body = (String) resp.get("body");
		 return validateLogoutResponse(transName,body);




  }

  /**
   * This is the method that does the actual check of a transaction body for a valid
   *  compact data response.
   *
   * @param transactionName Name of the Transaction checked (from the test script)
   * @param responseBody transaction body which should be valid compact
   *
   * @return results of the test.
   */
  protected TestResult validateLogoutResponse(String transactionName,
                                       String responseBody) {
    Document document = null;
    boolean valid=true;
    String status = "Failure";
	notes="";
	errorList="";
	 error=false;
    String jException="";


    try {
      if ((responseBody != null) && (responseBody.length() > 0)) {


		responseBody="<?xml version=\"1.0\"?>"+"\r\n<!DOCTYPE RETS SYSTEM \""+docTypeLocation+"\">\r\n"+responseBody;


        ByteArrayInputStream bais = new ByteArrayInputStream(
                                        (responseBody).getBytes());

        String inputSourceUrl = "file:" + PropertyManager.getConfigDirectory().replace(File.separatorChar, '/') + "/";
        InputSource is = new InputSource(inputSourceUrl);
        is.setByteStream(bais);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new org.xml.sax.ErrorHandler(){
			public void warning(org.xml.sax.SAXParseException e){
				appendNotes("Warning: "+xmlParseErrorMessage(e)+"\n");
			}
			public void error(org.xml.sax.SAXParseException e){
				appendNotes("Error: "+xmlParseErrorMessage(e)+"\n");
				error=true;
			}
			public void fatalError(org.xml.sax.SAXParseException e)
			throws org.xml.sax.SAXParseException{
				appendNotes("Fatal Error: "+xmlParseErrorMessage(e)+"\n");
				error=true;
				throw e;
			}
		});

        document = builder.parse(is);

      }

      // if we can create an XML document from the stream using a validating parser, it is valid compact format
      if ((document != null)&&(!error)) {
        status = "Success";
        notes += "The response for transaction [" + transactionName
                + "] validated against the RETS Logout Response Format.  Response is "+responseBody;
      } else {
        notes += "The response body for transaction \"" + transactionName
		              + "\" did not validate against the RETS Logout Response Format. View the diagnostic message and response body below.\n\n  \n \t XML Parse Error: "+errorList+" \n\n Response body:\n\n{" + responseBody + "}";

      }
    } catch (org.xml.sax.SAXParseException e) {
      //              e.printStackTrace();
      notes += "The response body for transaction \"" + transactionName
              + "\" did not validate against the RETS Logout Response Format. View the diagnostic message and response body below.\n\n  \n \t XML Parse Error: "+errorList+" \n\n Response body:\n\n{" + responseBody + "}";
      jException=e.toString();
    }
    catch (Exception e) {
	      //              e.printStackTrace();
	      notes += "The response body for transaction \"" + transactionName
	              + "\" did not validate against the RETS Logout Response Format. View the diagnostic message and response body below.\n\n  Diagnostic message: \n \t"+e.getMessage()+"\n\n Response body:\n\n{" + responseBody + "}";
	      jException=e.toString();
    }


    return reportResult("ValidateUpdateResponse:  "+transactionName,
                        "Checks to see if the the transaction is valid RETS Logout Response Format",
                        status, notes,jException,"n/a");
  }

  String xmlParseErrorMessage(org.xml.sax.SAXParseException e){
	  int line = e.getLineNumber();
	   int col = e.getColumnNumber();
	   String message = e.getMessage()
	  					+ "\t Line=\t" + line
	  					+ "\t Column=\t" + col;

	  return message;
  }

  String appendNotes(String note)
  {
	  this.errorList+=note;
	  return errorList;
  }

  protected void setDocTypeLocation(String docName){
	  docTypeLocation=docName;

	}



}
