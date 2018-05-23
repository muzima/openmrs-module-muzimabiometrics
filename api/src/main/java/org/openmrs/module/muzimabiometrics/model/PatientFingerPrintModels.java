package org.openmrs.module.muzimabiometrics.model;

public class PatientFingerPrintModels {
    private String patientUUID;
    private String fingerprintTemplate;
    private int id;
    private String givenName;
    private String familyName;
    private String gender;
    private String identifiers;
    private String scannedFinger;

    public PatientFingerPrintModels() {
    }

    public PatientFingerPrintModels(String patientUUID, String fingerprintTemplate, int id,
                                   String givenName, String familyName, String gender, String identifiers, String scannedFinger) {
        this.patientUUID = patientUUID;
        this.fingerprintTemplate = fingerprintTemplate;
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.gender = gender;
        this.identifiers = identifiers;
        this.scannedFinger = scannedFinger;
    }

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


    public void setPatientUUID(String patientUUID) {
        this.patientUUID = patientUUID;
    }

    public void setFingerprintTemplate(String fingerprintTemplate) {
        this.fingerprintTemplate = fingerprintTemplate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getScannedFinger() { return scannedFinger; }

    public void setScannedFinger(String scannedFinger) { this.scannedFinger = scannedFinger; }

    public String getIdentifiers() { return identifiers; }

    public void setIdentifiers(String identifiers) { this.identifiers = identifiers; }
}
