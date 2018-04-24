package org.openmrs.module.muzimabiometrics.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.MuzimaConstants;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.List;
@Resource(name = RestConstants.VERSION_1 + "/" + MuzimaConstants.MODULE_ID + "/fingerprint",
        supportedClass = MuzimaFingerprint.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*","1.10.*","1.11.*"})
@Handler(supports = MuzimaFingerprint.class)
public class MuzimaFingerprintResource extends DataDelegatingCrudResource<MuzimaFingerprint> {

    private static final Log log = LogFactory.getLog(MuzimaFingerprintResource.class);

    @Override
    protected NeedsPaging<MuzimaFingerprint> doGetAll(RequestContext context) throws ResponseException {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        List<MuzimaFingerprint> all = service.getAll();
        return new NeedsPaging<MuzimaFingerprint>(all, context);
    }

    @Override
    public MuzimaFingerprint getByUniqueId(String uuid) {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        return service.findByUniqueId(uuid);
    }

    @Override
    public Object retrieve(String patientUUID, RequestContext context) throws ResponseException {
        MuzimaFingerprintService service = Context.getService(MuzimaFingerprintService.class);
        MuzimaFingerprint fingerprint = service.getFingerprintByPatientUUID(patientUUID);
        if (fingerprint == null) {
            return null;
        }
        return asRepresentation(fingerprint, context.getRepresentation());
    }

    @Override
    protected void delete(MuzimaFingerprint media, String s, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    public MuzimaFingerprint newDelegate() {
        return new MuzimaFingerprint();
    }


    public MuzimaFingerprint save(MuzimaFingerprint media) {
        return null;

    }

    @Override
    public void purge(MuzimaFingerprint media, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        DelegatingResourceDescription description = null;

        if (rep instanceof DefaultRepresentation || rep instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("id");
            description.addProperty("patientUUID");
            description.addProperty("fingerprint");
            description.addSelfLink();
        }
        return description;
    }
}

