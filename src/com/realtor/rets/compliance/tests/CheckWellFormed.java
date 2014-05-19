/*
 * Test1.java
 *
 */
package com.realtor.rets.compliance.tests;

import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;
import org.realtor.rets.retsapi.RETSTransaction;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;


/**
 * Extends the TestEvaluator Interface and checks for well formed transaction
 * response bodies.  All rets requests should return well formed responses
 * (even  compact responses).  In addition to checking well formedness, will
 * list parameters sent to search and getMetadata transactions.
 *
 * @author pobrien
 */
public class CheckWellFormed extends BaseEvaluator {
  /**
   * Creates a new instance of CheckWellFormed
   */
  public CheckWellFormed() {
    super();
    specReference = "The RETS Specification does not specifically state "
                    + "that reponse body of transactions are well formed \n"
                    + "unless 'Content-Type' equal 'text/xml' (section 3.7),"
                    + " this test only fails when response has 'text/xml' in\n"
                    + " the 'Content-Type' and the response body is not well formed";
  }

	protected TestResult processResults(String transName, RETSTransaction t) {
      if (t == null) {
        return null;
      }

      Map resp = t.getResponseMap();
      String cName = t.getClass().getName();
      Map m = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
	  boolean isXml = CollectionUtils.hasValue(m,"content-type","text/xml");
	  int cnt=CollectionUtils.keyCount(m,"content-type");



        if (cnt > 1) {
          String mContent =
            "Multiple Content-Type's are reported the response for transaction : "
             + transName;
          String note = "Some clients may fail or be confused if multiple 'Content-Type' values returned ";
          TestResult testResult = reportResult("CheckWellFormed", mContent,
                                               "Info", note, "n/a");
          return testResult;
        }


      if (isXml) {
        String body = (String) resp.get("body");
        return checkWellFormed(transName, body);
      } else {
        String notes = "Transaction " + cName
                       + " does not include a Content-Type of 'text/xml'";
        return reportResult("CheckWellFormed",
                                              "Checks to see if the body of a transaction is well formed",
                                              "Info", notes);
      }



  }

  /**
   * This is the class that does the actual check of a transaction body for
   * well formed xml.
   *
   * @param transactionName Name of the Transaction checked (from the test
   *        script)
   * @param responseBody transaction body which should be well formed xml
   *
   * @return results of the test.
   */
  protected TestResult checkWellFormed(String transactionName,
                                       String responseBody) {
    Document document = null;
    String status = "Failure";
    String notes = "";
    String jException = "";

    try {
      if ((responseBody != null) && (responseBody.length() > 0)) {
        ByteArrayInputStream bais = new ByteArrayInputStream(
                                        responseBody.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String inputSourceUrl = "file:" + PropertyManager.getConfigDirectory().replace(File.separatorChar, '/') + "/";
        InputSource is = new InputSource(inputSourceUrl);
        is.setByteStream(bais);
        document = builder.parse(is);
      }

      // if we can create an XML document from the stream, it is well formed XML.
      if (document != null) {
        status = "Success";
        notes = "The response for transaction [" + transactionName
                + "] is well formed";
      } else {
        notes = "The response body for transaction \"" + transactionName
                + "\" was not well formed {" + responseBody + "}";
      }
    } catch (org.xml.sax.SAXParseException e) {
      //              e.printStackTrace();
      notes = "The response body for transaction \"" + transactionName
              + "\" was not well formed. View the diagnostic message and response body below.\n\n  Diagnostic message: \n \t"
              + xmlParseErrorMessage(e) + "\n\n Response body:\n\n{"
              + responseBody + "}";
      jException = e.toString();
    } catch (Exception e) {
      //              e.printStackTrace();
      notes = "The response body for transaction \"" + transactionName
              + "\" was not well formed. View the diagnostic message and response body below.\n\n  Diagnostic message: \n \t"
              + e.getMessage() + "\n\n Response body:\n\n{" + responseBody
              + "}";
      jException = e.toString();
    }

    return reportResult("CheckWellFormed:  " + transactionName,
                        "Checks to see if the the body of a transaction is well formed",
                        status, notes, jException, "n/a");
  }

  /**
   * Error message parsed, provide more information in the TestResult
   *
   * @param e Exception to report on
   *
   * @return Error Message
   */
  String xmlParseErrorMessage(org.xml.sax.SAXParseException e) {
    int line = e.getLineNumber();
    int col = e.getColumnNumber();
    String publicId = e.getPublicId();
    String systemId = e.getSystemId();
    String message = "XML Parse error: " + e.getMessage() + "\t Line=\t" + line
                     + "\t Column=\t" + col;

    return message;
  }
}
