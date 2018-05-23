var _FINGERPRINT_DATA = "";
$("#enrollFingerprint").hide();
$("#addFingerprints").hide();
$("#signinscreen").show();

function getMinimumFingerPrintQuality(){
   var quality = 70;
   $.ajax({
       url: "fingerprint/getMinimumFingerprintQuality.form",
       type: "POST",
       contentType: 'application/json',
       dataType: 'text',
       async: false,
       success: function(result){
           quality = result;
       },
       error: function(msg){
           console.log("Internal server Error while getting setting"+JSON.stringify(msg));
       }
   });

   return quality;
}

function updateIdentificationStatus(patientUuid){
    if(patientUuid=="PATIENT_NOT_FOUND"){
        updateControls(3);
    }else {
        _PATIENT_UUID = patientUuid;
        $.ajax({
            url: "fingerprint/getPatient.form",
            type: "POST",
            data: _PATIENT_UUID,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
                //console.log(msg);
                $("#signinscreen").hide();
                updatePatientListTable(msg, 1);
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
                updateControls(8);
            }

        });
    }
};

function identifyPatient(fingerprintData){
    _FINGERPRINT_DATA = fingerprintData;
    var identifiedPatient = 'no data found';
    var fingerprint = document.getElementById('fingerprint');
    fingerprint.value = _FINGERPRINT_DATA;
    var appendFingerprint = document.getElementById('appendfingerprint');
    appendFingerprint.value = _FINGERPRINT_DATA;
    var tblStoreAppendfingerprint = document.getElementById('tblStoreAppendfingerprint');
    tblStoreAppendfingerprint.value = _FINGERPRINT_DATA;
    var tblDataAppendfingerprint = document.getElementById('tblDataAppendfingerprint');
    tblDataAppendfingerprint.value = _FINGERPRINT_DATA;

    $.ajax({
        url: "fingerprint/identifyPatient.form",
        type: "POST",
        data: _FINGERPRINT_DATA,
        contentType: 'application/json',
        dataType: 'json',
        async: false,
        success: function(msg) {
            console.log(msg);
            identifiedPatient = JSON.stringify(msg);
        },
        error: function(msg, status, error){
            console.log("server error +++++++++++++"+JSON.stringify(error));
            updateControls(3);
        }

    });
    return identifiedPatient;
};

function registerPatient(fingerprintData){
        var fingerprint = document.getElementById('fingerprint');
        fingerprint.value = fingerprintData;
        updateControls(3);
};

function appendFingerPrint(fingerPrint,patientUUID,scannedFinger) {
    _PatientUuid = patientUUID;
    _FINGERPRINT_DATA = fingerPrint;
    _scannedFinger = scannedFinger;
    var jsonData = "{patient: {patientUuid : '"+_PatientUuid+"' , fingerPrint :'"+ _FINGERPRINT_DATA+"' , scannedFinger :'"+ _scannedFinger+"'}}";
    $.ajax({
        url: "fingerprint/appendFingerPrint.form",
        type: "POST",
        data: jsonData,
        contentType: 'application/json',
        dataType: 'json',
        async: false,
        success: function(msg) {
            $("#signinscreen").hide();
            $("#body-wrapperr").hide();
            $("#body-wrapper").hide();
            $("#addFingerprints").hide();
            //update
            $("#enrollFingerprint").show();
            $("#basicdemographicform").hide();
            $("#registeredWell").hide();
            $("#unregisteredscantext").hide();
            $("#scanwaittext").hide();
            $("#appendWell").slideDown("slow");
            //end update
        },
        error: function(msg, status, error){
            console.log("server error +++++++++++++"+JSON.stringify(error));
            updateControls(8);
        }

    });

}

