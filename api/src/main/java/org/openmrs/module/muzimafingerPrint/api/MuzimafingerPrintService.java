package org.openmrs.module.muzimafingerPrint.api;

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.muzimafingerPrint.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.PatientModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public interface MuzimafingerPrintService extends OpenmrsService {


    List<PatientFingerPrintModel> getAllPatientsWithFingerPrint();
    PatientModel getPatientByUUID(String uuid);

//    void enroll();
//
//    Patient identify(String template) throws IOException;
//
//    void initializeBiometric() throws IOException;
}
