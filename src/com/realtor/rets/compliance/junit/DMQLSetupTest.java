/* $Header$
 */
package com.realtor.rets.compliance.junit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.jdom.Document;

import com.realtor.rets.compliance.DMQLTestConfigurer;
import com.realtor.rets.compliance.TestExecuter;
import com.realtor.rets.compliance.metadata.MetadataFacade;
import com.realtor.rets.compliance.metadata.MetadataField;

/**
 *  JUnit test class for testing the DMQLTestConfigurer class and any associated
 *      classes involved with the DMQL test setup.
 *
 * @author pobrien
 */
public class DMQLSetupTest extends TestCase {

    private static Log log = LogFactory.getLog(DMQLSetupTest.class);

    private MetadataFacade m_mdFacade = null;
    private String m_password = "password";
    private String m_serverUrl = "http://localhost:8080/rets/server/login";
    private String m_transLogDir = "C:/tmp/ComplianceLogs";
    private String m_userAgent = null;
    private String m_username = "266123";

    private static final String msf_testResourceName = "Property";
    private static final String msf_testClassName = "Residential-Property";
    private static final String msf_testFieldType = "DataType";
    private static final String msf_testFieldVal = "Character";
    private static final String msf_testResourceID2 = "Agent";
    private static final String msf_testResourceID3 = "Office";

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        String[] appArgs = new String[4];
        appArgs[0] = "-p";
        appArgs[1] = "TestParameters.properties";
        appArgs[2] = "-u";
        appArgs[3] = "C:/aopenden/development/deploy/retsCompliance/config/TestClient.properties";

        TestExecuter.readCommandLineOptions(appArgs);

        DMQLTestConfigurer DMQLTester = null;
        MetadataFacade mdFacade = null;
        log.debug("JUNIT - Running DMQL tests");
        DMQLTester = new DMQLTestConfigurer(m_username, m_password, m_serverUrl, m_transLogDir, null,null);
        assertNotNull("DMQLTestConfigurer obj is null", DMQLTester);

        try {
           m_mdFacade = DMQLTester.setupDMQLTests();
        } catch (Exception e) {
            log.debug("ERROR: " + e);
        }

    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDMQLgetMDFacade() {

        assertNotNull("MetaData Facade obj is null", m_mdFacade);

        Document domDoc =  null;

        domDoc = m_mdFacade.getMetadataDocument();
        assertNotNull("Dom obj is null", domDoc);
        assertNotNull("DomDoc Does NOT have the correct root Element",
                domDoc.getRootElement().getChild("METADATA"));

    }

    public void testGetResourceNames() {
        assertNotNull("MetaData Facade obj is null", m_mdFacade);

        ArrayList mdResourceNames = (ArrayList)m_mdFacade.getResourceStandardNames();
        assertNotNull("ResourceNames Array is null", mdResourceNames);
        assertTrue("No Resource Names were returned by the metaData", mdResourceNames.size() > 0);

        assertNotNull("First ResourceName is NULL", mdResourceNames.get(0));
        for (int iCount = 0; iCount < mdResourceNames.size(); iCount++) {
            log.debug((iCount + 1) + " Resource Name: " + mdResourceNames.get(iCount));
        }

    }

    public void testGetClassNames() {
        assertNotNull("MetaData Facade obj is null", m_mdFacade);

        ArrayList mdClassNames =
            (ArrayList)m_mdFacade.getClassStandardNames(msf_testResourceName);
        assertNotNull("Class Names Array is null", mdClassNames);
        assertTrue("No Class Names were returned by the metaData", mdClassNames.size() > 0);
        assertNotNull("First ClassName is NULL", mdClassNames.get(0));
        for (int iCount = 0; iCount < mdClassNames.size(); iCount++) {
            log.debug((iCount + 1) + " Class Name: " + mdClassNames.get(iCount));
        }

        mdClassNames = null;
        mdClassNames = (ArrayList)m_mdFacade.getAllClassStandardNames();
        assertNotNull("Class Names Array - ALL Names - is null", mdClassNames);
        assertTrue("ALL Class Names were NOT returned by the metaData", mdClassNames.size() > 0);
        assertNotNull("First ClassName is NULL", mdClassNames.get(0));

        for (int iCount = 0; iCount < mdClassNames.size(); iCount++) {
            log.debug((iCount + 1) + " CLASS Name: " + mdClassNames.get(iCount));
        }

    }

