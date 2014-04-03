package com.realtor.rets.compliance.gui;

/*
 * com.realtor.rets.compliance.gui.Client.java
 *
  */

import com.realtor.rets.compliance.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;

/**
 *  This is a GUI client for the RETS Compliance Platform.  This class provides
 *  a GUI interface for executing and evaluating Compliance Test Scripts
 *
 * @author pobrien
 */
public class Client extends JFrame
{
    private JButton buttonLogDirectory;
    private JButton buttonResultsDirectory;
    private JButton buttonSelectTest;
    private JButton configureButton;
    private JButton dmqlButton;
    private JButton downloadButton;
    private JTextArea jTextArea1;
    private JTextField textLogDir;
    private JTextField textResultsDir;
    private JPasswordField textPassword;
    private JTextField textServerURL;
    private JTextField textUserAgent;
    private JTextField textUserAgentPassword;
    private JTextField textUsername;
    private JComboBox retsVersion;
    private static final String [] RETS_VERSIONS = {"1.8","1.7.2"};
    //private static final String [] RETS_VERSIONS = {"1.0", "1.5", "1.7","1.8"};
    private static final String DEFAULT_RETS_VERSION = "1.8";

    /** Creates new form com.realtor.rets.compliance.gui.Client */
    public Client() {
        initComponents();
        loadProperties();
        try {
            Enumeration enumeration = this.getClass().getClassLoader().getResources("*.dtd");
            if ( enumeration != null ) {
                while ( enumeration.hasMoreElements() ) {
                    System.out.println(enumeration.nextElement());
                }
            } else {
                System.out.println("--- Nothing returned by classloader");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        retsVersion = new JComboBox();
        for (int i = 0; i < RETS_VERSIONS.length; i++) {
            retsVersion.addItem(RETS_VERSIONS[i]);
        }
        textUsername = new JTextField();
        textServerURL = new JTextField();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        textPassword = new JPasswordField();
        textUserAgent = new JTextField();
        textUserAgentPassword = new JTextField();
        buttonSelectTest = new JButton();
        textLogDir = new JTextField();
        buttonLogDirectory = new JButton();
        textResultsDir = new JTextField();
        buttonResultsDirectory = new JButton();
        configureButton = new JButton();
        dmqlButton = new JButton();

        getContentPane().setLayout(new BorderLayout());

        setTitle("RETS Compliance Test Platform");
        setForeground(Color.white);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                saveTestClientProperties();
                dispose();
            }
        });

