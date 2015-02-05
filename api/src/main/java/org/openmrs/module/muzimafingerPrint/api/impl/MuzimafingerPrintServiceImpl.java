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

            //need to find out the encounter id from json data
            int encounterTypeId = NumberUtils.toInt("2", -999);
            EncounterType encounterType = Context.getEncounterService().getEncounterType(encounterTypeId);
            encounter.setEncounterType(encounterType);

            String providerString = jsonObject.getString("patient.provider_id");
            User user = Context.getUserService().getUserByUsername(providerString);
            encounter.setCreator(user);

            String locationString = jsonObject.getString("patient.location_id");
            int locationId = NumberUtils.toInt(locationString, -999);
            Location location = Context.getLocationService().getLocation(locationId);
            encounter.setLocation(location);

            //need to get data from json
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date encounterDatetime = new Date();//formatter.parse(jsonObject.getString("patient.encounter_datetime"));
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
            return jsonObject.getString("patient.fingerprint");
        }
        return "";
    }

    private Patient CreatePatient(final Encounter encounter, String patientData) throws JSONException {
        Patient patient = new Patient();
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");

            //setting names
            PersonName personName = new PersonName();
            personName.setFamilyName(jsonObject.getString("patient.family_name"));
            personName.setGivenName(jsonObject.getString("patient.given_name"));
            personName.setMiddleName((jsonObject.getString("patient.middle_name")));
            patient.addName(personName);

            //setting identifiers
            Set<PatientIdentifier> patientIdentifiers = new HashSet<PatientIdentifier>();
            PatientIdentifier patientIdentifier = new PatientIdentifier();

            //need to find out the name from json data
            PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByName("OpenMRS Identification Number");
            patientIdentifier.setIdentifierType(identifierType);
            patientIdentifier.setIdentifier(jsonObject.getString("patient.amrs_id"));
            patientIdentifier.setLocation(encounter.getLocation());
            patientIdentifier.setPreferred(true);

            patientIdentifiers.add(patientIdentifier);
            patient.setIdentifiers(patientIdentifiers);

            patient.setGender((jsonObject.getString("patient.sex")));
        }
        encounter.setPatient(patient);
        return patient;
    }

    private Patient getPatientFromUUID(String uuid){
        return Context.getPatientService().getPatientByUuid(uuid);
    }


}