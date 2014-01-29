/* $Header$ 
 */
package com.realtor.rets.compliance.metadata;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.xml.JDOMParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 *  This class represents a "sub-intereface" to the RETS metadata that is returned
 *      for the DMQL test setup/configuration 
 * 
 */
public class MetadataFacade {
    
    protected Document metaDataDocument;    

    private final static String msf_xPathQueryResources = "/RETS/METADATA/METADATA-SYSTEM/SYSTEM/METADATA-RESOURCE/Resource";
    
    private final static String msf_xPathQueryStandardResourceNames = msf_xPathQueryResources + "/StandardName";
    private final static String msf_xPathQueryStandardClass = msf_xPathQueryResources + "[StandardName=";
    private final static String msf_xPathQueryStandardAllClasses = msf_xPathQueryResources + "/Resource/METADATA-CLASS/Class/StandardName";
 
    private final static String msf_xPathQuerySystemResourceNames = msf_xPathQueryResources + "/SystemName";
    private final static String msf_xPathQuerySystemClass = msf_xPathQueryResources + "[SystemName=";
    private final static String msf_xPathQuerySystemAllClasses = msf_xPathQueryResources + "/Resource/METADATA-CLASS/Class/SystemName";
    
    private static final int msf_BEFORE = -1;
    private static final int msf_EQUAL = 0;
    private static final int msf_AFTER = 1;
    
    private static Log log = LogFactory.getLog(MetadataFacade.class);
        
    /**
     * 
     */
    public MetadataFacade() {
        super();        
    }
    
    public boolean setMetaDataXML(String MetaDataResponse) throws Exception{
        JDOMParser domParser = new JDOMParser();
        InputStream metadataInputStream = MetadataUtils.InputStreamFromString(MetaDataResponse);        
        metaDataDocument = (Document)domParser.parseXML(metadataInputStream);
        boolean isXmlLoaded = false;
        
        if (metaDataDocument != null ) {
            JXPathContext docContext = JXPathContext.newContext(metaDataDocument);
            String thisClassSysName = null;
            
            try {
                isXmlLoaded = true;
                thisClassSysName = (String)docContext.getValue(msf_xPathQueryStandardResourceNames);
                if (log.isDebugEnabled()) {
                    log.debug("SUCCESS - FOUND a class with System Name: " + thisClassSysName);
                }
            } catch (JXPathException je) {                
                log.error("JXPath ERROR: " + je.getMessage());
                log.error(je);
                throw je;
            } catch (Exception e) {
                log.error("JXPath ERROR: " + e.getMessage());
                log.error(e);
                throw e;
            }
            
        }
        
        return isXmlLoaded;        
    }
    
    /**
     * @return Collection of the ResourceName Strings
     */
    public Collection getResourceStandardNames() {
        Collection allResourceStrdNames = new ArrayList();
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        resourcesIterator = mdDocContext.iterate(msf_xPathQueryStandardResourceNames);        
        while(resourcesIterator.hasNext()) {
            allResourceStrdNames.add(resourcesIterator.next());
        }

        return allResourceStrdNames;
    }
    
    /**
     * @return Collection of the ResourceName Strings
     */
    public Collection getResourceSystemNames() {
        Collection allResourceStrdNames = new ArrayList();
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        resourcesIterator = mdDocContext.iterate(msf_xPathQuerySystemResourceNames);        
        while(resourcesIterator.hasNext()) {
            allResourceStrdNames.add(resourcesIterator.next());
        }

        return allResourceStrdNames;
    }
  
