package org.openmrs.module.muzimafingerPrint.web.controller;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimafingerPrint.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.api.MuzimafingerPrintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by vikas on 15/01/15.
 */
@Controller
@RequestMapping(value = "module/muzimafingerPrint")
public class MuzimaFingerprintController {

    @ResponseBody
    @RequestMapping(value = "fingerprint/identify.form", method = RequestMethod.POST)
    public List<PatientFingerPrintModel> GetAllPatient() throws Exception {

           MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
           List<PatientFingerPrintModel> patients = service.getAllPatientsWithFingerPrint();
           return patients;
    }

    @ResponseBody
    @RequestMapping(value = "fingerprint/register.form", method = RequestMethod.POST, headers = {"content-type=application/json"})
    public PatientFingerPrintModel registerPatient(@RequestBody String jsonPayload) throws Exception {
            MuzimafingerPrintService service = Context.getService(MuzimafingerPrintService.class);
            PatientFingerPrintModel patients = service.savePatient(jsonPayload);
            return patients;
    }
}
