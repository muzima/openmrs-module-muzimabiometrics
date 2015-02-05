package org.openmrs.module.muzimafingerPrint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.BaseOpenmrsData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MuzimaFingerprint extends BaseOpenmrsData{
    private Integer id;
    private String patientId;
    private String fingerprint;



    public MuzimaFingerprint(){
    }
    public MuzimaFingerprint(String patientId, String fingerprint){

        if (getUuid()==null) {
            setUuid(UUID.randomUUID().toString());
        }
        this.patientId = patientId;
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
                ", patientId=" + getPatientId() +
                ", fingerprint='" + getFingerprint()+ '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
