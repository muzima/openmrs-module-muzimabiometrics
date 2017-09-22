package org.openmrs.module.muzimabiometrics.api.impl;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.MuzimaTemporaryFingerprint;
import org.openmrs.module.muzimabiometrics.PatientJsonParser;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.openmrs.module.muzimabiometrics.api.db.MuzimaFingerprintDAO;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
@Service
public class MuzimaFingerprintServiceImpl extends BaseOpenmrsService implements MuzimaFingerprintService {

    private MuzimaFingerprintDAO dao;
    //private final EnrollCompletionHandler enrollCompletionHandler = new EnrollCompletionHandler();

    public void setDao(MuzimaFingerprintDAO dao) {
        this.dao = dao;
    }

    public MuzimaFingerprintDAO getDao() {
        return dao;
    }

    @Override
    public Patient savePatient(String patientData,byte[] firstFingerPrint,byte[] secondFingerprint,byte[] thirdFingerprint) throws JSONException, ParseException {

        PatientJsonParser patientJsonParser = new PatientJsonParser();
        Patient patient = patientJsonParser.CreatePatient(patientData);
        Context.getPatientService().savePatient(patient);
        MuzimaFingerprint muzimaFingerprint = new MuzimaFingerprint();
        muzimaFingerprint.setPatientUUID(patient.getUuid().toString());
        muzimaFingerprint.setFirstFingerprint(firstFingerPrint);
        muzimaFingerprint.setSecondFingerprint(secondFingerprint);
        muzimaFingerprint.setThirdFingerprint(thirdFingerprint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return patient;
    }
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint){
        return dao.saveMuzimaFingerprint(Fingerprint);
    }
    @Override
    public PatientFingerPrintModel identifyPatient(String fingerprint) throws IOException {
        Patient patient = null;
        return new PatientFingerPrintModel(patient.getUuid(), null, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(),patient.getIdentifiers().toString());
    }

    public List<PatientFingerPrintModel> findPatients(String searchInput) {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<Patient> searchResults = Context.getPatientService().getPatients(searchInput);
        MuzimaFingerprint mfingerprint = null;
        byte[] fingerprint;
        if(searchResults.size() != 0){
            for(Patient patientSearched : searchResults) {
                fingerprint = null;
                mfingerprint = getFingerprintByPatientUUID(patientSearched.getUuid());
                if (mfingerprint != null) {
                    fingerprint = mfingerprint.getFirstFingerprint();
                }
                patients.add(new PatientFingerPrintModel(patientSearched.getUuid(),
                                fingerprint,
                                patientSearched.getId(),
                                patientSearched.getGivenName(),
                                patientSearched.getFamilyName(),
                                patientSearched.getGender(),
                                patientSearched.getIdentifiers().toString())
                );
            }
        }
        return patients;
    }

    @Override
    public List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(String identifier) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<PatientIdentifier> patientIdentifiers = patientJsonParser.getPatientIdentifier(identifier);
        if(patientIdentifiers.size() != 0){
            for(PatientIdentifier patientIdentifier : patientIdentifiers){
                patients.add(new PatientFingerPrintModel(patientIdentifier.getPatient().getUuid(),
                        null,
                        patientIdentifier.getPatient().getId(),
                        patientIdentifier.getPatient().getGivenName(),
                        patientIdentifier.getPatient().getFamilyName(),
                        patientIdentifier.getPatient().getGender(),
                        patientIdentifier.getPatient().getIdentifiers().toString()));
            }
        }
        return patients;
    }

    /*@Override
    public boolean updatePatient(String patientWithFingerprint) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        ByteArrayInputStream fingerprint = patientJsonParser.getFingerPrintFromJson(patientWithFingerprint);
        String patientUUID = patientJsonParser.getPatientUUIDFromJson(patientWithFingerprint);
        MuzimaFingerprint muzimaFingerprint = dao.findByPatientUUID(patientUUID);
        if( muzimaFingerprint == null) {
            muzimaFingerprint = new MuzimaFingerprint(patientUUID, fingerprint);
        } else {
            muzimaFingerprint.setFingerprint(fingerprint);
        }
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return true;
    }*/

    @Override
    public PatientFingerPrintModel addFingerprintToPatient(String patientUUID,byte[] firstFingerImage,byte[] secondeFingerImage,byte[] thirdFingerImage,Boolean fingerprintAlreadyExist) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        PatientFingerPrintModel patient = null;
        Patient pat = null;
        //ByteArrayInputStream fingerprint = patientJsonParser.getFingerPrintFromJson(patientWithFingerprint);
        if(!fingerprintAlreadyExist) {
            MuzimaFingerprint muzimaFingerprint = new MuzimaFingerprint();
            muzimaFingerprint.setPatientUUID(patientUUID);
            muzimaFingerprint.setFirstFingerprint(firstFingerImage);
            muzimaFingerprint.setSecondFingerprint(secondeFingerImage);
            muzimaFingerprint.setThirdFingerprint(thirdFingerImage);
            dao.saveMuzimaFingerprint(muzimaFingerprint);
        }
        pat = Context.getPatientService().getPatientByUuid(patientUUID);
        patient = new PatientFingerPrintModel(pat.getUuid(),
                firstFingerImage,
                pat.getId(),
                pat.getGivenName(),
                pat.getFamilyName(),
                pat.getGender(),
                pat.getIdentifiers().toString());
        return patient;
    }

    @Override
    public MuzimaFingerprint getFingerprintByPatientUUID(String patientUUID) {
        return dao.findByPatientUUID(patientUUID);
    }

    @Override
    public List<MuzimaFingerprint> getAll() {
        return dao.getAll();
    }

    @Override
    public MuzimaFingerprint findByUniqueId(String uuid) {
        return dao.findByUuid(uuid);
    }

    public void enrollFingerPrints(java.util.List<PatientFingerPrintModel> patientModels) throws IOException {


    }

    private List<PatientFingerPrintModel> getAllPatientsWithFingerPrint() {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<MuzimaFingerprint> fingerprints = dao.getAll();
        for (MuzimaFingerprint fingerprint : fingerprints) {
            if(fingerprint.getFirstFingerprint() != null) {
                Patient patient = Context.getPatientService().getPatientByUuid(fingerprint.getPatientUUID());
                patients.add(new PatientFingerPrintModel(patient.getUuid(),fingerprint.getFirstFingerprint(), patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(),patient.getIdentifiers().toString()));
            }
        }
        return patients;
    }

    public String identifyFinger(String fingerprint) throws IOException {

        return "PATIENT_NOT_FOUND";

    }
}
