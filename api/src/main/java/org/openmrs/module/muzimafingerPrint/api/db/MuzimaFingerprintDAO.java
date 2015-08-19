package org.openmrs.module.muzimafingerPrint.api.db;

import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;

import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimaFingerprintDAO {

    public List<MuzimaFingerprint> getAll();
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint);
    public MuzimaFingerprint findById(Integer id);
    public MuzimaFingerprint findByUuid(String uuid);
    public MuzimaFingerprint findByPatientUUID(String patientUUID);
}