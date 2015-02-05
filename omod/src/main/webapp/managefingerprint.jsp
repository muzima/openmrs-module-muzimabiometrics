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
<div id="body-wrapper" class="test-page">
    <div class = "row forms-list">
        <div class ="col-lg-12" >

            <table>
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
                <tr>
                    <td id = 'paitentId'>
                        <a href=""></a>
                    </td>
                    <td id = 'GivenN'></td>
                    <td id = 'FamilyN'></td>
                    <td id = 'Gender'></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div id = "registrationForm">
<form autocomplete="off" id="formData" data-edited="false">
<h3 id="form-title">Registration Form</h3>
<fieldset name="patient">
    <h4>
        <span lang="" class=" active">Demographics</span>
    </h4>
    <label class="">
        <span lang="" class=" active">Family Name</span>
        <span class="required">*</span>
        <br>
        <input autocomplete="off" type="text" name="patient.family_name" required="required" data-type-xml="string">
        <span class="" lang="">Value not allowed</span>
        <span class="" lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Given Name</span>
        <span class="required">*</span>
        <br>
        <input autocomplete="off" type="text" name="patient.given_name" required="required"
               data-type-xml="string">
        <span class="active" lang="">Value not allowed</span>
        <span class="" lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Middle Name</span>
        <br><input autocomplete="off" type="text" name="patient.middle_name" data-type-xml="string">
        <span class="active" lang="">Value not allowed</span>
        <span class="active" lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Sex</span>
        <span class="required">*</span>
        <br><select name="patient.sex" required="required" data-type-xml="string">
        <option value="">...</option>
        <option value="M">Male</option>
        <option value="F">Female</option>
    </select>
        <span class="" style="display:none;"></span>
        <span class="active" lang="">Value not allowed</span>
        <span class="" lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Phone Number</span>
        <br>
        <input autocomplete="off" type="text" name="patient.phone_number"
               data-constraint="regex(., '^\d{8,12}$')" data-type-xml="string">
        <span class="active" lang="">Value not allowed</span>
        <span class=" active" lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Mothers Name</span>
        <br><input autocomplete="off" type="text" name="person_attribute4" data-type-xml="string">
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">AMRS Universal ID Assigned</span>
        <span class="required">*</span>
        <br><input type="button" class="barcode_img"><input autocomplete="off" type="barcode"
                                                            name="patient.amrs_id" required="required"
                                                            data-constraint="regex(., '^\w+[-]?\d{1}') and (checkdigit(.))"
                                                            data-type-xml="barcode">
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
    <label class="">
        <span lang="" class=" active">Finger Print</span>
        <br>
        <input type="button" class="fingerprint_img">
        <input id = "fingerprint"  name="patient.fingerprint" >
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
</fieldset>
<!--end of group
      -->
<fieldset class=" ">
    <h4>
        <span lang="" class=" active">Identifier</span>
    </h4>
    <fieldset class=" " name="other_identifier_type_group"><span class="repeat-number"></span>
        <label class="">
            <span lang="" class=" active">Identifier</span>
            <span class="required">*</span>
            <br><select name="other_identifier_type_group/other_identifier_type" required="required"
                        data-type-xml="string">
            <option value="">...</option>
            <option value="ampath_medical_record_number">AMRS Medical Record Number</option>
            <option value="ccc_number">CCC Number</option>
            <option value="hct_id">HCT ID</option>
            <option value="kenyan_national_id_number">KENYAN NATIONAL ID NUMBER</option>
            <option value="mtct_plus_id">MTCT Plus ID</option>
            <option value="mtrh_hospital_number">MTRH Hospital Number</option>
            <option value="old_ampath_medical_record_number">Old AMPATH Medical Record Number</option>
            <option value="pmtct_id">pMTCT ID</option>
        </select>
               <span class="" style="display:none;">
                </span>
            <span class=" active" lang="">Value not allowed</span><span class=" active" lang="">This field is required</span></label>
        <label class="  disabled" style="">
            <span lang="" class=" active">Enter Identifier</span>
            <span class="required">*</span>
            <br><input autocomplete="off" type="text"
                       name="other_identifier_type_group/other_identifier_value" required="required"
                       data-constraint="regex(., '^\w+[-]?\d{1}') and (checkdigit(.))"
                       data-relevant="other_identifier_type_group/other_identifier_type='hct_id'   or other_identifier_type_group/other_identifier_type='pmtct_id'   or other_identifier_type_group/other_identifier_type='ampath_medical_record_number'"
                       data-type-xml="string" disabled="">
            <span class=" active" lang="">Value not allowed</span><span class=" active" lang="">This field is required</span></label>
        <label class="" style="display: block;">
            <span lang="" class=" active">Enter Identifier</span>
            <span class="required">*</span>
            <br><input autocomplete="off" type="text"
                       name="other_identifier_type_group/no_checkdigit_id_value" required="required"
                       data-relevant="other_identifier_type_group/other_identifier_type!='hct_id'       and other_identifier_type_group/other_identifier_type!='pmtct_id'              and other_identifier_type_group/other_identifier_type!='ampath_medical_record_number'"
                       data-type-xml="string">
            <span class=" active" lang="">Value not allowed</span><span class=" active" lang="">This field is required</span></label>
        <label class="" style="display: block;">
            <span lang="" class=" active">Confirm Identifier</span>
            <span class="required">*</span>
            <br><input autocomplete="off" type="text"
                       name="other_identifier_type_group/confirm_identifier_value" required="required"
                       data-constraint=". =other_identifier_type_group/no_checkdigit_id_value"
                       data-relevant="other_identifier_type_group/other_identifier_type!='hct_id'    and other_identifier_type_group/other_identifier_type!='pmtct_id'    and other_identifier_type_group/other_identifier_type!='ampath_medical_record_number'"
                       data-type-xml="string">
            <span class=" active" lang="">Value not allowed</span><span class=" active" lang="">This field is required</span></label>
        <button type="button" class="btn repeat"><i class="icon-plus"></i></button>
        <button type="button" disabled="" class="btn remove"><i class="icon-minus"></i></button>
    </fieldset>
    <!--end of repeat fieldset with name
       other_identifier_type_group-->
