package org.openmrs.module.muzimafingerPrint.panels;

import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NFingerScanner;
import com.neurotec.util.concurrent.CompletionHandler;
import org.json.JSONException;
import org.openmrs.module.muzimafingerPrint.model.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.services.JavaScriptCallerService;
import org.openmrs.module.muzimafingerPrint.settings.FingersTools;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.DatatypeConverter;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by vikas on 19/02/15.
 */
public class ScanFingerprint extends BasePanel implements ActionListener {

    private static final String LAUNCH_FINGERPRINT_APP = "Click to start scanning process ";
    private static final String INITIALIZING_FINGERPRINT_MODULE = "initializing fingerprint";
    private static final String OBTAINING_LICENCES = "Obtaining licences, please wait.";
    private static final String SEARCHING_FOR_DEVICE = "Connecting to fingerprint device.";
    private static final String SCANNING_FINGERPRINT_PROGRESS = "Scanning fingerprint. please put your finger on scanner.";
    private static final String SCANNING_FINGERPRINT_COMPLETED = "Scanning fingerprint completed";
    private static final String IDENTIFYING_PATIENT = "Identification of patient started";

    private static final String NO_LICENCE_FOUND = "No licence Found";
    private static final String NO_DEVICE_FOUND = "No device Found";
    private static final String NO_PATIENT_FOUND = "No patient Found";
    private static final String SCANNING_FAILED = "Unable to scan finger";
    private static final String INTERNAL_ERROR = "Something went wrong while identifying the patient.";

    private final CaptureCompletionHandler captureCompletionHandler = new CaptureCompletionHandler();
    private JLabel lblProgressMessage;
    private JPanel panelMain;
    private JPanel panelButtons;
    private JPanel panelMessage;
    private JList scannerList;
    private JButton btnTryAgain;
    private JButton btnLaunchApplet;
    private final NDeviceManager deviceManager;
    private NSubject subject;
    private JavaScriptCallerService service;

