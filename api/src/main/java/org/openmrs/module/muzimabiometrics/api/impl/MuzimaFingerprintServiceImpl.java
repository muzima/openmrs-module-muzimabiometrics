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
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModels;
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
        String scannedFinger = patientJsonParser.getScannedFingerFromJson(patientData);//System.out.println("Patient Scanned Finger "+scannedFinger);
        muzimaFingerprint.setScannedFinger(scannedFinger);//System.out.println("Patient Scanned Finger "+muzimaFingerprint);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return patient;
    }
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint Fingerprint){
        return dao.saveMuzimaFingerprint(Fingerprint);
    }
    @Override
    public List<PatientFingerPrintModels> identifyPatient(String fingerprint) throws IOException {
        Patient patient = null;
        List<PatientFingerPrintModels> patients = getAllPatientsWithFingerPrint(fingerprint);
        /*enrollFingerPrints(patients);
        String patientUUID =  identifyFinger(fingerprint);
        patient = Context.getPatientService().getPatientByUuid(patientUUID);


        if(patient == null)
            return null;
        return new PatientFingerPrintModels(patient.getUuid(), fingerprint, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(),patient.getIdentifiers().toString());
        */
        return patients;
    }

    public List<PatientFingerPrintModels> findPatients(String searchInput) {
        List<PatientFingerPrintModels> patients = new ArrayList<PatientFingerPrintModels>();
        List<Patient> searchResults = Context.getPatientService().getPatients(searchInput);
        MuzimaFingerprint mfingerprint = null;
        String fingerprint;
        String scannedFinger;
        if(searchResults.size() != 0){
            for(Patient patientSearched : searchResults) {
                fingerprint = null;
                scannedFinger = null;
                mfingerprint = getFingerprintByPatientUUID(patientSearched.getUuid());
                if (mfingerprint != null) {
                    fingerprint = mfingerprint.getFirstFingerprint();
                    scannedFinger = mfingerprint.getScannedFinger();
                }
                patients.add(new PatientFingerPrintModels(patientSearched.getUuid(),
                                fingerprint,
                                patientSearched.getId(),
                                patientSearched.getGivenName(),
                                patientSearched.getFamilyName(),
                                patientSearched.getGender(),
                                patientSearched.getIdentifiers().toString(),
                                scannedFinger
                                )
                );
            }
        }
        return patients;
    }

    @Override
    public List<PatientFingerPrintModels> identifyPatientByOtherIdentifier(String identifier) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        List<PatientFingerPrintModels> patients = new ArrayList<PatientFingerPrintModels>();
        List<PatientIdentifier> patientIdentifiers = patientJsonParser.getPatientIdentifier(identifier);
        if(patientIdentifiers.size() != 0){
            for(PatientIdentifier patientIdentifier : patientIdentifiers){
                patients.add(new PatientFingerPrintModels(patientIdentifier.getPatient().getUuid(),
                        null,
                        patientIdentifier.getPatient().getId(),
                        patientIdentifier.getPatient().getGivenName(),
                        patientIdentifier.getPatient().getFamilyName(),
                        patientIdentifier.getPatient().getGender(),
                        patientIdentifier.getPatient().getIdentifiers().toString(),
                        null));
            }
        }
        return patients;
    }
    @Override
    public PatientFingerPrintModels addFingerprintToPatient(String patientUUID,String firstFingerPrint,String secondeFingerPrint,String thirdFingerPrint, String scannedFinger) throws JSONException {
        PatientFingerPrintModels patient = null;
        Patient pat = null;
        MuzimaFingerprint muzimaFingerprint=new MuzimaFingerprint();
        muzimaFingerprint.setPatientUUID(patientUUID);
        muzimaFingerprint.setFirstFingerprint(firstFingerPrint);
        muzimaFingerprint.setSecondFingerprint(secondeFingerPrint);
        muzimaFingerprint.setThirdFingerprint(thirdFingerPrint);
        muzimaFingerprint.setScannedFinger(scannedFinger);
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        pat = Context.getPatientService().getPatientByUuid(patientUUID);
        patient = new PatientFingerPrintModels(pat.getUuid(),
                firstFingerPrint,
                pat.getId(),
                pat.getGivenName(),
                pat.getFamilyName(),
                pat.getGender(),
                pat.getIdentifiers().toString(),
                scannedFinger
                );
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

    public void enrollFingerPrints(java.util.List<PatientFingerPrintModels> patientModels) throws IOException {


    }

    private List<PatientFingerPrintModels> getAllPatientsWithFingerPrint(String fingerprintData) {
        List<PatientFingerPrintModels> patients = new ArrayList<PatientFingerPrintModels>();
        List<MuzimaFingerprint> fingerprints = dao.getAll();
        for (MuzimaFingerprint fingerprint : fingerprints) {
            if(fingerprint.getFirstFingerprint() != null) {
                Patient patient = Context.getPatientService().getPatientByUuid(fingerprint.getPatientUUID());
                    patients.add(new PatientFingerPrintModels(patient.getUuid(), fingerprint.getFirstFingerprint(), patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(), patient.getIdentifiers().toString(),fingerprint.getScannedFinger()));
            }
        }
        return patients;
    }

    public String identifyFinger(String fingerprint) throws IOException {

        return "PATIENT_NOT_FOUND";

    }

    public List<PatientFingerPrintModels> getPatientByPatientUuid(String patientUuid){
        List<PatientFingerPrintModels> patient = new ArrayList<PatientFingerPrintModels>();
        List<PatientFingerPrintModels> patients = getAllPatientsWithFingerPrint(patientUuid);
        for(PatientFingerPrintModels pat:patients){
            if(patientUuid.equals(pat.getPatientUUID())){
                patient.add(pat);
            }
        }
        return patient;
    }
}
