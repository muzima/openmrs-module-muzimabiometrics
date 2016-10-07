package org.openmrs.module.muzimabiometrics;

import com.neurotec.lang.NCore;

import java.applet.Applet;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.openmrs.module.muzimabiometrics.panels.MainPanel;

public class SimpleFingersApplication extends Applet {

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
    }

    public void stop() {
    }

    public void destroy() {
        NCore.shutdown();
    }
}
