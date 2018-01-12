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
    private boolean fingerprintIsSet,firstImageIsSet,secondImageIsSet,thirdImageIsSet;
    private byte[] fingerForIdentification,firstImageBytes,secondImageBytes,thirdImageBytes;
    private String patientUUID;
    private int bozorth_threshold=40;
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
        Patient patient = service.savePatient(jsonPayload,firstImageBytes,secondImageBytes,thirdImageBytes);
        return new PatientFingerPrintModel(patient.getUuid(),firstImageBytes, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender());
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
        byte[] patientFirstFingerByte,patientSecondFingerByte,patientThirdFingerByte;
        JlibFprint.fp_print_data newJlibFprintData,firstFingerData,secondFingerData,thirdFingerData;
        ByteArrayInputStream newScanByteArray,patientFirstFingerByteArray,patientSecondFingerByteArray,patientThirdFingerByteArray;
        ObjectInput newScanInput,patientFirstFingerInput,patientSecondFingerInput,patientThirdFingerInput;
        fingerForIdentification=request;
        byte[] b=Base64.decodeBase64(request);
        ByteArrayInputStream is=new ByteArrayInputStream(b);
        fingerprintIsSet=true;
        int firstFingerMatchValue=0;
        int secondFingerMatchValue=0;
        int thirdFingerMatchValue=0;
        if(request!= null) {
            newScanByte=Base64.decodeBase64(fingerForIdentification);
            newScanByteArray=new ByteArrayInputStream(newScanByte);
            newScanInput=new ObjectInputStream(newScanByteArray);
            newJlibFprintData=(JlibFprint.fp_print_data)newScanInput.readObject();
            List<MuzimaFingerprint> muzimaFingerprintList=service.getAll();
            for(MuzimaFingerprint muzimaFingerprint1:muzimaFingerprintList){
                patientFirstFingerByte=Base64.decodeBase64(muzimaFingerprint1.getFirstFingerprint());
                patientFirstFingerByteArray=new ByteArrayInputStream(patientFirstFingerByte);
                patientFirstFingerInput=new ObjectInputStream(patientFirstFingerByteArray);
                firstFingerData=(JlibFprint.fp_print_data)patientFirstFingerInput.readObject();
                firstFingerMatchValue= JlibFprint.img_compare_print_data(newJlibFprintData,firstFingerData);
                System.out.println("first finger matchValue+++++++++++++++++++++++++++"+firstFingerMatchValue);
                patientSecondFingerByte=Base64.decodeBase64(muzimaFingerprint1.getSecondFingerprint());
                patientSecondFingerByteArray=new ByteArrayInputStream(patientSecondFingerByte);
                patientSecondFingerInput=new ObjectInputStream(patientSecondFingerByteArray);
                secondFingerData=(JlibFprint.fp_print_data)patientSecondFingerInput.readObject();
                secondFingerMatchValue=JlibFprint.img_compare_print_data(newJlibFprintData,secondFingerData);
                System.out.println("second finger match value +++++++++++++++++++++"+secondFingerMatchValue);
                patientThirdFingerByte=Base64.decodeBase64(muzimaFingerprint1.getThirdFingerprint());
                patientThirdFingerByteArray=new ByteArrayInputStream(patientThirdFingerByte);
                patientThirdFingerInput=new ObjectInputStream(patientThirdFingerByteArray);
                thirdFingerData=(JlibFprint.fp_print_data)patientThirdFingerInput.readObject();
                thirdFingerMatchValue=JlibFprint.img_compare_print_data(newJlibFprintData,thirdFingerData);
                System.out.println("third finger match value +++++++++++++++++++++++++++++"+thirdFingerMatchValue);
                if(firstFingerMatchValue > bozorth_threshold && secondFingerMatchValue > bozorth_threshold && thirdFingerMatchValue > bozorth_threshold){
                    patientUUID=muzimaFingerprint1.getPatientUUID();
                    System.out.println("[OK] The two fingerprints are compatible!");
                    break;
                }
            }
        }

    }
    @ResponseBody
    @RequestMapping(value="enrollFirstImage.form",method=RequestMethod.POST)
    public synchronized void enrollFirstImage(@RequestBody byte[] request){
        firstImageBytes=request;
        firstImageIsSet=true;
        System.out.println("first image enrolled ++++++++++++++++++++++++++++++++"+firstImageBytes);
    }
    @ResponseBody
    @RequestMapping(value="enrollSecondImage.form",method=RequestMethod.POST)
    public synchronized void enrollSecondImage(@RequestBody byte[] request){
        secondImageBytes=request;
        secondImageIsSet=true;
        System.out.println("second image enrolled ++++++++++++++++++++++++++++++++++++"+secondImageBytes);
    }
    @ResponseBody
    @RequestMapping(value="enrollThirdImage.form",method=RequestMethod.POST)
    public synchronized void enrollThirdImage(@RequestBody byte[] request){
        thirdImageBytes=request;
        thirdImageIsSet=true;
        System.out.println("third image enrolled ++++++++++++++++++++++++++++++++++++"+thirdImageBytes);
    }
    @RequestMapping(value = "getFingerprint.form",  method = RequestMethod.GET)
    public synchronized void getFingerprint(HttpServletRequest request,HttpServletResponse response,Model model)throws IOException, ParseException
    {
        if(request !=null) {
            if (request.getParameter("fingerprintIsSet") != null) {
                if (request.getParameter("fingerprintIsSet").equalsIgnoreCase("clear")) {
                    fingerprintIsSet = false;
                    firstImageIsSet=false;
                    secondImageIsSet=false;
                    thirdImageIsSet=false;
                    patientUUID="";
                }
            }
        }
        System.out.println("fingerprintis set ++++++++++++++++++++++++++++++"+fingerprintIsSet);
        response.getWriter().print(fingerprintIsSet);
    }
    @RequestMapping(value = "fetchEnrolledFingers.form",  method = RequestMethod.GET)
    public synchronized void fetchEnrolledFingers(HttpServletRequest request,HttpServletResponse response,Model model)throws IOException, ParseException
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("firstImageIsSet",firstImageIsSet);
        jsonObject.put("secondImageIsSet",secondImageIsSet);
        jsonObject.put("thirdImageIsSet",thirdImageIsSet);
        response.getWriter().print(jsonObject);
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
        MuzimaFingerprint muzimaFingerprint=service.getFingerprintByPatientUUID(patientWithFingerprint);
        Boolean fingerprintAlreadyExist=false;
        if(muzimaFingerprint !=null){
            fingerprintAlreadyExist=true;
        }
        PatientFingerPrintModel patient = service.addFingerprintToPatient(patientWithFingerprint,firstImageBytes,secondImageBytes,thirdImageBytes,fingerprintAlreadyExist);
        List<PatientFingerPrintModel> patientFingerPrintModels = new ArrayList<PatientFingerPrintModel>();
        patientFingerPrintModels.add(patient);
        return patientFingerPrintModels;
    }

    @RequestMapping(value = "patientEdit.form",method=RequestMethod.GET)
    public void editPatient(HttpServletRequest request, Model model)
    {
        model.addAttribute("patientUuid",request.getParameter("patientUuid"));
    }
}
