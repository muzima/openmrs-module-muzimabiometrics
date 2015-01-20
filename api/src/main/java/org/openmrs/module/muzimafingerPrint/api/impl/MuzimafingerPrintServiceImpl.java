package org.openmrs.module.muzimafingerPrint.api.impl;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.muzimafingerPrint.PatientFingerPrintModel;
import org.openmrs.module.muzimafingerPrint.api.MuzimafingerPrintService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by vikas on 15/10/14.
 */
@Service
public class MuzimafingerPrintServiceImpl extends BaseOpenmrsService implements MuzimafingerPrintService {

    private PatientDAO patientDAO;

    public MuzimafingerPrintServiceImpl(PatientDAO dao) {
        this.patientDAO = dao;
    }

    public MuzimafingerPrintServiceImpl(){

    }
    @Override
    public List<PatientFingerPrintModel> getAllPatientsWithFingerPrint() {
        List<PatientFingerPrintModel> patients = new ArrayList<PatientFingerPrintModel>();
        List<Patient> allPatients = patientDAO.getAllPatients(false);
        for (Patient patient : allPatients) {
            String fingerPrint = extractFingerPrintForPatient(patient);
            if(fingerPrint != null)
                patients.add(new PatientFingerPrintModel(patient, fingerPrint));
        }
        return patients;
    }

    private String extractFingerPrintForPatient(Patient patient) {
        PersonAttribute attribute = patient.getAttribute("fingerprint");
        if (attribute == null)
            return null;
        String value = attribute.getValue();
        return value;
    }
    private Patient getPatientFromUUID(String uuid){
        return patientDAO.getPatientByUuid(uuid);
    }

}