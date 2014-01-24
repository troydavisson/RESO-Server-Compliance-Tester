package com.realtor.rets.compliance.gui;

import com.realtor.rets.compliance.DMQLTestConfigurer;
import com.realtor.rets.compliance.PropertyManager;
import com.realtor.rets.compliance.metadata.MetadataFacade;
import com.realtor.rets.compliance.metadata.MetadataField;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
  * @author pobrien
 */
public class DMQLConfigurator extends JDialog {

	// constants for the ComboBoxes
	private static final int COMBOBOX_RESOURCE_INDEX = 1;
    private static final int COMBOBOX_CLASS_INDEX = 2;
    private static final int COMBOBOX_TYPE_INDEX = 3;
    private static final int COMBOBOX_DATATYPE_INDEX = 4;
    private static final int COMBOBOX_VALUENAME_INDEX = 5;

    // properties constants
	private static final String DMQL_PROPERTY_PREFIX = "DMQL.";
	private static final String DMQL_RESOURCE_PROPERTY_NAME = DMQL_PROPERTY_PREFIX + "Search.SearchType";
	private static final String DMQL_CLASS_PROPERTY_NAME = DMQL_PROPERTY_PREFIX + "SearchClass";
	private static final String DMQL_TYPE_PROPERTY_NAME = DMQL_PROPERTY_PREFIX + "Type";
	private static final char PROPERTY_KEY_TOKEN_SEPARATOR = '.';
	private static final String DMQL_PROPERTY_SUFFIX_FIELD = PROPERTY_KEY_TOKEN_SEPARATOR + "Field";
	private static final String DMQL_PROPERTY_SUFFIX_VALUE = PROPERTY_KEY_TOKEN_SEPARATOR + "Value";

	private static final String [] RESOURCES = {"Property"};

	private static final String [] TYPES = {"Standard", "System"};

	// a TreeMap of data types mapped to value types
	private static TreeMap valueTypeToDataTypeMap = new TreeMap();

	// a TreeSet of required DMQL properties
	private static TreeSet neededSet = new TreeSet();

	// a List of ValueType/DataType pairs that do not require values
	private static final java.util.List NO_VALUE_LIST = Arrays.asList(new String[] {
		"Date" + PROPERTY_KEY_TOKEN_SEPARATOR + "Today"
	});

	private static final int DMQL_TABLE_DISPLAY_ROWS = 11;
	private static final int DMQL_TABLE_DISPLAY_NUDGE = 3;

	static {
		// initialize the valueTypeToDataTypeMap
		valueTypeToDataTypeMap.put("Character", new String [] { "Contains", "StartsWith", "AndOr" });
		valueTypeToDataTypeMap.put("DateTime", new String [] { "Minimum", "Maximum", "Today" });
		valueTypeToDataTypeMap.put("Decimal", new String [] { "Minimum", "Maximum", "RangeLow", "RangeHigh" });

    	// initialize the neededSet
    	for (int typeCounter = 0; typeCounter < TYPES.length; typeCounter++) {
    		Iterator iterator = valueTypeToDataTypeMap.keySet().iterator();
    		while (iterator.hasNext()) {
    			String valueType = (String) iterator.next();
    			String [] dataType = (String []) valueTypeToDataTypeMap.get(valueType);
    			for (int i = 0; i < dataType.length; i++) {
    				neededSet.add(getMapString(TYPES[typeCounter], valueType, dataType[i]));
    			}
    		}
    	}
	}

    private DMQLTestConfigurer dmqlTestConfigurer = null;
    private MetadataFacade facade = null;

    // to key system names to standard names
    private HashMap classNameMap = new HashMap();

    private JComboBox resourceComboBox = new JComboBox();
    private JComboBox classComboBox = new JComboBox();
    private JComboBox typeComboBox = new JComboBox();
    private JComboBox dataTypeComboBox = new JComboBox();
    private JComboBox fieldComboBox = new JComboBox();
    private JComboBox valueNameComboBox = new JComboBox();
    private JLabel valueLabel;
    private JTextField valueTextField = new JTextField(10);
    private JTable dmqlTable;

    // the PropertyManager object for this dialog box
    private PropertyManager propertyManager = null;