    public ScanFingerprint() {
        super();
        requiredLicenses = new ArrayList<String>();
        requiredLicenses.add("Biometrics.FingerExtraction");
        requiredLicenses.add("Biometrics.FingerMatchingFast");
        requiredLicenses.add("Biometrics.FingerMatching");
        requiredLicenses.add("Biometrics.FingerQualityAssessment");
        requiredLicenses.add("Biometrics.FingerSegmentation");
        requiredLicenses.add("Biometrics.FingerSegmentsDetection");
        requiredLicenses.add("Biometrics.Standards.Fingers");
        requiredLicenses.add("Biometrics.Standards.FingerTemplates");
        requiredLicenses.add("Devices.FingerScanners");

        optionalLicenses = new ArrayList<String>();
        optionalLicenses.add("Images.WSQ");

        FingersTools.getInstance().getClient().setUseDeviceManager(true);

        deviceManager = FingersTools.getInstance().getClient().getDeviceManager();
        deviceManager.setDeviceTypes(EnumSet.of(NDeviceType.FINGER_SCANNER));
        deviceManager.initialize();

        lblProgressMessage = new JLabel(LAUNCH_FINGERPRINT_APP);
        btnTryAgain = new JButton("Try Again");
        btnLaunchApplet = new JButton("Scan fingerprint");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        try {
            if (actionEvent.getSource() == btnTryAgain) {
                btnTryAgain.setVisible(false);
                RunFingerprintScanProcess();
            }
            else if(actionEvent.getSource() == btnLaunchApplet){
                btnLaunchApplet.setVisible(false);
                RunFingerprintScanProcess();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Applet Error", JOptionPane.PLAIN_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Applet Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    @Override
    protected void initGUI() throws IOException, JSONException {
        panelMain = new JPanel();
        panelMain.setBackground(Color.white);
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));

        panelMessage = new JPanel();
        panelMessage.setBackground(Color.white);
        panelMessage.add(lblProgressMessage);
        panelMessage.setLayout(new FlowLayout(FlowLayout.TRAILING));

        panelButtons = new JPanel();
        panelButtons.setBackground(Color.white);
        panelButtons.setLayout(new FlowLayout(FlowLayout.LEADING));

        btnTryAgain.setVisible(false);
        btnTryAgain.addActionListener(this);

        btnLaunchApplet.setVisible(true);
        btnLaunchApplet.addActionListener(this);

        panelButtons.add(btnTryAgain);
        panelButtons.add(btnLaunchApplet);

        panelMain.add(panelMessage);
        panelMain.add(panelButtons);

        add(panelMain);

        scannerList = new JList();
        scannerList.setModel(new DefaultListModel());
        scannerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scannerList.addListSelectionListener(new ScannerSelectionListener());
    }

    private void RunFingerprintScanProcess() throws IOException, JSONException {

        lblProgressMessage.setText(OBTAINING_LICENCES);
        if (obtainLicenses()) {
            lblProgressMessage.setText(SEARCHING_FOR_DEVICE);
            if (SearchDevice()) {
                lblProgressMessage.setText(SCANNING_FINGERPRINT_PROGRESS);
                if (!ScanFingerPrint()) {
                    lblProgressMessage.setText(SCANNING_FAILED);
                    btnTryAgain.setVisible(true);
                }
            } else {
                lblProgressMessage.setText(NO_DEVICE_FOUND);
                btnTryAgain.setVisible(true);
            }
        } else {
            lblProgressMessage.setText(NO_LICENCE_FOUND);
            btnTryAgain.setVisible(true);
        }

    }

    private boolean IdentifyPatient() throws JSONException {
        service = new JavaScriptCallerService((Applet) this.getParent().getParent());
        String fingerprint = DatatypeConverter.printBase64Binary(subject.getTemplateBuffer().toByteArray());
        PatientFingerPrintModel patient = service.identifyPatient(fingerprint);
        if (patient != null) {
            return true;
        }
        return false;
    }

    private boolean ScanFingerPrint() {

        NFinger finger = new NFinger();
        subject = new NSubject();
        subject.getFingers().add(finger);
        NBiometricTask task = FingersTools.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.CAPTURE, NBiometricOperation.CREATE_TEMPLATE), subject);
        FingersTools.getInstance().getClient().performTask(task, null, captureCompletionHandler);
        return true;

    }

    private boolean SearchDevice() {
        DefaultListModel model = (DefaultListModel) scannerList.getModel();
        model.clear();
        for (NDevice device : deviceManager.getDevices()) {
            model.addElement(device);
        }
        NFingerScanner scanner = (NFingerScanner) FingersTools.getInstance().getClient().getFingerScanner();
        if ((scanner == null) && (model.getSize() > 0)) {
            scannerList.setSelectedIndex(0);
            return true;
        } else if (scanner != null) {
            scannerList.setSelectedValue(scanner, true);
            return true;
        }
        return false;
    }

    private NFingerScanner getSelectedScanner() {
        return (NFingerScanner) scannerList.getSelectedValue();
    }

    @Override
    protected void setDefaultValues() {

    }

    @Override
    protected void updateControls() {

    }

    @Override
    protected void updateFingersTools() {
        FingersTools.getInstance().getClient().reset();
        FingersTools.getInstance().getClient().setUseDeviceManager(true);
        FingersTools.getInstance().getClient().setFingersReturnProcessedImage(true);

    }


    protected boolean obtainLicenses() {

        try {
            boolean status = FingersTools.getInstance().obtainLicenses(getRequiredLicenses());
            FingersTools.getInstance().obtainLicenses(getOptionalLicenses());
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Licenses Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
    }

    private class ScannerSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            FingersTools.getInstance().getClient().setFingerScanner(getSelectedScanner());
        }

    }

    private class CaptureCompletionHandler implements CompletionHandler<NBiometricTask, Object> {

        @Override
        public void completed(final NBiometricTask result, final Object attachment) {
            if (result != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (result.getStatus() == NBiometricStatus.OK) {
                            lblProgressMessage.setText(IDENTIFYING_PATIENT);
                            try {
                                    if (IdentifyPatient()) {
                                        lblProgressMessage.setText(LAUNCH_FINGERPRINT_APP);
                                        btnLaunchApplet.setVisible(true);
                                    } else {
                                        lblProgressMessage.setText(NO_PATIENT_FOUND);
                                        String template = DatatypeConverter.printBase64Binary(subject.getTemplateBuffer().toByteArray());
                                        service.registerPatient(template);
                                        btnLaunchApplet.setVisible(true);
                                        lblProgressMessage.setText(LAUNCH_FINGERPRINT_APP);
                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                lblProgressMessage.setText(INTERNAL_ERROR);
                                btnTryAgain.setVisible(true);
                            }
                        }
                    }

                });
            }
        }

        @Override
        public void failed(final Throwable th, final Object attachment) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lblProgressMessage.setText(SCANNING_FAILED);
                    btnTryAgain.setVisible(true);
                    th.printStackTrace();
                }

            });
        }

    }
}
