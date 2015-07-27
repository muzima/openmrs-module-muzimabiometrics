<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/animate/animate.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/bootstrap/css/bootstrap.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/custom/custom.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/styles/font-awesome/css/font-awesome.min.css"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-1.10.1.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery-ui-1.10.4.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/jquery/jquery.validate.min.js"/>
<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/underscore/underscore-min.js"/>

<div>
    <div class="navbar navbar-custom">
        <div>
            <a href="#"><i class="icon-home"></i> Muzima Fingerprint Module</a>
        </div>
    </div>
    <div id = "applet">
        <applet name="Muzima fingerprint module" id="Abis" code="org.openmrs.module.muzimafingerPrint.SimpleFingersApplication"  width="100%"  height="100">
            <param name="jnlp_href" value="${pageContext.request.contextPath}/moduleResources/muzimafingerPrint/fingerprint.jnlp" />
            <param name="codebase_lookup" value="false" />
            <param name="separate_jvm" value="true" />
            <param name="server_address" value="/local" />
            <param name="server_port" value="5000" />
        </applet>
    </div>
    <div id = "otherIdentificationOption">
        <h4>Do you want to add fingerprint?</h4>
        <a href="#" id="Yes">Yes</a>
        <input type="hidden" value="${pUuid}" id="uid">
        <a href="#" id="No">No</a>
    </div>
</div>

<openmrs:htmlInclude file="/moduleResources/muzimafingerPrint/js/custom/Custom.js"/>