</fieldset>
<!--end of group
      -->
<fieldset class="">
    <label class="">
        <span lang="" class=" active">Will Record Date of Birth...</span>
        <span class="required">*</span>
        <br><select name="patient.birthdate_type" required="required" data-type-xml="string">
        <option value="">...</option>
        <option value="birthdate">By Birth-Date</option>
        <option value="age">By Age</option>
    </select>
            <span class="" style="display:none;">
                </span>
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
    <label class="  disabled" style="">
        <span lang="" class=" active">How old are you now (in years)?</span>
        <span class="required">*</span>
        <br><input autocomplete="off" type="number
            " name="patient.age" required="required" data-constraint=". >= 0 and . <=125"
                   data-relevant="../birthdate_type = 'age'" data-type-xml="int" disabled="">
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
    <label class="" style="display: block;">
        <span lang="" class=" active">Select Birthdate</span>
        <span class="required">*</span>
        <br><input autocomplete="off" type="date" name="patient.birth_date" required="required" data-type-xml="date" style="display: none;">

        <div class="widget input-append date">
            <input class="ignore input-small" type="text" value="" placeholder="yyyy-mm-dd"><span class="add-on"><i
                class="icon-calendar"></i></span></div>
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
    <label class="" style="display: block;">
        <span lang="" class=" active">Is this birthdate an estimate?</span>
        <span class="required">*</span>
        <br><select name="patient.birthdate_estimated" required="required" data-relevant="../birthdate_type = 'birthdate'" data-type-xml="string">
        <option value="">...</option>
        <option value="true">Yes</option>
        <option value="false">No</option>
    </select>
            <span class="" style="display:none;">
                </span>
        <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                    lang="">This field is required</span></label>
</fieldset>
<!--end of group
      -->
<label class="">
    <span lang="" class=" active">Encounter Location</span>
    <span class="required">*</span>
    <br><select name="patient.location_id" required="required" data-type-xml="string">
    <option value="">...</option>
    <option value="84">AMPATH-MTRH</option>
    <option value="19">Busia</option>
    <option value="7">Chulaimbo</option>
    <option value="17">Iten</option>
    <option value="11">Kitale</option>
    <option value="2">Mosoriot Health Centre</option>
    <option value="20">Port Victorial</option>
    <option value="12">Teso District Hospital</option>
    <option value="3">Turbo Health Centre</option>
    <option value="8">Webuye</option>
</select>
         <span class="" style="display:none;">
                </span>
    <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                lang="">This field is required</span></label>
<label class="">
    <span lang="" class=" active">Provider ID</span>
    <span class="required">*</span>
    <br><select name="patient.provider_id" required="required" data-type-xml="select1">
    <option value="">...</option>
    <option value="1">Super User</option>
