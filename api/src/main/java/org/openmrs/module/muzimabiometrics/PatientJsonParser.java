package org.openmrs.module.muzimabiometrics;

import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.*;
import org.openmrs.api.context.Context;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            return jsonObject.getString("patient.fingerprint");
        }
        return "";
    }

    public Patient CreatePatient(String patientData) throws JSONException, ParseException {
        Patient patient = new Patient();
        JSONArray jsonArray = new JSONArray("["+patientData+"]");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectMain = jsonArray.getJSONObject(i);
            JSONObject jsonObject = jsonObjectMain.getJSONObject("patient");

            //setting names
            PersonName personName = new PersonName();
            personName.setFamilyName(jsonObject.getString("patient.family_name"));
            personName.setGivenName(jsonObject.getString("patient.given_name"));
            personName.setMiddleName((jsonObject.getString("patient.middle_name")));
            patient.addName(personName);

            if(jsonObjectMain.has("personAddress")) {
                JSONObject jsonObjectPatientAddress = jsonObjectMain.getJSONObject("personAddress");
                PersonAddress personAddress = new PersonAddress();
                personAddress.setAddress1((jsonObjectPatientAddress.getString("personAddress.address1")));
                personAddress.setAddress2((jsonObjectPatientAddress.getString("personAddress.address2")));
                personAddress.setCityVillage((jsonObjectPatientAddress.getString("personAddress.cityVillage")));
                personAddress.setStateProvince((jsonObjectPatientAddress.getString("personAddress.stateProvince")));
                personAddress.setCountry((jsonObjectPatientAddress.getString("personAddress.country")));
                personAddress.setPostalCode((jsonObjectPatientAddress.getString("personAddress.postalCode")));
                patient.addAddress(personAddress);
            }

            //setting identifiers
            Set<PatientIdentifier> patientIdentifiers = new HashSet<PatientIdentifier>();

            JSONObject jsonObjectIdentifier = jsonObjectMain.getJSONObject("identifier");
            String identifierString = jsonObjectIdentifier.toString();

            CharSequence seq = ":[";
            boolean bool = identifierString.contains(seq);

            if(!bool){
                PatientIdentifier patientIdentifier = new PatientIdentifier();
                PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(jsonObjectIdentifier.getString("identifier.identifierType"));
                patientIdentifier.setIdentifierType(identifierType);
                patientIdentifier.setIdentifier(jsonObjectIdentifier.getString("identifier.amrs_id"));

                Location location = Context.getLocationService().getLocationByUuid(jsonObjectIdentifier.getString("identifier.location_id"));
                patientIdentifier.setLocation(location);
                patientIdentifier.setPreferred(true);

                patientIdentifiers.add(patientIdentifier);

            }else {
                JSONArray jsonObjectIdentifierType = jsonObjectIdentifier.getJSONArray("identifier.identifierType");
                JSONArray jsonObjectIdentifierValue = jsonObjectIdentifier.getJSONArray("identifier.amrs_id");
                JSONArray jsonObjectIdentifierLocation = jsonObjectIdentifier.getJSONArray("identifier.location_id");

                for (int j = 0; j < jsonObjectIdentifierType.length(); j++) {
                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                    PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(jsonObjectIdentifierType.get(j).toString());
                    patientIdentifier.setIdentifierType(identifierType);
                    patientIdentifier.setIdentifier(jsonObjectIdentifierValue.get(j).toString());

                    Location location = Context.getLocationService().getLocationByUuid(jsonObjectIdentifierLocation.get(j).toString());

                    patientIdentifier.setLocation(location);

                    if (j == 1)
                        patientIdentifier.setPreferred(true);
                    else
                        patientIdentifier.setPreferred(false);

                    patientIdentifiers.add(patientIdentifier);
                }
            }

            patient.setIdentifiers(patientIdentifiers);
            patient.setGender((jsonObject.getString("patient.sex")));

            int age = Integer.valueOf(jsonObject.getString("patient.age"));
            patient.setBirthdate(new SimpleDateFormat("YYYY-MM-dd").parse(jsonObject.getString("patient.birthdate")));
            if(jsonObject.has("patient.birthdateEstimatedInput")) {
                patient.setBirthdateEstimated(true);
            }else{
                patient.setBirthdateEstimated(false);
            }
           // patient.setBirthdateFromAge(age,null);
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
