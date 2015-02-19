
function showMessage() {
        return "This is from javascript";
    };

function identifyPatient(fingerprintData){

//     $.ajax({
//                url: "fingerprint/identifyPatient.form",
//                type: "POST",
//                data: fingerprintData,
//                contentType: 'application/json',
//                dataType: 'json',
//                async: false,
//                success: function(msg) {
//                    console.log(msg);
//                    return msg;
//                },
//                error: function(msg){
//                    alert("Internal server error");
//                }
//            });
         var xmlhttp;
         var data = 'no data found';
        if (window.XMLHttpRequest) {
             xmlhttp=new XMLHttpRequest();
        }
        else {
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
              if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                     data = xmlhttp.responseText;
              }
        }
        xmlhttp.open("POST","fingerprint/identifyPatient.form",false);
        xmlhttp.setRequestHeader("Content-type","application/json");
        xmlhttp.send(fingerprintData);
        return '['+data+']';
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
        $.ajax({
            url: "../../ws/rest/v1/user",
            type: "GET",
            async: true,
            success: function(result) {
               var options = $("#ProviderOptions");
               $.each(result.results,  function(val, text) {
                   options.append($("<option />").val(text.uuid).text(text.display));
               });
            },
            error: function(msg){
                    alert("Internal server error : while getting provider list ");
                }
            }
        );

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
                    required : true
                },
                sex :{
                    required : true
                },
                phone_number :{
                    required : true
                },
                amrs_id : {
                    required : true,
                    checkDigit: true
                },
                fingerprint:{
                    required : true
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
            },
            submitHandler:function(){
                var jsonData = JSON.stringify($('form').serializeEncounterForm());
                $.ajax(
                   {
                        url: "fingerprint/register.form",
                        type: "POST",
                        data: jsonData,
                        contentType: 'application/json',
                        dataType: 'json',
                        async: false,
                        success: function(msg) {
                            alert("Patient Created!");
                            showRegistration('false');
                        },
                        error: function(msg){
                            alert("Internal server error");
                        }
                    }
                );
            }

        });
    });
showRegistration('false');

