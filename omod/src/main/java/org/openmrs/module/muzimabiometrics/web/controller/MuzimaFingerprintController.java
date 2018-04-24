package org.openmrs.module.muzimabiometrics.web.controller;

import jlibfprint.JlibFprint;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.openmrs.module.muzimabiometrics.model.PatientFingerPrintModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Controller
@RequestMapping(value = "module/muzimabiometrics")
public class MuzimaFingerprintController {
    private static final Log log= LogFactory.getLog(MuzimaFingerprint.class);
    private MuzimaFingerprintService service;
    private boolean fingerprintIsSet,firstImageIsSet,secondImageIsSet,thirdImageIsSet;
    private String fingerForIdentification,firstImageBytes,secondImageBytes,thirdImageBytes;
    private String patientUUID="";
    private int bozorth_threshold=40;
    private String scannerException="";
    @ResponseBody
    @RequestMapping(value = "patient/{fingerprint}/fingerprint.form",  method = RequestMethod.GET)
    public MuzimaFingerprint getPatientFingerprintById(@PathVariable String patientUUID){
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        return service.getFingerprintByPatientUUID(patientUUID);
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/enrollPatient.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public synchronized JSONArray enrollPatient(@RequestBody String jsonPayload) throws Exception {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        Patient patient = service.savePatient(jsonPayload,firstImageBytes,secondImageBytes,thirdImageBytes);
        PatientFingerPrintModel patientFingerPrintModel=new PatientFingerPrintModel(patient.getUuid(),firstImageBytes, patient.getId(), patient.getGivenName(), patient.getFamilyName(), patient.getGender(),patient.getIdentifiers().toString());
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(patientFingerPrintModel);
        return jsonArray;
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
                jsonObject.put("identifiers",patient.getIdentifiers().toString());
                jsonArray.add(jsonObject);
            }
        }
        scannerException="";
        response.getWriter().print(jsonArray);
    }
    @ResponseBody
    @RequestMapping(value = "setFingerprint.form",  method = RequestMethod.POST)
    public synchronized void setFingerprint(@RequestBody String request,Model model){
        service=Context.getService(MuzimaFingerprintService.class);
        fingerForIdentification=request;
        fingerprintIsSet=true;
        if(request!= null) {
            try {
                matchPatients(fingerForIdentification);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    public int processFingerprintMatching(String fingerToVerify,String storedFingerprint) throws IOException, ClassNotFoundException {
        String patientFingerByte,scanByteToVerify;
        return 1;
    }
    public void matchPatients(String fingerToMatch) throws IOException, ClassNotFoundException {
        int firstFingerMatchValue=0;
        int secondFingerMatchValue=0;
        int thirdFingerMatchValue=0;
        patientUUID="";
        List<MuzimaFingerprint> muzimaFingerprintList=service.getAll();
        for(MuzimaFingerprint muzimaFingerprint1:muzimaFingerprintList){
            if(muzimaFingerprint1.getFirstFingerprint() !=null) {
                firstFingerMatchValue=processFingerprintMatching(fingerToMatch,muzimaFingerprint1.getFirstFingerprint());
            }
            if(muzimaFingerprint1.getSecondFingerprint() !=null) {
                secondFingerMatchValue = processFingerprintMatching(fingerToMatch,muzimaFingerprint1.getSecondFingerprint());
            }
            if(muzimaFingerprint1.getThirdFingerprint()!=null) {
                thirdFingerMatchValue = processFingerprintMatching(fingerToMatch,muzimaFingerprint1.getThirdFingerprint());
            }
            if(firstFingerMatchValue > bozorth_threshold || secondFingerMatchValue > bozorth_threshold || thirdFingerMatchValue > bozorth_threshold){
                patientUUID=muzimaFingerprint1.getPatientUUID();
                break;
            }
        }
    }
    @ResponseBody
    @RequestMapping(value="enrollFirstImage.form",method=RequestMethod.POST)
    public synchronized void enrollFirstImage(@RequestBody String request){
        firstImageBytes=request;
        firstImageIsSet=true;
        try {
            matchPatients(firstImageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value="enrollSecondImage.form",method=RequestMethod.POST)
    public synchronized void enrollSecondImage(@RequestBody String request){
        secondImageBytes=request;
        secondImageIsSet=true;
    }
    @ResponseBody
    @RequestMapping(value="enrollThirdImage.form",method=RequestMethod.POST)
    public synchronized void enrollThirdImage(@RequestBody String request){
        thirdImageBytes=request;
        thirdImageIsSet=true;
    }
    @ResponseBody
    @RequestMapping(value = "getFingerprint.form",  method = RequestMethod.GET)
    public JSONObject getFingerprint(HttpServletRequest request,HttpServletResponse response,Model model)throws IOException, ParseException
    {
        if(request !=null) {
            if (request.getParameter("fingerprintIsSet") != null) {
                if (request.getParameter("fingerprintIsSet").equalsIgnoreCase("clear")) {
                    fingerprintIsSet = false;
                    firstImageIsSet=false;
                    secondImageIsSet=false;
                    thirdImageIsSet=false;
                    patientUUID="";
                    scannerException="";
                }
            }
        }
        JSONObject resp=new JSONObject();
        resp.put("fingerprintIsSet",fingerprintIsSet);
        resp.put("exception",scannerException);
        resp.put("patientUUID",patientUUID);
        return resp;
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
        log.error("Function Call Gets Here");
        List<PatientFingerPrintModel> patient = service.identifyPatient(fingerprint);
        return patient;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/identifyPatientByOtherIdentifier.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(@RequestBody String identifier) throws Exception {

        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        List<PatientFingerPrintModel> patients = service.identifyPatientByOtherIdentifier(identifier);
        return patients;
    }

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
        List<PatientFingerPrintModel> patientFingerPrintModels = new ArrayList<PatientFingerPrintModel>();
        if(muzimaFingerprint==null) {
            PatientFingerPrintModel patient = service.addFingerprintToPatient(patientWithFingerprint, firstImageBytes, secondImageBytes, thirdImageBytes);
            patientFingerPrintModels.add(patient);
        }
        return patientFingerPrintModels;
    }

    @RequestMapping(value = "patientEdit.form",method=RequestMethod.GET)
    public void editPatient(HttpServletRequest request, Model model)
    {
        model.addAttribute("patientUuid",request.getParameter("patientUuid"));
    }
    @RequestMapping(value="scannerException.form", method =RequestMethod.POST)
    public void setException(@RequestBody String request){
        scannerException=request;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/getPatient.form", method = RequestMethod.POST, headers = {"content-type=application/json","Accept=application/json"})
    public List<PatientFingerPrintModel> getPatientByPatientUuid(@RequestBody String patientUuid) throws Exception {

        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        List<PatientFingerPrintModel> patient = service.getPatientByPatientUuid(patientUuid);

        return patient;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/appendFingerPrint.form", method = RequestMethod.POST, headers = {"content-type=application/json","Accept=application/json"})
    public PatientFingerPrintModel appendFingerToExistingPatient(@RequestBody String patientUuid, String fingerPrint) throws Exception {
        org.json.JSONArray jsonArray = new org.json.JSONArray("["+patientUuid+"]");
        for(int i = 0; i < jsonArray.length(); i++){
            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("patient");
            patientUuid = jsonObject.getString("patientUuid");
            fingerPrint = jsonObject.getString("fingerPrint");
        }
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        PatientFingerPrintModel patient = service.addFingerprintToPatient(patientUuid,fingerPrint,fingerPrint,fingerPrint);
        return patient;
    }
}
