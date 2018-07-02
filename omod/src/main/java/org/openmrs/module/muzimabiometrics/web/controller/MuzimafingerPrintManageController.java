/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.muzimabiometrics.web.controller;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.muzimabiometrics.api.MuzimaFingerprintService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
@Controller
public class MuzimafingerPrintManageController {
	@RequestMapping(value = "module/muzimabiometrics/managefingerprint.form",  method = RequestMethod.GET)
	public synchronized void findPatient(HttpServletRequest request, Model model)throws IOException, ParseException
	{
		if(request != null) {
			if(request.getParameter("patientUuid") != null) {
				Patient patient = Context.getPatientService().getPatientByUuid(request.getParameter("patientUuid"));
				if(patient != null) {
					model.addAttribute("identifier", patient.getPatientIdentifier(1));
				}
			}
			if(request.getParameter("clearFinger") != null){
				model.addAttribute("fingerprintIsSet",null);
			}
		}
	}
}
