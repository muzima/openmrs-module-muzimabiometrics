<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/openmrs2.0/index.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/openmrs2.0/openmrs.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/font-awesome/css/font-awesome.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-1.10.1.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-ui-1.10.4.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.validate.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/underscore/underscore-min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/deployJava.js"/>

<div>
<br>
<br>
<header id="style-guide-header">
    <div  class="col-lg-8">
        <h2><i class="icon-home"></i> Muzima Fingerprint Module</h2>
    </div>
</header>
<div id = "applet">
    <script>
        var attributes = {code:'Muzima fingerprint module',  width:"100%", height:"100"} ;
        var parameters = {jnlp_href: '/openmrs-standalone/moduleResources/muzimafingerPrint/fingerprint.jnlp'} ;
        deployJava.runApplet(attributes, parameters, '1.6');
    </script>
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
                    <th>Given</th>
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
<br>
<div id = "registrationSection">
<div id = "updatePatient">
    <input id = "btnUpdatePatient" type="button" value= "Update Selected patient">
    <input id = "btnCancel" type="button" value= "Cancel">
</div>
<br>
<div id = "otherIdentificationOption">
	<h4>Do you want to search Patient by other identifier?</h4>
	<a href="#" id="btnYes">Yes</a>
	<a href="#" id="btnNo">No</a>
</div>
<br>
<div id = "otherIdentifiers">
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
    <input id = "btnCancel" type="button" value= "Cancel">
</div>
<div id = "registrationForm">
<form id = "formData" method = "post" action = "">
		<h3 id="form-title">Registration Form</h3>
		<fieldset  name="patient">
			<div class="form-group">
				<label for= "family_name">Family Name</label>
				<input autocomplete="off" type="text" name="family_name">

				<label for= "given_name">Given Name</label>
				<input autocomplete="off" type="text" name="given_name">

				<label for= "middle_name">Middle Name</label>
				<input autocomplete="off" type="text" name="middle_name">

				<label for= "sex">Sex</label>
				<select name="sex">
			        <option value="">...</option>
			        <option value="M">Male</option>
			        <option value="F">Female</option>
			    </select>

				<label for= "phone_number">Phone Number</label>
				<input autocomplete="off" type="text" name="phone_number">

				<label for= "mothers_name">Mothers Name</label>
				<input autocomplete="off" type="text" name="mothers_name">

				<label for= "amrs_id">AMRS Universal ID Assigned</label>
				<input autocomplete="off" type="text" name="amrs_id">

				<label for= "fingerprint">Finger Print</label>
				<input id = "fingerprint" autocomplete="off" type="text" name="fingerprint">

				<label for= "birth_date">Date of Birth</label>
				<input autocomplete="off" type="date" name="birth_date">

				<label for= "location_id">Encounter Location</label>
				<select id = "LocationOptions" name="location_id">
				    <option  value="">...</option>
				</select>

				<label for= "provider_id">Provider ID</label>
				<select name="provider_id" id = "ProviderOptions">
				    <option value="">...</option>

				</select>

				<label for= "encounter_datetime">Encounter Date</label>
				<input autocomplete="off" type="date" name="encounter_datetime">

			</div>
		</fieldset>
	</form>
	<br/>
      <input id = "btnCreatePatient" type="button" value= "create patient">
</div>
</div>
</div>

<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/Custom.js"/>
