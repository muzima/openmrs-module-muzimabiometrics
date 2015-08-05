package org.openmrs.module.muzimafingerPrint.api.impl;

import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NTemplate;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NMatchingResult;
import com.neurotec.io.NBuffer;
import com.neurotec.util.concurrent.CompletionHandler;

import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;
import org.openmrs.module.muzimafingerPrint.PatientJsonParser;
import org.openmrs.module.muzimafingerPrint.api.MuzimafingerPrintService;
import org.openmrs.module.muzimafingerPrint.api.db.MuzimaFingerprintDAO;
import org.openmrs.module.muzimafingerPrint.model.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.settings.FingersTools;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
@Service
public class MuzimafingerPrintServiceImpl extends BaseOpenmrsService implements MuzimafingerPrintService {

    private MuzimaFingerprintDAO dao;
    private final EnrollCompletionHandler enrollCompletionHandler = new EnrollCompletionHandler();

    public void setDao(MuzimaFingerprintDAO dao) {
        this.dao = dao;
    }

    public MuzimaFingerprintDAO getDao() {
        return dao;
    }

    @Override
    public PatientFingerPrintModel savePatient(String patientData) throws JSONException, ParseException {

        PatientJsonParser patientJsonParser = new PatientJsonParser();
        Patient patient = patientJsonParser.CreatePatient(patientData);
        Context.getPatientService().savePatient(patient);
        MuzimaFingerprint muzimaFingerprint = patientJsonParser.CreatePatientFingerPrint(patient, patientData);
        dao.saveMuzimaFingerprint(muzimaFingerprint);

        return new PatientFingerPrintModel(patient.getUuid(),muzimaFingerprint.getFingerprint(), patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender());
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

        boolean status = FingersTools.getInstance().obtainLicenses(requiredLicenses);
        if(status){

            List<PatientFingerPrintModel> patients = getAllPatientsWithFingerPrint();
            enrollFingerPrints(patients);
            String patientUUID = identifyFinger(fingerprint);
            patient = Context.getPatientService().getPatientByUuid(patientUUID);
        }
        if(patient == null)
            return null;
        return new PatientFingerPrintModel(patient.getUuid(), fingerprint, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender());
    }

    public List<PatientFingerPrintModel> findPatients(String searchInput) {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<Patient> searchResults = Context.getPatientService().getPatients(searchInput);
        MuzimaFingerprint mfingerprint = null;
        String fingerprint = null;
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
                        "No-finger-print",
                        patientIdentifier.getPatient().getId(),
                        patientIdentifier.getPatient().getGivenName(),
                        patientIdentifier.getPatient().getFamilyName(),
                        patientIdentifier.getPatient().getGender()));
            }
        }
        return patients;
    }

    @Override
    public boolean updatePatient(String patientWithFingerprint) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        String fingerprint = patientJsonParser.getFingerPrintFromJson(patientWithFingerprint);
        String patientUUID = patientJsonParser.getPatientUUIDFromJson(patientWithFingerprint);
        MuzimaFingerprint muzimaFingerprint = dao.findByPatientUUID(patientUUID);
        if( muzimaFingerprint == null) {
            muzimaFingerprint = new MuzimaFingerprint(patientUUID, fingerprint);
        } else {
            muzimaFingerprint.setFingerprint(fingerprint);
        }
        dao.saveMuzimaFingerprint(muzimaFingerprint);
        return true;
    }

    @Override
    public PatientFingerPrintModel addFingerprintToPatient(String patientWithFingerprint) throws JSONException {
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        PatientFingerPrintModel patient = null;
        Patient pat = null;
        String fingerprint = patientJsonParser.getFingerPrintFromJson(patientWithFingerprint);
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
        NBiometricTask enrollTask = FingersTools.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.ENROLL), null);
        if (patientModels.size() > 0) {
            for (PatientFingerPrintModel model : patientModels) {
                NTemplate template = createTemplate(model.getFingerprintTemplate());
                enrollTask.getSubjects().add(createSubject(template, model.getPatientUUID()));
            }
            FingersTools.getInstance().getClient().performTask(enrollTask, NBiometricOperation.ENROLL, enrollCompletionHandler);
        } else {
            return;
        }

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
    private NTemplate createTemplate(String fingerPrintTemplateString) {
        byte[] templateBuffer = DatatypeConverter.parseBase64Binary(fingerPrintTemplateString);//Base64.decode(fingerPrintTemplateString);
        return new NTemplate(new NBuffer(templateBuffer));
    }

    private NSubject createSubject(NTemplate template, String id) throws IOException {
        NSubject subject = new NSubject();
        subject.setTemplate(template);
        subject.setId(id);
        return subject;
    }

    public String identifyFinger(String fingerprint) throws IOException {

        NTemplate nTemplate = createTemplate(fingerprint);
        NSubject nSubject = createSubject(nTemplate, "0");
        NBiometricTask task = FingersTools.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.IDENTIFY), nSubject);
        NBiometricStatus status = FingersTools.getInstance().getClient().identify(nSubject);
        if (status == NBiometricStatus.OK) {
            for (NMatchingResult result : nSubject.getMatchingResults()) {
                return result.getId();
            }
        }
        return "PATIENT_NOT_FOUND";

    }
    private class EnrollCompletionHandler implements CompletionHandler<NBiometricTask, Object> {

        @Override
        public void completed(final NBiometricTask result, final Object attachment) {

        }

        @Override
        public void failed(final Throwable throwable, final Object attachment) {

        }

    }

}
