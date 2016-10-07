package org.openmrs.module.muzimabiometrics.api.handler;

import net.minidev.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Handler;

import org.openmrs.api.context.Context;
import org.openmrs.module.muzima.exception.QueueProcessorException;
import org.openmrs.module.muzima.model.QueueData;
import org.openmrs.module.muzima.model.handler.QueueDataHandler;
import org.openmrs.module.muzima.web.resource.utils.JsonUtils;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;


@Handler(supports = QueueData.class, order = 100)
public class JsonFingerprintAddQueueDataHandler  implements QueueDataHandler {
    private static final String DISCRIMINATOR_VALUE = "json-fingerprint-add";
    private final Log log = LogFactory.getLog(JsonFingerprintAddQueueDataHandler.class);
    private QueueProcessorException queueProcessorException;
    private String payload;

    @Override
    public String getDiscriminator() {
        return DISCRIMINATOR_VALUE;
    }

    @Override
    public void process(final QueueData queueData) throws QueueProcessorException {
        log.info("Processing added fingerprint data: " + queueData.getUuid());
        queueProcessorException = new QueueProcessorException();
        try {
            if (validate(queueData)) {
                extractAndSaveFingerprintData();
            }
        } catch (Exception e) {
            if (!e.getClass().equals(QueueProcessorException.class)) {
                queueProcessorException.addException(e);
            }
        } finally {
            if (queueProcessorException.anyExceptions()) {
                throw queueProcessorException;
            }
        }
    }

    @Override
    public boolean validate(QueueData queueData) {
        log.info("Processing added fingerprint data: " + queueData.getUuid());
        queueProcessorException = new QueueProcessorException();
        try {
            payload = queueData.getPayload();

            return true;
        } catch (Exception e) {
            queueProcessorException.addException(e);
            return false;
        } finally {
            if (queueProcessorException.anyExceptions()) {
                throw queueProcessorException;
            }
        }
    }

    private void extractAndSaveFingerprintData(){

        String fingerprint = "";
        String patientUUID = JsonUtils.readAsString(payload, "$['patient']['patient.uuid']");
        Object personAttributeNameObject = JsonUtils.readAsObject(payload, "$['patient']['patient.person_attribute_name']");
        Object personAttributeValueObject = JsonUtils.readAsObject(payload, "$['patient']['patient.person_attribute_value']");

        if(personAttributeNameObject  instanceof JSONArray){
            JSONArray personAttributeName = (JSONArray)personAttributeNameObject;
            JSONArray personAttributeValue = (JSONArray)personAttributeValueObject;
            for (int i = 0; i < personAttributeName.size(); i++){
                if(personAttributeName.get(i).toString().equals("fingerprint")){
                    fingerprint = personAttributeValue.get(i).toString();
                }
            }
        }
        if(!StringUtils.isEmpty(patientUUID) && !StringUtils.isEmpty(fingerprint)){
            MuzimaFingerprint delegate = new MuzimaFingerprint(patientUUID,fingerprint);
            MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
            service.addFingerprintToPatient(delegate);
        } else {
            queueProcessorException.addException(new Exception("Unable to parse and save fingerprint data: "));
        }

    }

    @Override
    public boolean accept(final QueueData queueData) {
        return StringUtils.equals(DISCRIMINATOR_VALUE, queueData.getDiscriminator());
    }
}