    public void testGetClassNameStrdNames() {

        Hashtable mdClassNames =
            (Hashtable)m_mdFacade.getClassesNameStrdName(msf_testResourceName);

        assertNotNull("Class Names hashtable is NOT null", mdClassNames);
        assertTrue("Class Names hashtable is EMPTY", mdClassNames.size() > 0);
        log.debug("Class Names hashtable has " + mdClassNames.size() + " elements in it");

        String thisClassName = null;
        thisClassName = (String)mdClassNames.get(msf_testClassName);
        assertEquals("The WRONG ClassName value was returned", "RES",thisClassName);

    }

    public void testGetMetadataFields() {
        assertNotNull("MetaData Facade obj is null", m_mdFacade);
        ArrayList mdFields = null;
/*        mdFields =
            (ArrayList) m_mdFacade.getMetadataFieldsByDataType(
                    msf_testResourceName, msf_testClassName, msf_testFieldVal);
*/
        mdFields =
            (ArrayList) m_mdFacade.getMetadataFieldsByDataType("Standard",
                    msf_testResourceName, msf_testClassName, "Decimal");

        assertNotNull("Metadata Fields Array is null", mdFields);
        assertTrue("Metadata Fields Array has no elements in it", mdFields.size() > 0);

        MetadataField mdField = null;
        Iterator mdFieldsIterator = mdFields.iterator();
        while(mdFieldsIterator.hasNext()) {
            mdField = (MetadataField)mdFieldsIterator.next();
            assertTrue("This is NOT a character field", mdField.getDataType().equalsIgnoreCase("Character"));
            assertTrue("There is NOT Maximum Length sub-Field", mdField.getMaximumLength() > 0);
            assertNotNull("This mdField has no StandardName sub-field value", mdField.getStandardName());
            assertTrue("This mdField has no StandardName sub-field value", mdField.getStandardName().length() > 0);

            if (log.isDebugEnabled()) {
                log.debug("Field Elem " + mdField.getStandardName()
                        + " has dataType: "  + mdField.getDataType()
                        + " and MaxLen: "+ mdField.getMaximumLength());
            }

        }
    }

    public void testGetMetadataFieldByStrdName() {
        assertNotNull("MetaData Facade obj is null", m_mdFacade);
        MetadataField mdField = null;

        try {
            mdField = m_mdFacade.getMetadataFieldByStrdName(msf_testResourceName,
                                                  msf_testClassName, "City");
        } catch (Exception e) {
            log.debug(e);
        }

        assertNotNull("This MetadataField obj is null - No mdField has been returned", mdField);
        assertTrue("This MetadataField obj DOES NOT contain a correct DBName", mdField.getDBName().equalsIgnoreCase("City"));
        assertTrue("This MetadataField obj DOES NOT contain a correct System Name", mdField.getSystemName().equalsIgnoreCase("City"));
        assertTrue("MaxLength is NOT correct for this MetadataField", mdField.getMaximumLength()== 50);
    }

