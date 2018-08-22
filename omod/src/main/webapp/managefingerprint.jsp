<!DOCTYPE html>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="View mUzima FingerPrint" otherwise="/login.htm" />

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

   td {
      font-size: 16px;
   }
   th{
      font-size: 19px;
   }
</style>
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
    <div id="signinscreen">
        <div id="find-patient">
            <div class="row forms-list">
                <div class="col-lg-12">
                    <table id="find-options">
                        <thead>
                        <tr>
                            <th><div id="defaultFingerDiv" align="center" style="font-size: 1.3em;font-family: Verdana,Arial,Helvetica,sans-serif;"></div></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td width="100%">
                                <div id="downloadDiv" align="center">
                                    <input id="download" type="submit" onclick="app.identifyFinger()" onclick style="width: auto;" value="Launch fingerprint scanner" class="btn btn-primary btn-block">
                                    <input type="hidden" id="startScanning"/>
                                    <input type="hidden" id="fingerprintScan"/>
                                </div>
                                <div id="refreshDiv" align="center">
                                    <input id="refresh" type="button" style="width: auto;"
                                           value="Re-launch fingerprint scanner"
                                           class="btn btn-info btn-block">
                                </div>
                                <div id="spinner" align="center">
                                    <div class="loader"></div>
                                    <span>Please wait while scanner is starting up</span>
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
                        <th>Matching patient list</th>
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
                <input type="hidden" id="tblDataAppendfingerprint" autocomplete="false">
            </div>
        </div>
    </div>
    <!-- end of body wrapper for non create -->

    <!-- start of body wrapper -->
    <div id="body-wrapperr" style="display:none; ">
        <div class="row forms-list">
            <div class="col-lg-12">
                <table id="tblStore">
                    <thead>
                    <tr class="forms-header">
                        <th>Found Similar People</th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th colspan='9'>Here is a list of people who seem similar to the one you are about to create.<br/>
                        If the person you are in the process of creating already exists, select them from the list below. Otherwise click the button below to continue creating a new person.
                        </th>
                    </tr>
                    </thead>
                    <thead>
                    <tr class="forms-list-header">
                        <th style="display:none;"></th>
                        <th style="display:none;"></th>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Family Name</th>
                        <th>Identifier(s)</th>
                        <th>Gender</th>
                        <th style="display:none;">Scanned Finger</th>
                        <th>Fingerprint</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                    <thead>
                    <th colspan='8'>
                    </th>
                    </thead>
                    <thead>
                    <th colspan='8' style="text-align:center;">
                        <div style="float:left;width:70%;">
                            <input id="createNewPatient" type="button" value="I cannot find the person on the list"
                                   class="btn btn-primary btn-block" style="background-color:#009D8E !important;">
                        </div>
                        <div style="float:right;width:25%;">
                            <input id="CancelCreateNewPatient" type="button" value="Back"
                                   class="btn btn-primary btn-block" style="background-color:red !important;">
                        </div>
                    </th>
                    </thead>
                </table>
                <input type="hidden" id="tblStoreAppendfingerprint" autocomplete="false">
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
                    <p>Finger swipe was too short, please scan again.</p>
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
                    <p>Weak finger pressure, please insert some pressure on to the scanner and retry.</p>
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
                    <p>Unable to load fingerprint software, please contact the system admin.</p>
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
            <h4>No patient found with this fingerprint, do you want to register?</h4>
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
                        <h2 id="appendMessage">Patient fingerprint appended successfully</h2>
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
                        <h2 id="registerMessage">Patient registered successfully</h2>
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
                        <h2 id="scanHeaderMessage">Fingerprint not in the system. Please identify by name or identifier</h2>
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
        <h3 id="form-title">Create a new patient</h3>
        <form id="formData" method="post" action="">
        <table id="parentTable" cellspacing="0" cellpadding="7" border="1" width="98%">
               <tr id="parentTableRow">
                    <th valign="top">Person Name</th>
                    <td>
                        <table id="personNameTable" cellspacing="2">
                            <thead>
                                <tr>
                                    <th>Given <span class="required">*</span></th>
                                    <th>Middle</th>
                                    <th>Family Name <span class="required">*</span></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input autocomplete="off" type="text" name="patient.given_name" id="patient.given_name" class="form-control lettersOnly" required></td>
                                    <td><input autocomplete="off" type="text" name="patient.middle_name" id="patient.middle_name" class="form-control lettersOnly"></td>
                                    <td><input autocomplete="off" type="text" name="patient.family_name" id="patient.family_name" class="form-control lettersOnly" required></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
               </tr>
               <tr id="parentTableRow">
                    <th valign="top">Identifier(s)</th>
                    <td>
                        <table id="identifierTable" cellspacing="2">
                            <thead>
                                <tr>
                                    <th width="30%">Identifier Type <span class="required">*</span></th>
                                    <th width="25%">Value <span class="required">*</span></th>
                                    <th width="25%">Location <span class="required">*</span></th>
                                    <th width="10%">Preferred <span class="required">*</span></th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="section repeat identifier_type" id="identifier_type" data-name="identifier_type">
                                    <td><select class="identifierType" name="identifier.identifierType" id="IdentifierOptions" required>
                                            <option value="">...</option>
                                        </select>
                                    </td>
                                    <td><input class="identifier-value" autocomplete="off" required name="identifier.amrs_id" type="text" style="display:block">
                                    <label class="errors" style="display:none;background-color: lightpink;">Please enter a valid identifier that matches Regex</label>
                                    </td>
                                    <td><select id="LocationOptions" name="identifier.location_id" class="" required>
                                            <option value="">...</option>
                                        </select>
                                    </td>
                                    <td><input class="" id="preferred" required name="identifier.preferred" type="radio"></td><td class="removeButton" valign="middle"></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><input class='btn btn-primary add_section pull-left' type='button' value='Add Identifier'/></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
               </tr>
               <tr id="parentTableRow">
                   <th valign="top">Demographics</th>
                   <td>
                       <table id="identifierTable" cellspacing="2">
                           <thead>
                               <tr>
                                   <th>Gender <span class="required">*</span></th>
                                   <th>Age <span class="required">*</span></th>
                                   <th>Birthdate(format:dd/mm/yyyy) <span class="required">*</span></th>
                               </tr>
                           </thead>
                           <tbody>
                               <tr>
                                   <td><input type="radio" id="sex1" name="patient.sex" value="F" class=""> Female&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sex2" name="patient.sex" value="M" class=""> Male</td>
                                   <td><input type="text" autocomplete="off" id="ageregistration" readonly name="patient.age" class="" required></td>
                                   <td><input autocomplete="off" id="datepicker" readonly name="patient.birthdate" class="">&nbsp;&nbsp;Estimated&nbsp;<input type="checkbox" id="patient.birthdateEstimatedInput" name="patient.birthdateEstimatedInput" class=""></td>
                               </tr>
                           </tbody>
                       </table>
                   </td>
              </tr>
              <tr id="parentTableRow">
                   <th valign="top">Address</th>
                   <td>
                     <table id="identifierTable" cellspacing="2">
                         <tbody>
                             <tr>
                                 <td>Address</td><td colspan="3"><input name="personAddress.address1" class="" type="text"></td>
                             </tr>
                             <tr>
                                 <td>Address 2</td><td colspan="3"><input name="personAddress.address2" class="" type="text"></td>
                             </tr>
                             <tr>
                                 <td>City/Village</td><td><input name="personAddress.cityVillage" class="" type="text"></td><td>State/Province</td><td><input name="personAddress.stateProvince" class="" type="text"></td>
                             </tr>
                             <tr>
                                  <td>Country</td><td><input name="personAddress.country" class="" type="text"></td><td>Postal Code</td><td><input name="personAddress.postalCode" class="" type="text"></td>
                              </tr>
                         </tbody>
                     </table>
                   </td>
              </tr>
              <tr id="parentTableRow">
                 <th valign="top">Fingerprint</th>
                 <td>
                     <table id="fingerDetailsTable" cellspacing="2"  border="0">
                         <thead>
                             <tr>
                                 <th>Fingerprint</th>
                                 <th style="display:none;">Scanned Finger <span class="required">*</span></th>
                             </tr>
                         </thead>
                         <tbody>
                             <tr>
                                 <td><img src="${pageContext.request.contextPath}/moduleResources/muzimabiometrics/images/done.png"/><input type="hidden" id="fingerprint" name="patient.fingerprint" autocomplete="false"></td>
                                 <td style="display:none;"><select id="scannedFinger" name="patient.scanned_finger">
                                         <option value="">...</option>
                                         <option value="L1">Left thumb</option>
                                         <option value="L2">Left index finger</option>
                                         <option value="L3">Left middle finger</option>
                                         <option value="L4">Left ring finger</option>
                                         <option value="L5">Left pinky/baby finger</option>
                                         <option value="R1">Right thumb</option>
                                         <option value="R2">Right index finger</option>
                                         <option value="R3">Right middle finger</option>
                                         <option value="R4">Right ring finger</option>
                                         <option value="R5">Right pinky/baby finger</option>
                                      </select>
                                 </td>

                             </tr>
                         </tbody>
                     </table>
                 </td>
              </tr>
            </table>
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
        <div style="background-color:#f9f159 !important;"><B>Fingerprint not identified in the system. Search for patient by identifier/name or create a new patient</B></div>
        <div id="" style="margin-left:0px; ">
            <h3 id="form-title">Patient Search</h3>
            <form id="formData" method="post" action="" style="border: 1px solid #eeeeee;padding:20px;">
                Patient Identifier or Patient Name: <input value="" id="patientSearch" autocomplete="off" placeholder=" " type="text" onkeyup="activate(this.value, event)">

            </form>
            <form method="post" action="">
            <table id="tblSearchResults">
                <thead id="hiddableTHead" style="display:none;">
                <tr class="forms-list-header">
                    <th style="display:none;"></th>
                    <th style="display:none;"></th>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Family Name</th>
                    <th>Identifier(s)</th>
                    <th>Gender</th>
                    <th style="display:none;">Scanned Finger</th>
                    <th>Fingerprint</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            </form>
        </div>
        <h1>OR</h1>
        <h3 id="form-title">Create Patient </h3>
        <form id="formData" method="post" action="" style="border: 1px solid #eeeeee;padding:20px;">
            <p>To create a new person, enter the person's name and other information below first to double-check that
                they don't already have a record in the system. </p>
            <table id="demographicsTable" cellspacing="0" cellpadding="7" width="98%">
                <tbody>
                    <tr>
                        <td>Person Name<span class="required">*</span></td>
                        <td><input autocomplete="off" type="text" id="family_name" name="family_name" class="" value="${identifier}" style="min-width:400px;" required></td>
                    </tr>
                    <tr>
                        <td>Birthdate<span class="required">*</span></td>
                        <td>
                            <table>
                                <tr>
                                    <td>Date: <input type="text" autocomplete="off" id="datepicker1" name="datebirth" class="" ><input type="hidden" id="appendfingerprint"  autocomplete="false"></td>
                                </tr>
                                <tr>
                                    <td>OR</td>
                                </tr>
                                <tr>
                                    <td>Age: Yrs<input type="text" autocomplete="off" id="years" name="years" class="validAge" required>Months: <input type="text" autocomplete="off" id="months" name="months" class="validMonths"><input type="hidden" id="estimatedDOB" name="estimatedDOB" class=""></td>
                                <tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>Gender<span class="required">*</span></td>
                        <td><input id="gender1" type="radio" name="sex" value="F" class="">&nbsp;Female&nbsp;<input type="radio" id="gender2" name="sex" value="M" class="">&nbsp;Male&nbsp;</td>
                    </tr>
                </tbody>
            </table>
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
    $(function() {
        $("#datepicker1").datepicker({
            onSelect: function(dateText, inst) {
                var formatedDate = $(this).val();
                var dateOfBirth = new Date(formatedDate);
                var dateDiff = new Date(new Date() - dateOfBirth);
                var age = (dateDiff.toISOString().slice(0, 4) - 1970);
                var m = dateDiff.getMonth();
                if (m < 0) {
                    age--;
                }
                $("#years").val(age);
                $("#months").val(m);

                $("#estimatedDOB").val("No");
            },
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            maxDate: new Date,
            changeYear: true
        });
    });
