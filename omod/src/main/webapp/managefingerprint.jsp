<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/font-awesome/css/font-awesome.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery-1.10.1.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery-ui-1.10.4.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery.validate.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/underscore/underscore-min.js"/>
<div>
    <div class="navbar navbar-custom">
        <div>
            <a href="#"><i class="icon-home"></i> Muzima Fingerprint Module</a>
        </div>
    </div>
    <div>
        <div id="find-patient">
            <div class = "row forms-list">
                <div class ="col-lg-12" >
                    <table  id="find-options">
                        <thead>
                        <tr class = "forms-header">
                            <th>Find Patient(s)</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Patient Identifier or Patient Name:<input type="text" id="findPatients" name="findPatients" value="${identifier}" onkeypress="activate(this.value, event)"></td>
                            <td><div>
                            <a href="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/fingerprint.jnlp">Scan Fingerprint</a>
                            </div>
                            </td>
                            <input type="hidden" id="fingerprintScan" />
                            <td><button type="button" id="refresh">Refresh</button></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="patientCreated">
        <h5>Patient Created</h5>
    </div>
    <div id="body-wrapper">
        <div class = "row forms-list">
            <div class ="col-lg-12" >
                <table  id="tblData">
                    <thead >
                    <tr class = "forms-header">
                        <th>Patient List</th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <thead >
                    <tr class = "forms-list-header">
                        <th>Id</th>
                        <th>First Name</th>
                        <th>Family Name</th>
                        <th>Gender</th>
                        <th>Fingerprint</th>
                        <th>Dispense Drugs</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div id="searchResults">
        <h5>No Patient found</h5>
    </div>
    <div id = "registrationSection">
        <div id = "updatePatient" class = "button-loc">
            <input id = "btnUpdatePatient" type="button" value= "Update Selected patient">
            <input id = "btnCancel" class = "doCancel" type="button" value= "Cancel">
        </div>
        <div id = "otherIdentificationOption">
            <h4>No Patient found with this fingerprint, Do you want to register?</h4>
            <a href="#" id="btnYes">Yes</a>
            <a href="#" id="btnNo">No</a>
        </div>
        <div id="enrollFingerprint">
            <a href="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/enroll-fingerprint.jnlp">Enroll Two More Fingerprints</a>
        <button type="button" id="enrollFingers">Process Fingers</button>
        </div>
        <div id = "registrationForm">
            <form id = "formData" method = "post" action = "">
                <h3 id="form-title">Registration Form</h3>
                <fieldset  name="patient">
                    <div class="form-group">
                        <label for= "given_name">First Name</label>
                        <input autocomplete="off" type="text" name="given_name">

                        <label for= "middle_name">Middle Name</label>
                        <input autocomplete="off" type="text" name="middle_name">

                        <label for= "family_name">Family Name</label>
                        <input autocomplete="off" type="text" name="family_name">
                        <br/>
                        <label class="radio-inline">
                            <input type="radio" name="sex" value = "F"><b>Female</b>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="sex" value = "M"><b>Male</b>
                        </label>
                        <br/><br/>
                        <label for= "phone_number">Phone Number</label>
                        <input autocomplete="off" type="text" name="phone_number">

                        <label for= "mothers_name">Mothers Name</label>
                        <input autocomplete="off" type="text" name="mothers_name">

                        <label for= "amrs_id">AMRS Universal ID Assigned</label>
                        <input autocomplete="off" type="text" name="amrs_id">

                        <label for= "fingerprint">Finger Print</label>
                        <img src ="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/images/done.png"/>
                        <label for= "birth_date">Date of Birth</label>
                        <input autocomplete="off" type="date" name="birth_date">

                        <label for= "location_id">Encounter Location</label>
                        <select id = "LocationOptions" name="location_id">
                            <option  value="">...</option>
                        </select>

                    </div>
                </fieldset>
            </form>
            <br/>
            <div class = "button-loc">
                <input id = "btnCreatePatient" type="button" value= "create patient">
            </div>
        </div>
    </div>
</div>
<script>

    var openmrsContextPath = '${pageContext.request.contextPath}';

</script>

<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/custom/Custom.js"/>
