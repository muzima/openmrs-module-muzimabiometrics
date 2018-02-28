$(document).ready(function(){
  $("#showdefaultsettings").hide();
});
$(document).ready(function(){
  $("#activatemaincontainersignin").click(function(){

      $('#body-wrapper').hide();
      $('#body-wrapperr').hide();
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
      $('#body-wrapper').hide();
      $('#body-wrapperr').hide();
      $("#homecontainer").fadeOut();
      $('#basicdemographicform').hide();
      $("#enrollFingerprint").hide();
      $("#showdefaultsettings").slideUp();
      $("#mainframecontainer").slideDown();
      $("#signinscreen").fadeOut();
      $("#enrollFingerprint").show();
      $("#scanHeaderMessage").text("Patient Registration Section");
      $("#userNotificationScan").css("color","green");
      $("#userNotificationScan").text("Please scan the left thumb finger three times to register by clicking the button below.");
      $("#unregisteredscantext").show();
      $("#scanwaittext").hide();
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
$(document).ready(function(){
    $( "#activatehomecontainer" ).hover(function(){
            $("#activatehomecontainer").css("animation","skew 3s infinite");
            $("#activatehomecontainer").css("transform","skew(20deg)");
            $("#activatehomecontainer").css("animation-direction","alternate");
    });
});