</script>
<script>
    $(function() {
        $( "#datepicker" ).datepicker({
            onSelect: function(dateText, inst) {
                var formatedDate = $(this).val();
                var dateOfBirth = new Date(formatedDate);
                var dateDiff = new Date(new Date() - dateOfBirth);
                var age = (dateDiff.toISOString().slice(0, 4) - 1970);
                var m = dateDiff.getMonth();
                if (m < 0) {
                    age--;
                }
                if(age==0 && m==0){
                    $("#ageregistration").val("< 1 month");
                }else if(age==0 && m>0){
                    $("#ageregistration").val(m+" Months");
                }else{
                    $("#ageregistration").val(age+" Yrs "+m+" Months");
                }

            },
            dateFormat: 'yy-mm-dd',
            changeMonth: true,
            maxDate: new Date,
            changeYear: true
        });
    });
    $(document).ready(function(){
    	$("#years").keyup(function() {
    		var date = new Date();
    		var inputYears=$(this).val();
    		var inputMonth = $("#months").val();
    		if(inputYears<120 && !isNaN(inputYears))
    		{
    		    if(!isNaN(inputMonth)) date.setMonth(date.getMonth() - (inputMonth-1));
                date.setFullYear(date.getFullYear() - inputYears);
                var month=date.getMonth();
                if (month.toString().length < 2) month = '0'+month;
                var day = "0"+1;
                var newdate = [date.getFullYear(), month, day].join('-');
    			$("#datepicker1").val(newdate);
    			$("#estimatedDOB").val("Yes");
    		}
    	});
    });

    $(document).ready(function(){
        $("#months").keyup(function() {
            var date = new Date();
            var inputMonths=$(this).val();
            var inputYears=$("#years").val();
            if(inputMonths<12 && !isNaN(inputMonths))
            {
                if(!isNaN(inputYears)) date.setFullYear(date.getFullYear() - inputYears);
                date.setMonth(date.getMonth() - (inputMonths-1));
                var year = date.getFullYear()
                var month = date.getMonth();
                if(parseInt(month)==0) {
                    month = 12;
                    year = year-1;
                }
                if(month.toString().length < 2) month = '0'+month;
                var day = "0"+1;
                var newdate = [year, month, day].join('-');
                $("#datepicker1").val(newdate);
                $("#estimatedDOB").val("Yes");
            }
        });
    });
</script>
