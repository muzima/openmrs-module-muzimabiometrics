package org.openmrs.module.muzimabiometrics;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.context.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * Created by vikas on 17/02/15.
 */
public class PatientJsonParser {

    public Encounter CreateEncounter(String patientData) throws JSONException, ParseException {
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

            SimpleDateFormat formatter = Context.getDateFormat();
            Date encounterDatetime = formatter.parse(jsonObject.getString("encounter_datetime"));
            encounter.setEncounterDatetime(encounterDatetime);
        }
        return  encounter;
    }

    /*public MuzimaFingerprint CreatePatientFingerPrint(Patient patient, String patientData) throws JSONException {

        MuzimaFingerprint fingerprint = new MuzimaFingerprint();
        //String fingerprintData = getFingerPrintFromJson(patientData);
        fingerprint.setPatientUUID(patient.getUuid().toString());
        fingerprint.setFingerprint("elly");

        return fingerprint;
    }*/

    public String getFingerPrintFromJson(String patientData) throws JSONException {
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");
            return jsonObject.getString("fingerprint");
        }
        return "";
    }

    public Patient CreatePatient(String patientData) throws JSONException, ParseException {
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

            PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByName("AMRS Universal ID");
            patientIdentifier.setIdentifierType(identifierType);
            patientIdentifier.setIdentifier(jsonObject.getString("amrs_id"));

            String locationString = jsonObject.getString("location_id");
            Location location = Context.getLocationService().getLocationByUuid(locationString);

            patientIdentifier.setLocation(location);

            patientIdentifier.setPreferred(true);

            patientIdentifiers.add(patientIdentifier);
            patient.setIdentifiers(patientIdentifiers);

            patient.setGender((jsonObject.getString("sex")));

            String birthDate = jsonObject.getString("birth_date");
            SimpleDateFormat formatter = Context.getDateFormat();
            formatter.setLenient(false);
            patient.setBirthdate(formatter.parse(birthDate));
        }
        //encounter.setPatient(patient);
        return patient;
    }
    public List<PatientIdentifier> getPatientIdentifier(String jsonIdentifier) throws JSONException {

        List<PatientIdentifier> patientIdentifiers = new ArrayList<PatientIdentifier>();
        JSONArray jsonArray = new JSONArray("["+jsonIdentifier+"]");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");

            PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(jsonObject.getString("identifier_id"));
            patientIdentifiers = Context.getPatientService().getPatientIdentifiers(jsonObject.getString("identifier_value"), identifierType);
        }
        return patientIdentifiers;
    }

    public String getPatientUUIDFromJson(String patientWithFingerprint) throws JSONException {
        JSONArray jsonArray = new JSONArray("["+patientWithFingerprint+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");
            return jsonObject.getString("patientUUID");
        }
        return "";
    }
}
