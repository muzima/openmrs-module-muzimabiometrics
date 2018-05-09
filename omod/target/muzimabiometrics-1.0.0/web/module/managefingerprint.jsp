<!DOCTYPE html>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/styles/font-awesome/css/font-awesome.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery-1.10.1.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery-ui-1.10.4.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/jquery/jquery.validate.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/underscore/underscore-min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/bootstrap/js/bootstrap.min.js"/>
<style>
    .loader {
        border: 5px solid #f3f3f3;
        -webkit-animation: spin 1s linear infinite;
        animation: spin 1s linear infinite;
        border-top: 5px solid #555;
        border-radius: 50%;
        width: 40px;
        height: 40px;
    }

    @-webkit-keyframes spin {
        0% {
            -webkit-transform: rotate(0deg);
        }
        100% {
            -webkit-transform: rotate(360deg);
        }
    }

    @keyframes spin {
        0% {
            transform: rotate(0deg);
        }
        100% {
            transform: rotate(360deg);
        }
    }

    @keyframes skew {
        0% {
            transform: skewX(20deg);
        }
        100% {
            transform: skewX(-20deg);
        }
    }
</style>
<!--start of trial -->
<br/><br/><br/>
<nav class="navbar navbar-default" style="background-color:#009D8E !important;">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#codebrainery-toggle-nav" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="color:white !important;font-size:2em !important    ;">mUzima Biometrics
                Module</a>
        </div>

        <div class="collapse navbar-collapse" id="codebrainery-toggle-nav">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <button id="activatehomecontainer" type="button" class="btn btn-success btn-lg"
                            style="background-color:#4ebaaa !important;"
                            title="This is the mUzima Fingerprint module welcome screen.">Welcome
                    </button>&nbsp;&nbsp;&nbsp;&nbsp;
                </li>
                <li>
                    <button id="activatemaincontainersignin" type="button" class="btn btn-success btn-lg"
                            style="background-color:#4ebaaa !important;"
                            title="This screen allows users who have appended their fingerprints to their details to login simply by scanning the fingerprint">
                        Identification
                    </button>&nbsp;&nbsp;&nbsp;&nbsp;
                </li>
                <li>
                    <button id="activatemaincontainersignup" type="button" class="btn btn-success btn-lg"
                            style="background-color:#4ebaaa !important;"
                            title="This screen allows you to signup a new user with fingerprint capability or append an old patient without fingerprint capability with a fingerprint ability.">
                        Registration
                    </button>&nbsp;&nbsp;&nbsp;&nbsp;
                </li>
                <li>
                    <button type="button" class="btn btn-success btn-lg" style="background-color:#4ebaaa !important;"
                            title="allows you to change the url to be loaded by the web view" onclick="app.changeUrl()">
                        Change URL
                    </button>
                </li>
            </ul>
        </div>

    </div> <!-- close container div -->
</nav>
<!-- close navbar nav -->
<br/>
<div id="homecontainer" class="container">
    <table>
        <thead>
        <tr class="forms-header">
            <th>
                <h3>Welcome to mUzima Fingerprint</h3>
            </th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th>
                <p>
                    This module allows you to register a new patient with a fingerprint scan option or to append an
                    existing user with the fingerprint capability. It is also used to access patient information whose
                    fingerprint had already been registered in the system. Navigate through the menu bars above. You can
                    hover the mouse over a individual menu above if you are not sure what to do in order to get more
                    information about it. Enjoy!</p>
            </th>
        </tr>
        </thead>
    </table>
</div>
<div id="showdefaultsettings" style="display:hidden;" class="container">
    <table>
        <thead>
        <tr class="forms-header" style="text-align:center;">
            <th>
                <p>Choose the default finger in the dropdown below and click Apply Setting button to save you
                    setting.</p>
            </th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th>
                <select id="defaultfinger" style="width:100%;text-align:center;">
                    <option>left thumb</option>
                    <option>left index</option>
                    <option>left middle</option>
                    <option>left ring</option>
                    <option>left little</option>
                    <option>right thumb</option>
                    <option>right index</option>
                    <option>right middle</option>
                    <option>right ring</option>
                    <option>right little</option>
                </select>
            </th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th>
                <input id="applydefaultfinger" type="button" value="Apply Setting" class="btn btn-primary btn-block"
                       style="background-color:#009D8E !important;">
            </th>
        </tr>
        </thead>
    </table>

