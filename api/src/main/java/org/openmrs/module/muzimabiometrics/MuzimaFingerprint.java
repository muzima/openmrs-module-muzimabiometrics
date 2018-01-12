package org.openmrs.module.muzimabiometrics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.BaseOpenmrsData;

import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MuzimaFingerprint extends BaseOpenmrsData{
    private Integer id;
    private String patientUUID;
    private byte[] firstFingerprint;
    private byte[] secondFingerprint;
    private byte[] thirdFingerprint;

    public MuzimaFingerprint(){
    }
    public MuzimaFingerprint(String patientUUID, byte[] firstFingerprint,byte[] secondFingerprint,byte[] thirdFingerprint){

        if (getUuid()==null) {
            setUuid(UUID.randomUUID().toString());
        }
        this.patientUUID = patientUUID;
        this.firstFingerprint = firstFingerprint;
        this.secondFingerprint=secondFingerprint;
        this.thirdFingerprint=thirdFingerprint;
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
                ", firstFingerprint='" + getFirstFingerprint()+ '\'' +
                ", secondFingerprint='" + getSecondFingerprint()+ '\'' +
                ", thirdFingerprint='" + getThirdFingerprint()+ '\'' +
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

    public byte[] getFirstFingerprint() {
        return firstFingerprint;
    }

    public void setFirstFingerprint(byte[] firstFingerprint) {
        this.firstFingerprint = firstFingerprint;
    }

    public byte[] getSecondFingerprint() {
        return secondFingerprint;
    }

    public void setSecondFingerprint(byte[] secondFingerprint) {
        this.secondFingerprint = secondFingerprint;
    }

    public byte[] getThirdFingerprint() {
        return thirdFingerprint;
    }

    public void setThirdFingerprint(byte[] thirdFingerprint) {
        this.thirdFingerprint = thirdFingerprint;
    }
}
