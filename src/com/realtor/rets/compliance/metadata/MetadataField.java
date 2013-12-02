/* $Header$ 
 */
package com.realtor.rets.compliance.metadata;

/**
 *  This class encapsulates the data for the RETS Metadata element/node
 *      at the following XPath:
 * /RETS/METADATA/METADATA-SYSTEM/System/METADATA-RESOURCE/Resource/METADATA-CLASS/Class/METADATA-TABLE/Field
 * 
 */
public class MetadataField implements Comparable {
    
    private String DataType;
    private String DBName;
    private String Default;
    private String LongName;
    private String LookupName;
    private long Maximum;
    private int MaximumLength;
    private long Minimum;
    private int requiredSetNumber;
    private String Searchable;    
    private String ShortName;
    private String StandardName;
    private String SystemName;
    
    protected final static String dataTypeFieldName = "DataType"; 
    protected final static String dbNameFieldName = "DBName";
    protected final static String defaultFieldName = "Default";
    protected final static String longNameFieldName = "LongName";
    protected final static String lookupNameFieldName = "LookupName";
    protected final static String maximumFieldName = "Maximum";
    protected final static String maximumLengthFieldName = "MaximumLength";
    protected final static String minimumFieldName = "Minimum";
    protected final static String requiredSetNumberFieldName = "Required";
    protected final static String searchableFieldName = "Searchable";
    protected final static String shortNameFieldName = "ShortName";
    protected final static String standardNameFieldName = "StandardName";
    protected final static String systemNameFieldName = "SystemName";
    
    protected final static String classNameClass = "ClassName";
    protected final static String standardNameClass = "StandardName";

    protected final static String resourceIdResource = "ResourceID";
    protected final static String standardNameResource = "StandardName";

    
    public String getDBName() {
        return DBName;
    }
    public void setDBName(String name) {
        DBName = name;
    }
    public String getLongName() {
        return LongName;
    }
    public void setLongName(String longName) {
        LongName = longName;
    }
    public String getLookupName() {
        return LookupName;
    }
    public void setLookupName(String lookupName) {
        LookupName = lookupName;
    }
    
    public int getRequiredSetNumber() {
        return requiredSetNumber;
    }
    public void setRequiredSetNumber(String requiredSetNumber) {
        if (requiredSetNumber != null && requiredSetNumber.length() != 0) {
            Integer iRequired = new Integer(requiredSetNumber);
            this.requiredSetNumber = iRequired.intValue();
        } else {
            this.requiredSetNumber = 0;
        }
    }
    
    public String getShortName() {
        return ShortName;
    }
    public void setShortName(String shortName) {
        ShortName = shortName;
    }
            
    public String getDataType() {
        return DataType;
    }
    public void setDataType(String dataType) {
        DataType = dataType;
    }

    /**
     * @return Returns the default value for the Field
     */
    public String getDefault() {
        return Default;
    }
    /**
     * @param default1 default value for this Field
     */
    public void setDefault(String default1) {
        Default = default1;
    }
    
    /**
     * @return Returns the Field's maximum value 
     */
    public long getMaximum() {
        return Maximum;
    }
    /**
     * @param maximum the Field's maximum value to set; converts value to Long
     */
    public void setMaximum(String maximum) {
        if (maximum != null && maximum.length() != 0) {
            Float maxFieldValue = new Float(maximum);
            this.Maximum = maxFieldValue.longValue();
        } else {
            this.Maximum = Long.MIN_VALUE;
        }
    }
    /**
     * @return Returns the maximumLength.
     */
    public int getMaximumLength() {
        return MaximumLength;
    }
    /**
     * @param maximum   the Field's maximumLength value; converts value to int
     */    
    public void setMaximumLength(String maximumLength) {
        if (maximumLength != null && maximumLength.length() != 0) {
            Integer maxLen = new Integer(maximumLength);
            this.MaximumLength = maxLen.intValue();
        } else {
            this.MaximumLength = Integer.MIN_VALUE;
        }
    }
    
    public long getMinimum() {
        return Minimum;
    }
    /**
     * @param minimum Field Value - converts passed-in value to long
     */
    public void setMinimum(String minimum) {
        if (minimum != null && minimum.length() != 0) {        
            Long minFieldValue = new Long(minimum);
            this.Minimum = minFieldValue.longValue();
        } else {
            this.Minimum = Long.MIN_VALUE;
        }
    }
    /**
     * @return Returns the searchable.
     */
    public String getSearchable() {
        return Searchable;
    }
    /**
     * @param searchable The searchable to set.
     */
    public void setSearchable(String searchable) {
        Searchable = searchable;
    }
    /**
     * @return Returns the standardName.
     */
    public String getStandardName() {
        return StandardName;
    }
    /**
     * @param standardName The standardName to set.
     */
    public void setStandardName(String standardName) {
        StandardName = standardName;
    }

    public String getSystemName() {
        return SystemName;
    }
    public void setSystemName(String systemName) {
        SystemName = systemName;
    }

    public String toString() {
        return SystemName + " (" + StandardName + ")";
    }
    
    /**
        Implementation of the Comparable interface to sort MetadataField objects
        by standard name.
     */
    public int compareTo(Object o) {
    	if (o instanceof MetadataField) {
    		MetadataField metadataField = (MetadataField) o;
    		return getStandardName().compareTo(metadataField.getStandardName());
    	}
    	else {
    		throw new UnsupportedOperationException();
    	}
    }
}