package com.realtor.rets.compliance.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * @author pobrien
 */
public class ClientLauncher extends JApplet implements ActionListener
{
    private JButton button = null;

    public void init() {
        button = new JButton("Launch Client");
        button.addActionListener(this);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(button);
    }

    public void actionPerformed(ActionEvent e) {
        Client client = new Client();
        client.show();

    }


}
