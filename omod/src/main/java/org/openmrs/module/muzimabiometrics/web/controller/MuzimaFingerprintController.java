package org.openmrs.module.muzimabiometrics.web.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.PatientJsonParser;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jlibfprint.JlibFprint;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by vikas on 15/01/15.
 */
@Controller
@RequestMapping(value = "module/muzimabiometrics")
public class MuzimaFingerprintController {
    private MuzimaFingerprintService service;
    private ObjectInputStream fingerprint;
    private boolean fingerprintIsSet;
    private byte[] fingerprintPayload;
    private String patientUUID;

    @ResponseBody
    @RequestMapping(value = "patient/{fingerprint}/fingerprint.form",  method = RequestMethod.GET)
    public MuzimaFingerprint getPatientFingerprintById(@PathVariable String patientUUID){

        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        return service.getFingerprintByPatientUUID(patientUUID);
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/enrollPatient.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public synchronized PatientFingerPrintModel enrollPatient(@RequestBody String jsonPayload) throws Exception {
            MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        PatientJsonParser patientJsonParser = new PatientJsonParser();
        Patient patient = service.savePatient(jsonPayload,fingerprintPayload);
        return new PatientFingerPrintModel(patient.getUuid(),fingerprintPayload, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender());
    }
    @RequestMapping(value = "identifyPatientByFingerprint.form", method = RequestMethod.POST)
    public synchronized void postFingerprintScan(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException, ClassNotFoundException, JlibFprint.EnrollException {
        service=Context.getService(MuzimaFingerprintService.class);
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        if(request!= null && patientUUID !=null && patientUUID !=""){
            if(request.getParameter("fingerprintIsSet").equalsIgnoreCase("true")) {
                Patient patient = Context.getPatientService().getPatientByUuid(patientUUID);
                jsonObject.put("patientUUID",patient.getUuid());
                jsonObject.put("id",patient.getId());
                jsonObject.put("givenName",patient.getGivenName());
                jsonObject.put("familyName",patient.getFamilyName());
                jsonObject.put("gender",patient.getGender());
                jsonArray.add(jsonObject);
            }
        }
        response.getWriter().print(jsonArray);
    }
    @ResponseBody
    @RequestMapping(value = "setFingerprint.form",  method = RequestMethod.POST)
    public synchronized void setFingerprint(@RequestBody byte[] request,Model model) throws IOException, ParseException, ClassNotFoundException {
        int matchValue;
        service=Context.getService(MuzimaFingerprintService.class);
    byte[] newScanByte;
        byte[] savedScanByte;
        JlibFprint.fp_print_data newJlibFprintData,savedJlibfprintData;
        ByteArrayInputStream newScanByteArray,savedScanByteArray;
        ObjectInput newScanInput,savedScanInput;
        fingerprintPayload=request;
        byte[] b=Base64.decodeBase64(request);
        ByteArrayInputStream is=new ByteArrayInputStream(b);
        fingerprint=new ObjectInputStream(is);
        fingerprintIsSet=true;
        if(request!= null) {
            newScanByte=Base64.decodeBase64(fingerprintPayload);
            System.out.println("newScanByte+++++++++++++++++++++++++++++++++"+newScanByte);
            newScanByteArray=new ByteArrayInputStream(newScanByte);
            newScanInput=new ObjectInputStream(newScanByteArray);
            newJlibFprintData=(JlibFprint.fp_print_data)newScanInput.readObject();
            List<MuzimaFingerprint> muzimaFingerprintList=service.getAll();
            for(MuzimaFingerprint muzimaFingerprint1:muzimaFingerprintList){
                savedScanByte=Base64.decodeBase64(muzimaFingerprint1.getFingerprint());
                System.out.println("savedScanByte+++++++++++++++++++++++++++++++++"+muzimaFingerprint1.getFingerprint());
                savedScanByteArray=new ByteArrayInputStream(savedScanByte);
                savedScanInput=new ObjectInputStream(savedScanByteArray);
                savedJlibfprintData=(JlibFprint.fp_print_data)savedScanInput.readObject();
                matchValue = JlibFprint.img_compare_print_data(newJlibFprintData,savedJlibfprintData);
                System.out.println("matchValue+++++++++++++++++++++++++++"+matchValue);
                if (matchValue > JlibFprint.BOZORTH_THRESHOLD)
                {
                    patientUUID=muzimaFingerprint1.getPatientUUID();
                    System.out.println("[OK] The two fingerprints are compatible!");
                    break;
                }
            }
        }

    }
    @RequestMapping(value = "getFingerprint.form",  method = RequestMethod.GET)
    public synchronized void getFingerprint(HttpServletRequest request,HttpServletResponse response,Model model)throws IOException, ParseException
    {
        if(request !=null) {
            if (request.getParameter("fingerprintIsSet") != null) {
                if (request.getParameter("fingerprintIsSet").equalsIgnoreCase("clear")) {
                    fingerprintIsSet = false;
                    patientUUID="";
                }
            }
        }
        response.getWriter().print(fingerprintIsSet);
    }
    @ResponseBody
    @RequestMapping(value = "fingerprint/identifyPatient.form", method = RequestMethod.POST, headers = {"content-type=application/json","Accept=application/json"})
    public List<PatientFingerPrintModel> identifyPatient(@RequestBody String fingerprint) throws Exception {

        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        PatientFingerPrintModel patient = service.identifyPatient(fingerprint);
        if(patient != null) {
            patients.add(patient);
        }
        return patients;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/identifyPatientByOtherIdentifier.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(@RequestBody String identifier) throws Exception {

        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        List<PatientFingerPrintModel> patients = service.identifyPatientByOtherIdentifier(identifier);
        return patients;
    }

   /* @ResponseBody
    @RequestMapping(value = "fingerprint/UpdatePatientWithFingerprint.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public boolean UpdatePatientWithFingerprint(@RequestBody String patientWithFingerprint) throws Exception {

        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        return service.updatePatient(patientWithFingerprint);
    }*/

    @ResponseBody
    @RequestMapping(value = "fingerprint/findPatients.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> findPatientsByNameOrIdentifier(@RequestBody String searchInput)
    {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        List<PatientFingerPrintModel> patients = service.findPatients(searchInput);
        return patients;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/addFingerprint.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> addFingerprint(@RequestBody String patientWithFingerprint) throws Exception
    {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        PatientFingerPrintModel patient = service.addFingerprintToPatient(patientWithFingerprint);
        List<PatientFingerPrintModel> patientFingerPrintModels = new ArrayList<PatientFingerPrintModel>();
        patientFingerPrintModels.add(patient);
        return patientFingerPrintModels;
    }

    @RequestMapping(value = "editPatient.form")
    public void editPatient(HttpServletRequest request, Model model)
    {
        model.addAttribute("patientUuid",request.getParameter("patientUuid"));
    }
}
