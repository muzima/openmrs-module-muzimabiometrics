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
<div ng-app="muzimafingerPrint">
    <div class="row-fluid">
            <div class="col-lg-12">
                <div ng-view></div>
            </div>
    </div>
</div>
