package org.openmrs.module.muzimabiometrics;

import org.openmrs.module.webservices.rest.web.RestConstants;

public class MuzimaConstants {
        public static final String MODULE_ID = "muzimabiometrics";
        public static final String MUZIMA_NAMESPACE = RestConstants.VERSION_1 + "/" + MuzimaConstants.MODULE_ID;
        public static final String BASE_REQUEST_MAPPING = "/rest/" + MUZIMA_NAMESPACE;
        public static final String MINIMUM_FINGERPRINT_QUALITY_PROPERTY_NAME = "mUzimabiometrics.defaultMinimumFingerprintQuality";
        public static final String DEFAULT_FINGER_PROPERTY_NAME = "mUzimabiometrics.defaultFinger";

}
