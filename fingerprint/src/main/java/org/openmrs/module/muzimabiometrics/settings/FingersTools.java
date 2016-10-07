package org.openmrs.module.muzimabiometrics.settings;

import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.licensing.NLicense;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class FingersTools {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static FingersTools instance;

	private static final String ADDRESS = "/local";
	private static final String PORT = "5000";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static FingersTools getInstance() {
		synchronized (FingersTools.class) {
			if (instance == null) {
				instance = new FingersTools();
			}
			return instance;
		}
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private final Map<String, Boolean> licenses;
	private final NBiometricClient client;
	private final NBiometricClient defaultClient;

	// ===========================================================
	// Private constructor
	// ===========================================================

	private FingersTools() {
		licenses = new HashMap<String, Boolean>();
		client = new NBiometricClient();
        client.setMatchingThreshold(48);
        client.setFingersMatchingSpeed(NMatchingSpeed.LOW);
		defaultClient = new NBiometricClient();
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private boolean isLicenseObtained(String license) {
		if (license == null) throw new NullPointerException("license");
		if (licenses.containsKey(license)) {
			return licenses.get(license);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public boolean obtainLicenses(List<String> names) throws IOException {
		if (names == null) {
			return true;
		}
		boolean result = true;
		for (String license : names) {
			if (isLicenseObtained(license)) {
				System.out.println(license + ": " + " already obtained");
			} else {
				boolean state = NLicense.obtainComponents(ADDRESS, PORT, license);
				licenses.put(license, state);
				if (state) {
					System.out.println(license + ": obtainted");
				} else {
					result = false;
					System.out.println(license + ": not obtained");
				}
			}
		}
		return result;
	}

	public void releaseLicenses() {
		Set<Entry<String, Boolean>> entries = licenses.entrySet();
		if (entries.isEmpty()) {
			System.out.print("Releasing licenses... Nothing to release.\n");
		} else {
			StringBuilder sb = new StringBuilder(256);
			for (Entry<String, Boolean> entry : entries) {
				if (entry.getValue()) {
					sb.append(entry.getKey()).append(',');
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			try {
				System.out.print("Releasing licenses... ");
				NLicense.releaseComponents(sb.toString());
				System.out.print("done.\n");
				licenses.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Boolean> getLicenses() {
		return licenses;
	}

	public NBiometricClient getClient() {
		return client;
	}

	public NBiometricClient getDefaultClient() {
		return defaultClient;
	}

}
