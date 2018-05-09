$(document).ready(function(){
  $("#showdefaultsettings").hide();
});
$(document).ready(function(){
  $("#activatemaincontainersignin").click(function(){
      $('#basicdemographicform').hide();
      $('#body-wrapper').hide();
      $('#body-wrapperr').hide();
      $('#addFingerprints').hide();
      $("#refreshDiv").hide();
      $("#registeredWell").hide();
      $("#appendWell").hide();
      $("#downloadDiv").show();
      $("#searchResults").hide();
      $("#homecontainer").fadeOut();
      $('#basicdemographicform').hide();
      $("#enrollFingerprint").hide();
      $("#showdefaultsettings").slideUp();
      $("#registrationSections").fadeOut();
      $("#mainframecontainer").slideDown();
      $("#signinscreen").fadeIn();
  });
});
//hide mainframecontainer,showdefaultsettingscontainer show homecontainer
$(document).ready(function(){
  $("#activatehomecontainer").click(function(){
      $('#body-wrapper').hide();
      $('#body-wrapperr').hide();
      $("#registeredWell").hide();
      $("#appendWell").hide();
      $("#mainframecontainer").slideUp();
       $("#showdefaultsettings").slideUp();
      $("#registrationSections").fadeOut();
      $("#homecontainer").fadeIn();
  });
});
//end
//activate settings
$(document).ready(function(){
  $("#activedefaultsettings").click(function(){
      $("#mainframecontainer").hide();
      $("#homecontainer").hide();
      $("#registrationSections").fadeOut();
      $("#showdefaultsettings").show();
  });
});
$(document).ready(function(){
  $("#activatemaincontainersignup").click(function(){
      $('#basicdemographicform').show();
      $('#body-wrapper').hide();
      $('#body-wrapperr').hide();
      $("#searchResults").hide();
      $("#homecontainer").fadeOut();
      //$('#basicdemographicform').hide();
      $("#enrollFingerprint").hide();
      $("#showdefaultsettings").slideUp();
      $("#mainframecontainer").slideDown();
      $("#signinscreen").fadeOut();
      $("#registeredWell").hide();
      $("#appendWell").hide();
      $("#enrollFingerprint").show();
      $("#addFingerprints").hide();
      $("#unregisteredscantext").slideDown();
      $("#scanHeaderMessage").text("Patient Registration Section");
      $("#scanHeaderMessage").show();
      $("#userNotificationScan").css("color","green");
      $("#userNotificationScan").show();
      $("#userNotificationScan").text("Click the button below to activate the scanner.");
      $("#scanwaittext").hide();
      $("#interfacecontrol").show();

  });
});
$(document).ready(function(){
    $("#interfacecontrol").click(function(){
    $("#unregisteredscantext").slideUp("slow");
    $("#scanwaittext").fadeIn("slow");
    });
});
$(document).ready(function(){
    $("#cancelregistration").click(function(){
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
                });
    });
});
$(document).ready(function(){
    $("#createNewPatient").click(function(){
        //show registration form
           $('#body-wrapper').hide();
           $('#body-wrapperr').hide();
           $('#otherIdentificationOption').hide();
           $('#registrationForm').hide();
           $('#basicdemographicform').hide();
           $('#updatePatient').hide();
           $('#searchResults').hide();
           $('#patientCreated').hide();
           $('#registrationForm').slideDown("slow");
           $('#signinscreen').hide();
        //end showing registration form
    });
});
$(document).ready(function(){
    $("#btnCancelTransaction").click(function(){
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
                        });
    });
});
$(document).ready(function(){
    $("#CancelCreateNewPatient").click(function(){
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
                        });
    });
});