</div>
<!-- end of trial-->
<div id="mainframecontainer" class="container" style="display:none;">
    <!--<div class="navbar navbar-custom">
        <div>
            <a href="#"><i class="icon-home"></i> mUzima Fingerprint Module</a>
        </div>
    </div>-->

    <!-- sign in screen -->
    <div id="signinscreen" style="display:hidden;">
        <div id="find-patient">
            <div class="row forms-list">
                <div class="col-lg-12">
                    <table id="find-options">
                        <thead>
                        <tr class="forms-header">
                            <th colspan="3"><h2 style="font-size:1.3em;">Find Patient by Patient Identifier or Patient
                                Name or by scanning finger print below</h2></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td> Patient Identifier or Patient Name:<input type="text" id="findPatients"
                                                                           name="findPatients" value="${identifier}"
                                                                           onkeypress="activate(this.value, event)">
                            </td>
                            <td colspan="2">
                                <div id="downloadDiv">
                                    <a style="text-decoration:none;" href="#" onclick="app.identifyFinger()"
                                       id="download"> <input type="button" value="Click here to identify patient"
                                                             class="btn btn-primary btn-block"
                                                             style="background-color:#009D8E !important;"> </a>
                                    <input type="hidden" id="startScanning"/>
                                    <input type="hidden" id="fingerprintScan"/>
                                </div>
                                <div id="refreshDiv">
                                    <!-- <span>Fingerprint processing timed out, please click refresh to continue</span>
                                    <button type="button" id="refresh" class="btn btn-lg btn-primary">Refresh</button>-->
                                    <input id="refresh" type="button"
                                           value="Fingerprint scanner initiation has been completed.Click here to re-initiate if scanner did not start."
                                           class="btn btn-primary btn-block"
                                           style="background-color:#009D8E !important;">
                                </div>
                                <div id="spinner">
                                    <div class="loader"></div>
                                    <span>please wait while scanner is starting up</span>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- -->

    <div id="patientCreated">
        <h5>Patient Created</h5>
    </div>
    <!-- start of body wrapper for non create -->
    <div id="body-wrapper">
        <div class="row forms-list">
            <div class="col-lg-12">
                <table id="tblData">
                    <thead>
                    <tr class="forms-header">
                        <th>Matching Patient List</th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <thead>
                    <tr class="forms-list-header">
                        <th style="display:none;"></th>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Family Name</th>
                        <th>Identifier(s)</th>
                        <th>Gender</th>
                        <th>Fingerprint</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- end of body wrapper for non create -->

    <!-- start of body wrapper -->
    <div id="body-wrapperr" style="display:none;">
        <div class="row forms-list">
            <div class="col-lg-12">
                <table id="tblStore">
                    <thead>
                    <tr class="forms-header">
                        <th>Possible Matching Patient List</th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th colspan='6'>The following persons were found with similar characterictics as entered, click
                            Append Fingerprint Button next to the person to append the Finger Print to the Existing
                            Patient. If a tick sign is in the Fingerprint Column, then the patient has already been
                            registered fully hence no need to create the person
                        </th>
                    </tr>
                    </thead>
                    <thead>
                    <tr class="forms-list-header">
                        <th style="display:none;"></th>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Family Name</th>
                        <th>Identifier(s)</th>
                        <th>Gender</th>
                        <th>Fingerprint</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                    <thead>
                    <th colspan='6'>Is the person not listed above? click on Create Person button below to register a
                        new patient
                    </th>
                    </thead>
                    <thead>
                    <th colspan='6' style="text-align:center;">
                        <div style="float:left;width:70%;">
                            <input id="createNewPatient" type="button" value="Create Person"
                                   class="btn btn-primary btn-block" style="background-color:#009D8E !important;">
                        </div>
                        <div style="float:right;width:25%;">
                            <input id="CancelCreateNewPatient" type="button" value="Go Back"
                                   class="btn btn-primary btn-block" style="background-color:red !important;">
                        </div>
                    </th>
                    </thead>
                </table>
            </div>
        </div>
    </div>
    <!-- end of body wrapper -->

    <!-- start of search results -->
    <div id="searchResults" style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <h5>No patient found with this identifier. Retry with a different identifier or scan the patient's
            fingerprint</h5>
    </div>
    <!-- end of search results -->

    <div id="exception_device" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Scanner not found, please insert scanner to continue.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="exception_quality" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Low quality image captured, please scan again.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="exception_swipe" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Finger swipe was too short,please scan again.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="exception_center" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Please center your finger and scan again.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="exception_pressure" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Weak finger pressure ,please insert some pressure on to the scanner and retry.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="exeption_native" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Scanner Error</h4>
                </div>
                <div class="modal-body">
                    <p>Unable to load fingerprint software,please contact the system admin.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- start of registration section -->
    <div id="registrationSection">
        <div id="updatePatient" class="btn btn-lg btn-primary">
            <input id="btnUpdatePatient" type="button" value="Update Selected patient" class="btn btn-lg btn-primary">
            <input id="btnCancel" class="btn btn-lg btn-primary" type="button" value="Cancel">
        </div>
        <div id="otherIdentificationOption">
            <h4>No patient found with this fingerprint, Do you want to register?</h4>
            <a href="#" id="btnYes">Yes</a>
            <a href="#" id="btnNo">No</a>
        </div>
        <div style="height:20px;"></div>
        <div id="enrollFingerprint"
             style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;width:100%;">
            <table id="appendWell" border="0" style="text-align:center !important;width:100% !important;">
                <thead>
                <tr class="forms-header" style="text-align:center;">
                    <th style="text-align:center;font-size:1.3em;">
                        <h2 id="appendMessage">Patient Fingerprint Appended Successfully</h2>
                    </th>
                </tr>
                </thead>
                <thead style="text-align:center;">
                <tr style="text-align:center;">
                    <th style="text-align:center;color: green;">
                        <br/>
                        <p id="appendBody">The patient fingerprint has been appended successfully in the database</p>
                        <br/>
                    </th>
                </tr>
                </thead>
            </table>
            <table id="registeredWell" border="0" style="text-align:center !important;width:100% !important;">
                <thead>
                <tr class="forms-header" style="text-align:center;">
                    <th style="text-align:center;font-size:1.3em;">
                        <h2 id="registerMessage">Patient Registered Successfully</h2>
                    </th>
                </tr>
                </thead>
                <thead style="text-align:center;">
                <tr style="text-align:center;">
                    <th style="text-align:center;color: green;">
                        <br/>
                        <p id="registerBody">The patient has been successfully registered</p>
                        <br/>
                    </th>
                </tr>
                </thead>
            </table>
            <table id="unregisteredscantext" border="0" style="text-align:center !important;width:100% !important;">
                <thead>
                <tr class="forms-header" style="text-align:center;">
                    <th style="text-align:center;font-size:1.3em;">
                        <h2 id="scanHeaderMessage">Patient Fingerprint Identification Result</h2>
                    </th>
                </tr>
                </thead>
                <thead style="text-align:center;">
                <tr style="text-align:center;">
                    <th style="text-align:center;color: green;">
                        <br/>
                        <p id="userNotificationScan">Fingerprint does not match any patient. Please register fingerprint
                            by clicking the button below.</p>
                        <br/>
                    </th>
                </tr>
                </thead>
                <thead style="text-align:center;">
                <tr style="text-align:center;">
                    <th style="text-align:center;">
                        <a id="interfacecontrol" style="text-decoration:none;width:100% !important;"
                           onclick="app.enrollFinger()"><input type="button" value="Scan Desired Finger"
                                                               class="btn btn-primary btn-block"
                                                               style="background-color:#009384 !important;"></a>
                    </th>
                </tr>
                </thead>
            </table>
            <table id="scanwaittext" style="display:none;">
                <thead>
                <tr class="forms-header">
                    <th style="text-align:center;">mUzima Registration Progress</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th style="color: green;">
                        <br/><br/>
                        <marquee>The scanner start up has been initiated.If the scanner takes too long to initiate
                            please click the button below to re-initiate it.
                        </marquee>
                        <br><br/>
                    </th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th>
                        <a style="text-decoration:none;" onclick="app.enrollFinger()"><input type="button"
                                                                                             value="Re-initiate Scanner"
                                                                                             class="btn btn-lg btn-primary btn-block"
                                                                                             style="background-color:#009384 !important;"></a>
                    </th>
                </tr>
                </thead>
            </table>
        </div>
        <button type="button" id="enrollFingers" class="btn btn-lg btn-success" style="display:none;">PROCEED TO
            REGISTER
        </button>

        <div style="height:20px;"></div>

        <div id="addFingerprints" style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;">
            <span id="selected-patient" style="font-weight:bold;color:green;"></span><br>
            <span style="display:none;"><input type="hidden" name="appendPatientUuid" id="appendPatientUuid"></span>
            <input type="button" value="Append Fingerprint" class="btn btn-lg btn-primary btn-block"
                   style="background-color:#009D8E !important;" onclick="app.appendFinger()">
        </div>
    </div>
    <!--start of registration click content -->


    <div id="registrationSections">
        <p></p>
    </div>
    <!--end of registration click content -->
    <div id="registrationForm" style="margin-left:0px;">
        <h3 id="form-title">New Patient Registration Form</h3>
        <form id="formData" method="post" action="" style="border: 1px solid #eeeeee;padding:20px;">
            <fieldset style="width:100%;">
                <legend>Person Name</legend>
                <div class="form-group">
                    <label for="patient.given_name">Given</label>
                    <input autocomplete="off" type="text" name="patient.given_name" id="patient.given_name" class="form-control" required>
                </div>
                <label for="patient.middle_name">Middle Name</label>
                <input autocomplete="off" type="text" name="patient.middle_name" id="patient.middle_name" class="form-control" required>
                <div class="form-group">
                    <label for="patient.family_name">Family Name</label>
                    <input autocomplete="off" type="text" name="patient.family_name" id="patient.family_name" class="form-control" required>
                </div>
            </fieldset>
            <fieldset style="width:100%;">
                <legend>Additional Details</legend>
                <div class="form-group">
                    <label for="patient.phone_number">Phone Number</label>
                    <input autocomplete="off" type="text" name="patient.phone_number" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="patient.mothers_name">Mothers Name</label>
                    <input autocomplete="off" type="text" name="patient.mothers_name" class="form-control">
                </div>
            </fieldset>
           <fieldset style="width:100%;">
           	<legend>ID Number(s)</legend>
           	<div class="form-group">
           		<div class="section repeat identifierType" id="identifierType" data-name="identifierType">
           			<div class="form-group">
           				<label for="identifierType">Identifier Type</label>
           				<select class="form-control identifierType" name="identifier.identifierType" id="IdentifierOptions">
           					<option value="">...</option>
           				</select>
           			</div>
           			<div class="form-group">
           				<label for="amrs_id">Enter Identifier</label>
           				<input class="form-control" id="amrs_id" autocomplete="off" required name="identifier.amrs_id" type="text">
           			</div>
           			<div class="form-group">
                       <label for="identifier.location_id">Identifier Location</label>
                       <select id="LocationOptions" name="identifier.location_id" class="form-control" required>
                           <option value="">...</option>
                       </select>
                    </div>
           			<div class="form-group">
           				<label for="preferred">Preferred</label>
           				<input class="" id="preferred" required name="identifier.preferred" type="radio">
           			</div>
           		</div>
           	</div>
           </fieldset>
            <fieldset style="width:100%;">
                <legend>mUzima Biometrics</legend>
                <div class="form-group">
                    <label for="fingerprint">Finger Print</label>
                    <img src="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/images/done.png"/>
                    <input type="hidden" id="fingerprint" name="patient.fingerprint" value="TlQAEJUQAAAKAU5GVBCLEAAACgFORlImgRAZZQGIAfQB9AEAAFeCAwEfAAAAB/UjB3hRIA83C2pslcANNAh8HtFgFlAKfSvegB85CHKY7oAZUgl2mCZhF18GdYZpARRaFHGtogEkKgh9TsHBCSsMdp/CQRlkCm1MBmIOVhFwLzXCE0gtdq5CYiBTCX0xWQIaYAl1M6YCE2ARccauYglAB3Cv/uIXYwZztSWjH0gIfC5uAw5YB2+uioMfRQhwro4jHGQHcKsipA9RCWuqIsQTUQ1jqlYEIi0OcaBhRBpbC2ejfSQeUgttq5UECAERdJzhxCAlGHGfCcUQJRdylxnlDjETfJEZxRpDCm2RKmUQFRN4FEklEBIHfI1x5QojDXWJluUVGAVzBqQB////////AgY3Af8v/////wDyAgY2CgiFBQYVCABT/////wTxDAk3BABF/////wfx/////wP0DAkTBQIhAv8fBAcnCQsXBgEyAQBnAgVTCQsVCggk////////EwxkCQOB////////AQZUCg9BBQQTBwyDDQ4lCwZRD/8vCAFIBgskDhIxDhIVCgFLBglVDRBBAwc0////ERAoDQlTEBI6CwZDCQcqDBFTFhIkDwpzCwkVDRBSCg4nEhUy/////wjxCQ1DERRhGBY1Eg5yDP8v////ExQUEA1jFRoX/w/zCg4UEBZxEf8f////FxgWFBAkDREUExchGBZGFRCREhAYFh4hHCE5Gv9vDhAlFBhiIhxlFRIhE/8f/////xvzGRQxEBQ0FxlSHiJIHBZyFBMkF/8/Gx4VHBiCDxJHFR1iIf9P////GRcT////////HhhBFRY1GCJy/x/xHRokDxU1HB8hIP8vIRpCFhgkGRtU////IhxDGhwhGB5zIiARIR0x/////yHzHR8hGCKB/xr0HSBD////////IBYWHhtG/////yHyAQRN/QESAAcABAAA7CVtmUANNAeAId3gFkcJfZbiIBlQCXgr7uAfOAV1lRrhFlwGd3xZYRNOFnWrjuEjLgl/nq6hGGQKbk0KIhI4UHhKHgIOUgx0rTIiIFEJfy5qghRYFXczaUIaYQh5yJ5iCToHczS2QhNfDnGu7oIXYQV2thFjH0cIfrB2oxtfBnWuekMfRQhzL35jDk8Hcq8SRA9QCW6rEmQTUQtmrUaEIT0LcqJNpBlaC2ejaaQdUgttmtEkIEQTbp/5JBA+FHOZCSUOPRB8kA1FGkIKbpcNJSEcJnmFHgUQARd4EUWlECYJepVGhSAUHHyKZUUKIQ12jX1lEi8CfzmSRRUTBHYAlQUUCwR8vAH///////8BBRUJ/48EBRT/APH/////AvH/////A/UKBxQEATEKB0gC/1//////BvH/AfECAzcHCygFADL/APX/BPMHCxUICUP///////8WCnUHA5EEAiQDCoQMCyYIBVENCWP/BfQEB3ULDjIN/z//APgFCDMLE2IDBkX///8QDykMB1QVDlINCJMFB1YMD0ERDxMLBUEHBioKEFIJCDYTFEP///////8VEzUNCYUICyIPETQHDFMQEWIXFTYNDrMK/y////8SERQPDGIMEBQSFjIXFUcTD5IQ/x////8WFxYRDzUU/x//DfQJDiURFZITDxkVIzUaITn/DfMOETcSF5IiGoUUEzIS/x////8ZHDcYETIPETQWGFEcIzgaFWIREiQW/z8ZHBQiF7EYFhP///8dICIcFzEUFTUXI2IfHhEb/x8NFEUaHhEf/x8h/z8VFyMYGUP///8jH0MZFiT/////IPIjHHQbFBUaHBMkHzH/IfMi/y//IfIeGhEXHHMYHTL/////////HPH/DfcbHjP///////8fFSgXI6Ik/z//IfP///8kHhIXHIT///////////8iGjQXI6HbIa6RwCIBBYIg2cAWTQiWKeEAHzsEkZjuoBlWCJCZLsEXWQaQh1aBFEgPkq2mQSQ4BZpRycEJJAmHoMqBGWQGik3+oQ5QD4syNmITPSuUrkaiIFEGny1aQhRKFpQwYSIaXgeWM6YiE1kOkcW6oglBBYiuCkMYYgOVtSnjH0cFnS9qIw5XBoqtmmMcYgSOr5qjH0UFj6gmBBRVCX6oKiQQUgeGqmJEIi4JjZ9lhBpQCISihYQeUwiKrJEECBsRhZ3pBCEmEI6bHUUQNQuXkR0FG0IHi5QuRQ8tC5uMdUULHwiVkInlEh8CjowB////////BgIkA/9/BAUUB/9v////AAORCwg3A/9P/wD0/wbx////AAJ0CwgTBAEhAf8fAwYnCAoXBf8v////AQRCCAwVCgdSAP8v////FAtkCAKB////////AQlkDA+hBAMTBguDDQ4nDAVRD/8vB/9PBQgjChIxDg8WCf8/BQhVDQxBAgY0////ERAoDQhTFQ5BDwqBBQhVDRBBEBI7DAVCCAYqCxFTFRIkDwl0CgwRDRBiCQ4nEhYy/////wfxCA1DERNhGBU1Eg5yC/8v////FBMUEA1jFhoX/w/zCQ4UEBVxDREUFBchGBVGFhCREf8f////FxgWExAkDhAlExhiIBx2FhIhEhAYFRgkHB9JGv9vFP8f/////xvzGRMxEBM0/xnyHSBJHBWCExQkF/8/Gx0VIBiiDxJHFh5hH/9P////GRcT////////HRhBEhZUGB2EIP8vHhoTFRgkGRtU////IBxUGhYVHB0UIP8fH/8//xr0Hhw1////////HBUnHRtX/////x/y8iGW3uAYTgiRIuUAF0IHlS3pQB8sBJOWHgEXVgaShFKhE0ESlauOwSMqBpyetsEYZAeKShICDkkOjkoSwhEwSJStMiIgUQehNkJiEz8jlC5mYhRGEZUzbUIaUwaax6JiCTUFijSyQhNbDJGw9oIXYQOWtBVjH0YFnzF6Qw5OBY2uguMbYQSRsYYjH0MFkawOJBNTCIKuGkQPTweJrE6EIToHjqFRxBlZCIOjdcQdUQmKmdlkIDcOjZoNpQ8jC5eQESUaQgaLlCKFDiALnJZlpSABMI6McWUKGAmUjXrFHg4NiY19BRIjApOMAf////8C9AkGFAMBMQMEFP///////wAFFgkGNwD/T/////8F8f8B8QACNgYKKAT/P////wEDQwYKFgj/X////////xYJdQYCkQMAJAIJdAwLJgoEYQ3/P////wQIQwoRUhEHM////wQGVQoOIgIFNf///xAPKQwGVA4NGAcIUgQGZgwLQRQOUg0KkQQGZgwPQRIPEwsEQgYJJf8Q8gcINg8RtP///////xQRNQ0HhQsGKA8SNAYMUxASYhcUNhEOgwn/L////xMSFA8MYhX/H/8N9AcOJRIUkgwQFBMWMhcURxUPohD/H////xYXFhIPNQ4PNhIXciAadhURMhEOFhQbMRoeSf8N8xP/H////xkbNxgSMg8SNBYYURsgOBoUchITJBb/PxkbFCAXkRgWE/////8d9BsXMQ0VRBcbcyD/Hxz/HxQXIxgZQx//H/8g9P8V9hobFSD/Hx7/PxgZVP///////x8bJP8R+hwgMf///////xsYFRkdQv////8g8xoUFxcbhP////8e8eknfVFgDzkMZXSR4A04CGwe0UAWSgprLNZgHzkNXpr+4BlWCWOaMoEXXwdeh2kBFFwSXayVgSQmCmVVvmEQIyRZoMphGWQKWU72gQ5HFV9J+uESPlpoLjXCEzgxaK5GYiBTC2UwUcIZXwxcM5riElUSYsW2Ygk8CWGvAuMXXghetimDH04LZTBm4w1ZCluvkmMfRgxZrpZDHGQJWqoq5A9QClWqOWQTSw5TqV5EIiwTYZ9lZBpSDVSjgUQeUw5YoKGECBYMXi26xBEZFVSl5cQQJyhdneXEICcdX5cVxQ5CHmeQGeUaRg1ZBUVFEBUUZo1x5QoyD2aOckUTIghlCYllExkGaHeV5QgBCHCIlkUWFwlj1AH///////8CBjcB/y//////APICBjYI/48FBhX/APP/////BPENCTcE/0//////B/H///8DB0YNCRMFAiEC/x8EBygJDBcGATIBAGcCBVMJDBULCEL///////8YDXUJA5EK/x//AfgGCSILEyEFBBMHDZMOESQLBkEQ/y//AfgICxMME1ETCjMIASoGCUQODDEPExUQCIQLCRUOEUEDBzX///8SESgOCVMREzoMC0MJBysNElMWE1QQCnQMCRYOEVIKDycTFjL///////8ODTgSFWEZFzYTD3IN/y//GPEUFRQRDmMWGxf/EPMKDxQRF3ES/x////8YGRYVESQOEhQUGCEZF0cWEZETERgXICEcIhgb/28PETYZIDIjHGMWEyEU/x//////HvMaFTERFTT/GvIgJkgcF1MVFCQY/z8eIBUcGWL/E/cWH2Ii/0////8jHTEbEFIWFxMZJlMWHCEZJnIjISIfGzQaGBP///////8gGUEQFjUdJjMh/y8iG0IXGSQaHlT///8mI0Qj/x//IvMfFiQdICX/G/QfIUP///8l/y8hHBMZIIQmJBH/IvL/////IvIhIyEZJpH///8bImIj/z////8jHBMgHkf/////JPH/KndZwA42CmpsnWANOAhwIdnAFkoKbS3q4B86CmKX8mAZUghnlyZBF2IHY4BhgRNZFV+tjUEkKQtpoMbhGGQLW03S4Q8uHFpKCgIOUxNhNQpCEzRMaUYOwhJHTGqvOgIgUgtmLmpiFFkbZTJpQhphCWHHqkIJPQlkNK5CE2AQY6/yYhdeCGG2HWMfSQtnMHpDDlAJXrCCQx9HDF2wisMbYglerSJkD1AKWKopBBNLDlarVsQhPxBhoF0EGloOVaN1xB1SD1kYzuQRIBpWmtlEID4WXKPdZBAgHF0X/uQRKiFZmwlFDzQiZpgNRQ4+FmeQEUUaRQ1blxEFISAuZIttZQovEGiUdoUSJwdnhJbFFR8IZXqZRQgBC3MAmcUTHgZpBK3FBw8LfPgB////////AgYmCQGR/////wDxAgYVCf+PBQYU/wDy/////wTxDQhIBP9f/////wfx/////wP1DQgUBQIxAAIxBAM3CAsnBgEy/wH1AgVDCA4VCwlC////////GQ11CAORBQQkAw2EDw4mCwZRCv8f/wH4BggkDBE0EP8//wH4CQwUDhRiERAYDAEaBghFDw4yFApECQE6BgtBDw5CAwdF////ExIpDwhUGBFSEAySCwgmDxJBFhITDgZBCAcqDRNSChE4FBdD////////GBQ1EAqFDA4SEhY0CA9TExZiGhQ4EBGzDf8v////FRYUEg9iF/8f/xD0ChElFhiRE/8f////GRoWFhI1DxMUFRkyGhhHFxKiFBEWGCIhHCQp/xDzERI2GiIhHB4zFxQhFf8f////Ix1DGxYyEhY0GRtRIiY4HBhSFhUkGf8/HSIUHBphKB8xHv8fFxgjGiJRGxkT////I/8vIhohFxgjHB8RJSBDIf8fKCUiJCBDHhwRGiJiEBdFHiY0Jf8/JCFiEBc0HiASJf9PJP8/GBoTGx1C////JiVEHRkk////////JiJ0/xT6IR81////J/8fIB8yIiZBKP8f/yTyJR8SIh1G/////yjx////FySxJf8//yny/////yn0ISVxGiaB/////////xD9JyQk" autocomplete="false">
                </div>
            </fieldset>
            <fieldset style="width:100%;">
                <legend>Demographics</legend>
                <div class="form-group">
                    <div class="form-inline">
                        <input type="radio" id="sex1" name="patient.sex" value="F" class="form-control"><b>Female</b>
                        <input type="radio" id="sex2" name="patient.sex" value="M" class="form-control"><b>Male</b>
                    </div>
                    <label for="age">Age</label>
                    <input autocomplete="off" id="ageregistration" readonly name="patient.age" class="form-control" required>
                    <br/>
                    or
                    <br/>
                    <label>Birthdate (Format: dd/mm/yyyy)</label>
                    <input autocomplete="off" id="datepicker" name="patient.birthdate" class="form-control">
                </div>
            </fieldset>
            <fieldset>
                <legend>Address</legend>
                <div class="form-group">
                    <label for="address1">Address</label>
                    <input name="personAddress.address1" class="form-control" type="text">
                </div>
                <div class="form-group">
                    <label for="address2">Address 2</label>
                    <input name="personAddress.address2"  class="form-control" type="text">
                </div>
                <div class="form-group">
                    <label for="cityorvillage">City/Village</label>
                    <input name="personAddress.cityVillage"  class="form-control" type="text">
                </div>
                <div class="form-group">
                    <label for="stateProvince">State/Province</label>
                    <input name="personAddress.stateProvince"  class="form-control" type="text">
                </div>
                <div class="form-group">
                    <label for="country">Country</label>
                    <input name="personAddress.country"  class="form-control" type="text">
                </div>
                <div class="form-group">
                    <label for="postalCode">Postal Code</label>
                    <input name="personAddress.postalCode"  class="form-control" type="text">
                </div>
            </fieldset>
        </form>
        <br/>
        <div style="float:left;width:70%;">
            <input id="btnCreatePatient" type="button" value="Register Patient" class="btn btn-lg btn-primary btn-block"
                   style="background-color:#009D8E !important;">
        </div>
        <div style="float:right;width:25%;">
            <input id="btnCancelTransaction" type="button" value="Cancel Registration"
                   class="btn btn-lg btn-primary btn-block" style="background-color:red !important;">
        </div>
    </div>
    <!-- start of person basic demographics form -->
    <div id="basicdemographicform" style="margin-left:0px;">
        <h3 id="form-title">Create Patient </h3>
        <form id="formData" method="post" action="" style="border: 1px solid #eeeeee;padding:20px;">
            <p>To create a new person, enter the person's name and other information below first to double-check that
                they don't already have a record in the system. </p>
            <div class="form-group">
                <label for="family_name">Person Name</label>
                <input autocomplete="off" type="text" id="family_name" name="family_name" class="form-control"
                       value="${identifier}" required>
            </div>
            <fieldset style="width:100%;">
                <legend>Date of Birth or Age</legend>
                <div class="form-group">
                    <label for="age">Age</label>
                    <input autocomplete="off" id="age" name="age" class="form-control" required>
                    <br/>
                    or
                    <br/>
                    <label for="datepicker1">Birth Date</label>
                    <input autocomplete="off" id="datepicker1" name="datebirth" class="form-control" >
                </div>
            </fieldset>
            <div class="form-inline">
                <label for="gender">gender</label>
                <input id="gender1" type="radio" name="sex" value="F" class="form-control"><b>Female</b>
                <input type="radio" id="gender2" name="sex" value="M" class="form-control"><b>Male</b>
            </div>
        </form>
        <br/>
        <div style="width:100% !important;">
            <div style="width:65% !important;float:left;">
                <input id="btnSearchPatient" type="button" value="Create Person"
                       class="btn btn-lg btn-primary btn-block" style="background-color:#54BFB2 !important;">
            </div>
            <div style="width:28% !important;float:right;">
                <input id="cancelregistration" type="button" value="Cancel Registration"
                       class="btn btn-lg btn-primary btn-block" style="background-color:red !important;">
            </div>
        </div>
    </div>
    <!-- end of person basic demographic form -->
    <div id="patient-exists" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:green;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Server Error</h4>
                </div>
                <div class="modal-body">
                    <p>The patient identifier already exists, please use a different one.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div id="missing-first-scan" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Fingerprint Notification</h4>
                </div>
                <div class="modal-body">
                    <p>First left thumb scan has not been performed, please <a id="interfacecontrol" style="text-decoration:none;" href="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/enroll-fingerprint.jnlp"><b
                            style="background-color:#009D8E !important;">:-CLICK HERE TO SCAN AFRESH-:</b></a> or
                        navigate to the webstart app that you started to scan first print</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close Notification</button>
                </div>
            </div>
        </div>
    </div>
    <div id="missing-second-scan" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Fingerprint Notification</h4>
                </div>
                <div class="modal-body">
                    <p>Second left thumb scan has not been performed, please <a id="interfacecontrol"
                                                                                style="text-decoration:none;"
                                                                                href="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/enroll-fingerprint.jnlp"><b
                            style="background-color:#009D8E !important;">:-CLICK HERE TO SCAN AFRESH-:</b></a> or
                        navigate to the webstart app that you started to scan second print</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close Notification</button>
                </div>
            </div>
        </div>
    </div>
    <div id="missing-third-scan" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Fingerprint Notification</h4>
                </div>
                <div class="modal-body">
                    <p>Third left thumb scan has not been performed, please <a id="interfacecontrol" style="text-decoration:none;" href="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/enroll-fingerprint.jnlp"><b
                            style="background-color:#009D8E !important;">:-CLICK HERE TO SCAN AFRESH-:</b></a> or
                        navigate to the webstart app that you started to scan third print</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close Notification</button>
                </div>
            </div>
        </div>
    </div>
    <div id="fingerprint-exists" class="modal fade" role="dialog"
         style="margin:0 auto;padding:10px;border: 1px solid gray;text-align: center;color:red;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Fingerprint Notification</h4>
                </div>
                <div class="modal-body">
                    <p>This patient's fingerprint matches an existing patient, it cannot be appended</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close Notification</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>

    var openmrsContextPath = '${pageContext.request.contextPath}';

