package org.openmrs.module.muzimabiometrics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.BaseOpenmrsData;

import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MuzimaFingerprint extends BaseOpenmrsData{
    private Integer id;
    private String patientUUID;
    private String fingerprint;



    public MuzimaFingerprint(){
    }
    public MuzimaFingerprint(String patientUUID, String fingerprint){

        if (getUuid()==null) {
            setUuid(UUID.randomUUID().toString());
        }
        this.patientUUID = patientUUID;
        this.fingerprint = fingerprint;
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
                ", fingerprint='" + getFingerprint()+ '\'' +
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

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
