package org.openmrs.module.muzimafingerPrint.api.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;
import org.openmrs.module.muzimafingerPrint.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.api.MuzimafingerPrintService;
import org.openmrs.module.muzimafingerPrint.api.db.hibernet.MuzimaFingerprintDAO;
import org.springframework.stereotype.Service;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vikas on 15/10/14.
 */
@Service
public class MuzimafingerPrintServiceImpl extends BaseOpenmrsService implements MuzimafingerPrintService {

    private MuzimaFingerprintDAO muzimaFingerprintDAO;

    public MuzimafingerPrintServiceImpl(MuzimaFingerprintDAO fingerprintDAO) {
        this.muzimaFingerprintDAO = fingerprintDAO;
    }

    public MuzimafingerPrintServiceImpl() {

    }
    @Override
    public List<PatientFingerPrintModel> getAllPatientsWithFingerPrint() {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<MuzimaFingerprint> fingerprints = muzimaFingerprintDAO.getAll();
        for (MuzimaFingerprint fingerprint : fingerprints) {
            if(fingerprint.getFingerprint() != null) {
                Patient patient = Context.getPatientService().getPatient(Integer.parseInt(fingerprint.getPatientId()));
                patients.add(new PatientFingerPrintModel(patient, fingerprint.getFingerprint()));
            }
        }
        return patients;
    }

    @Override
    public PatientFingerPrintModel savePatient(String patientData) throws JSONException, ParseException {

        Encounter encounter = CreateEncounter(patientData);
        Patient patient = CreatePatient(encounter, patientData);
        Context.getPatientService().savePatient(patient);
        Context.getEncounterService().saveEncounter(encounter);
        MuzimaFingerprint muzimaFingerprint = CreatePatientFingerPrint(patient, patientData);

        return new PatientFingerPrintModel(patient,muzimaFingerprint.getFingerprint());
    }

    private Encounter CreateEncounter(String patientData) throws JSONException, ParseException {
        Encounter encounter = new Encounter();
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");

            int encounterTypeId = NumberUtils.toInt("1", -999);
            EncounterType encounterType = Context.getEncounterService().getEncounterType(encounterTypeId);
            encounter.setEncounterType(encounterType);

            String providerString = jsonObject.getString("provider_id");
            User user = Context.getUserService().getUserByUuid(providerString);
            encounter.setCreator(user);

            String locationString = jsonObject.getString("location_id");
            Location location = Context.getLocationService().getLocationByUuid(locationString);
            encounter.setLocation(location);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date encounterDatetime = formatter.parse(jsonObject.getString("encounter_datetime"));
            encounter.setEncounterDatetime(encounterDatetime);
        }
        return  encounter;
    }

    private MuzimaFingerprint CreatePatientFingerPrint(Patient patient, String patientData) throws JSONException {

        MuzimaFingerprint fingerprint = new MuzimaFingerprint();
        String fingerprintData = getFingerPrintFromJson(patientData);
        fingerprint.setPatientId(patient.getPatientId().toString());
        fingerprint.setFingerprint(fingerprintData);
        muzimaFingerprintDAO.saveMuzimaFingerprint(fingerprint);

        return fingerprint;
    }

    private String getFingerPrintFromJson(String patientData) throws JSONException {
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");
            return jsonObject.getString("fingerprint");
        }
        return "";
    }

    private Patient CreatePatient(final Encounter encounter, String patientData) throws JSONException, ParseException {
        Patient patient = new Patient();
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");

            //setting names
            PersonName personName = new PersonName();
            personName.setFamilyName(jsonObject.getString("family_name"));
            personName.setGivenName(jsonObject.getString("given_name"));
            personName.setMiddleName((jsonObject.getString("middle_name")));
            patient.addName(personName);

            //setting identifiers
            Set<PatientIdentifier> patientIdentifiers = new HashSet<PatientIdentifier>();
            PatientIdentifier patientIdentifier = new PatientIdentifier();

            PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByName("OpenMRS Identification Number");
            patientIdentifier.setIdentifierType(identifierType);
            patientIdentifier.setIdentifier(jsonObject.getString("amrs_id"));
            patientIdentifier.setLocation(encounter.getLocation());
            patientIdentifier.setPreferred(true);

            patientIdentifiers.add(patientIdentifier);
            patient.setIdentifiers(patientIdentifiers);

            patient.setGender((jsonObject.getString("sex")));
        }
        encounter.setPatient(patient);
        return patient;
    }

    private Patient getPatientFromUUID(String uuid){
        return Context.getPatientService().getPatientByUuid(uuid);
    }


}