function updatePersonListTable(Patients, updateControlsStatus){
    updateControls(updateControlsStatus);
    $("#tblStore tbody tr").remove();
    var identifiers;
    Patients.forEach( function (patient){
        identifiers=patient.identifiers.replace(/[\[\]']+/g,'');
        $("#tblStore tbody").append( "<tr>"
            + "<td style='display:none;'>"+patient.patientUUID+"</td>"
            + "<td style='display:none;'>"+patient.id+"</td>"
            + "<td><a href='"+openmrsContextPath+"/patientDashboard.form?patientId="+patient.id+"'>"+patient.id+" </a></td>"
            + "<td>"+patient.givenName +"</td>"
            + "<td>"+ patient.familyName+"</td>"
            + "<td>"+ identifiers+"</td>"
            + "<td>"+ patient.gender+"</td>"
            + "<td>"+showScannedFinger(patient.fingerprintTemplate,patient.scannedFinger,patient.id)+"</td>"
            + "<td>"+show(patient.fingerprintTemplate,patient.patientUUID)+"</td>"
            + "</tr>");
    });
};
//function to handle create data
function updatePatientListTable(Patients, updateControlsStatus){
    updateControls(updateControlsStatus);
    $("#tblData tbody tr").remove();
    var identifiers;
    Patients.forEach( function (patient){
        identifiers=patient.identifiers.replace(/[\[\]']+/g,'');
        $("#tblData tbody").append( "<tr>"
            + "<td style='display:none;'>"+patient.patientUUID+"</td>"
            + "<td style='display:none;'>"+patient.id+"</td>"
            + "<td><a href='"+openmrsContextPath+"/patientDashboard.form?patientId="+patient.id+"'>"+patient.id+" </a></td>"
            + "<td>"+patient.givenName +"</td>"
            + "<td>"+ patient.familyName+"</td>"
            + "<td>"+ identifiers+"</td>"
            + "<td>"+ patient.gender+"</td>"
            + "<td>"+show(patient.fingerprintTemplate,patient.patientUUID)+"</td>"
            + "</tr>");
    });
};

function updatePatientList(Patients){
    $("#tblSearchResults tbody tr").remove();
    $("#hiddableTHead").show();
    var identifiers;
    Patients.forEach( function (patient){
        identifiers=patient.identifiers.replace(/[\[\]']+/g,'');
        $("#tblSearchResults tbody").append( "<tr>"
            + "<td style='display:none;'>"+patient.patientUUID+"</td>"
            + "<td style='display:none;'>"+patient.id+"</td>"
            + "<td><a href='"+openmrsContextPath+"/patientDashboard.form?patientId="+patient.id+"'>"+patient.id+" </a></td>"
            + "<td>"+patient.givenName +"</td>"
            + "<td>"+ patient.familyName+"</td>"
            + "<td>"+ identifiers+"</td>"
            + "<td>"+ patient.gender+"</td>"
            + "<td>"+showScannedFinger(patient.fingerprintTemplate,patient.scannedFinger,patient.id)+"</td>"
            + "<td>"+show(patient.fingerprintTemplate,patient.patientUUID)+"</td>"
            + "</tr>");
    });
};

function clearTable(){
     $("#tblSearchResults tbody tr").remove();
     $("#hiddableTHead").hide();
}

function activate(val, e){
    var key=e.keyCode || e.which;
    if(parseInt(key)==13){
    //do nothing
    }
    else{
        $.ajax({
            type: "POST",
            url: "fingerprint/findPatients.form",
            contentType: "application/json",
            data: val,
            dataType: 'json',
            success: function (result) {
                //console.log(result);
                if(result.length == 0) {
                   // $("#signinscreen").hide();
                  //  updateControls(4);
                  clearTable();
                }
                else{
                    updatePatientList(result);
                    console.log("patientsearch is "+JSON.stringify(result));
                }
            },
            error: function(msg, status, error){
                clearTable();
                console.log("server error+++++++++++++++++++"+JSON.stringify(msg));
            }
        });
    }
}

function show(fingerprint, patientUuid){
    if(fingerprint!==null) {
        return "<img src ='"+openmrsContextPath+"/moduleResources/muzimabiometrics/images/done.png'/>"
    }
    else {
        return "<button type='button' >Append Fingerprint</button>"
    }
};

function showScannedFinger(fingerprint, scannedFinger,patientId){
    //console.log("This is the scanned Finger "+scannedFinger);
    var fingerCode = "";
    $.ajax({
        url: "fingerprint/getDefaultFinger.form",
        type: "POST",
        contentType: 'application/json',
        dataType: 'text',
        async: false,
        success: function(result){
            fingerCode = result.charAt(0).toUpperCase() + result.slice(1);
        },
        error: function(msg){
            console.log("Internal server Error while getting setting"+JSON.stringify(msg));
            return null;
        }
    });

    if(scannedFinger!==null){
         var finger = "";
         if(scannedFinger == "L1"){
              finger = "Left thumb";
         }else if(scannedFinger == "L2"){
              finger = "Left index finger";
         }else if(scannedFinger == "L3"){
              finger = "Left middle finger";
         }else if(scannedFinger == "L4"){
              finger = "Left ring finger";
         }else if(scannedFinger == "L5"){
              finger = "Left pinky/baby finger";
         }else if(scannedFinger == "R1"){
              finger = "Right thumb";
         }else if(scannedFinger == "R2"){
              finger = "Right index finger";
         }else if(scannedFinger == "R3"){
              finger = "Right middle finger";
         }else if(scannedFinger == "R4"){
              finger = "Right ring finger";
         }else if(scannedFinger == "R5"){
              finger = "Right pinky/baby finger";
         }
         return finger;
    }else{
         //console.log("Final Finger Code "+fingerCode);
         var optionL5 = '<option value="L5">Left pinky/baby finger</option>';
         var optionL1 ='<option value="L1">Left thumb</option>';
         var optionL2 ='<option value="L2">Left index finger</option>';
         var optionL3 ='<option value="L3">Left middle finger</option>';
         var optionL4 ='<option value="L4">Left ring finger</option>';
         var optionR1 ='<option value="R1">Right thumb</option>';
         var optionR2 ='<option value="R2">Right index finger</option>';
         var optionR3 ='<option value="R3">Right middle finger</option>';
         var optionR4 ='<option value="R4">Right ring finger</option>';
         var optionR5 ='<option value="R5">Right pinky/baby finger</option>';

         if(fingerCode==="L1"){
             optionL1 ='<option value="L1" selected>Left thumb</option>';
         }else if(fingerCode==="L2"){
             optionL2 ='<option value="L2" selected>Left index finger</option>';
         }else if(fingerCode==="L3"){
             optionL3 ='<option value="L3" selected>Left middle finger</option>';
         }else if(fingerCode==="L4"){
             optionL4 ='<option value="L4" selected>Left ring finger</option>';
         }else if(fingerCode==="L5"){
             optionL5 = '<option value="L5" selected>Left pinky/baby finger</option>';
         }else if(fingerCode==="R1"){
             optionL1 ='<option value="R1" selected>Right thumb</option>';
         }else if(fingerCode==="R2"){
             optionL2 ='<option value="R2" selected>Right index finger</option>';
         }else if(fingerCode==="R3"){
             optionL3 ='<option value="R3" selected>Right middle finger</option>';
         }else if(fingerCode==="R4"){
             optionL4 ='<option value="R4" selected>Right ring finger</option>';
         }else if(fingerCode==="R5"){
             optionR5 = '<option value="R5" selected>Right pinky/baby finger</option>';
         }


         return '<select class="patientList" id=patientid'+patientId+' name="patient.scanned_finger" required>'+
         ''+optionL1+
         ''+optionL2+
         ''+optionL3+
         ''+optionL4+
         ''+optionL5+
         ''+optionR1+
         ''+optionR2+
         ''+optionR3+
         ''+optionR4+
         ''+optionR5+
         '</select>';
    }
};

function addFPrint(patientUuid){
    window.location = openmrsContextPath+'/module/muzimabiometrics/patientEdit.form?patientUuid='+patientUuid;
};


function updateControls(status){
    if(status==0){
        //Hiding all the section loading time - case0
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#enrollFingerprint').hide();
    } else if(status ==1){
        //Show patient found for scan process - case1
        $('#body-wrapper').show();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#basicdemographicform').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    } else if(status ==2){
        //Show other option i.e. to register - case2
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').show();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    } else if(status ==3){
        //Show registration section - case3
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#basicdemographicform').show();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    } else if(status ==4){
        //No patient found with search by name or identifier - case4
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').show();
        $('#patientCreated').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    } else if(status ==5){
        //Show created patient - case5
        $('#body-wrapper').show();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    } else if(status ==6){
        //Show patient found for scan process - case6
        $('#body-wrapperr').show();
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#basicdemographicform').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    }else if(status ==7){
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').show();
        $('#basicdemographicform').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#enrollFingerprint').hide();
        $('#signinscreen').hide();
    }else if(status ==8){
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#basicdemographicform').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
        $('#enrollFingerprint').show();
        $('#signinscreen').hide();
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
    var defaultKey = "patient";
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


$(function(){
    $.ajax({
            url: "fingerprint/getDefaultFinger.form",
            type: "POST",
            contentType: 'application/json',
            dataType: 'text',
            async: false,
            success: function(result){
                var fingerCode = result.charAt(0).toUpperCase() + result.slice(1);
                var finger = "";
                if(fingerCode == "L1"){
                     finger = "left thumb";
                }else if(fingerCode == "L2"){
                     finger = "left index finger";
                }else if(fingerCode == "L3"){
                     finger = "left middle finger";
                }else if(fingerCode == "L4"){
                     finger = "left ring finger";
                }else if(fingerCode == "L5"){
                     finger = "left pinky/baby finger";
                }else if(fingerCode == "R1"){
                     finger = "right thumb";
                }else if(fingerCode == "R2"){
                     finger = "right index finger";
                }else if(fingerCode == "R3"){
                     finger = "right middle finger";
                }else if(fingerCode == "R4"){
                     finger = "right ring finger";
                }else if(fingerCode == "R5"){
                     finger = "right pinky/baby finger";
                }else{
                     finger = "finger";
                }
                $("#scannedFinger").val(fingerCode);
                $("#defaultFingerDiv").append("Please place your "+finger+" on scanner");
            },
            error: function(msg){
                $("#defaultFingerDiv").append("Please place your finger on scanner");
                console.log("Internal server Error while getting setting"+JSON.stringify(msg));
            }
    });

    $.ajax({
            url: "../../ws/rest/v1/location",
            type: "GET",
            async: true,
            success: function(result) {

                var options = $("#LocationOptions");
                $.each(result.results, function(val, text) {
                    options.append($("<option />").val(text.uuid).text(text.display));
                });
            },
            error: function(msg){
                alert("Internal server error : while getting location list ");
            }
        }
    );

    $.get( "../../ws/rest/v1/user", function(result) {
        var options = $("#ProviderOptions");
        $.each(result.results,  function(val, text) {
            options.append($("<option />").val(text.uuid).text(text.display));
        });

    }).fail(function() {
        alert("Internal server error : while getting provider list ");
        });

    $.ajax({
            url: "../../ws/rest/v1/patientidentifiertype?v=full",
            type: "GET",
            async: true,
            success: function(result) {

                var options = $("#IdentifierOptions");
                $.each(result.results, function(val, text) {

                    options.append($("<option class='"+text.validator+"' />").val(text.uuid).text(text.display));
                });
            },
            error: function(msg){
                alert("Internal server error : while getting Identifier list ");
            }
        }
    );

    $(".identifierType").change(function(){
        var _identifierTypeUuid = $(this).val();
        var $element=$(this);
        $.ajax({
            url: "../../ws/rest/v1/patientidentifiertype?v=full",
            type: "GET",
            async: true,
            success: function(result) {

                var identifierValueElement = $element.closest('.repeat').find('.identifier-value');
                $(identifierValueElement).attr("class","identifier-value");
                $.each(result.results, function(val, text) {
                    if(text.uuid == _identifierTypeUuid){
                        $(identifierValueElement).addClass(text.validator);
                    }
                });
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
            }
        });
    });

    $("#btnByIdentifier").click(function(){
        var jsonData = JSON.stringify($('#IdentifierForm').serializeEncounterForm());
        //console.log(jsonData);
        $.ajax({
            url: "fingerprint/identifyPatientByOtherIdentifier.form",
            type: "POST",
            data: jsonData,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
               // console.log(msg);
                updatePatientListTable(msg, 1);
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
            }
        });

    });
    $("#btnYes").click(function(){
        updateControls(3);
    });

	function enrollFingerprint(){
		updateControls(3);
	}

    $("#btnNo").click(function(){
        updateControls(0);
    });
    $("#Yes").click(function(){
        var patientUuid = $('#uid').val();
        var jsonData = "{patient: {patientUUID : '"+patientUuid+"' , fingerprint :'"+ _FINGERPRINT_DATA+"'}}";
       // console.log(jsonData);
        $.ajax({
            url: "fingerprint/addFingerprint.form",
            type: "POST",
            data: jsonData,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
               // console.log(msg);
                window.location = openmrsContextPath+'/module/muzimabiometrics/managefingerprint.form?patientUuid='+patientUuid;
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
            }
        });
    });
    $("#No").click(function(){
        updateControls(0);
        window.location = openmrsContextPath+'/module/muzimabiometrics/managefingerprint.form';
    });
    $(".doCancel").click(function(){
        updateControls(3);
    });
    $("#btnUpdatePatient").click(function(){
        var selectedPatientId = $('input[name=selectedPatient]:checked').val();
        var jsonData = {};
        jsonData.patient = {
            patientUUID: selectedPatientId,
            fingerPrint: _FINGERPRINT_DATA
        };
        $.ajax({
            url: "fingerprint/UpdatePatientWithFingerprint.form",
            type: "POST",
            data: jsonData,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
                alert("Patient updated!");
                updateControls(0);
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
            }
        });

    });

    $("#btnCreatePatient").click(function(){
        if($("#formData").valid()){
         $.ajax({
            url:"../../ws/rest/v1/patient?q="+$("#amrs_id").val(),
            type:"GET",
            contentType: 'application/json',
            async:true,
            success:function(data){
              if(data.results.length>0){

                $('#patient-exists').modal('show');
              }
              else{
                var jsonData = JSON.stringify($('#formData').serializeEncounterForm());
                $.ajax({
                    url: "fingerprint/enrollPatient.form",
                    type: "POST",
                    data: jsonData,
                    contentType: 'application/json',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        //console.log("new Patient data "+JSON.stringify(msg));
                        $('#formData').trigger("reset");
                        $("#registrationForm").hide();
                        $("#enrollFingerprint").show();
                        $("#unregisteredscantext").hide();
                        $("#scanwaittext").hide();
                        $("#registeredWell").show();
                    },
                    error: function(msg, status, error){
                        //var logging = document.getElementById("logging");
                       // logging.value = "There is an error in success "+JSON.stringify(error)+" == "+msg+" ++ "+status;
                        console.log("server error +++++++++++++"+JSON.stringify(error));
                        console.log("the error message is "+msg);
                        console.log("the status is "+status);
                        console.log("the error logged is "+error);
                    }
                });
              }
            },
            error:function(error){
              console.log("patient by identifier error +++++++++++++++++++++++++++"+JSON.stringify(error));
            }
          });
        }
        else
        {
            var logging = document.getElementById("logging");
            logging.value = "There is an error ";
        }
    });

//search basic demographics
$(document).ready(function(){
    $("#btnSearchPatient").click(function(){
        //check if forms are field
        if($("#family_name").val() == "")
        {
            $("#family_name").attr("placeholder", "This field cannot be empty, please enter the person name");
        }
        else if($("#age").val() == "")
        {
            $("#age").attr("placeholder", "This field cannot be empty, please enter the age of person");
        }
        else{
                if($("#gender1").is(":checked")==false && $("#gender2").is(":checked")==false)
                {
                    alert("please choose your gender to proceed");
                }
                else
                {
                     $("#ageregistration").val($("#age").val());
                     $("#datepicker").val($("#datepicker1").val());
                     if($("#gender1").is(":checked")==true){
                           $("#sex1").prop("checked", true);
                     }
                     if($("#gender2").is(":checked")==true){
                          $("#sex2").prop("checked", true);
                     }
                     var names =$('#family_name').val();
                     var arr = names.split(' ');
                     $("#patient\\.given_name").val(arr[0]);
                     if(arr.length>=3){
                        $("#patient\\.middle_name").val(arr[1]);
                        $("#patient\\.family_name").val(arr[2]);
                     }
                     if(arr.length==2){
                        $("#patient\\.family_name").val(arr[1]);
                     }

                     if($("#estimatedDOB").val()=="Yes"){
                        $("#patient\\.birthdateEstimatedInput").prop("checked", true);
                     }

                    //start of getting data
                    $.ajax({
                        type: "POST",
                        url: "fingerprint/findPatients.form",
                        contentType: "application/json",
                        data: $("#family_name").val(),
                        dataType: 'json',
                        success: function (result) {
                            //console.log(result);
                            if(result.length == 0) {
                                window.clearInterval(yellowMan);
                                updateControls(7);
                            }
                            else{
                                $("#enrollFingerprint").hide();
                                updatePersonListTable(result, 6);
                                window.clearInterval(yellowMan);
                                //console.log("patientsearch is "+JSON.stringify(result));
                            }
                        },
                        error: function(msg, status, error){
                            console.log("server error+++++++++++++++++++"+JSON.stringify(msg));
                        }
                    });
                    //end that method
                }

        }
        //end check
    });
    });
//end search of basic demographics

    $.validator.addMethod("org.openmrs.patient.impl.LuhnIdentifierValidator", function (value, element) {
            var splitName = value.split('.');
            var num = value.split('-');
            if (num.length != 2) {
                return false;
            }
            return $.fn.luhnCheckDigit(num[0]) == num[1];
        }, "Please enter digits that matches Luhn CheckDigit algorithm."
    );

    $.validator.addMethod("org.openmrs.module.idgen.validator.LuhnMod10IdentifierValidator", function (value, element) {
            var num = value.split('-');
            var validChars = "0123456789";
            if (num.length != 2) {
                return false;
            }
            return $.fn.luhnNCheckDigit(num[0],validChars) == num[1];
        }, "Please enter digits that matches Luhn Mod-10 CheckDigit algorithm."
    );

    $.validator.addMethod("org.openmrs.module.idgen.validator.LuhnMod25IdentifierValidator", function (value, element) {
            var num = value.split('-');
            var validChars = "34679ACDEFGHJKLMNPRTUVWXY";
            if (num.length != 2) {
                return false;
            }
            return $.fn.luhnNCheckDigit(num[0],validChars) == num[1];
        }, "Please enter digits that matches Luhn Mod-25 CheckDigit algorithm."
    );

    $.validator.addMethod("org.openmrs.module.idgen.validator.LuhnMod30IdentifierValidator", function (value, element) {
            var num = value.split('-');
            var validChars = "0123456789ACDEFGHJKLMNPRTUVWXY";
            if (num.length != 2) {
                return false;
            }
            return $.fn.luhnNCheckDigit(num[0],validChars) == num[1];
        }, "Please enter digits that matches Luhn Mod-30 CheckDigit algorithm."
    );

    $.validator.addMethod("org.openmrs.patient.impl.VerhoeffIdentifierValidator", function (value, element) {
            var num = value.split('-');
            return $.fn.verhoeffCheckDigit(num[0]) == 0;
        }, "Please enter digits that matches Verhoeff CheckDigit algorithm."
    );

    $.validator.addMethod("lettersOnly", function(value, element) {
      return this.optional(element) || /^[a-z]+$/i.test(value);
    }, "Please enter letters only please");

    $.validator.addMethod("numbersOnly", function(value, element) {
      return this.optional(element) || /^\d+$/.test(value);
    }, "Please enter numbers only please");

    $.validator.addMethod("nonFutureDate",
        function (value, element) { return Date.parse(value.replace("-","/")) < new Date().getTime(); },
        "Date can not be in the future."
    );

    $.fn.luhnCheckDigit = function (number) {
        var validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";
        number = number.toUpperCase().trim();
        var sum = 0;
        for (var i = 0; i < number.length; i++) {
            var ch = number.charAt(number.length - i - 1);
            if (validChars.indexOf(ch) < 0) {
                return false;
            }
            var digit = ch.charCodeAt(0) - 48;
            var weight;
            if (i % 2 == 0) {
                weight = (2 * digit) - parseInt(digit / 5) * 9;
            }
            else {
                weight = digit;
            }
            sum += weight;
        }
        sum = Math.abs(sum) + 10;
        return (10 - (sum % 10)) % 10;
    };

    $.fn.luhnNCheckDigit = function (number,validChars) {
       number = number.toUpperCase().trim();
       var sum = 0;
       var mod = validChars.length;
       for (var i = 0; i < number.length; i++) {
           var ch = number.charAt(number.length - i - 1);
           if (validChars.indexOf(ch) < 0) {
               //console.log("Wrong characters");
               return "Invalid Characters";
           }
           var digit = ch.charCodeAt(0) - 48;
           var weight;
           if (i % 2 == 0) {
               weight = (2 * digit) - parseInt(digit / 5) * 9;
           }
           else {
               weight = digit;
           }
           sum += weight;
       }
       sum = Math.abs(sum) + mod;
       return (mod - (sum % mod)) % mod;
    };

    /*
    For more info on the algorithm: http://en.wikipedia.org/wiki/Verhoeff_algorithm
    by Sergey Petushkov, 2014
    */
    $.fn.verhoeffCheckDigit = function (number) {
      // multiplication table d
      var d=[
          [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
          [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
          [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
          [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
          [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
          [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
          [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
          [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
          [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
          [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
      ];

      // permutation table p
      var p=[
          [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
          [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
          [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
          [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
          [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
          [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
          [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
          [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]
      ];

      // inverse table inv
      var inv = [0, 4, 3, 2, 1, 5, 6, 7, 8, 9];
      var array;

      if (Object.prototype.toString.call(number) == "[object Number]"){
         array = String(number);
      }

      if (Object.prototype.toString.call(number) == "[object String]"){
         array = number.split("").map(Number);
      }

      var c = 0;
      var invertedArray = array.reverse();

      for (var i = 0; i < invertedArray.length; i++){
          c=d[c][p[(i % 8)][invertedArray[i]]];
      }
      return c;
    };

    $('#formData').validate({
        rules :{
            family_name : {
                required : true,
                lettersOnly: true
            },
            given_name : {
                required : true,
                lettersOnly: true
            },
            middle_name:{
              required : true,
              lettersOnly: true
            },
            sex :{
                required : true
            },
            phone_number :{

            },
            amrs_id : {
                required : true,
               // numbersOnly:true
               // checkDigit: true
            },
            age :{
                required : true,
                numbersOnly:true
            },
            location_id:{
                required : false
                }
//            },
//            provider_id : {
//                required : true
//            },
//            encounter_datetime:{
//                required : true,
//                nonFutureDate: true
//            }
        }
    });
    updateControls(0);
    if($("#findPatients").val() != null && $("#findPatients").val() != "")
    {
        $.ajax({
            type: "POST",
            url: "fingerprint/findPatients.form",
            contentType: "application/json",
            data: $("#findPatients").val(),
            dataType: 'json',
            success: function (result) {
                //console.log(result);
                if(result.length == 0) {
                    updateControls(4);
                }
                else{
                    updatePatientListTable(result, 1);
                }
            },
            error: function(msg, status, error){
                console.log("server error +++++++++++++"+JSON.stringify(error));
            }
        });
    }
    $("#reload").on("click",function() {
        $.ajax({
            type: "GET",
            url: "getFingerprint.form?fingerprintIsSet=clear",
            contentType: "application/json",
            success: function (result) {
                document.getElementById("fingerprintScan").value = "false";
                document.getElementById("startScanning").value="false";
                //console.log("fingerprint reset to ");
                location.href = "managefingerprint.form";
            }
        })
    });
    $("#refresh").on("click",function() {
        $.ajax({
            type: "GET",
            url: "getFingerprint.form",
            contentType: "application/json",
            success: function (response) {
                if (response.fingerprintIsSet == true) {
                    document.getElementById("fingerprintScan").value = response.fingerprintIsSet;
                    $.ajax({
                        type: "POST",
                        url: "identifyPatientByFingerprint.form?fingerprintIsSet=" + response.fingerprintIsSet,
                        contentType: "application/json",
                        success: function (result) {
                            muzimaFingerprint = JSON.parse(result);
                            //console.log("result is " + JSON.stringify(muzimaFingerprint));
                            if (muzimaFingerprint.length == 0 || muzimaFingerprint[0] === "") {
                                $("#enrollFingerprint").show();
                                $("#signinscreen").fadeOut("slow");
                                updateScanningView(1);
                            }
                            else {
                                updatePatientListTable(muzimaFingerprint, 1);
                                updateScanningView(1);
                            }
                            //console.log(JSON.stringify(muzimaFingerprint));
                        },
                        error: function (msg, status, error) {
                            console.log("sever encountered an error");
                        }
                    });
                }
                else if(response.exception !=""){
                  updateScanningView(1);
                  console.log("there is an exception ++++++++++++++++++++"+response.exception);
                  if(response.exception=="-2"){
                    $('#exception_device').modal('show');
                  }
                  else if(response.exception=="100"){
                    $('#exception_quality').modal('show');
                  }
                  else if(response.exception=="101"){
                    $('#exception_swipe').modal('show');
                  }
                  else if(response.exception=="102"){
                    $('#exception_center').modal('show');
                  }
                  else if(response.exception=="103"){
                    $('#exception_pressure').modal('show');
                  }
                  else if(response.exception=="-1"){
                    $('#exeption_native').modal('show');
                  }
                }
                else {
                    //location.href = "managefingerprint.form";
                    updateScanningView(3);
                    $("#refreshDiv").hide();
                    $("#downloadDiv").show();
                    console.log("please refresh to load fingerprint again");
                }

            },
            error: function (msg, status, error) {
                console.log("server encountered an error");
            }
        });
    });

    $("#download").on("click",function() {
        document.getElementById("startScanning").value = "true";
        updateScanningView(2);
        setTimeout(function(){
        $.ajax({
            type: "GET",
            url: "getFingerprint.form",
            contentType: "application/json",
            success: function (response) {
                if (response.fingerprintIsSet == true) {
                    document.getElementById("fingerprintScan").value = response.fingerprintIsSet;
                    $.ajax({
                        type: "POST",
                        url: "identifyPatientByFingerprint.form?fingerprintIsSet=" + response.fingerprintIsSet,
                        contentType: "application/json",
                        success: function (result) {
                            muzimaFingerprint = JSON.parse(result);
                            //console.log("result is " + JSON.stringify(muzimaFingerprint));
                            if (muzimaFingerprint.length == 0 || muzimaFingerprint[0] === "") {
                                $("#enrollFingerprint").show();
                                $("#signinscreen").fadeOut();
                                updateScanningView(1);
                            }
                            else {
                                updatePatientListTable(muzimaFingerprint, 1);
                                updateScanningView(1);
                            }
                        },
                        error: function (msg, status, error) {
                            console.log("server encountered an error");
                        }
                    });
                }
                else if(response.exception !=""){
                  updateScanningView(1);
                  console.log("there is an exception ++++++++++++++++++++"+response.exception);
                  if(response.exception=="-2"){
                    $('#exception_device').modal('show');
                  }
                  else if(response.exception=="100"){
                    $('#exception_quality').modal('show');
                  }
                  else if(response.exception=="101"){
                    $('#exception_swipe').modal('show');
                  }
                  else if(response.exception=="102"){
                    $('#exception_center').modal('show');
                  }
                  else if(response.exception=="103"){
                    $('#exception_pressure').modal('show');
                  }
                  else if(response.exception=="-1"){
                    $('#exeption_native').modal('show');
                  }
                }
                else {
                    //location.href = "managefingerprint.form";
                    updateScanningView(3);
                    console.log("please scan fingerprint again");
                }

            },
            error: function (msg, status, error) {
                console.log("server encountered an error repeat regisration");
            }
        });
    },7000);
    });
    function updateScanningView(value){
        if(value==1){
            $("#spinner").hide();
            $("#downloadDiv").show();
            $("#refreshDiv").hide();
        }
        if(value==2){
            $("#spinner").show();
            $("#downloadDiv").hide();
            $("#refreshDiv").hide();
        }
        if(value==3){
            $("#spinner").hide();
            $("#downloadDiv").hide();
            $("#refreshDiv").show();
        }
    }
    updateScanningView(1);
    //modify function to show register patient button(headache solved)
           var yellowMan=setInterval(function(){
            //start ajax
            $.ajax({
                    type:"GET",
                    url:"fetchEnrolledFingers.form",
                    contentType:"application/json",
                    success:function(result){
                        var fingersStatus=JSON.parse(result);
                        if(fingersStatus.firstImageIsSet==true && fingersStatus.secondImageIsSet==true && fingersStatus.thirdImageIsSet==true){
                            updateControls(3);
                            $("#enrollFingerprint").hide();
                        }
                    }
                });
            //end ajax
            }, 3000);
    //end of modifying function to show register patient button
    $("#addFingers").on("click",function(){
        $.ajax({
            type:"GET",
            url:"fetchEnrolledFingers.form",
            contentType:"application/json",
            success:function(result){
                var fingersStatus=JSON.parse(result);
                if(fingersStatus.firstImageIsSet==true && fingersStatus.secondImageIsSet==true && fingersStatus.thirdImageIsSet==true){
                  $.ajax({
                      type: "GET",
                      url: "getFingerprint.form",
                      contentType: "application/json",
                      success: function (response){
                        if(response.patientUUID==""){
                        $("#addFingerprints").hide();
                        $.ajax({
                            type:"POST",
                            url:"fingerprint/addFingerprint.form",
                            contentType:"application/json",
                            data:patientUUID,
                            success:function(result){
                                $("#enrollFingerprint").html("<span style='font-weight:bold;color:green;'>Fingerprint Successfully added</span>").show();
                                updatePatientListTable(result, 5);
                            }
                        })
                      }
                      else{
                        $('#fingerprint-exists').modal('show');
                      }
                      },
                      error:function(error){
                        console.log("error is ++++++++"+JSON.stringify(error));
                      }
                    })
                }
                else if(fingersStatus.firstImageIsSet==false){
                  $('#missing-first-scan').modal('show');
                }
                else if(fingersStatus.secondImageIsSet==false){
                  $('#missing-second-scan').modal('show');
                }
                else{
                  $('#missing-third-scan').modal('show');
                }
            }
        })
    });

    $("#tblData").on("click","tr td button",function(){
      var patientUuid=$(this).parent().parent().find('td:nth-child(1)').html();
      var appendFingerprint = document.getElementById('tblDataAppendfingerprint').value();
      var scannedFinger = $(this).parent().parent().find('td:nth-child(7)').val();
      appendFingerPrint(appendFingerprint,patientUuid,scannedFinger);
    });

    $("#tblStore").on("click","tr td button",function(){
        var patientUuid=$(this).parent().parent().find('td:nth-child(1)').html();
        var appendFingerprint = $("#tblStoreAppendfingerprint").val();
        var patientId = $(this).parent().parent().find('td:nth-child(2)').html();
        var field = "#patientid"+patientId;
        var scannedFinger = $(field).find(":selected").val();
        //console.log("Scanned Finger"+scannedFinger);
        appendFingerPrint(appendFingerprint,patientUuid,scannedFinger);
    });

    $("#tblSearchResults").on("click","tr td button",function(){
         var patientUuid = $(this).parent().parent().find('td:nth-child(1)').html();
         var appendFingerprint = $("#appendfingerprint").val();
         var patientId = $(this).parent().parent().find('td:nth-child(2)').html();
         var field = "#patientid"+patientId;
         var scannedFinger = $(field).find(":selected").val();
         appendFingerPrint(appendFingerprint,patientUuid,scannedFinger);
    });
});

