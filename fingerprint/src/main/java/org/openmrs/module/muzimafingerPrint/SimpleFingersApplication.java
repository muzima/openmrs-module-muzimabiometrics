package org.openmrs.module.muzimafingerPrint;

import org.json.JSONException;
import org.openmrs.module.muzimafingerPrint.panels.MainPanel;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.neurotec.lang.NCore;

public class SimpleFingersApplication extends Applet {

//    public static void main(String[] arg) throws IOException, JSONException {
//        JFrame frame = new JFrame();
//        frame.setTitle("Muzima Fingerprint Identification");
//        MainPanel panel = new MainPanel();
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//
//    }
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {

                    MainPanel panel = null;
                    try {
                        panel = new MainPanel();
                    } catch (IOException e) {
                        e.getStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    add(panel);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        System.out.println("starting...");
    }
    public void stop() {
        System.out.println("stopping...");
    }
    public void destroy() {
        System.out.println("preparing to unload...");
        NCore.shutdown();
    }
}