        // labels/text boxes panel
        GridBagLayout gbl = new GridBagLayout();
        JPanel infoPanel = new JPanel(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel usernameLabel = new JLabel("User Name");
        gbl.setConstraints(usernameLabel, gbc);
        infoPanel.add(usernameLabel);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        textUsername.setColumns(20);
        textUsername.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                textUsernameActionPerformed(evt);
            }
        });
        gbl.setConstraints(textUsername, gbc);
        infoPanel.add(textUsername);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel passwordLabel = new JLabel("Password");
        gbl.setConstraints(passwordLabel, gbc);
        infoPanel.add(passwordLabel);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        textPassword.setColumns(20);
        gbl.setConstraints(textPassword, gbc);
        infoPanel.add(textPassword, gbc);


        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel serverUrlLabel = new JLabel("Server URL");
        gbl.setConstraints(serverUrlLabel, gbc);
        infoPanel.add(serverUrlLabel);

        textServerURL.setColumns(40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(textServerURL, gbc);
        infoPanel.add(textServerURL);


        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel userAgentLabel = new JLabel("User Agent");
        gbl.setConstraints(userAgentLabel, gbc);
        infoPanel.add(userAgentLabel);

        textUserAgent.setColumns(40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(textUserAgent, gbc);
        infoPanel.add(textUserAgent);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
		JLabel userAgentPasswordLabel = new JLabel("User Agent Password");
		gbl.setConstraints(userAgentPasswordLabel, gbc);
		infoPanel.add(userAgentPasswordLabel);

		textUserAgentPassword.setColumns(40);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(textUserAgentPassword, gbc);
        infoPanel.add(textUserAgentPassword);


        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel logDirectoryLabel = new JLabel("Log Directory");
        gbl.setConstraints(logDirectoryLabel, gbc);
        infoPanel.add(logDirectoryLabel);

        textLogDir.setColumns(40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(textLogDir, gbc);
        infoPanel.add(textLogDir);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel resultsDirectoryLabel = new JLabel("Results Directory");
        gbl.setConstraints(resultsDirectoryLabel, gbc);
        infoPanel.add(resultsDirectoryLabel);

        textResultsDir.setColumns(40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(textResultsDir, gbc);
        infoPanel.add(textResultsDir);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JLabel retsVersionLabel = new JLabel("RETS Version");
        gbl.setConstraints(retsVersionLabel, gbc);
        infoPanel.add(retsVersionLabel);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(retsVersion, gbc);
        infoPanel.add(retsVersion);



        // create the buttonPanel

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        buttonSelectTest.setText("Run Selected Scripts");
        buttonSelectTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buttonSelectTestActionPerformed(evt);
            }
        });

        buttonPanel.add(buttonSelectTest, gbc);

        configureButton.setText("Configure...");
        configureButton.setActionCommand("ConfigureButton");
        //configureButton.setContentAreaFilled(false);
        configureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	configButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(configureButton, gbc);

        dmqlButton.setText("Configure DMQL...");
        dmqlButton.setActionCommand("ConfigureDMQLButton");
        //dmqlButton.setContentAreaFilled(false);
        dmqlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dmqlButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(dmqlButton, gbc);

        downloadButton = new JButton();
        downloadButton.setText("Download Tests");
        downloadButton.setActionCommand("DownloadButton");
        //downloadButton.setContentAreaFilled(false);
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                downloadScriptsActionPerformed(evt);
            }
        });
        /*
        buttonPanel.add(downloadButton, gbc);
        */
        buttonLogDirectory.setText("Select Log Directory");
        buttonLogDirectory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buttonLogDirectoryActionPerformed(evt);
            }
        });

        buttonPanel.add(buttonLogDirectory, gbc);

        buttonResultsDirectory.setText("Select Results Directory");
        buttonResultsDirectory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buttonResultsDirectoryActionPerformed(evt);
            }
        });

        buttonPanel.add(buttonResultsDirectory, gbc);


        // add the infoPanel plus the buttonPanel to the topPanel
        gbl = new GridBagLayout();
        JPanel topPanel = new JPanel(gbl);
        gbc = new GridBagConstraints();

        gbc.weightx = 1.0;
        gbl.setConstraints(infoPanel, gbc);
        topPanel.add(infoPanel);

        gbc.weightx = 4.0;
        gbl.setConstraints(buttonPanel, gbc);
        topPanel.add(buttonPanel);

        // add the topPanel to the top of the content pane
		getContentPane().add(topPanel, BorderLayout.NORTH);


		// create the text area in the center of the content pane
		JPanel middlePanel = new JPanel();
		middlePanel.setPreferredSize(new Dimension(750, 350));
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setEditable(false);
        jTextArea1.setBorder(new CompoundBorder());

        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane1.setPreferredSize(new Dimension(650, 350));
        middlePanel.add(jScrollPane1, BorderLayout.CENTER);
        getContentPane().add(middlePanel, BorderLayout.CENTER);


        JPanel copyrightPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel label1 = new JLabel();
        label1.setFont(new Font("Dialog", Font.ITALIC, 10));
        label1.setText(" Developed by Ronin Technologies");
        copyrightPanel.add(label1, gbc);
        JLabel label2 = new JLabel();
        label2.setFont(new Font("Dialog", Font.ITALIC, 10));
        label2.setText("for RESO ");
        copyrightPanel.add(label2, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(copyrightPanel);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void setEnabled(boolean enabled) {
        buttonLogDirectory.setEnabled(enabled);
        buttonResultsDirectory.setEnabled(enabled);
        buttonSelectTest.setEnabled(enabled);
        configureButton.setEnabled(enabled);
        downloadButton.setEnabled(enabled);
        dmqlButton.setEnabled(enabled);
        textLogDir.setEnabled(enabled);
        textResultsDir.setEnabled(enabled);
        textPassword.setEnabled(enabled);
        textServerURL.setEnabled(enabled);
        textUserAgent.setEnabled(enabled);
        textUserAgentPassword.setEnabled(enabled);
        textUsername.setEnabled(enabled);
        retsVersion.setEnabled(enabled);
    }

    private void configButtonActionPerformed(ActionEvent evt) {
        Configuration dialog = new Configuration(this);
        dialog.show();
    }

    private void dmqlButtonActionPerformed(ActionEvent evt) {
    	Cursor oldCursor = this.getCursor();
    	try {
        	setCursor(Cursor.WAIT_CURSOR);
            DMQLTestConfigurer dmqlTestConfigurer =
                    new DMQLTestConfigurer( textUsername.getText(),
                                            new String(textPassword.getPassword()),
                                            textServerURL.getText(),
                                            textLogDir.getText(),
                                            textUserAgent.getText(),
                                            textUserAgentPassword.getText());
            DMQLConfigurator dialog = new DMQLConfigurator(dmqlTestConfigurer, this);
            dialog.show();
        }
    	catch (Exception e) {
            JOptionPane.showMessageDialog(this, "EXCEPTION : " + e.getMessage());
        }
        finally {
        	setCursor(oldCursor);
        }

    }

    private void downloadScriptsActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(PropertyManager.getConfigDirectory()));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        ExampleFileFilter filter = new ExampleFileFilter( "xml");
