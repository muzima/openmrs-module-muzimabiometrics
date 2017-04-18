package org.openmrs.module.muzimabiometrics;

import org.openmrs.BaseOpenmrsData;

/**
 * Created by root on 3/26/17.
 */
public class MuzimaTemporaryFingerprint extends BaseOpenmrsData {
    private Integer id;
    private byte[] fingerprint;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }
}
