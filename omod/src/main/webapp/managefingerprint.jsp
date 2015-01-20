<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/openmrs2.0/index.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/openmrs2.0/openmrs.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/font-awesome/css/font-awesome.min.css"/>

<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/underscore/underscore-min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/js/bootstrap.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/angular/angular.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/angular/angular-resource.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/angular/angular-sanitize.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/angular/ui-bootstrap-0.3.0.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/angular/angular-strap.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/app.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/deployJava.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/fingerprintController.js"/>

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

    var attributes = {code:'HelloWorld',  width:0, height:0} ;
    var parameters = {jnlp_href: '/openmrs-standalone/moduleResources/muzimafingerPrint/fingerprint.jnlp'} ;
    deployJava.runApplet(attributes, parameters, '1.6');

</script>

<div ng-app="muzimafingerPrint">
    <div class="row-fluid">
            <div class="col-lg-12">
                <div ng-view></div>
            </div>
    </div>
</div>
