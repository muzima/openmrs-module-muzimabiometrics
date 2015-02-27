package org.openmrs.module.muzimafingerPrint.api;

import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;
import org.openmrs.module.muzimafingerPrint.model.PatientFingerPrintModel;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimafingerPrintService extends OpenmrsService {

    @Transactional
    PatientFingerPrintModel savePatient(String patientData) throws JSONException, ParseException;

    PatientFingerPrintModel identifyPatient(String fingerprint) throws IOException;

    List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(String identifier) throws JSONException;

    @Transactional
    boolean updatePatient(String patientWithFingerprint) throws JSONException;

    MuzimaFingerprint getFingerprintByPatientUUID(String patientUUID);

    List<MuzimaFingerprint> getAll();

    MuzimaFingerprint findByUniqueId(String uuid);
}
