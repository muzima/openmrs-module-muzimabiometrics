package org.openmrs.module.muzimabiometrics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import jlibfprint.JlibFprint;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class EnrollFingerprint {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    JlibFprint jlibfprint = new JlibFprint();
    JlibFprint.fp_print_data secondFinger,thirdFinger;
    public EnrollFingerprint(){

        prepareGUI();
    }
    public static void main(String[] args){
        EnrollFingerprint  enrollFingerprint = new EnrollFingerprint();
        enrollFingerprint.enrollSecondFinger();
        enrollFingerprint.enrollThirdFinger();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Patient Fingerprint Enrollment");
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
    private void enrollSecondFinger(){
        JButton enrollSecondFinger = new JButton("Enroll Second Finger");
        enrollSecondFinger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    secondFinger = jlibfprint.enroll_finger();
                    CloseableHttpClient client = HttpClientBuilder.create().build();
                    ByteArrayOutputStream secondFingerBO = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(secondFingerBO);
                    oos.writeObject(secondFinger);
                    oos.flush();
                    byte[] secondFingerEncodedByteArray = Base64.encodeBase64(secondFingerBO.toByteArray());
                    System.out.println("encodedByteArray +++++++++++++++++++"+secondFingerEncodedByteArray);
                    String url="http://localhost:8080/openmrs/module/muzimabiometrics/enrollSecondFingerprint.form";
                    HttpPost request=new HttpPost(url);
                    request.setEntity(new ByteArrayEntity(secondFingerEncodedByteArray));
                    request.setHeader("Content-type", "application/octet-stream");
                    HttpResponse response = client.execute(request);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        System.out.println(line);
                    }
                    statusLabel.setText("Completed scanning second finger");
                } catch (JlibFprint.EnrollException e) {
                    e.printStackTrace();
                }
                catch(IOException ex){
                }
            }
        });
        controlPanel.add(enrollSecondFinger);
        mainFrame.setVisible(true);
    }
    private void enrollThirdFinger(){
        JButton enrollThirdFinger = new JButton("Enroll Third Finger");
        enrollThirdFinger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                statusLabel.setText("Completed scanning third finger");
                try {
                    thirdFinger = jlibfprint.enroll_finger();
                    CloseableHttpClient client = HttpClientBuilder.create().build();
                    ByteArrayOutputStream thirdFingerBO = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(thirdFingerBO);
                    oos.writeObject(thirdFinger);
                    oos.flush();
                    byte[] thirdFingerEncodedByteArray = Base64.encodeBase64(thirdFingerBO.toByteArray());
                    System.out.println("encodedByteArray +++++++++++++++++++"+thirdFingerEncodedByteArray);
                    String url="http://localhost:8080/openmrs/module/muzimabiometrics/enrollThirdFingerprint.form";
                    HttpPost request=new HttpPost(url);
                    request.setEntity(new ByteArrayEntity(thirdFingerEncodedByteArray));
                    request.setHeader("Content-type", "application/octet-stream");
                    HttpResponse response = client.execute(request);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (JlibFprint.EnrollException e) {
                    e.printStackTrace();
                }
                catch(IOException ex){
                }
            }
        });
        controlPanel.add(enrollThirdFinger);
        mainFrame.setVisible(true);
    }
}