</script>

<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/custom/settings.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/custom/Custom.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimabiometrics/js/custom/muzima.js"/>

<script>
    /*$( function() {
        $( "#datepicker" ).datepicker({
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            maxDate: new Date,
            changeYear: true
        });
    } );*/
</script>
<script>
    $( function() {
        $("#datepicker1").datepicker({
            onSelect: function(dateText, inst) {
                var formatedDate = $(this).val();
                var standardBirthDate=new Date(formatedDate);
                var dateToday=new Date();
                var age = dateToday.getFullYear() - standardBirthDate.getFullYear();
                var m = dateToday.getMonth() - standardBirthDate.getMonth();
                if (m < 0 || (m === 0 && dateToday.getDate() < standardBirthDate.getDate())) {
                    age--;
                }
                $("#age").val(age);
            },
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            maxDate: new Date,
            changeYear: true
        });
    } );
</script>
<script>
    $( function() {
        $( "#datepicker" ).datepicker({
            onSelect: function(dateText, inst) {
                var formatedDate = $(this).val();
                var standardBirthDate=new Date(formatedDate);
                var dateToday=new Date();
                var age = dateToday.getFullYear() - standardBirthDate.getFullYear();
                var m = dateToday.getMonth() - standardBirthDate.getMonth();
                if (m < 0 || (m === 0 && dateToday.getDate() < standardBirthDate.getDate())) {
                    age--;
                }
                if(age==0){
                     $("#ageregistration").val("< 1 yr");
                }else{
                    $("#ageregistration").val(age+" yrs");
                }

            },
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            maxDate: new Date,
            changeYear: true
        });
    } );
    $(document).ready(function(){
        $("#age").keyup(function() {
            var dateToday = new Date();
            var todayYear = dateToday.getFullYear();
            var inputAge=$(this).val();
            if(inputAge<120 && !isNaN(inputAge))
            {
                var bornYear=todayYear-inputAge;
                $("#datepicker1").val(bornYear+"-01-01");
            }
            else
            {
                $("#age").val("enter a valid age");
                $("#datepicker1").val("");

            }

        });
    });
</script>
