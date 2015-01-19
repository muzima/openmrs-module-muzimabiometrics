package org.openmrs.module.muzimafingerPrint;

public class PatientFingerPrintModel {
    private String patientUUID;
    private String fingerprintTemplate;

    public String getPatientUUID() {
        return patientUUID;
    }

    public String getFingerprintTemplate() {
        return fingerprintTemplate;
    }

    public PatientFingerPrintModel(String patientUUID, String fingerprintTemplate) {
        this.patientUUID = patientUUID;
        this.fingerprintTemplate = fingerprintTemplate;
    }
}