    public Hashtable getResourceNameStrdNames() {
        
        Hashtable resourceIdsByStrdName = null;
        Iterator nodesIterator          = null;
        List mdResourceNodes            = null;
        Element mdResourceNode          = null;
        String thisResourceId           = null;
        String thisStrdName             = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        mdResourceNodes = mdDocContext.selectNodes(msf_xPathQueryResources);
        
        resourceIdsByStrdName = new Hashtable();
        nodesIterator = mdResourceNodes.iterator();        
        while(nodesIterator.hasNext()) {
            mdResourceNode = (Element)nodesIterator.next();
            thisResourceId = mdResourceNode.getChildText(MetadataField.resourceIdResource);
            thisStrdName = mdResourceNode.getChildText(MetadataField.standardNameResource);
            resourceIdsByStrdName.put(thisStrdName, thisResourceId);
        }
        
            
        return resourceIdsByStrdName;
        //return mdResourceNodes;
    }
    
    
    /**
     * @param resourceName  the resource name String value on which to base the query
     * @return  Collection of all Class Name Strings
     */
    public Collection getClassStandardNames(String resourceName){
        Collection allClassStrdNames    = null;
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        String xPathQuery = msf_xPathQueryStandardClass + "'" + resourceName + "']/METADATA-CLASS/Class/StandardName";        
        resourcesIterator = mdDocContext.iterate(xPathQuery);
        allClassStrdNames = new ArrayList();
        
        while(resourcesIterator.hasNext()) {
            String value = (String) resourcesIterator.next();
            if (value.trim().length() > 0) {
                allClassStrdNames.add(value);
            }
        }

        if (allClassStrdNames.isEmpty()) {
	        xPathQuery = msf_xPathQueryStandardClass + "'" + resourceName + "']/METADATA-CLASS/Class/ClassName";        
	        resourcesIterator = mdDocContext.iterate(xPathQuery);
	        while(resourcesIterator.hasNext()) {
	            String value = (String) resourcesIterator.next();
	            allClassStrdNames.add(value);
	        }
        }
        
        return allClassStrdNames;
    }
    
    /**
     * @param resourceName  the resource name String value on which to base the query
     * @return  Collection of all Class Name Strings
     */
    public Collection getClassSystemNames(String resourceName){
        Collection allClassSystemNames    = null;
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        String xPathQuery = msf_xPathQuerySystemClass + "'" + resourceName + "']/METADATA-CLASS/Class/SystemName";        
        resourcesIterator = mdDocContext.iterate(xPathQuery);
        allClassSystemNames = new ArrayList();
        
        while(resourcesIterator.hasNext()) {
        	allClassSystemNames.add(resourcesIterator.next());
        }
        
        return allClassSystemNames;
    }
   
    public Hashtable getClassesNameStrdName(String resourceName) {
        Hashtable classNamesByStrdName  = null;
        Iterator classNodesIterator     = null;
        List mdClassNodes               = null;
        Element mdClassNode             = null;
        String thisClassName            = null;
        String thisStrdName             = null;
        StringBuffer xPathQuery         = null;
        
        xPathQuery = new StringBuffer(msf_xPathQueryStandardClass);
        xPathQuery.append("'" + resourceName + "']/METADATA-CLASS/Class"); 
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        mdClassNodes = mdDocContext.selectNodes(xPathQuery.toString());
        
        classNamesByStrdName = new Hashtable();
        classNodesIterator = mdClassNodes.iterator();
        while(classNodesIterator.hasNext()) {
            mdClassNode = (Element)classNodesIterator.next();
            thisClassName = mdClassNode.getChildText(MetadataField.classNameClass);
            thisStrdName = mdClassNode.getChildText(MetadataField.standardNameClass);
            if (thisStrdName.trim().length() == 0) {
                thisStrdName = thisClassName;
            }
            classNamesByStrdName.put(thisStrdName, thisClassName);            
        }

        return classNamesByStrdName;
    }
    
