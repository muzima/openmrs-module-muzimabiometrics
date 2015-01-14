<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/css/bootstrap.min.css"/>

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

<script>
    var attributes = {code:'HelloWorld',  width:300, height:300} ;
    var parameters = {jnlp_href: '/openmrs-standalone/moduleResources/muzimafingerPrint/Hello.jnlp'} ;
    deployJava.runApplet(attributes, parameters, '1.6');
</script>

<div ng-app="muzimafingerPrint">
    <div class="row-fluid">
            <div class="col-lg-12">
                <div ng-view></div>
            </div>
    </div>
</div>
