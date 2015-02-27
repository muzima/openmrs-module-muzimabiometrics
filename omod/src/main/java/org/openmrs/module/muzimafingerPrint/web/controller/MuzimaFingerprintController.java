package org.openmrs.module.muzimafingerPrint.web.controller;

import org.directwebremoting.guice.RequestParameters;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;
import org.openmrs.module.muzimafingerPrint.api.MuzimafingerPrintService;
import org.openmrs.module.muzimafingerPrint.model.PatientFingerPrintModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikas on 15/01/15.
 */
@Controller
@RequestMapping(value = "module/muzimafingerPrint")
public class MuzimaFingerprintController {

    @ResponseBody
    @RequestMapping(value = "patient/{fingerprint}/fingerprint.form",  method = RequestMethod.GET)
    public MuzimaFingerprint getPatientFingerprintById(@PathVariable String patientUUID){

        MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
        return service.getFingerprintByPatientUUID(patientUUID);
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/register.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> registerPatient(@RequestBody String jsonPayload) throws Exception {
            MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
            PatientFingerPrintModel patients = service.savePatient(jsonPayload);
            List<PatientFingerPrintModel> patientFingerPrintModels = new ArrayList<PatientFingerPrintModel>();
            patientFingerPrintModels.add(patients);
            return patientFingerPrintModels;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/identifyPatient.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public PatientFingerPrintModel identifyPatient(@RequestBody String fingerprint) throws Exception {

        MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
        PatientFingerPrintModel patient = service.identifyPatient(fingerprint);
        return patient;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/identifyPatientByOtherIdentifier.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public List<PatientFingerPrintModel> identifyPatientByOtherIdentifier(@RequestBody String identifier) throws Exception {

        MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
        List<PatientFingerPrintModel> patients = service.identifyPatientByOtherIdentifier(identifier);
        return patients;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/UpdatePatientWithFingerprint.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public boolean UpdatePatientWithFingerprint(@RequestBody String patientWithFingerprint) throws Exception {

        MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
        return service.updatePatient(patientWithFingerprint);
    }

}
