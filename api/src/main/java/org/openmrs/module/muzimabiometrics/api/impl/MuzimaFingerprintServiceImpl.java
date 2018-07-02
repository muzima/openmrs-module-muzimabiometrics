package org.openmrs.module.muzimabiometrics.api.impl;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.muzima.web.resource.wrapper.FakeCohort;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.MuzimaTemporaryFingerprint;
import org.openmrs.module.muzimabiometrics.PatientJsonParser;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.openmrs.module.muzimabiometrics.api.db.MuzimaFingerprintDAO;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(MuzimaFingerprintServiceImpl.class.getSimpleName());

    //private final EnrollCompletionHandler enrollCompletionHandler = new EnrollCompletionHandler();

    public void setDao(MuzimaFingerprintDAO dao) {
        this.dao = dao;
    }

    public MuzimaFingerprintDAO getDao() {
        return dao;
    }

    @Override
    public Patient savePatient(String patientData,String firstFingerPrint,String secondFingerprint,String thirdFingerprint) throws JSONException, ParseException {

        PatientJsonParser patientJsonParser = new PatientJsonParser();
        Patient patient = patientJsonParser.CreatePatient(patientData);
        Context.getPatientService().savePatient(patient);
        MuzimaFingerprint muzimaFingerprint = new MuzimaFingerprint();
        String fingerprint = patientJsonParser.getFingerPrintFromJson(patientData);
        muzimaFingerprint.setPatientUUID(patient.getUuid().toString());
        muzimaFingerprint.setFirstFingerprint(fingerprint);
        muzimaFingerprint.setSecondFingerprint(fingerprint);
        muzimaFingerprint.setThirdFingerprint(fingerprint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return patient;
    }
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint){
        return dao.saveMuzimaFingerprint(Fingerprint);
    }
    @Override
    public List<PatientFingerPrintModel> identifyPatient(String fingerprint) throws IOException {
        Patient patient = null;
        List<PatientFingerPrintModel> patients = getAllPatientsWithFingerPrint(fingerprint);
        /*enrollFingerPrints(patients);
        String patientUUID =  identifyFinger(fingerprint);
        patient = Context.getPatientService().getPatientByUuid(patientUUID);


        if(patient == null)
            return null;
        return new PatientFingerPrintModel(patient.getUuid(), fingerprint, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(),patient.getIdentifiers().toString());
        */
        return patients;
    }

    public List<PatientFingerPrintModel> findPatients(String searchInput) {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<Patient> searchResults = Context.getPatientService().getPatients(searchInput);
        MuzimaFingerprint mfingerprint = null;
        String fingerprint;
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
    @Override
    public PatientFingerPrintModel addFingerprintToPatient(String patientUUID,String firstFingerPrint,String secondeFingerPrint,String thirdFingerPrint) throws JSONException {
        PatientFingerPrintModel patient = null;
        Patient pat = null;
        MuzimaFingerprint muzimaFingerprint=new MuzimaFingerprint();
        muzimaFingerprint.setPatientUUID(patientUUID);
        muzimaFingerprint.setFirstFingerprint(firstFingerPrint);
        muzimaFingerprint.setSecondFingerprint(secondeFingerPrint);
        muzimaFingerprint.setThirdFingerprint(thirdFingerPrint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        pat = Context.getPatientService().getPatientByUuid(patientUUID);
        patient = new PatientFingerPrintModel(pat.getUuid(),
                firstFingerPrint,
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

    private List<PatientFingerPrintModel> getAllPatientsWithFingerPrint(String fingerprintData) {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<MuzimaFingerprint> fingerprints = dao.getAll();
        for (MuzimaFingerprint fingerprint : fingerprints) {
            if(fingerprint.getFirstFingerprint() != null) {
                Patient patient = Context.getPatientService().getPatientByUuid(fingerprint.getPatientUUID());
                    patients.add(new PatientFingerPrintModel(patient.getUuid(), fingerprint.getFirstFingerprint(), patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(), patient.getIdentifiers().toString()));
            }
        }
        return patients;
    }

    public String identifyFinger(String fingerprint) throws IOException {

        return "PATIENT_NOT_FOUND";

    }

    public List<PatientFingerPrintModel> getPatientByPatientUuid(String patientUuid){
        List<PatientFingerPrintModel> patient = new ArrayList<PatientFingerPrintModel>();
        List<PatientFingerPrintModel> patients = getAllPatientsWithFingerPrint(patientUuid);
        for(PatientFingerPrintModel pat:patients){
            if(patientUuid.equals(pat.getPatientUUID())){
                patient.add(pat);
            }
        }
        return patient;
    }
}
