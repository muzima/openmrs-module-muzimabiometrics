'use strict';
function fingerprintController($scope, $window, $location)
{
    $scope.init = function(){

    }
    $scope.identifyPatient = function(){
        var attributes = {code:'HelloWorld',  width:0, height:0} ;
        var parameters = {jnlp_href: '/openmrs-standalone/moduleResources/muzimafingerPrint/fingerprint.jnlp'} ;
        deployJava.runApplet(attributes, parameters, '1.6');
    }

}