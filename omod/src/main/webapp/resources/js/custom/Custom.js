var _FINGERPRINT_DATA = "";
function showMessage() {
    return "This is from javascript";
};
$("#enrollFingerprint").hide();
$("#addFingerprints").hide();
function identifyPatient(fingerprintData){
    _FINGERPRINT_DATA = fingerprintData;
    var identifiedPatient = 'no data found';

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
            updatePatientListTable(msg, 1);
        },
        error: function(msg, status, error){
            console.log("server error +++++++++++++"+JSON.stringify(error));
        }

    });

    return identifiedPatient;
};

function updatePatientListTable(Patients, updateControlsStatus){
    updateControls(updateControlsStatus);
    $("#tblData tbody tr").remove();
    var identifiers;
    Patients.forEach( function (patient){
        identifiers=patient.identifiers.replace(/[\[\]']+/g,'');
        $("#tblData tbody").append( "<tr>"
            + "<td style='display:none;'>"+patient.patientUUID+"</td>"
            + "<td><a href='"+openmrsContextPath+"/patientDashboard.form?patientId="+patient.id+"'>"+patient.id+" </a></td>"
            + "<td>"+patient.givenName +"</td>"
            + "<td>"+ patient.familyName+"</td>"
            + "<td>"+ identifiers+"</td>"
            + "<td>"+ patient.gender+"</td>"
            + "<td>"+show(patient.fingerprintTemplate,patient.patientUUID)+"</td>"
            + "</tr>");
    });
};
function activate(val, e){
    var key=e.keyCode || e.which;
    if (key==13){
        $.ajax({
            type: "POST",
            url: "fingerprint/findPatients.form",
            contentType: "application/json",
            data: val,
            dataType: 'json',
            success: function (result) {
                console.log(result);
                if(result.length == 0) {
                    updateControls(4);
                }
                else{
                    $("#enrollFingerprint").hide();
                    updatePatientListTable(result, 1);
                    console.log("patientsearch is "+JSON.stringify(result));
                }
            },
            error: function(msg, status, error){
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
        return "<button type='button'>Add Fingerprints</button>"
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
    } else if(status ==1){
        //Show patient found for scan process - case1
        $('#body-wrapper').show();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
    } else if(status ==2){
        //Show other option i.e. to register - case2
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').show();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
    } else if(status ==3){
        //Show registration section - case3
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').show();
        $('#updatePatient').hide();
        $('#searchResults').hide();
        $('#patientCreated').hide();
    } else if(status ==4){
        //No patient found with search by name or identifier - case4
        $('#body-wrapper').hide();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').show();
        $('#patientCreated').hide();
    } else if(status ==5){
        //Show created patient - case5
        $('#body-wrapper').show();
        $('#otherIdentificationOption').hide();
        $('#registrationForm').hide();
        $('#updatePatient').hide();
        $('#searchResults').hide();
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
            url: "../../ws/rest/v1/patientidentifiertype",
            type: "GET",
            async: true,
            success: function(result) {

                var options = $("#IdentifierOptions");
                $.each(result.results, function(val, text) {
                    options.append($("<option />").val(text.uuid).text(text.display));
                });
            },
            error: function(msg){
                alert("Internal server error : while getting Identifier list ");
            }
        }
    );

    $("#btnByIdentifier").click(function(){
        var jsonData = JSON.stringify($('#IdentifierForm').serializeEncounterForm());
        console.log(jsonData);
        $.ajax({
            url: "fingerprint/identifyPatientByOtherIdentifier.form",
            type: "POST",
            data: jsonData,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
                console.log(msg);
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
    $("#btnNo").click(function(){
        updateControls(0);
    });
    $("#Yes").click(function(){
        var patientUuid = $('#uid').val();
        var jsonData = "{patient: {patientUUID : '"+patientUuid+"' , fingerprint :'"+ _FINGERPRINT_DATA+"'}}";
        console.log(jsonData);
        $.ajax({
            url: "fingerprint/addFingerprint.form",
            type: "POST",
            data: jsonData,
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function(msg) {
                console.log(msg);
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
                cconsole.log("server error +++++++++++++"+JSON.stringify(error));
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
                        console.log("new Patient data "+JSON.stringify(msg));
                        $('#formData').trigger("reset");
                        $("#enrollFingerprint").html("<span style='font-weight:bold;color:green;'>Patient Successfully Saved</span>").show();
                        updatePatientListTable(msg, 5);
                        document.getElementById("fingerprintScan").value="false";
                    },
                    error: function(msg, status, error){
                        console.log("server error +++++++++++++"+JSON.stringify(error));
                    }
                });
              }
            },
            error:function(error){
              console.log("patient by identifier error +++++++++++++++++++++++++++"+JSON.stringify(error));
            }
          });
        }
    });

    $.validator.addMethod("checkDigit", function (value, element) {
            var num = value.split('-');
            if (num.length != 2) {
                return false;
            }
            return $.fn.luhnCheckDigit(num[0]) == num[1];
        }, "Please enter digits that matches CheckDigit algorithm."
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
                checkDigit: true
            },
            age :{
                required : true,
                numbersOnly:true
            },
            location_id:{
                required : true
            },
            provider_id : {
                required : true
            },
            encounter_datetime:{
                required : true,
                nonFutureDate: true
            }
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
                console.log(result);
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
                console.log("fingerprint reset to ");
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
                            console.log("result is " + JSON.stringify(muzimaFingerprint));
                            if (muzimaFingerprint.length == 0 || muzimaFingerprint[0] === "") {
                                $("#enrollFingerprint").show();
                                updateScanningView(1);
                            }
                            else {
                                updatePatientListTable(muzimaFingerprint, 1);
                                updateScanningView(1);
                            }
                            console.log(JSON.stringify(muzimaFingerprint));
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
                    console.log("please refresh to load fingerptint again");
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
                            console.log("result is " + JSON.stringify(muzimaFingerprint));
                            if (muzimaFingerprint.length == 0 || muzimaFingerprint[0] === "") {
                                $("#enrollFingerprint").show();
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
                    console.log("please scan fingerptint again");
                }

            },
            error: function (msg, status, error) {
                console.log("server encountered an error repeat regisration");
            }
        });
    },15000);
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
    $("#enrollFingers").on("click",function(){
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
    $("#addFingers").on("click",function(){
        $.ajax({
            type:"GET",
            url:"fetchEnrolledFingers.form",
            contentType:"application/json",
            success:function(result){
                var fingersStatus=JSON.parse(result);
                if(fingersStatus.firstImageIsSet==true && fingersStatus.secondImageIsSet==true && fingersStatus.thirdImageIsSet==true){
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
      $("#selected-patient").empty();
      patientUUID=$(this).parent().parent().find('td:nth-child(1)').html();
      $.ajax({
        url:"../../ws/rest/v1/patient/"+patientUUID,
        type:"GET",
        contentType: 'application/json',
        success:function(response){
          var display=response.display;
          $("#selected-patient").append("<h3>Append fingerprint to: "+display+"</h3>");
          $("#addFingerprints").show();
        },
        error:function(error){
          console.log("selected patient to add fingerprint error +++++++++++++++++++++"+JSON.stringify(error));
        }
      });
    });
});
