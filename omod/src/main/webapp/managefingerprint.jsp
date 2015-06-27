<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/font-awesome/css/font-awesome.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-1.10.1.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-ui-1.10.4.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.validate.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/underscore/underscore-min.js"/>

<div>
<div class="navbar navbar-custom">
    <div>
        <a href="#"><i class="icon-home"></i> Muzima Fingerprint Module</a>
    </div>
</div>
<div id = "applet">
    <applet name="Muzima fingerprint module" id="Abis" code="org.openmrs.module.muzimafingerPrint.SimpleFingersApplication"  width="100%"  height="100">
        <param name="jnlp_href" value="/openmrs/moduleResources/muzimafingerPrint/fingerprint.jnlp" />
        <param name="codebase_lookup" value="false" />
        <param name="separate_jvm" value="true" />
        <param name="server_address" value="/local" />
        <param name="server_port" value="5000" />
    </applet>
</div>
<div id="body-wrapper" class="test-page">
    <div class = "row forms-list">
        <div class ="col-lg-12" >

            <table  id="tblData">
                <thead >
                <tr class = "forms-header">
                    <th>Patient List</th>
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
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>
<div id = "registrationSection">
<div id = "updatePatient" class = "button-loc">
    <input id = "btnUpdatePatient" type="button" value= "Update Selected patient">
    <input id = "btnCancel" class = "doCancel" type="button" value= "Cancel">
</div>
<div id = "otherIdentificationOption">
	<h4>No Patient found with this fingerprint, Do you want to search Patient by other identifier?</h4>
	<a href="#" id="btnYes">Yes</a>
	<a href="#" id="btnNo">No</a>
</div>
<div id = "otherIdentifiers">
    <h4>Please select other Identifier Type and respective value</h4>
    <form id = "IdentifierForm" method = "post" action = "">
        <fieldset  name="Identifier">
            <div class="form-group">
                <label for= "identifier_id">Patient Identifier Type</label>
                <select id = "IdentifierOptions" name="identifier_id">
                    <option  value="">...</option>
                </select>
                <label for= "identifier_value">Identifier Value</label>
                <input autocomplete="off" type="text" name="identifier_value">
            </div>
        </fieldset>
        <br/>
    </form>
    <input id = "btnByIdentifier" type="button" value= "Identify patient">
    <input id = "btnCancel" class = "doCancel" type="button" value= "Cancel">
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
				<img src ="/openmrs/moduleResources/muzimafingerPrint/images/done.png"/>
				<input id = "fingerprint" autocomplete="off" type="hidden" name="fingerprint">

				<label for= "birth_date">Date of Birth(Format: MM/dd/yyyy)</label>
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

<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/Custom.js"/>
