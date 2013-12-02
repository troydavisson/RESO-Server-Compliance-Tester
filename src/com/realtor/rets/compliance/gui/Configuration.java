/*
 * Properties.java
 *
 */

package com.realtor.rets.compliance.gui;

import com.realtor.rets.compliance.PropertyManager;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.IOException;


/**
 *
 * @author  pobrien
 */
public class Configuration extends JDialog
{
    private Map map = new HashMap();
    PropertyManager pm = PropertyManager.getParametersPropertyManager();
    PropertyManager backup = null; // need to implement a deep copy

    /** Creates new form Properties */
    public Configuration(JFrame parentFrame) {
        super(parentFrame);
        initComponents(parentFrame);
        loadProperties();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents(JFrame parentFrame) {

        String[] keys =
                {
                  "ChangePassword.Username", "ChangePassword.OldPassword", "ChangePassword.NewPassword",
                  "GetMetadata.Class", "GetMetadata.Table", "GetObject.Id", "GetObject.Resource", "GetObject.Type",
                  "Search.Class", "Search.QueryStandard", "Search.QuerySystem", "Search.SearchType", "Search.SelectStandard", "Search.SelectSystem","Search.Payload",
                  "Update.Delimeter", "Update.Record", "Update.Resource", "Update.ClassName", "Update.Validate", "Update.Type",
                  "GetPayloadList.Id","PostObject.UpdateAction","PostObject.Type","PostObject.Resource","PostObject.UploadFile"
                };

//        int x = 0;
//        String[] keys = new String[pm.getProperties().keySet().size()];
//        Iterator iterator = pm.getProperties().keySet().iterator();
//        while ( iterator.hasNext() ) {
//            keys[x++] = iterator.next().toString();
//            System.out.println("." + x + " -> " + keys[x]);
//        }

        JLabel[] labels = new JLabel[keys.length];
        JTextField[] textFields = new JTextField[keys.length];
        for ( int i=0; i < textFields.length; i++  ) {
            textFields[i] = new JTextField();
            textFields[i].setColumns(20);
            labels[i] = new JLabel();
            labels[i].setHorizontalAlignment(SwingConstants.RIGHT);
        }

        getContentPane().setLayout(new BorderLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        basePanel.setLayout(new java.awt.GridLayout((keys.length + 1) / 2, 4));

        JLabel emptyLabel = new JLabel("             ");
//==============================================================================================================
//        authenticationPanel.setLayout(new GridLayout(7,2));

        for ( int i=0; i < keys.length; i++ ) {
            labels[i].setText(keys[i]);
            basePanel.add(labels[i]);
            basePanel.add(textFields[i]);
            map.put(keys[i], textFields[i]);
        }

        getContentPane().add(basePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel buttonSubPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        buttonSubPanel.add(saveButton);
        buttonSubPanel.add(cancelButton);
        buttonPanel.add(buttonSubPanel, BorderLayout.EAST);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        setTitle("Configure");
        setModal(true);

        pack();
        if (parentFrame != null) {
	        setLocationRelativeTo(parentFrame);
        }
    }

    private void saveActionPerformed(java.awt.event.ActionEvent evt)
    {
        try {
            saveProperties();
            pm.persist();
        } catch (Exception ioe) {
            JOptionPane.showMessageDialog(
                    this,
                    "EXCEPTION => " + ioe.getMessage(),
                    "Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt)
    {
        loadProperties();
        dispose();
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        loadProperties();
        dispose();
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setField(String key, String value) {
        getMap().put(key, value);
    }

    public String getField(String key) {
        return (String) getMap().get(key);
    }

    public void loadProperties() {
        pm.load();

        // For each textfield, look for property
        JTextField textfield = null;
        String key = null, value = null, empty = "";
        Iterator iterator = getMap().keySet().iterator();
        while ( iterator.hasNext() ) {
            key = (String) iterator.next();
            textfield = (JTextField) getMap().get(key);
            value = pm.getProperty(key);
            if ( value != null )
                textfield.setText((String)value);
            else
                textfield.setText(empty);
        }
    }

    public void saveProperties() throws IOException {
        // For each textfield, look for property
        JTextField textfield = null;
        String key = null, value = null, empty = "";
        Iterator iterator = getMap().keySet().iterator();
        while ( iterator.hasNext() ) {
            key = (String) iterator.next();
            textfield = (JTextField) getMap().get(key);
            pm.putValue(key, textfield.getText());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Configuration(null).show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel basePanel = new JPanel();

}