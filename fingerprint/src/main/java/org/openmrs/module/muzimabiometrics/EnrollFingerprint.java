package org.openmrs.module.muzimabiometrics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.concurrent.TimeUnit;

import jlibfprint.JlibFprint;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class EnrollFingerprint {
    private static final Log log= LogFactory.getLog(EnrollFingerprint.class);
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private String baseUrl="http://localhost:8080/openmrs/module/muzimabiometrics";
    JlibFprint jlibfprint = new JlibFprint();
    JlibFprint.fp_print_data firstImage,secondImage,thirdImage;
    JButton firstCaptureBtn,secondCaptureBtn,thirdCaptureBtn,exit;
    int matchValue=0;
    int bozorthThreshold=40;
    String exception="";
    public EnrollFingerprint(){
        prepareGUI();
    }
    public static void main(String[] args){
        EnrollFingerprint  enrollFingerprint = new EnrollFingerprint();
            enrollFingerprint.enrollFingerFirstTime();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Patient Fingerprint Registration");
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
    private void enrollFingerFirstTime(){
        firstCaptureBtn = new JButton("Capture Thumb Finger First Time");
        firstCaptureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    firstImage=jlibfprint.enroll_finger();
                    statusLabel.setText("Scanning............");
                    enrollFinger(firstImage,"/enrollFirstImage.form","First Capturing Completed");
                    enrollFingerSecondTime();
                } catch (JlibFprint.EnrollException e) {
                    if(e.enroll_exception==-2){
                        exception="<html><font color='red'>Scanner not found, please insert scanner to continue.</font></html>";
                    }
                    else if(e.enroll_exception==100){
                        exception="<html><font color='red'>Low quality image captured, please scan again.</font></html>";
                    }
                    statusLabel.setText(exception);
                    e.printStackTrace();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        controlPanel.add(firstCaptureBtn);
        mainFrame.setVisible(true);
    }
    private void enrollFingerSecondTime(){
        firstCaptureBtn.setVisible(false);
        secondCaptureBtn = new JButton("Capture Thumb Finger Second Time");
        secondCaptureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    secondImage=jlibfprint.enroll_finger();
                    matchValue = JlibFprint.img_compare_print_data(firstImage,secondImage);
                    if(matchValue>bozorthThreshold){
                        enrollFinger(secondImage,"/enrollSecondImage.form","Second Capturing Completed");
                        enrollFingerThirdTime();
                    }
                    else{
                        statusLabel.setText("<html><font color='red'>Captured finger does not match, please rescan.</font></html>");
                    }
                } catch (JlibFprint.EnrollException e) {
                    if(e.enroll_exception==-2){
                        exception="<html><font color='red'>Scanner not found, please insert scanner to continue.</font></html>";
                    }
                    else if(e.enroll_exception==100){
                        exception="<html><font color='red'>Low quality image captured, please scan again.</font></html>";
                    }
                    statusLabel.setText(exception);
                    e.printStackTrace();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        controlPanel.add(secondCaptureBtn);
        mainFrame.setVisible(true);
    }
    private void enrollFingerThirdTime(){
        secondCaptureBtn.setVisible(false);
        thirdCaptureBtn = new JButton("Capture Thumb Finger Third Time");
        thirdCaptureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                statusLabel.setText("Third Capturing Completed");
                try {
                    thirdImage=jlibfprint.enroll_finger();
                    matchValue = JlibFprint.img_compare_print_data(secondImage,thirdImage);
                    if(matchValue>bozorthThreshold){
                        enrollFinger(thirdImage,"/enrollThirdImage.form","Third Capturing Completed");
                        exit();
                    }
                    else{
                        statusLabel.setText("<html><font color='red'>Captured finger does not match, please rescan</font></html>");
                    }
                } catch (JlibFprint.EnrollException e) {
                    if(e.enroll_exception==-2){
                        exception="<html><font color='red'>Scanner not found, please insert scanner to continue.</font></html>";
                    }
                    else if(e.enroll_exception==100){
                        exception="<html><font color='red'>Low quality image captured, please scan again.</font></html>";
                    }
                    statusLabel.setText(exception);
                    e.printStackTrace();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        controlPanel.add(thirdCaptureBtn);
        mainFrame.setVisible(true);
    }
//  no longer useful, manual ways need to be overwritten
 private void exit(){
        thirdCaptureBtn.setVisible(false);
        exit=new JButton("Exit");
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        controlPanel.add(exit);
        mainFrame.setVisible(true);
    }
    private void enrollFinger(JlibFprint.fp_print_data image,String path,String msg) throws JlibFprint.EnrollException, IOException {
        //the finger enrolment process begins here, unhide the button here
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ByteArrayOutputStream fingerBO = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(fingerBO);
        oos.writeObject(image);
        oos.flush();
        byte[] fingerEncodedByteArray = Base64.encodeBase64(fingerBO.toByteArray());
        System.out.println("encodedByteArray "+fingerEncodedByteArray);
        log.info("encodedByteArray "+fingerEncodedByteArray);
        String url=baseUrl+path;
        HttpPost request=new HttpPost(url);
        request.setEntity(new ByteArrayEntity(fingerEncodedByteArray));
        request.setHeader("Content-type", "application/octet-stream");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null){
            System.out.println(line);
        }
        statusLabel.setText("<html><font color='green'>"+msg+"</font></html>");
    }
}
