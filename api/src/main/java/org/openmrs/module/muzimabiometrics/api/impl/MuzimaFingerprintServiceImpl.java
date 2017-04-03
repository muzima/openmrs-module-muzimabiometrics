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
    public Patient savePatient(String patientData,byte[] fingerprint) throws JSONException, ParseException {

        PatientJsonParser patientJsonParser = new PatientJsonParser();
        Patient patient = patientJsonParser.CreatePatient(patientData);
        Context.getPatientService().savePatient(patient);
        MuzimaFingerprint muzimaFingerprint = new MuzimaFingerprint();
        muzimaFingerprint.setPatientUUID(patient.getUuid().toString());
        muzimaFingerprint.setFingerprint(fingerprint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return patient;
    }
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint){
        return dao.saveMuzimaFingerprint(Fingerprint);
    }
    @Override
    public PatientFingerPrintModel identifyPatient(String fingerprint) throws IOException {
        Patient patient = null;
        List<String> requiredLicenses = new ArrayList<String>();
        requiredLicenses.add("Biometrics.FingerExtraction");
        requiredLicenses.add("Biometrics.FingerMatchingFast");
        requiredLicenses.add("Biometrics.FingerMatching");
        requiredLicenses.add("Biometrics.FingerQualityAssessment");
        requiredLicenses.add("Biometrics.FingerSegmentation");
        requiredLicenses.add("Biometrics.FingerSegmentsDetection");
        requiredLicenses.add("Biometrics.Standards.Fingers");
        requiredLicenses.add("Biometrics.Standards.FingerTemplates");
        requiredLicenses.add("Devices.FingerScanners");

        return new PatientFingerPrintModel(patient.getUuid(), null, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender());
    }

    public List<PatientFingerPrintModel> findPatients(String searchInput) {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<Patient> searchResults = Context.getPatientService().getPatients(searchInput);
        MuzimaFingerprint mfingerprint = null;
        byte[] fingerprint = null;
        if(searchResults.size() != 0){
            for(Patient patientSearched : searchResults) {
                mfingerprint = getFingerprintByPatientUUID(patientSearched.getUuid());
                if (mfingerprint != null) {
                    fingerprint = mfingerprint.getFingerprint();
                }
                patients.add(new PatientFingerPrintModel(patientSearched.getUuid(),
                                fingerprint,
                                patientSearched.getId(),
                                patientSearched.getGivenName(),
                                patientSearched.getFamilyName(),
                                patientSearched.getGender())
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
                        patientIdentifier.getPatient().getGender()));
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
    public PatientFingerPrintModel addFingerprintToPatient(MuzimaFingerprint muzimaFingerprint){
        byte[] fingerprint = muzimaFingerprint.getFingerprint();
        String patientUUID = muzimaFingerprint.getPatientUUID();
        if(fingerprint!=null && !StringUtils.isEmpty(patientUUID)){
            dao.saveMuzimaFingerprint(muzimaFingerprint);
            Patient pat = Context.getPatientService().getPatientByUuid(patientUUID);
            return new PatientFingerPrintModel(pat.getUuid(),
                    fingerprint,
                    pat.getId(),
                    pat.getGivenName(),
                    pat.getFamilyName(),
                    pat.getGender());
        } else {
            return null;
        }


    }

    @Override
    public PatientFingerPrintModel addFingerprintToPatient(String patientWithFingerprint) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        PatientFingerPrintModel patient = null;
        Patient pat = null;
        //ByteArrayInputStream fingerprint = patientJsonParser.getFingerPrintFromJson(patientWithFingerprint);
        byte[] fingerprint =null;
        String patientUUID = patientJsonParser.getPatientUUIDFromJson(patientWithFingerprint);
        MuzimaFingerprint muzimaFingerprint = new MuzimaFingerprint();
        muzimaFingerprint.setPatientUUID(patientUUID);
        muzimaFingerprint.setFingerprint(fingerprint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        pat = Context.getPatientService().getPatientByUuid(patientUUID);
        patient = new PatientFingerPrintModel(pat.getUuid(),
                fingerprint,
                pat.getId(),
                pat.getGivenName(),
                pat.getFamilyName(),
                pat.getGender());
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
            if(fingerprint.getFingerprint() != null) {
                Patient patient = Context.getPatientService().getPatientByUuid(fingerprint.getPatientUUID());
                patients.add(new PatientFingerPrintModel(patient.getUuid(),fingerprint.getFingerprint(), patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender()));
            }
        }
        return patients;
    }

    public String identifyFinger(String fingerprint) throws IOException {

        return "PATIENT_NOT_FOUND";

    }
}
