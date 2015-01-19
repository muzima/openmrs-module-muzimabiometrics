
function showMessage() {
    return "This is from javascript";
};

function GetAllPatient(){
    var xmlhttp;
    if (window.XMLHttpRequest){

         xmlhttp=new XMLHttpRequest();
      }
    else{

        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    xmlhttp.onreadystatechange=function(){

          if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
                return xmlhttp.responseText;
            }
     }
        xmlhttp.open("POST","fingerprint/identify.form",false);
        xmlhttp.send();
    };

function SendIdentifiedPatientID(){

};
