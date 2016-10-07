package org.openmrs.module.muzimabiometrics.services;

import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 19/01/15.
 */
public class JavaScriptCallerService {

   private Applet applet;
    public JavaScriptCallerService(Applet appletWindow)
    {
        applet = appletWindow;
    }

    public void registerPatient(String fingerprintData) {
        callRegisterPatientJavaScriptFunction(fingerprintData);
    }

    public void callRegisterPatientJavaScriptFunction(String fingerprintData)
    {
        try {
            JSObject window = JSObject.getWindow(applet);
            window.call("registerPatient", new Object[] {fingerprintData});
        } catch (JSException jse) {
            jse.printStackTrace();
        }
    }

    public PatientFingerPrintModel identifyPatient(String fingerprint) throws JSONException {

        String JSONPatient = callIdentifyPatientJavascriptFunction(fingerprint);
        if (JSONPatient.equals("no data found") || JSONPatient.equals("[]")) {
            return null;
        }
        PatientFingerPrintModel patient = PatientFingerPrintModelBuilder(JSONPatient);
        return patient;
    }

    private String callIdentifyPatientJavascriptFunction(String fingerprintData) {
        try {
            JSObject window = JSObject.getWindow(applet);
            String strdata = (String)window.call("identifyPatient", new Object[] {fingerprintData});
            return strdata;

        } catch (JSException jse) {
            jse.printStackTrace();
        }
        return null;
    }

    private PatientFingerPrintModel PatientFingerPrintModelBuilder(String jsonPatients) throws JSONException {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        JSONArray jsonArray = new JSONArray(jsonPatients);
        for (int i = 0; i< jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            PatientFingerPrintModel patient = new PatientFingerPrintModel();
            patient.setPatientUUID(object.getString("patientUUID"));
            patient.setFingerprintTemplate(object.getString("fingerprintTemplate"));
            patient.setId(Integer.parseInt(object.getString("id")));
            patient.setFamilyName(object.getString("familyName"));
            patient.setGivenName(object.getString("givenName"));
            patient.setGender(object.getString("gender"));
            patients.add(patient);
        }
        return patients.get(0);
    }
}
