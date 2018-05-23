package org.openmrs.module.muzimabiometrics.api;

import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.MuzimaTemporaryFingerprint;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModels;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimaFingerprintService extends OpenmrsService {

    @Transactional
    Patient savePatient(String patientData,String firstFingerPrint,String secondFingerprint,String thirdFingerprint) throws JSONException, ParseException;

    List<PatientFingerPrintModels> identifyPatient(String fingerprint) throws IOException;

    List<PatientFingerPrintModels> identifyPatientByOtherIdentifier(String identifier) throws JSONException;


    //boolean updatePatient(String patientWithFingerprint) throws JSONException;

    List<PatientFingerPrintModels> findPatients(String searchInput);

    MuzimaFingerprint getFingerprintByPatientUUID(String patientUUID);

    List<MuzimaFingerprint> getAll();

    MuzimaFingerprint findByUniqueId(String uuid);

    @Transactional
    PatientFingerPrintModels addFingerprintToPatient(String patientUUID,String firstFingerImage,String secondeFingerImage,String thirdFingerImage,String scannedFinger) throws JSONException;

    List<PatientFingerPrintModels> getPatientByPatientUuid(String patientUuid);
}