//        chooser.addChoosableFileFilter(filter);
        chooser.setDialogTitle("Select Directory to Save Test Scripts To");
        int returnVal = chooser.showSaveDialog(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION) && (chooser.getSelectedFile() != null)) {
//            System.out.println("----- SELECTED FILE=> " + chooser.getSelectedFile().getPath());
            System.setProperty("testDir",PropertyManager.getConfigDirectory());
            ResourceManager resourceManager = new ResourceManager();
            File saveToDirectory = new File(chooser.getSelectedFile().getPath());
            // Get file list
            try {
                String line = null;
                InputStream inputStream =
                        this.getClass().getClassLoader().getResourceAsStream("testscripts.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ( (line = reader.readLine()) != null ) {
                    resourceManager.saveResourceStreamsLocally(line, saveToDirectory);
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }


    private void buttonLogDirectoryActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(PropertyManager.getConfigDirectory()));
        chooser.setDialogTitle("Select a directory to write Transaction logs");
        int returnVal = chooser.showOpenDialog(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION) && (chooser.getSelectedFile() != null)) {
            File file = chooser.getSelectedFile();
            textLogDir.setText(file.getAbsolutePath());

        }

    }
    
    private void buttonResultsDirectoryActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(PropertyManager.getConfigDirectory()));
        chooser.setDialogTitle("Select a directory to write Results files");
        int returnVal = chooser.showOpenDialog(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION) && (chooser.getSelectedFile() != null)) {
            File file = chooser.getSelectedFile();
            textResultsDir.setText(file.getAbsolutePath());

        }

    }

    private void buttonSelectTestActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter( "xml");
        File testScriptDirectory = new File(PropertyManager.getTestDirectory());
        chooser.setCurrentDirectory(testScriptDirectory);
        chooser.setMultiSelectionEnabled(true);
        chooser.addChoosableFileFilter(filter);
        chooser.setDialogTitle("Select one or more test scripts");
        int returnVal = chooser.showOpenDialog(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION) && (chooser.getSelectedFiles() != null)) {
            File files [] = chooser.getSelectedFiles();
            StringBuffer sb = new StringBuffer();
            saveTestClientProperties();
            ReportForm rf = new ReportForm(files, textUsername.getText(),
                                       new String(textPassword.getPassword()),
                                       textServerURL.getText(),
                                       textUserAgent.getText(),
                                       textUserAgentPassword.getText(),
                                       textLogDir.getText(), textResultsDir.getText(),
                                       this);
            sb.append("The tests listed below are being executed, this could take a while.");
            for (int i = 0; i< files.length;i++) {
                sb.append("\n\tExecuting Test : "+files[i].getAbsolutePath());
            }
            jTextArea1.setText(sb.toString());
        }

    }

    private void loadProperties()
    {
        PropertyManager pm = PropertyManager.getClientPropertyManager();
        pm.load();

        textUsername.setText(pm.getProperty("username"));
        textPassword.setText(pm.getProperty("password"));
        textServerURL.setText(pm.getProperty("loginurl"));
        textUserAgent.setText(pm.getProperty("useragent"));
        textUserAgentPassword.setText(pm.getProperty("uaPassword"));
        textLogDir.setText(pm.getProperty("transactionlogdirectory"));
        textResultsDir.setText(pm.getProperty("resultsdirectory"));
        String versionString = pm.getProperty("retsVersion");
        if (versionString == null) {
            retsVersion.setSelectedItem(DEFAULT_RETS_VERSION);
        }
        else {
            // read the numeric version of "RETS/1.5" (or whatever) value
            int index = versionString.indexOf("/");
            if (index > 0) {
                retsVersion.setSelectedItem(versionString.substring(index + 1));
            }
        }

        // Not sure where to put this right now...
        ResourceManager rm = new ResourceManager();
        rm.downloadDTDs();
    }

    private void saveTestClientProperties() {
        try {
            PropertyManager pm = PropertyManager.getClientPropertyManager();
            pm.putValue("username", textUsername.getText());
            pm.putValue("password", new String(textPassword.getPassword()));
            pm.putValue("loginurl", textServerURL.getText());
            pm.putValue("useragent", textUserAgent.getText());
            pm.putValue("uaPassword",textUserAgentPassword.getText());
            pm.putValue("transactionlogdirectory", textLogDir.getText());
            pm.putValue("resultsdirectory", textResultsDir.getText());
            pm.putValue("retsVersion", "RETS/" + retsVersion.getSelectedItem());
            pm.persist();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }


    private void textUsernameActionPerformed(ActionEvent evt) {
        loadProperties();
//        if (textUsername.getText().equalsIgnoreCase("reload"))
//        {
//            loadProperties();
//        }
//        else if (textUsername.getText().equalsIgnoreCase("local"))
//        {
//            textUsername.setText("266123");
//            textPassword.setText("password");
//            textServerURL.setText("http://127.0.0.1:8080/rets/server/login");
//            textUserAgent.setText("RETS Reference Implementation/1.5");
//            textLogDir.setText("");
//        }

    }

    private File [] getTestScripts()
    {
        URL aURL =
                this.getClass().getClassLoader().getResource(PropertyManager.getTestDirectory());
        File f = new File(aURL.getFile());
        FilenameFilter ff = new FilenameFilter (){
            public boolean accept(File dir, String name) {
                String upperCaseName = name.toUpperCase();
                return (upperCaseName.endsWith(".XML"));
            }
        };
        return f.listFiles(ff);
    }

    /** Execute the program
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
    	TestExecuter.readCommandLineOptions(args);

        ResourceManager rm = new ResourceManager(PropertyManager.getConfigDirectory());
        rm.assureConfigDirectory();

        Client client = new Client();
        client.show();
    }
}