    public Hashtable getClassesNameSystemName(String resourceName) {
        Hashtable classNamesByStrdName  = null;
        Iterator classNodesIterator     = null;
        List mdClassNodes               = null;
        Element mdClassNode             = null;
        String thisClassName            = null;
        String thisStrdName             = null;
        StringBuffer xPathQuery         = null;
        
        xPathQuery = new StringBuffer(msf_xPathQuerySystemClass);
        xPathQuery.append("'" + resourceName + "']/METADATA-CLASS/Class"); 
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        mdClassNodes = mdDocContext.selectNodes(xPathQuery.toString());
        
        classNamesByStrdName = new Hashtable();
        classNodesIterator = mdClassNodes.iterator();
        while(classNodesIterator.hasNext()) {
            mdClassNode = (Element)classNodesIterator.next();
            thisClassName = mdClassNode.getChildText(MetadataField.classNameClass);
            thisStrdName = mdClassNode.getChildText(MetadataField.standardNameClass);
            classNamesByStrdName.put(thisStrdName, thisClassName);            
        }

        return classNamesByStrdName;
    }
    
    /** 
     * @return  Collection of all Class Name Strings
     */
    public Collection getAllClassStandardNames(){
        Collection allClassStrdNames    = null;
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        resourcesIterator = mdDocContext.iterate(msf_xPathQueryStandardAllClasses);
        allClassStrdNames = new ArrayList();
        
        while(resourcesIterator.hasNext()) {
            allClassStrdNames.add(resourcesIterator.next());
        }
        
        return allClassStrdNames;
    }    

    /** 
     * @return  Collection of all Class Name Strings
     */
    public Collection getAllClassSystemNames(){
        Collection allClassSystemNames    = null;
        Iterator resourcesIterator      = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);
        resourcesIterator = mdDocContext.iterate(msf_xPathQuerySystemAllClasses);
        allClassSystemNames = new ArrayList();
        
        while(resourcesIterator.hasNext()) {
        	allClassSystemNames.add(resourcesIterator.next());
        }
        