</select>
         <span class="" style="display:none;">
                </span>
    <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                lang="">This field is required</span></label>
<label class="">
    <span lang="" class=" active">Encounter Date</span>
    <span class="required">*</span>
    <br><input autocomplete="off" type="date" name="patient.encounter_datetime" required="required" data-constraint=". < (today() + 1)" data-type-xml="date" style="display: none;">

    <div class="widget input-append date"><input class="ignore input-small" type="text" value=""
                                                 placeholder="yyyy-mm-dd"><span class="add-on"><i class="icon-calendar"></i></span></div>
    <span class=" active" lang="">Value not allowed</span><span class=" active"
                                                                lang="">This field is required</span></label>

</form>
<input type="button" value="Create Patient" class="form-button" onclick="submit(this)">
</div>
</div>


<script>
    function showMessage() {
        return "This is from javascript";
    };

    function GetAllPatient(){
        var xmlhttp;
        var data = 'no data found';
        if (window.XMLHttpRequest){

             xmlhttp=new XMLHttpRequest();
          }
        else{

            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
          }
        xmlhttp.onreadystatechange=function(){

              if (xmlhttp.readyState==4 && xmlhttp.status==200)
                {
                     data = xmlhttp.responseText;
                }
         }
            xmlhttp.open("POST","fingerprint/identify.form",false);
            xmlhttp.send();
            return data;
        };

    function updatePatientList(id, GivenName, FamilyName, Gender){

         var tdID = document.getElementById("paitentId");
         tdID.innerHTML = id;
         var tdGName = document.getElementById("GivenN");
         tdGName.innerHTML = GivenName;
         var tdFName = document.getElementById("FamilyN");
         tdFName.innerHTML = FamilyName;
         var tdGender = document.getElementById("Gender");
         tdGender.innerHTML = Gender;
         return;
    };
    function RegisterPatient(fingerprint){
            var elm_reg = document.getElementById('fingerprint');
            elm_reg.value = fingerprint;
            showRegistration('true');
        };

    function showRegistration(status){
        if(status == 'true'){
            var elm_reg = document.getElementById("registrationForm");
            elm_reg.style.display = 'block';
            var elm_list = document.getElementById("body-wrapper");
            elm_list.style.display = 'none';
        }
        else{
            var elm_reg = document.getElementById("registrationForm");
            elm_reg.style.display = 'none';
            var elm_list = document.getElementById("body-wrapper");
            elm_list.style.display = 'block';
        }
    };

    var pushIntoArray = function (object, key, value) {
            if (JSON.stringify(value) == '{}' || value == "") {
                return object;
            }
            if (object[key] !== undefined) {
                if (!object[key].push) {
                    object[key] = [object[key]];
                }
                object[key].push(value || '');
            } else {
                object[key] = value || '';
            }
            return object;
        };

     var serializeNonConceptElements = function ($form) {
            var object = {};
            var $inputElements = $form.find('[name]').not('[data-concept]');
            $.each($inputElements, function (i, element) {
                if ($(element).is(':checkbox') || $(element).is(':radio')) {
                    if ($(element).is(':checked')) {
                        object = pushIntoArray(object, $(element).attr('name'), $(element).val());
                    }
                } else {
                    object = pushIntoArray(object, $(element).attr('name'), $(element).val());
                }
            });
            return object;
        };
     $.fn.serializeEncounterForm = function () {
             var jsonResult = $.extend({}, serializeNonConceptElements(this));
             var completeObject = {};
             var defaultKey = "observation";
             $.each(jsonResult, function (k, v) {
                 var key = defaultKey;
                 var dotIndex = k.indexOf(".");
                 if (dotIndex >= 0) {
                     key = k.substr(0, k.indexOf("."));
                 }
                 var objects = completeObject[key];
                 if (objects === undefined) {
                     objects = {};
                     completeObject[key] = objects;
                 }
                 objects[k] = v;
             });
             return completeObject;
         };

   var submit = function(form){
        var jsonData = JSON.stringify($('form').serializeEncounterForm());
        alert(jsonData);

        $.ajax(
           {
                url: "fingerprint/register.form",
                type: "POST",
                data: jsonData,
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                success: function(msg) {
                    alert(msg);
                }
            }
        );
   };
   showRegistration('false');
   var attributes = {code:'HelloWorld',  width:0, height:0} ;
   var parameters = {jnlp_href: '/openmrs-standalone/moduleResources/muzimafingerPrint/fingerprint.jnlp'} ;
   deployJava.runApplet(attributes, parameters, '1.6');

</script>
