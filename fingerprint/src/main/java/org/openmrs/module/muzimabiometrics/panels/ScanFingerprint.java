package org.openmrs.module.muzimabiometrics.panels;

import javax.jnlp.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

public class ScanFingerprint {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private static BasicService basicService = null;

    public ScanFingerprint(){
        prepareGUI();
    }
    public static void main(String[] args){
        ScanFingerprint  scanFingerprint = new ScanFingerprint();
        scanFingerprint.prepareScan();
        try {
            basicService = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
        }
        catch (UnavailableServiceException e) {
            System.err.println("Lookup failed: " + e);
        }
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Fingerprint Scanning Application");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }
    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = ScanFingerprint.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private void prepareScan(){
        headerLabel.setText("Scan patient fingerprint");
        JButton scanFinger = new JButton("Scan Finger");
        scanFinger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                statusLabel.setText("scanning..........");
                try {
                    URL url = new URL(actionEvent.getActionCommand());
                    basicService.showDocument(url);
                }
                catch (MalformedURLException ignored) {
                }
            }
        });
        controlPanel.add(scanFinger);
        mainFrame.setVisible(true);
    }
}