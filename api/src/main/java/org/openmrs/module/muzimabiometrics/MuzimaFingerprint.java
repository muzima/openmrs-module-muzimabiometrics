package org.openmrs.module.muzimabiometrics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.BaseOpenmrsData;

import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MuzimaFingerprint extends BaseOpenmrsData {
    private Integer id;
    private String patientUUID;
    private String firstFingerprint;
    private String secondFingerprint;
    private String thirdFingerprint;

    public MuzimaFingerprint() {
    }

    public MuzimaFingerprint(String patientUUID,String firstFingerprint, String secondFingerprint, String thirdFingerprint) {

        if (getUuid() == null) {
            setUuid(UUID.randomUUID().toString());
        }
        this.patientUUID = patientUUID;
        this.firstFingerprint = firstFingerprint;
        this.secondFingerprint = secondFingerprint;
        this.thirdFingerprint = thirdFingerprint;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuzimaFingerprint muzimaFingerprint = (MuzimaFingerprint) o;
        return this.getId().equals(muzimaFingerprint.getId());
    }

    @Override
    public String toString() {
        return "MuzimaFingerprint{" +
                "id=" + id +
                ", patientUUID=" + getPatientUUID() +
                ", firstFingerprint='" + getFirstFingerprint() + '\'' +
                ", secondFingerprint='" + getSecondFingerprint() + '\'' +
                ", thirdFingerprint='" + getThirdFingerprint() + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientUUID() {
        return patientUUID;
    }

    public void setPatientUUID(String patientUUID) {
        this.patientUUID = patientUUID;
    }

    public String getFirstFingerprint() {
        return firstFingerprint;
    }

    public void setFirstFingerprint(String firstFingerprint) {
        this.firstFingerprint = firstFingerprint;
    }

    public String getSecondFingerprint() {
        return secondFingerprint;
    }

    public void setSecondFingerprint(String secondFingerprint) {
        this.secondFingerprint = secondFingerprint;
    }

    public String getThirdFingerprint() {
        return thirdFingerprint;
    }

    public void setThirdFingerprint(String thirdFingerprint) {
        this.thirdFingerprint = thirdFingerprint;
    }
}