    // the TableModel for the dataset display table
    private DMQLTableModel dmqlTableModel;

    public DMQLConfigurator(DMQLTestConfigurer configurer, JFrame parentFrame) throws Exception {
        super(parentFrame);
        setDmqlTestConfigurer(configurer);
        initComponents(parentFrame);
    }

    private void initComponents(JFrame parentFrame) {

    	Iterator iterator = valueTypeToDataTypeMap.keySet().iterator();
    	while (iterator.hasNext()) {
            dataTypeComboBox.addItem(iterator.next());
        }
        dataTypeComboBox.setSelectedItem("Decimal");

        for (int i = 0; i < RESOURCES.length; i++) {
        	resourceComboBox.addItem(RESOURCES[i]);
        }

        for (int i = 0; i < TYPES.length; i++) {
            typeComboBox.addItem(TYPES[i]);
        }

        setupWindow(parentFrame);

        // initialize the internal PropertyManager object
    	propertyManager = PropertyManager.getParametersPropertyManager();
    	propertyManager.load();
        String selectedResource = propertyManager.getValue(DMQL_RESOURCE_PROPERTY_NAME);
    	if (selectedResource == null) {
    		selectedResource = "Property";
    	}
        String selectedClass = propertyManager.getValue(DMQL_CLASS_PROPERTY_NAME);
    	if (selectedClass == null) {
    		selectedClass = "RES";
    	}
    	String selectedType = propertyManager.getValue(DMQL_TYPE_PROPERTY_NAME);
    	if (selectedType == null) {
    		selectedType = "Standard";
    	}

    	resourceComboBox.setSelectedItem(selectedResource);
    	classComboBox.setSelectedItem(selectedClass);
    	typeComboBox.setSelectedItem(selectedType);
    }

    private void setupWindow(JFrame parentFrame) {

        getContentPane().setLayout(new BorderLayout());

        setTitle("Configure DMQL");
        setForeground(Color.white);

        setModal(true);
        setResizable(false);

        Dimension dimension = new Dimension(120,22);
        resourceComboBox.setPreferredSize(dimension);
        classComboBox.setPreferredSize(dimension);
        typeComboBox.setPreferredSize(dimension);
        dataTypeComboBox.setPreferredSize(dimension);
        fieldComboBox.setPreferredSize(dimension);

        valueNameComboBox = new JComboBox((String []) valueTypeToDataTypeMap.get(dataTypeComboBox.getSelectedItem()));
        valueNameComboBox.setPreferredSize(dimension);

        JPanel resourceClassPanel = new JPanel(new GridBagLayout());
        JPanel dataTypeFieldPanel = new JPanel(new GridBagLayout());
        JPanel summaryPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new BorderLayout());

        resourceClassPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), " View "));
        dataTypeFieldPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), " Add/Change DataSets "));
        summaryPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), " Current DataSets "));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(resourceClassPanel, gbc);
        centerPanel.add(dataTypeFieldPanel, gbc);
        centerPanel.add(summaryPanel, gbc);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints constraints = new GridBagConstraints();
        // Setup Resource Panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets.left = 10;
        constraints.anchor = GridBagConstraints.EAST;


        resourceClassPanel.add(new JLabel("Resource"), constraints);
        constraints.gridy = 1;
        resourceClassPanel.add(new JLabel("Class"), constraints);
        constraints.gridy = 2;
        resourceClassPanel.add(new JLabel("Type"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        resourceClassPanel.add(resourceComboBox, constraints);
        constraints.gridy = 1;
        resourceClassPanel.add(classComboBox, constraints);
        constraints.gridy = 2;
        resourceClassPanel.add(typeComboBox, constraints);

        //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        // Setup Datatype and Field Panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets.left = 10;
        constraints.anchor = GridBagConstraints.EAST;


        dataTypeFieldPanel.add(new JLabel("DataType"), constraints);
        constraints.gridy = 1;
        dataTypeFieldPanel.add(new JLabel("Field"), constraints);
        constraints.gridy = 2;
        dataTypeFieldPanel.add(new JLabel("Value Type"), constraints);
        constraints.gridy = 3;
        valueLabel = new JLabel("Value");
        dataTypeFieldPanel.add(valueLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        dataTypeFieldPanel.add(dataTypeComboBox, constraints);
        constraints.gridy = 1;
        dataTypeFieldPanel.add(fieldComboBox, constraints);
        constraints.gridy = 2;
        dataTypeFieldPanel.add(valueNameComboBox, constraints);
        constraints.gridy = 3;
        dataTypeFieldPanel.add(valueTextField, constraints);

        JButton selectButton = new JButton("Select");
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.EAST;
        dataTypeFieldPanel.add(selectButton, constraints);

        dmqlTableModel = new DMQLTableModel();
        dmqlTable = new JTable(dmqlTableModel);
        dmqlTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dmqlTable.getSelectionModel().addListSelectionListener(new DMQLTableSelectionListener());
        dmqlTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScrollPane = new JScrollPane(dmqlTable);
        tableScrollPane.setPreferredSize(new Dimension(500, DMQL_TABLE_DISPLAY_ROWS * dmqlTable.getRowHeight() + DMQL_TABLE_DISPLAY_NUDGE));

        summaryPanel.add(tableScrollPane, BorderLayout.SOUTH);

        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");
        JPanel tempPanel = new JPanel(new FlowLayout());
        tempPanel.add(saveButton);
        tempPanel.add(cancelButton);
        buttonPanel.add(tempPanel, BorderLayout.EAST);

        resourceComboBox.addActionListener(new ComboBoxActionListener(COMBOBOX_RESOURCE_INDEX));
        classComboBox.addActionListener(new ComboBoxActionListener(COMBOBOX_CLASS_INDEX));
        typeComboBox.addActionListener(new ComboBoxActionListener(COMBOBOX_TYPE_INDEX));
        dataTypeComboBox.addActionListener(new ComboBoxActionListener(COMBOBOX_DATATYPE_INDEX));
        valueNameComboBox.addActionListener(new ComboBoxActionListener(COMBOBOX_VALUENAME_INDEX));
        saveButton.addActionListener(new SaveButtonActionListener());

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectButtonPressed();
            }
        });

        pack();
        if (parentFrame != null) {
	        setLocationRelativeTo(parentFrame);
        }
    }

    public DMQLTestConfigurer getDmqlTestConfigurer() {
        return dmqlTestConfigurer;
    }

    /**
        Set up the current DataSets table.
        @param selectedResource
        @param selectedClass
        @param selectedType
     */
    private void setTable(String selectedResource, String selectedClass, String selectedType) {
    	if (propertyManager != null) {
    		dmqlTableModel.clear();
	        Iterator iterator = propertyManager.getKeys().iterator();
	        while (iterator.hasNext()) {
	        	String key = (String) iterator.next();
	        	if (key.startsWith(DMQL_PROPERTY_PREFIX + selectedType) && key.endsWith(DMQL_PROPERTY_SUFFIX_FIELD)) {

	        		String selectedField = propertyManager.getValue(key);

	        		StringTokenizer st = new StringTokenizer(key, "" + PROPERTY_KEY_TOKEN_SEPARATOR);
	        		st.nextToken(); // skip the DMQL token
	        		st.nextToken(); // skip the type token
	        		String selectedDataType = st.nextToken();
	        		String selectedValueType = st.nextToken();

	        		// get the Value property key
	        		String valueKey = key.substring(0, key.length() - DMQL_PROPERTY_SUFFIX_FIELD.length()) + DMQL_PROPERTY_SUFFIX_VALUE;

	        		// get the Value property
	        		String selectedValue = propertyManager.getValue(valueKey);

					DMQLSearchParameterSet data =
	                    new DMQLSearchParameterSet(selectedDataType, selectedField, selectedValueType, selectedValue);
					dmqlTableModel.addData(data);
	        	}
	        }
	        propertyManager.putValue(DMQL_RESOURCE_PROPERTY_NAME, selectedResource);
	        propertyManager.putValue(DMQL_CLASS_PROPERTY_NAME, selectedClass);
	    	propertyManager.putValue(DMQL_TYPE_PROPERTY_NAME, selectedType);
    	}
   }

    // Likely that parent window will someway or other invoke this, so propogate exception up
    // for it to display a dialog before given a useless window, which is what this frame would be
    // if failed
    public void setDmqlTestConfigurer(DMQLTestConfigurer dmqlTestConfigurer) throws Exception {
        this.dmqlTestConfigurer = dmqlTestConfigurer;
        facade = getDmqlTestConfigurer().setupDMQLTests();
    }

    /**
        Add/change the currently entered data into the table.
     */
    private void selectButtonPressed() {
        String selectedDataType = dataTypeComboBox.getSelectedItem() != null ? dataTypeComboBox.getSelectedItem().toString() : "";
        String selectedField = fieldComboBox.getSelectedItem() != null ? fieldComboBox.getSelectedItem().toString() : "";
        String selectedValueName = valueNameComboBox.getSelectedItem() != null ? valueNameComboBox.getSelectedItem().toString() : "";
        String selectedValue = valueTextField.getText();
        DMQLSearchParameterSet data =
                new DMQLSearchParameterSet(selectedDataType, selectedField, selectedValueName, selectedValue);
        dmqlTableModel.addData(data);

        // highlight the newly added row
        for (int row = 0; row < dmqlTable.getRowCount(); row++) {
        	if (dmqlTable.getValueAt(row, 0).equals(selectedDataType) &&
        			dmqlTable.getValueAt(row, 1).equals(selectedField) &&
        			dmqlTable.getValueAt(row, 2).equals(selectedValueName)) {
        		dmqlTable.getSelectionModel().setSelectionInterval(row, row);
        		break;
        	}
        }
    }

    /**
        Get the key String for a DMQLSearchParameterSet.
        @return the key String for a DMQLSearchParameterSet.
     */
    private static String getKey(DMQLSearchParameterSet dataRow) {
    	return dataRow.getDataType() + PROPERTY_KEY_TOKEN_SEPARATOR + dataRow.getValueType();
  	}

    /**
        Save the current PropertyManager values.
     */
    private void setPropertyManagerValues() {
    	if (propertyManager != null) {
    		// get the current type from the PropertyManager object
	    	String currentType = propertyManager.getValue(DMQL_TYPE_PROPERTY_NAME);

	    	// read the values from the table back into the PropertyManager object
	        String[] keys = dmqlTableModel.getKeys();
	        if ( keys != null ) {
	            for ( int i=0; i < keys.length; i++ ) {
	            	String prefix = DMQL_PROPERTY_PREFIX + currentType + PROPERTY_KEY_TOKEN_SEPARATOR + keys[i];
	            	DMQLSearchParameterSet dmqlSearchParameterSet = dmqlTableModel.getDataForKey(keys[i]);
	                propertyManager.putValue(prefix + DMQL_PROPERTY_SUFFIX_FIELD, dmqlSearchParameterSet.getField());
	                if (! NO_VALUE_LIST.contains(keys[i])) {
	                	propertyManager.putValue(prefix + DMQL_PROPERTY_SUFFIX_VALUE, dmqlSearchParameterSet.getValue());
	                }
	            }
	        }

	        // set the PropertyManager's resource/class/type values
	        propertyManager.putValue(DMQL_RESOURCE_PROPERTY_NAME, (String) resourceComboBox.getSelectedItem());
	        propertyManager.putValue(DMQL_CLASS_PROPERTY_NAME, (String) classComboBox.getSelectedItem());
	        propertyManager.putValue(DMQL_TYPE_PROPERTY_NAME, (String) typeComboBox.getSelectedItem());
    	}
    }

    /**
        Get the Map String for the specified type, data type, and value type.  Used by internal
        completion maps.
     */
    private static String getMapString(String type, String dataType, String valueType) {
    	return type + PROPERTY_KEY_TOKEN_SEPARATOR + dataType + PROPERTY_KEY_TOKEN_SEPARATOR + valueType;
	}

    /**
        Check the internal PropertiesManager object to see that all needed properties have been set;
        display a dialog of the missing properties if there are missing ones.
        @return True if the needed properties
     */
    private boolean checkPropertiesForCompletion() {
    	TreeSet testSet = new TreeSet(neededSet);

    	// remove from the TreeSet all the DMQL properties that have been set
    	Iterator iterator = propertyManager.iterator();
    	while (iterator.hasNext()) {
    		String key = (String) iterator.next();
    		if (key.startsWith(DMQL_PROPERTY_PREFIX) && key.endsWith(DMQL_PROPERTY_SUFFIX_FIELD)) {
	    		StringTokenizer st = new StringTokenizer(key, "" + PROPERTY_KEY_TOKEN_SEPARATOR);
    			st.nextToken();
   				String type = st.nextToken();
   				String dataType = st.nextToken();
   				String valueType = st.nextToken();
				testSet.remove(getMapString(type, dataType, valueType));
    		}
    	}

    	// all properties are set if the testSet is now empty
    	boolean isOK = testSet.isEmpty();
    	if (! isOK) {
    		// report any missing sets to the user via a dialog box
    		new SaveErrorDialog(testSet);
    	}

    	return isOK;
    }

    /**
        Perform whatever data changes are necessary for a ComboBox change.
        @param sourceIndex The ID of the ComboBox.
        @param selectedObject
     */
    synchronized private void comboBoxChange(int sourceIndex) {

    	// if we're changing the resource, class, or type, set the data for the internal
    	// PropertyManager object
        switch (sourceIndex) {
   			case COMBOBOX_RESOURCE_INDEX:
   			case COMBOBOX_CLASS_INDEX:
   			case COMBOBOX_TYPE_INDEX:
   				setPropertyManagerValues();
	    		break;
    	}

        String selectedResouceName = null;
        String selectedClassName = null;
        String selectedClassName2 = null;
        String selectedDataTypeName = null;
        Collection collection = null;
        Iterator iterator = null;
        String resourceName = null, className = null, fieldName = null;
        switch (sourceIndex) {
            case COMBOBOX_RESOURCE_INDEX:
                classComboBox.removeAllItems();
                // Hard code --- HORRROR!!!!!!
                if ( resourceComboBox.getSelectedItem() == null )
                    selectedResouceName = "Property";
                else
                    selectedResouceName = resourceComboBox.getSelectedItem().toString();
                collection = facade.getClassStandardNames(selectedResouceName);
                Hashtable map = facade.getClassesNameStrdName(selectedResouceName);
//                System.out.println("Classes-> " + collection.toString());
                iterator = collection.iterator();
                while (iterator.hasNext()) {
                    className = iterator.next().toString();
                    classNameMap.put((String) map.get(className), className);
                    className = (String) map.get(className);
                    classComboBox.addItem(className);
                }
                classComboBox.setSelectedIndex(0);
            case COMBOBOX_CLASS_INDEX:
            case COMBOBOX_TYPE_INDEX:
            	// de-select any rows in the dmqlTable
            	dmqlTable.getSelectionModel().clearSelection();
            	// repaint the table with the selected resource/class/type values
                String selectedResource = (String) resourceComboBox.getSelectedItem();
                String selectedClass = (String) classComboBox.getSelectedItem();
                String selectedType = (String) typeComboBox.getSelectedItem();
                if ((selectedResource != null) && (selectedClass != null) && (selectedType != null)) {
                	setTable(selectedResource, selectedClass, selectedType);
                }
            case COMBOBOX_DATATYPE_INDEX:
                selectedDataTypeName = (String) dataTypeComboBox.getSelectedItem();
                fieldComboBox.removeAllItems();
                // Hard code --- HORRROR!!!!!!
                if ( resourceComboBox.getSelectedItem() == null )
                    selectedResouceName = "Property";
                else
                    selectedResouceName = resourceComboBox.getSelectedItem().toString();
                if ( classComboBox.getSelectedItem() == null )
                    selectedClassName = "";
                else {
                    selectedClassName = (String) classNameMap.get(classComboBox.getSelectedItem().toString());
                    selectedClassName2 = classComboBox.getSelectedItem().toString();
                }
                if ( dataTypeComboBox.getSelectedItem() == null )
                    selectedDataTypeName = "Decimal";
                else
                    selectedDataTypeName = dataTypeComboBox.getSelectedItem().toString();
                MetadataField field = null;
                String type = (String) typeComboBox.getSelectedItem();
                Collection fieldCollection =
                        facade.getMetadataFieldsByDataType(
                                type,
                                selectedResouceName,
                                selectedClassName,
                                selectedDataTypeName);
                if (fieldCollection == null || fieldCollection.size() == 0) {
                	fieldCollection =
                        facade.getMetadataFieldsByDataType(
                                type,
                                selectedResouceName,
                                selectedClassName2,
                                selectedDataTypeName);
                }
//                List fieldList = Arrays.asList(fieldCollection.toArray());
//                Collections.sort(fieldList);
                if ((fieldCollection == null) || (fieldCollection.isEmpty())) {
//                    System.out.println("       NO FIELDS (NuLL)");
                    fieldComboBox.setEnabled(false);
                } else {
                    fieldCollection = new TreeSet(fieldCollection);
                    fieldComboBox.setEnabled(true);
//                    Iterator fieldIterator = fieldList.iterator();
                    Iterator fieldIterator = fieldCollection.iterator();
                    while (fieldIterator.hasNext()) {
                        field = (MetadataField) fieldIterator.next();
                        if (type.equals("Standard")) {
                            fieldComboBox.addItem(field.getStandardName());
                        }
                        else {
                            fieldComboBox.addItem(field.getSystemName());
                        }
                    }
                    fieldComboBox.setSelectedIndex(0);
                }

                String selectedDataType = dataTypeComboBox.getSelectedItem() != null ? dataTypeComboBox.getSelectedItem().toString() : "";
                String[] array = (String[]) valueTypeToDataTypeMap.get(selectedDataType);
                if ( array != null ) {
                    valueNameComboBox.removeAllItems();
                    for ( int i=0; i < array.length; i++ ) {
                        valueNameComboBox.addItem(array[i]);
                    }
                }
            case COMBOBOX_VALUENAME_INDEX:
                // disable the valueTextField for datatype/valuetypes that don't need a value set
            	boolean isEnabled = ! NO_VALUE_LIST.contains((String) dataTypeComboBox.getSelectedItem() + PROPERTY_KEY_TOKEN_SEPARATOR + valueNameComboBox.getSelectedItem());
            	valueLabel.setEnabled(isEnabled);
                valueTextField.setEnabled(isEnabled);
                break;
        }
    }


    static public void main(String[] args) throws Exception {
        // Delete these - development testing only
       String m_password = "password";
       String m_serverUrl = "http://localhost:8080/rets/server/login";
//       String m_transLogDir = "C:/tmp/ComplianceLogs";
       String m_transLogDir = "/ComplianceLogs";
       String m_userAgent = null;
       String m_username = "266123";
       String m_uaPassword=null;

       DMQLTestConfigurer dmqlTestConfigurer =
                new DMQLTestConfigurer(m_username, m_password, m_serverUrl, m_transLogDir, m_userAgent, m_uaPassword);
        DMQLConfigurator instance = new DMQLConfigurator(dmqlTestConfigurer, null);
//        instance.setDmqlTestConfigurer(dmqlTestConfigurer);
        instance.show();
//        instance.populate();
    }

    /**
        Class to describe a single DMQL search parameter set.
     */
    private class DMQLSearchParameterSet {
        private String dataType = null;
        private String field = null;
        private String valueType = null;
        private String value = null;

        public DMQLSearchParameterSet(String dataType, String field, String valueType, String value) {
            this.setDataType(dataType);
            this.setField(field);
            this.setValueType(valueType);
            this.setValue(value);
        }

        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getValueType() { return valueType; }
        public void setValueType(String valueType) { this.valueType = valueType; }

        public String toString() {
        	return "DMQLSearchParameterSet {dataType=" + dataType + ", field=" + field + ", value=" + value + ", valueType=" + valueType + "}";
        }
    }

    /**
     *  ActionListener implementation for the indexed ComboBoxes.
     */
    private class ComboBoxActionListener implements ActionListener {
    	private int index;

    	public ComboBoxActionListener(int index) {
    		this.index = index;
    	}

    	public void actionPerformed(ActionEvent e) {
           comboBoxChange(index);
        }
    }

    /**
        ActionListener implementation for the OK button.
     */
    private class SaveButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	setPropertyManagerValues();
        	// if the CTRL key was held down (completion check bypass) or the properties
        	// are complete
        	if (((e.getModifiers() & ActionEvent.CTRL_MASK) != 0) || checkPropertiesForCompletion()) {
				try {
	            	propertyManager.persist();
	            }
	            catch (Exception ex) {
	            	ex.printStackTrace();
	                JOptionPane.showMessageDialog(DMQLConfigurator.this, "FAILURE : Could not Update Properities");
	            }
	           	dispose();
        	}
        }
    }

    /**
        ListSelectionListener implemetation for the DMQL table.
     */
    private class DMQLTableSelectionListener implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent e) {
    		int row = dmqlTable.getSelectedRow();
    		if (row >= 0) {
    			dataTypeComboBox.setSelectedItem(dmqlTable.getValueAt(row, 0));
    			fieldComboBox.setSelectedItem(dmqlTable.getValueAt(row, 1));
    			valueNameComboBox.setSelectedItem(dmqlTable.getValueAt(row, 2));
    			valueTextField.setText((String) dmqlTable.getValueAt(row, 3));
    		}
    	}
    }

    /**
        JDialog to report save errors.
     */
    private class SaveErrorDialog extends JDialog {
		public SaveErrorDialog(Set testSet) {
			super(DMQLConfigurator.this);
	    	JDialog errorDialog = new JDialog(this, "Error: Missing Value(s)", true);
	    	errorDialog.setResizable(false);
	    	errorDialog.getContentPane().setLayout(new BorderLayout());

	    	JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    	titlePanel.add(new JLabel("Unable to save because of the following missing value(s)."));
	    	errorDialog.getContentPane().add(titlePanel, BorderLayout.NORTH);

	    	JPanel errorListPanel = new JPanel(new GridLayout(testSet.size(), 1));
	    	errorListPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    	Iterator iterator = testSet.iterator();
	    	while (iterator.hasNext()) {
	    		String labelString = ((String) iterator.next()).replace(PROPERTY_KEY_TOKEN_SEPARATOR, ' ');
	    		errorListPanel.add(new JLabel(labelString));
	    	}
	    	errorDialog.getContentPane().add(errorListPanel, BorderLayout.CENTER);

	    	JButton okButton = new JButton(" OK ");
	    	okButton.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			dispose();
	    		}
	    	});
	    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    	buttonPanel.add(okButton);
	    	errorDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	    	errorDialog.pack();
	    	errorDialog.setSize((int) (errorDialog.getWidth() * 1.1), errorDialog.getHeight());
	    	errorDialog.setLocationRelativeTo(this);
	    	errorDialog.setVisible(true);
		}
    }

    /**
        TableModel implementation for the DMQL parameters table.
     */
    private class DMQLTableModel extends AbstractTableModel {
        private String[] columnNames = {"Data Type", "Field", "Value Type", "Value"};

        private TreeMap dataMap = new TreeMap();

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
        	return dataMap.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public String[] getKeys() {
            int i = 0;
            String[] keyset = new String[dataMap.keySet().size()];
            Iterator iterator = dataMap.keySet().iterator();
            while ( iterator.hasNext() ) {
                keyset[i++] = (String) iterator.next();
            }
            return keyset;
        }

        public void clear() {
        	dataMap.clear();
        	fireTableDataChanged();
        }

        public void addData(DMQLSearchParameterSet dataRow) {
        	dataMap.put(getKey(dataRow), dataRow);
            fireTableDataChanged();
        }

        public DMQLSearchParameterSet getDataForKey(String key) {
            return (DMQLSearchParameterSet) dataMap.get(key);
        }

        public Object getValueAt(int row, int col) {
        	Iterator iterator = dataMap.keySet().iterator();
        	for (int i = 0; i < row; i++) {
        		iterator.next();
        	}
        	DMQLSearchParameterSet data = (DMQLSearchParameterSet) dataMap.get(iterator.next());
        	switch (col) {
        		case 0:
        			return data.getDataType();
        		case 1:
        			return data.getField();
        		case 2:
        			return data.getValueType();
        		case 3:
       				return data.getValue();
        	}
        	return null;
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}