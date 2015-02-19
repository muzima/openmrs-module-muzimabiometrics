package org.openmrs.module.muzimafingerPrint.panels;

import org.json.JSONException;
import org.openmrs.module.muzimafingerPrint.settings.FingersTools;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public final class MainPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private JPanel panelButtons;
    private JButton btnLaunchApplet;
    private EnrollFromScanner enrollFromScanner;
    private ScanFingerprint scanFingerprint;

    public MainPanel() throws IOException, JSONException {
        super(new GridLayout(1, 1));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initGUI();
    }

    private void initGUI() throws IOException, JSONException {

//        tabbedPane = new JTabbedPane();
//        tabbedPane.addChangeListener(this);

//        enrollFromScanner = new EnrollFromScanner();
//        enrollFromScanner.init();
//        tabbedPane.addTab("Enroll from scanner", enrollFromScanner);

        panelButtons = new JPanel();

        btnLaunchApplet = new JButton();
        btnLaunchApplet.setSize(20,20);
        btnLaunchApplet.setText("Launch Application");
        btnLaunchApplet.addActionListener(this);
        panelButtons.add(btnLaunchApplet);

        add(panelButtons);

        setPreferredSize(new Dimension(500, 100));

//        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

//    public void obtainLicenses(BasePanel panel) throws IOException {
//        if (!panel.isObtained()) {
//            boolean status = FingersTools.getInstance().obtainLicenses(panel.getRequiredLicenses());
//            FingersTools.getInstance().obtainLicenses(panel.getOptionalLicenses());
//            panel.getLicensingPanel().setRequiredComponents(panel.getRequiredLicenses());
//            panel.getLicensingPanel().setOptionalComponents(panel.getOptionalLicenses());
//            panel.updateLicensing(status);
//        }
//    }

//    @Override
//    public void stateChanged(ChangeEvent evt) {
//        if (evt.getSource() == tabbedPane) {
//            try {
//                switch (tabbedPane.getSelectedIndex()) {
//                    case 0: {
//                        obtainLicenses(enrollFromScanner);
//                        enrollFromScanner.updateFingersTools();
//                        enrollFromScanner.updateScannerList();
//                        break;
//                    }
//                    default: {
//                        throw new IndexOutOfBoundsException("unreachable");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Could not obtain licenses for components: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            btnLaunchApplet.setVisible(false);
            scanFingerprint = new ScanFingerprint();
            scanFingerprint.init();
            scanFingerprint.updateFingersTools();
            add(scanFingerprint);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
