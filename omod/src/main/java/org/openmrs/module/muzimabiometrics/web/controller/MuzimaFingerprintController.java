package org.openmrs.module.muzimabiometrics.web.controller;

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
    JlibFprint.fp_print_data newJlibFprintData,savedJlibfprintData;
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
    public synchronized void postFingerprintScan(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, ParseException, ClassNotFoundException {
        service=Context.getService(MuzimaFingerprintService.class);
        int matchValue=40;
        ByteArrayInputStream newFingerprintScan,savedFingerprintScan;
        byte newScanByte[],savedScanByte[];
        ByteArrayInputStream newScanByteArray,savedScanByteArray;
        ObjectInputStream newScanInputStream,savedScanInputStream;

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("patientUuid","");
        if(request != null) {
            if(request.getParameter("fingerprintIsSet") != null) {
                String requestInstance=request.getParameter("fingerprintIsSet");
                newScanByte=Base64.decodeBase64(fingerprintPayload);
                newScanByteArray=new ByteArrayInputStream(newScanByte);
                newScanInputStream=new ObjectInputStream(newScanByteArray);
                newJlibFprintData=(JlibFprint.fp_print_data)newScanInputStream.readObject();
            List<MuzimaFingerprint> muzimaFingerprintList=service.getAll();
            for(MuzimaFingerprint muzimaFingerprint1:muzimaFingerprintList){
                savedScanByte=Base64.decodeBase64(muzimaFingerprint1.getFingerprint());
                System.out.println("savedScanByte+++++++++++++++++++++"+savedScanByte);
                savedScanByteArray=new ByteArrayInputStream(savedScanByte);
                savedScanInputStream=new ObjectInputStream(savedScanByteArray);
                savedJlibfprintData=(JlibFprint.fp_print_data)savedScanInputStream.readObject();
                matchValue = JlibFprint.img_compare_print_data(newJlibFprintData,savedJlibfprintData);
                System.out.println("JlibFprint.BOZORTH_THRESHOLD+++++++++++++++++++++++++++"+JlibFprint.BOZORTH_THRESHOLD);
                if (matchValue > JlibFprint.BOZORTH_THRESHOLD)
                {
                    jsonObject.put("patientUuid",muzimaFingerprint1.getPatientUUID());
                    System.out.println("[OK] The two fingerprints are compatible!");
                    break;
                }
            }
            }
        }
        response.getWriter().print(jsonObject);
    }
    @ResponseBody
    @RequestMapping(value = "setFingerprint.form",  method = RequestMethod.POST)
    public synchronized void setFingerprint(@RequestBody byte[] request,Model model)throws IOException, ParseException
    {
        //byte b[] =Base64.decodeBase64()
        //ObjectInputStream os=new ObjectInputStream(request);
        fingerprintPayload=request;
        byte b[]=Base64.decodeBase64(request);
        ByteArrayInputStream is=new ByteArrayInputStream(b);
        fingerprint=new ObjectInputStream(is);
        fingerprintIsSet=true;
        model.addAttribute("fingerprintIsSet",fingerprintIsSet);
    }
    @RequestMapping(value = "getFingerprint.form",  method = RequestMethod.GET)
    public synchronized void getFingerprint(HttpServletRequest request,HttpServletResponse response,Model model)throws IOException, ParseException
    {
        model.addAttribute("fingerprintIsSet",fingerprintIsSet);
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