    public void testGetMetadataRequiredFields() {
        MetadataField mdField       = null;
        ArrayList reqFieldSets      = null;
        TreeSet reqFieldSubsets     = null;

        try {
            reqFieldSets =
                (ArrayList) m_mdFacade.getMetadataRequiredFields(msf_testResourceName,
                                                  msf_testClassName);
        } catch (Exception q) {
            System.out.println(q);
        }
        assertNotNull("This FieldsSet arrayList is null - Nothing has been returned", reqFieldSets);
        Iterator fieldsIterator         = null;
        MetadataField currentMdField    = null;
        int reqFieldNum_1_Count         = 0;
        int reqFieldNum_2_Count         = 0;
        int reqFieldNum_3_Count         = 0;
        int reqFieldNum_4_Count         = 0;
        int reqFieldNum_5_Count         = 0;

        Iterator fieldSetsIterator = reqFieldSets.iterator();
        log.debug("fieldSetsIterator COUNT: " + reqFieldSets.size());
        while (fieldSetsIterator.hasNext()) {
            reqFieldSubsets = (TreeSet) fieldSetsIterator.next();
            assertNotNull("This TreeSet of mdFields is null - Nothing has been returned", reqFieldSubsets);
            assertTrue("This TreeSet of mdFields Contains 0 elements", reqFieldSubsets.size() != 0);

            fieldsIterator = reqFieldSubsets.iterator();
            while (fieldsIterator.hasNext()) {
                currentMdField = (MetadataField) fieldsIterator.next();
                assertNotNull("This mdField is null!!", currentMdField);

                if (currentMdField.getRequiredSetNumber() == 1) {
                    reqFieldNum_1_Count++;
                } else if (currentMdField.getRequiredSetNumber() == 2) {
                    reqFieldNum_2_Count++;
                } else if (currentMdField.getRequiredSetNumber() == 3) {
                    reqFieldNum_3_Count++;
                } else if (currentMdField.getRequiredSetNumber() == 4) {
                    reqFieldNum_4_Count++;
                } else if (currentMdField.getRequiredSetNumber() == 5) {
                    reqFieldNum_5_Count++;
                }

            }
        }

        log.debug("reqFieldNum_2_Count = " + reqFieldNum_2_Count);
        log.debug("reqFieldNum_1_Count = " + reqFieldNum_1_Count);
        log.debug("reqFieldNum_3_Count = " + reqFieldNum_3_Count);
        log.debug("reqFieldNum_4_Count = " + reqFieldNum_4_Count);

        assertEquals("This mdField requiredNum = 1 Count is Incorrect", 3, reqFieldNum_1_Count);
        assertEquals("This mdField requiredNum = 2 Count is Incorrect", 3, reqFieldNum_2_Count);
        assertEquals("This mdField requiredNum = 3 Count is Incorrect", 1, reqFieldNum_3_Count);
        assertEquals("This mdField requiredNum = 4 Count is Incorrect", 3, reqFieldNum_4_Count);
        assertEquals("This mdField requiredNum = 5 Count is Incorrect", 5, reqFieldNum_5_Count);

    }

    public void testGetResourceNameStrdNames() {

        Hashtable mdResourceNames = null;
        mdResourceNames = (Hashtable)m_mdFacade.getResourceNameStrdNames();
/*
        List mdResourceNames = null;
        mdResourceNames = (List)m_mdFacade.getResourceNameStrdNames();  */

        assertNotNull("Resources Names hashtable is NOT null", mdResourceNames);
        assertTrue("md ResourceNames hashtable ha no Elements in it!", mdResourceNames.size() > 0);

        log.debug("Resource Names hashtable has " + mdResourceNames.size() + " elements in it");

        String thisResourceID = null;
        thisResourceID = (String)mdResourceNames.get(msf_testResourceName);
        assertEquals("The WRONG ResourceID value was returned", msf_testResourceName,
                     thisResourceID);

        thisResourceID = (String)mdResourceNames.get(msf_testResourceID2);
        assertEquals("The WRONG ResourceID value was returned - value is NOT 'Agent'", msf_testResourceID2,
                     thisResourceID);

        thisResourceID = (String)mdResourceNames.get(msf_testResourceID3);
        assertEquals("The WRONG ResourceID value was returned - value is NOT 'Office'", msf_testResourceID3,
                     thisResourceID);


    }

}
