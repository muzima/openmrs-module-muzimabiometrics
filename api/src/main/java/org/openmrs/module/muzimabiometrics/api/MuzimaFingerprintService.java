package org.openmrs.module.muzimabiometrics.api;

import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.MuzimaTemporaryFingerprint;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimaFingerprintService extends OpenmrsService {

    @Transactional
    Patient savePatient(String patientData,byte[] fingerprint) throws JSONException, ParseException;

    PatientFingerPrintModel identifyPatient(String fingerprint) throws IOException;

    List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(String identifier) throws JSONException;


    //boolean updatePatient(String patientWithFingerprint) throws JSONException;

    List<PatientFingerPrintModel> findPatients(String searchInput);

    MuzimaFingerprint getFingerprintByPatientUUID(String patientUUID);

    List<MuzimaFingerprint> getAll();

    MuzimaFingerprint findByUniqueId(String uuid);

    @Transactional
    PatientFingerPrintModel addFingerprintToPatient(String patientWithFingerprint) throws JSONException;
    @Transactional
    PatientFingerPrintModel addFingerprintToPatient(MuzimaFingerprint muzimaFingerprint);
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint);
}
