package org.openmrs.module.muzimafingerPrint.api.db.hibernet;


import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;

import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimaFingerprintDAO {

    public List<MuzimaFingerprint> getAll();
    public void saveMuzimaFingerprint(MuzimaFingerprint Fingerprint);
    public MuzimaFingerprint findById(Integer id);
    public MuzimaFingerprint findByUuid(String uuid);
    public MuzimaFingerprint findByPatientId(String patientId);
}
