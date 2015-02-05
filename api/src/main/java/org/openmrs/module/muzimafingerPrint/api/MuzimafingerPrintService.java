package org.openmrs.module.muzimafingerPrint.api;

import org.json.JSONException;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.muzimafingerPrint.PatientFingerPrintModel;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimafingerPrintService extends OpenmrsService {

    List<PatientFingerPrintModel> getAllPatientsWithFingerPrint();
    @Transactional
    PatientFingerPrintModel savePatient(String patientData) throws JSONException, ParseException;

}
