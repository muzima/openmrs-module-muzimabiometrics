package org.openmrs.module.muzimabiometrics.extension.html;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;
import org.openmrs.module.web.extension.LinkExt;

/**
 * This class defines the links that will appear on the administration page
 *
 * This extension is enabled by defining (uncommenting) it in the
 * /metadata/config.xml file.
 */
public class MuzimaBiometricsHeader extends LinkExt{
    /**
     * @see org.openmrs.module.web.extension.AdministrationSectionExt#getMediaType()
     */
    public Extension.MEDIA_TYPE getMediaType() {
        return Extension.MEDIA_TYPE.html;
    }

    /**
     * @see AdministrationSectionExt#getRequiredPrivilege()
     */
    @Override
    public String getRequiredPrivilege() {
        return "View mUzima FingerPrint";
    }

    /**
     * @see org.openmrs.module.web.extension.LinkExt#getLabel()
     */
    @Override
    public String getLabel() {
        return "Fingerprint";
    }

    /**
     * @see org.openmrs.module.web.extension.LinkExt#getUrl()
     */
    @Override
    public String getUrl() {
        return "/module/muzimabiometrics/managefingerprint.form";
    }
}
