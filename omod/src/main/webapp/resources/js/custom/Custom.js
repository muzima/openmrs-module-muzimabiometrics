var _FINGERPRINT_DATA = "";

function showMessage() {
    return "This is from javascript";
};
$("#enrollFingerprint").hide();
function identifyPatient(fingerprintData){
alert("identifyPatieent is called ");
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
            alert(error);
        }

    });

    return identifiedPatient;
};

function updatePatientListTable(Patients, updateControlsStatus){

    updateControls(updateControlsStatus);
    $("#tblData tbody tr").remove();
    Patients.forEach( function (patient){
        $("#tblData tbody").append( "<tr>"
            + "<td><a href='"+openmrsContextPath+"/patientDashboard.form?patientId="+patient.id+"'>"+patient.id+" </a></td>"
            + "<td>"+patient.givenName +"</td>"
            + "<td>"+ patient.familyName+"</td>"
            + "<td>"+ patient.gender+"</td>"
            + "<td>"+show(patient.fingerprintTemplate,patient.patientUUID)+"</td>"
            + "<td><a href='"+openmrsContextPath+"/module/pharmacy/home.form?patientUUID="+patient.patientUUID+"'>Dispense</a></td>"
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
                    updatePatientListTable(result, 1);
                }
            },
            error: function(msg, status, error){
                console.log(msg);
                alert(error);
            }
        });
    }
}

function show(fingerprint, patientUuid){
    if(fingerprint!==null) {
        return "<img src ='"+openmrsContextPath+"/moduleResources/muzimabiometrics/images/done.png'/>"
    }
    else {
        return "<input id = 'addFingerprint' type='button' value= 'Add fingerprint' class='addFingerprintButton' onClick='addFPrint(\""+patientUuid+"\")'/>"
    }
};

function addFPrint(patientUuid){
    window.location = openmrsContextPath+'/module/muzimabiometrics/editPatient.form?patientUuid='+patientUuid;
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
        $('#patientCreated').show();
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
                alert(error);
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
                alert(error);
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
                console.log(msg);
                alert(error);
            }
        });

    });

    $("#btnCreatePatient").click(function(){
        if($("#formData").valid()){
            var jsonData = JSON.stringify($('#formData').serializeEncounterForm());
            $.ajax({
                url: "fingerprint/enrollPatient.form",
                type: "POST",
                data: jsonData,
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                success: function(msg) {
                    $('#formData').trigger("reset");
                    updatePatientListTable(msg, 5);
                    document.getElementById("fingerprintScan").value="false";
                    alert("message is "+JSON.stringify(msg));
                },
                error: function(msg, status, error){
                    console.log(msg);
                    alert(error);
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
                required : true
            },
            given_name : {
                required : true
            },
            middle_name:{

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
            birth_date :{
                required : true,
                nonFutureDate: true

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
                console.log(msg);
                alert(error);
            }
        });
    }
    $("#refresh").on("click",function() {
        $.ajax({
            type: "GET",
            url: "getFingerprint.form?fingerprintIsSet=clear",
            contentType: "application/json",
            success: function (result) {
                document.getElementById("fingerprintScan").value = "false";
                console.log("fingerprint reset to "+result);
                location.href = "managefingerprint.form";
            }
        })
    });
    if ($("#fingerprintScan").val() !="true") {
        setTimeout(function(){
            $.ajax({
                type: "GET",
                url: "getFingerprint.form",
                contentType: "application/json",
                success: function (response) {
                    if(response=="true"){
                        document.getElementById("fingerprintScan").value=response;
                        $.ajax({
                            type: "POST",
                            url: "identifyPatientByFingerprint.form?fingerprintIsSet="+response,
                            contentType: "application/json",
                            success: function (result) {
                                muzimaFingerprint=JSON.parse(result);
                                console.log("result is "+JSON.stringify(muzimaFingerprint));
                                if(muzimaFingerprint.length==0 || muzimaFingerprint[0]===""){
                                    $("#enrollFingerprint").show();
                                }
                                else{
                                    updatePatientListTable(muzimaFingerprint,1);
                                }
                                console.log(JSON.stringify(muzimaFingerprint));
                            },
                            error: function(msg, status, error){
                                alert(error);
                            }
                        });
                    }
                    else{
                        location.href = "managefingerprint.form";
                    }
                },
                error: function(msg, status, error){
                    console.log(msg);
                    alert(error);
                }
            });
            },3000);
    }
    $("#enrollFingers").on("click",function(){
        $.ajax({
            type:"GET",
            url:"fetchEnrolledFingers.form",
            contentType:"application/json",
            success:function(result){
                var fingersStatus=JSON.parse(result);
                if(fingersStatus.secondFingerPrintIsSet==true && fingersStatus.thirdFingerprintIsSet==true){
                    updateControls(3);
                    $("#enrollFingerprint").hide();
                }
            }
        })
    });
});