        return allClassSystemNames;
    }    
    
    /**
     * protected Accessor method for querying to retrieve a collection of 
     *  RETS Metadata "Fields," given a standard Resource Name, a Standard Class
     *  name, a Field sub-type to match upon and the value to be matched
     *  
     * NOTE: in this implementation, we ONLY return MetadataField objects for   
     *      Metadata Field XML elements that contain a value in the StandardName
     *      subElement
     *      
     * @param resourceName  a Metadata Resource StandardName
     * @param className     a Metadata Class StandardName
     * @param fieldNodeType the name of the type of Metadata Field for our query
     * @param matchValue    the value we are matching against to fieldNodeType
     * @return  a Collection of MetadataField objects which ecapsulate the Field elements returned by this query              
     */
    protected Collection getMetadataFields(String type, String resourceName, String className, 
                                String fieldNodeType, String matchValue){        
        MetadataField mdField           = null;
        Collection mdFields             = null;
        List mdFieldNodes               = null;
        Iterator mdFieldNodesIterator   = null;
        
        JXPathContext mdDocContext = JXPathContext.newContext(metaDataDocument);        

        StringBuffer xPathQuery = getXPathQueryString(resourceName, className, fieldNodeType, matchValue);

        mdFieldNodes = mdDocContext.selectNodes(xPathQuery.toString());
        if (mdFieldNodes != null && mdFieldNodes.size() > 0) {
            mdFieldNodesIterator = mdFieldNodes.iterator();
            mdFields = new ArrayList();
            while (mdFieldNodesIterator.hasNext()) {
                Element currMdFieldElem = (Element)mdFieldNodesIterator.next();             
                mdFields = getMetadataFieldData(type, mdFields, currMdFieldElem);
            }
        }

        return mdFields;
    }
    
    /**
     * @param mdFields
     * @param currMdFieldElem
     * @return
     */
    private Collection getMetadataFieldData(String type, Collection mdFields, Element currMdFieldElem) {
        MetadataField mdField;
        String fieldName = MetadataField.standardNameFieldName;
        if (type.equals("System")) {
            fieldName = MetadataField.systemNameFieldName;
        }
        String mdFieldName = currMdFieldElem.getChildText(fieldName);
        
        if (mdFieldName != null && mdFieldName.length() > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Field Elem Name: " + mdFieldName);
            }   
            mdField = new MetadataField();
                                
            mdField.setDataType(currMdFieldElem.getChildText(MetadataField.dataTypeFieldName));
            mdField.setDBName(currMdFieldElem.getChildText(MetadataField.dbNameFieldName));
            mdField.setDefault(currMdFieldElem.getChildText(MetadataField.defaultFieldName));
            mdField.setLongName(currMdFieldElem.getChildText(MetadataField.longNameFieldName));
            mdField.setLookupName(currMdFieldElem.getChildText(MetadataField.lookupNameFieldName));
            mdField.setMaximum(currMdFieldElem.getChildText(MetadataField.maximumFieldName));
            mdField.setMaximumLength(currMdFieldElem.getChildText(MetadataField.maximumLengthFieldName));
            mdField.setMinimum(currMdFieldElem.getChildText(MetadataField.minimumFieldName));
            mdField.setRequiredSetNumber(currMdFieldElem.getChildText(MetadataField.requiredSetNumberFieldName));                
            mdField.setSearchable(currMdFieldElem.getChildText(MetadataField.searchableFieldName));
            mdField.setShortName(currMdFieldElem.getChildText(MetadataField.shortNameFieldName));
            mdField.setStandardName(currMdFieldElem.getChildText(MetadataField.standardNameFieldName));                
            mdField.setSystemName(currMdFieldElem.getChildText(MetadataField.systemNameFieldName));
            
            mdFields.add(mdField);
        }
        return mdFields;
    }

    public Collection getMetadataFieldsByDataType(String type, String resourceName, String className, 
                                    String dataType){
        
        Collection mdFields             = null;
        
        mdFields = getMetadataFields(type, resourceName, className, MetadataField.dataTypeFieldName, dataType);
        return mdFields;
    }
    
    public MetadataField getMetadataFieldByStrdName(String resourceName,
            String className, String fieldStandardName) throws Exception{

        ArrayList mdFields         = null;
        MetadataField thisMdField   = null;
        
        mdFields = (ArrayList) getMetadataFields("Standard", resourceName, className,
                                     MetadataField.standardNameFieldName,
                                     fieldStandardName);

        if (mdFields.size() == 1) {
          //we have the CORRECT number of "Field" objects to return
            thisMdField = (MetadataField)mdFields.get(0);            
        } else if (mdFields.size() > 1){
            String exceptionMessage = "RETS Metadata ERROR - this Resource-Class-FieldSystemName has more than 1 Field associated with it.";
            throw new Exception(exceptionMessage);
        }

        return thisMdField;
    }
    
    /**
     * @param resourceName
     * @param className
     * @return  a Collection of SortedSets that each represent a set of 
     *          Metadata Fields that all have the same requiredSetNumber  
     * @throws Exception
     */
    public Collection getMetadataRequiredFields(String resourceName, 
                                            String className)throws Exception{
        
        MetadataFieldSorter fieldSorter = null;
        Collection mdFieldSubsets       = null;     //collection of Sorted Sets
        Collection mdFieldsReturned     = null;
        MetadataField mdFieldCurrent    = null;
        MetadataField mdFieldStart      = null;
        MetadataField mdFieldLast       = null;
        TreeSet mdFieldsOrderedSet      = null;
        SortedSet mdFieldsReqSubset     = null;     //SubSet of Required Fields ALL have the same requiredSetNum        

        mdFieldsReturned = (ArrayList) getMetadataFields("Standard", resourceName, className,
                                                         MetadataField.requiredSetNumberFieldName,
                                                         ">0");        
        
        fieldSorter = new MetadataFieldSorter();
        mdFieldsOrderedSet = new TreeSet(fieldSorter);
        try{
            mdFieldsOrderedSet.addAll(mdFieldsReturned);
        } catch (Exception e) {
            log.error("Error Sorting Metadata Field Objects: " + e);
            throw e;
        }        
        
        mdFieldSubsets = new ArrayList();
        mdFieldStart = (MetadataField) mdFieldsOrderedSet.first();
        mdFieldLast = (MetadataField) mdFieldsOrderedSet.last();
        int matchRequiredSetNum = mdFieldStart.getRequiredSetNumber();
        int lastRequiredSetNum = mdFieldLast.getRequiredSetNumber();

        Iterator mdFieldsIterator = mdFieldsOrderedSet.iterator();
        while(mdFieldsIterator.hasNext()){
            mdFieldCurrent = (MetadataField) mdFieldsIterator.next();
            if (mdFieldCurrent.getRequiredSetNumber() > matchRequiredSetNum) {
                mdFieldsReqSubset = mdFieldsOrderedSet.subSet(mdFieldStart,mdFieldCurrent);
                mdFieldSubsets.add(mdFieldsReqSubset);                
                mdFieldStart = mdFieldCurrent;
                matchRequiredSetNum = mdFieldCurrent.getRequiredSetNumber();
            } else if (mdFieldCurrent.equals(mdFieldLast)) {
                mdFieldsReqSubset = mdFieldsOrderedSet.tailSet(mdFieldStart);
                mdFieldSubsets.add(mdFieldsReqSubset);
            }
        }

        return mdFieldSubsets;
        
    }
    
    /**
     * @param resourceName
     * @param className
     * @param fieldNodeType
     * @param matchValue
     * @return
     */
    private StringBuffer getXPathQueryString(String resourceName, String className, String fieldNodeType, String matchValue) {
        StringBuffer xPathQuery = new StringBuffer ("/RETS/METADATA/METADATA-SYSTEM/System/METADATA-RESOURCE/Resource[StandardName=");
        xPathQuery.append("'" + resourceName + "']/METADATA-CLASS/Class[ClassName=");
        xPathQuery.append("'" + className + "']/METADATA-TABLE/Field");
        
        String [] matchValues = new String [] {matchValue};
        if (matchValue.equals("Decimal")) {
            matchValues = new String [] {matchValue, "Int"};
        }
        
        xPathQuery.append("[");
        for (int i = 0; i < matchValues.length; i++) {
            if (i != 0) {
                xPathQuery.append(" or ");
            }
            xPathQuery.append(fieldNodeType);
            xPathQuery.append("='");
            xPathQuery.append(matchValues[i]);
            xPathQuery.append("'");
        }
        xPathQuery.append("]");

        if (log.isDebugEnabled()) {
            log.debug("getFields XPath String: " + xPathQuery.toString());
        }
        return xPathQuery;
    }

    /**
     * @return Returns the metaDataDocument.
     */
    public Document getMetadataDocument() {
        return metaDataDocument;
    }
    
    /**
     * Inner class provides an implementation of the <code>compare</code> 
     * method for specifically MetadataField objects
     */
     private static class MetadataFieldSorter implements Comparator, Serializable {
         
       public int compare(Object object1, Object object2){
           int result   = Integer.MIN_VALUE;
           MetadataField mdField1 = (MetadataField)object1;
           MetadataField mdField2 = (MetadataField)object2;
           
           result = compareFields(mdField1, mdField2);

         return result;
       }
       
       private int compareFields(MetadataField mdField1, MetadataField mdField2) {
           int compareResult = Integer.MIN_VALUE;
           
           if ( mdField1.getRequiredSetNumber() 
                   > mdField2.getRequiredSetNumber() ) { 
               compareResult = msf_AFTER;
           } else if ( mdField1.getRequiredSetNumber() 
                   < mdField2.getRequiredSetNumber() ) {
               compareResult = msf_BEFORE;
           } else {
               compareResult = msf_EQUAL;
               if (mdField1.getStandardName().compareTo(mdField2.getStandardName()) > 0) {
                   compareResult = msf_AFTER;
               } else {
                   compareResult = msf_BEFORE;
               }
           }
           
           return compareResult;
       }
       
     }    
}
