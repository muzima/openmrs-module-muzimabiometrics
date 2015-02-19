package org.openmrs.module.muzimafingerPrint.panels;

import com.neurotec.biometrics.*;
import com.neurotec.biometrics.swing.NFingerView;
import com.neurotec.biometrics.swing.NFingerViewBase.ShownImage;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NFingerScanner;
import com.neurotec.images.NImages;
import com.neurotec.io.NBuffer;
import org.openmrs.module.muzimafingerPrint.model.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.services.JavaScriptCallerService;
import org.openmrs.module.muzimafingerPrint.settings.FingersTools;
import org.openmrs.module.muzimafingerPrint.util.Utils;
import com.neurotec.util.concurrent.CompletionHandler;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.DatatypeConverter;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

public final class EnrollFromScanner extends BasePanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final NDeviceManager deviceManager;
    private final CaptureCompletionHandler captureCompletionHandler = new CaptureCompletionHandler();
    private final EnrollCompletionHandler enrollCompletionHandler = new EnrollCompletionHandler();
    private NSubject subject;
    private boolean scanning;
    private NFingerView view;
    private JFileChooser fcImage;
    private JFileChooser fcTemplate;
    private File oldImageFile;
    private File oldTemplateFile;

    private JButton btnCancel;
    private JButton btnForce;
    private JButton btnRefresh;
    private JButton btnIdentifyPatient;
    private JButton btnRegisterPatient;
    private JButton btnScan;
    private JCheckBox cbAutomatic;
    private JCheckBox cbShowProcessed;
    private JLabel lblInfo;
    private JPanel panelButtons;
    private JPanel panelInfo;
    private JPanel panelMain;
    private JPanel panelSave;
    private JPanel panelScanners;
    private JPanel panelSouth;
    private JList scannerList;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneList;

    private JavaScriptCallerService service;
    java.util.List<PatientFingerPrintModel> patients;

    public EnrollFromScanner() {
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

    }

    private void startCapturing() {
        lblInfo.setText("");
        if (FingersTools.getInstance().getClient().getFingerScanner() == null) {
            JOptionPane.showMessageDialog(this, "Please select scanner from the list.", "No scanner selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        // Create a finger.
        NFinger finger = new NFinger();

        // Set Manual capturing mode if automatic isn't selected.
        if (!cbAutomatic.isSelected()) {
            finger.setCaptureOptions(EnumSet.of(NBiometricCaptureOption.MANUAL));
        }

        // Add finger to subject and finger view.
        subject = new NSubject();
        subject.getFingers().add(finger);
        view.setFinger(finger);
        view.setShownImage(ShownImage.ORIGINAL);

        // Begin capturing.
        NBiometricTask task = FingersTools.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.CAPTURE, NBiometricOperation.CREATE_TEMPLATE), subject);
        FingersTools.getInstance().getClient().performTask(task, null, captureCompletionHandler);
        scanning = true;
        updateControls();
    }

    private void identifyPatient() throws IOException, JSONException {

        service = new JavaScriptCallerService((Applet)this.getParent().getParent().getParent().getParent().getParent().getParent().getParent());

       // String fingerprint = DatatypeConverter.printBase64Binary(subject.getTemplateBuffer().toByteArray());
        String fingerprint = "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/zAuFCL/r/w8hrnAEREEe4X9QBYQBX58OQEKQQ1mA1VBFEcGZH1mgQxICF2DtWEbJQppc+GBDE8NXAT1QRk5B2WDGUIXRgdgc1kiEFgMX2SdQgNGCFro3oIGSQpcYwZjClYJYAYWQxpUBmaIHcMeRQRmEykjJEAKYWc1Qw9aB2GDjYMbWApnayoEGFIMZl9FxBNUEGJaWiQETwhYlFrEIh4RaGR6xBlOFV+FfUQeSQ5kF4aEIBUGYCaJZCQUG2BUlUQKTQRlT8mED1UIaVDpJAxdB3GZFcUhQgpcoyZFJC8hYFM1pQJMA20sOkUcQV9nGz2FHSMpYj1JJRgfD3aQVQUkJkdgU1nlA1IHbTZZxRkeEHnKgcUNUwVqNIqFFUkMbSaVpRtCDni9oUUYIgh3QKUlD08LY/i9RSMZDmhDzaUKUwRvSdHlB1UHcLTRxRogC3kwEUYNNwdxBRKGITgObkAlBglQB3+gLaYcEgZmFzWGGxkHaJw55h4QCWcXPoYgEQhss0rGDTUIbxBJRh4PCGOemgYZFwZmHqHGFBYQbzuxRgdFDXAAvaYjHQ5j0AL///////8BA/8EAv8A//////8FB/8DBP//////AP8EBhQK/38HCDQJBGECAB8B//8C/x//AP8DCRYGCjQB////////DvgHCBP/AvQEAzMICRMMC2MPDVcQCHEDAT8F/x8GAxT/B/EODVYQCVEKBjMAA/YIDRUQDEL/////AvYGCxMU/48JDBMaFKb/CvP/BvP/C/MCCbIIEDMaFHMVEWMSEIIJCFb/DvEIB1b/D/H/FfYRDTH/FfcRDkEF/3//////DPMJCEUNEigTGnMQDTMODzQVFzUSE0MTEBgNEYQVFiMiG1IbCx4QDXcSFhQiJmH/////C/YQGhUtH3MRDzf///8ZHfQYFyIbE1QSETYXHRIgIlISESUPGJEeHREhFvEdIS8gF1ERDlgVGS8eHf8YFf8P//////8kFCUKEMMRGyQcLCMsHCILEOcTFhUnJmEtJBUaECUTGyIqJkIYGS//HvEjMBUhFvIZ//////87I7EhHfH/////FPMaHDYk/x8oLhMlEykWF1QhK/EwKP8lIP8WF/8dI/8pJxIbE3YWICMlLhEdHhH/////K/IwIV////8fFBMaJigtOkUuKREiExcWIDIhKPEbJxcqLxEsLSQUHLI5L0cqG1YWIiIpMyIyLvEpJTEWIEEdMDElKBMuMxE5L1knEyYvLBMmHBQbJxUpOXL///87MGIoHTMj/y8xLRIkGmMcJhIvNiP///8kFEccLBIvMVEoIR8wMj8zORYnKTE2OhYxLEImKhEpOZT/O/Q1NC8oIR8r/y86/x//LfEsLxQ2/18uIf8wN/////8zJ/////84KjcuKBIrMl8oI/8wNf87//83Mv87/y84NF8oIxYw/y8qJxc5/0//OvcxL1E7//84M/8yIf80Nf8pMzMyO/P///85Nib/////NvQqJyQ4OyT/////JPUxNhc5/6////////80NfIj/58=";
        PatientFingerPrintModel patient =  service.identifyPatient(fingerprint);

        if(patient!= null){
            service.updatePatientListView(patient);
        }
        else {
            btnRegisterPatient.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Patient Not Found", "Identification", JOptionPane.PLAIN_MESSAGE);

        }
    }

    private void updateShownImage() {
        if (cbShowProcessed.isSelected()) {
            view.setShownImage(ShownImage.RESULT);
        } else {
            view.setShownImage(ShownImage.ORIGINAL);
        }
    }


    void updateStatus(String status) {
        lblInfo.setText(status);
    }

    NSubject getSubject() {
        return subject;
    }

    NFingerScanner getSelectedScanner() {
        return (NFingerScanner) scannerList.getSelectedValue();
    }


    @Override
    protected void initGUI() {
        panelMain = new JPanel();
        panelScanners = new JPanel();
        scrollPaneList = new JScrollPane();
        scannerList = new JList();
        panelButtons = new JPanel();
        btnRefresh = new JButton();
        btnScan = new JButton();
        btnCancel = new JButton();
        btnForce = new JButton();
        cbAutomatic = new JCheckBox();
        scrollPane = new JScrollPane();
        panelSouth = new JPanel();
        panelInfo = new JPanel();
        lblInfo = new JLabel();
        panelSave = new JPanel();
        btnIdentifyPatient = new JButton();
        btnRegisterPatient = new JButton();
        cbShowProcessed = new JCheckBox();

        setLayout(new BorderLayout());

        panelMain.setLayout(new BorderLayout());

        panelScanners.setBorder(BorderFactory.createTitledBorder("Scanners list"));
        panelScanners.setLayout(new BorderLayout());

        scrollPaneList.setPreferredSize(new Dimension(0, 90));

        scannerList.setModel(new DefaultListModel());
        scannerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scannerList.setBorder(LineBorder.createBlackLineBorder());
        scrollPaneList.setViewportView(scannerList);

        panelScanners.add(scrollPaneList, BorderLayout.CENTER);

        panelButtons.setLayout(new FlowLayout(FlowLayout.LEADING));

        btnRefresh.setText("Refresh list");
        panelButtons.add(btnRefresh);

        btnScan.setText("Scan");
        panelButtons.add(btnScan);

        btnCancel.setText("Cancel");
        btnCancel.setEnabled(false);
        panelButtons.add(btnCancel);

        btnForce.setText("Force");
        panelButtons.add(btnForce);

        cbAutomatic.setSelected(true);
        cbAutomatic.setText("Scan automatically");
        panelButtons.add(cbAutomatic);

        panelScanners.add(panelButtons, BorderLayout.SOUTH);

        panelMain.add(panelScanners, BorderLayout.NORTH);
        panelMain.add(scrollPane, BorderLayout.CENTER);

        panelSouth.setLayout(new BorderLayout());

        panelInfo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        panelInfo.setLayout(new GridLayout(1, 1));

        lblInfo.setText(" ");
        panelInfo.add(lblInfo);

        panelSouth.add(panelInfo, BorderLayout.NORTH);

        panelSave.setLayout(new FlowLayout(FlowLayout.LEADING));

        btnIdentifyPatient.setText("Identify Patient");
        btnIdentifyPatient.setEnabled(true);
        panelSave.add(btnIdentifyPatient);

        btnRegisterPatient.setText("Register Patient");
        btnRegisterPatient.setEnabled(true);
        panelSave.add(btnRegisterPatient);

        cbShowProcessed.setSelected(true);
        cbShowProcessed.setText("Show processed image");
        panelSave.add(cbShowProcessed);

        panelSouth.add(panelSave, BorderLayout.SOUTH);

        panelMain.add(panelSouth, BorderLayout.SOUTH);

        add(panelMain, BorderLayout.CENTER);

        panelLicensing = new LicensingPanel(requiredLicenses, optionalLicenses);
        add(panelLicensing, java.awt.BorderLayout.NORTH);

        fcImage = new JFileChooser();
        fcImage.setFileFilter(new Utils.ImageFileFilter(NImages.getSaveFileFilter()));
        fcTemplate = new JFileChooser();
        view = new NFingerView();
        view.setShownImage(ShownImage.RESULT);
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                super.mouseClicked(ev);
                if (ev.getButton() == MouseEvent.BUTTON3) {
                    cbShowProcessed.doClick();
                }
            }

        });
        scrollPane.setViewportView(view);

        btnRefresh.addActionListener(this);
        btnScan.addActionListener(this);
        btnCancel.addActionListener(this);
        btnForce.addActionListener(this);
        btnIdentifyPatient.addActionListener(this);
        btnRegisterPatient.addActionListener(this);
        cbShowProcessed.addActionListener(this);
        scannerList.addListSelectionListener(new ScannerSelectionListener());
    }

    @Override
    protected void setDefaultValues() {
        // No default values.
    }

    @Override
    protected void updateControls() {
        btnScan.setEnabled(!scanning);
        btnCancel.setEnabled(scanning);
        btnForce.setEnabled(scanning && !cbAutomatic.isSelected());
        btnRefresh.setEnabled(!scanning);
       // btnIdentifyPatient.setEnabled(!scanning && (subject != null) && (subject.getStatus() == NBiometricStatus.OK));
        cbShowProcessed.setEnabled(!scanning);
        cbAutomatic.setEnabled(!scanning);
       // btnRegisterPatient.setEnabled(false);
    }

    @Override
    protected void updateFingersTools() {
        FingersTools.getInstance().getClient().reset();
        FingersTools.getInstance().getClient().setUseDeviceManager(true);
        FingersTools.getInstance().getClient().setFingersReturnProcessedImage(true);
    }

    public void updateScannerList() {
        DefaultListModel model = (DefaultListModel) scannerList.getModel();
        model.clear();
        for (NDevice device : deviceManager.getDevices()) {
            model.addElement(device);
        }
        NFingerScanner scanner = (NFingerScanner) FingersTools.getInstance().getClient().getFingerScanner();
        if ((scanner == null) && (model.getSize() > 0)) {
            scannerList.setSelectedIndex(0);
        } else if (scanner != null) {
            scannerList.setSelectedValue(scanner, true);
        }
    }

    public void cancelCapturing() {
        FingersTools.getInstance().getClient().cancel();
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            if (ev.getSource() == btnRefresh) {
                updateScannerList();
            } else if (ev.getSource() == btnScan) {
                startCapturing();
            } else if (ev.getSource() == btnCancel) {
                cancelCapturing();
            } else if (ev.getSource() == btnForce) {
                FingersTools.getInstance().getClient().force();
            } else if (ev.getSource() == btnIdentifyPatient) {
                identifyPatient();
            } else if (ev.getSource() == cbShowProcessed) {
                updateShownImage();
            } else if(ev.getSource() == btnRegisterPatient){

                String template = DatatypeConverter.printBase64Binary(subject.getTemplateBuffer().toByteArray());
                service.callRegisterPatientJavaScriptFunction(template);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class CaptureCompletionHandler implements CompletionHandler<NBiometricTask, Object> {

        @Override
        public void completed(final NBiometricTask result, final Object attachment) {
            if(result != null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        scanning = false;
                        updateShownImage();
                        if (result.getStatus() == NBiometricStatus.OK) {
                            updateStatus("Quality: " + getSubject().getFingers().get(0).getObjects().get(0).getQuality());
                        } else {
                            updateStatus(result.getStatus().toString());
                        }
                        updateControls();
                    }

                });
            }
        }

        @Override
        public void failed(final Throwable th, final Object attachment) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    th.printStackTrace();
                    scanning = false;
                    updateShownImage();
                    showErrorDialog(th);
                    updateControls();
                }

            });
        }

    }

    private class EnrollCompletionHandler implements CompletionHandler<NBiometricTask, Object> {

        @Override
        public void completed(final NBiometricTask result, final Object attachment) {

        }

        @Override
        public void failed(final Throwable throwable, final Object attachment) {

        }

    }

    private class ScannerSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            FingersTools.getInstance().getClient().setFingerScanner(getSelectedScanner());
        }

    }

}
