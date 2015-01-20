package org.openmrs.module.muzimafingerPrint;

import org.openmrs.Patient;

public class PatientFingerPrintModel {
    private String patientUUID;
    private String fingerprintTemplate;
    private int id;
    private String givenName;
    private String familyName;
    private String gender;

    public String getPatientUUID() {
        return patientUUID;
    }

    public String getFingerprintTemplate() {
        return fingerprintTemplate;
    }

    public int getId() {
        return id;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGender() {
        return gender;
    }

    public PatientFingerPrintModel(Patient patient, String fingerprintTemplate) {
        this.patientUUID = patient.getUuid();
        this.fingerprintTemplate = fingerprintTemplate;
        this.id = patient.getId();
        this.givenName = patient.getGivenName();
        this.familyName = patient.getFamilyName();
        this.gender = patient.getGender();
    }
}
