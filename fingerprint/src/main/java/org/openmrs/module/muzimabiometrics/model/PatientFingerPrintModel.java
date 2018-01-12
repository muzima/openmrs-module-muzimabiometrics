package org.openmrs.module.muzimabiometrics.model;

import java.io.ByteArrayInputStream;

public class PatientFingerPrintModel {
    private String patientUUID;
    private byte[] fingerprintTemplate;
    private int id;
    private String givenName;
    private String familyName;
    private String gender;
    private String identifiers;

    public PatientFingerPrintModel() {
    }

    public PatientFingerPrintModel(String patientUUID, byte[] fingerprintTemplate, int id,
                                   String givenName, String familyName, String gender, String identifiers) {
        this.patientUUID = patientUUID;
        this.fingerprintTemplate = fingerprintTemplate;
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.gender = gender;
        this.identifiers=identifiers;
    }

    public String getPatientUUID() {
        return patientUUID;
    }

    public byte[] getFingerprintTemplate() {
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

    public void setFingerprintTemplate(byte[] fingerprintTemplate) {
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

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }
}
