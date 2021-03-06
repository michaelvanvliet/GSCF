<%@ page import="org.dbnp.gdt.GdtService" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main"/>
	<title>Simple study wizard</title>
	
	<g:render template="javascripts" />
	
</head>
<body>
	<div class="simpleWizard samplespage">
		<h1>
			Study data
			<span class="stepNumber">(step 2 of 4)</span>
		</h1>

    	<span class="info"> 
			<span class="title">Import study data</span> 
			You can import your study data to the server by choosing an excel file from your local harddisk in the form below. The excel sheet should contain
			data on the first sheet, and the sheet should contain one row with headers. Each line can contain sample, subject and event data.
		</span> 
				
		<g:if test="${flash.error}">
			<div class="errormessage">
				${flash.error.toString().encodeAsHTML()}
			</div>
		</g:if>
		<g:if test="${flash.message}">
			<div class="message">
				${flash.message.toString().encodeAsHTML()}
			</div>
		</g:if>			
		
		<g:form class="simpleWizard" name="samples" action="simpleWizard">
			<input type="hidden" name="_eventId" value="refresh" />

			<div id="samplesDialog">
				<table border="0">
			    	<tr>
					    <td width="15%" class="required">
							Excel file to import:
					    </td>
					    <td colspan="2">
							<af:fileField buttonText="Open" hideDelete="${true}" name="importfile" value="${sampleForm?.importFile}"/>
					    </td>
					</tr>
					<tr>
					    <td>
						<div id="datatemplate">Subject template:</div>
					    </td>
					    <td>
					    	<% /* The select is written manually, since the grails select tag can't handle option titles */ %>
							<select rel="template" entity="${encodedEntity.Subject}" onChange="showTemplateDescription( 'templateDescription_subject', $( 'option:selected', $(this) ).attr( 'title' ) ); " name="subject_template_id">
								<option value="">- no subject template -</option>
								<g:each in="${templates.Subject}" var="templ">
									<option 
										value="${templ.id}"
										<g:if test="${templ.id == sampleForm?.templateId?.Subject}">selected="selected"</g:if>
										title="${templ.description?.encodeAsHTML()}"
									>${templ.name?.encodeAsHTML()}</option>
								</g:each>
							</select>
					    </td>
					    <td>
						    <%
								def subjectTemplateDescription = sampleForm?.template?.Subject?.description
							%>
							<div class="templatedescription" id="templateDescription_subject" <g:if test="${!subjectTemplateDescription}">style="display: none;"</g:if>>
								${subjectTemplateDescription?.encodeAsHTML()}
							</div>
					    </td>					    
					</tr>
					<tr>
					    <td>
						<div id="datatemplate">Event template:</div>
					    </td>
					    <td>
					    	<% /* The select is written manually, since the grails select tag can't handle option titles */ %>
							<select rel="template" entity="${encodedEntity.Event}" onChange="showTemplateDescription( 'templateDescription_event', $( 'option:selected', $(this) ).attr( 'title' ) ); " name="event_template_id">
								<option value="">- no event template -</option>
								<g:each in="${templates.Event}" var="templ">
									<option 
										value="${templ.id}"
										<g:if test="${templ.id == sampleForm?.templateId?.Event}">selected="selected"</g:if>
										title="${templ.description?.encodeAsHTML()}"
									>${templ.name?.encodeAsHTML()}</option>
								</g:each>
							</select>
					    </td>
					    <td>
						    <%
								def eventTemplateDescription = sampleForm?.template?.Event?.description
							%>
							<div class="templatedescription" id="templateDescription_event" <g:if test="${!eventTemplateDescription}">style="display: none;"</g:if>>
								${eventTemplateDescription?.encodeAsHTML()}
							</div>
					    </td>						    
					</tr>
					<tr>
					    <td>
						<div id="datatemplate">Sampling event template:</div>
					    </td>
					    <td>
					    	<% /* The select is written manually, since the grails select tag can't handle option titles */ %>
							<select rel="template" entity="${encodedEntity.SamplingEvent}" onChange="showTemplateDescription( 'templateDescription_samplingEvent', $( 'option:selected', $(this) ).attr( 'title' ) ); " name="samplingEvent_template_id">
								<option value="">- no sampling event template -</option>
								<g:each in="${templates.SamplingEvent}" var="templ">
									<option 
										value="${templ.id}"
										<g:if test="${templ.id == sampleForm?.templateId?.SamplingEvent}">selected="selected"</g:if>
										title="${templ.description?.encodeAsHTML()}"
									>${templ.name?.encodeAsHTML()}</option>
								</g:each>
							</select>
					    </td>
					    <td>
						    <%
								def samplingEventTemplateDescription = sampleForm?.template?.SamplingEvent?.description
							%>
							<div class="templatedescription" id="templateDescription_samplingEvent" <g:if test="${!samplingEventTemplateDescription}">style="display: none;"</g:if>>
								${samplingEventTemplateDescription?.encodeAsHTML()}
							</div>
					    </td>						    
					</tr>
					<tr>
					    <td class="required">
							<div id="datatemplate">Sample template:</div>
					    </td>
					    <td width="25%">
					    	<% /* The select is written manually, since the grails select tag can't handle option titles */ %>
							<select rel="template" entity="${encodedEntity.Sample}" onChange="showTemplateDescription( 'templateDescription_sample', $( 'option:selected', $(this) ).attr( 'title' ) ); " name="sample_template_id">
								<g:each in="${templates.Sample}" var="templ">
									<option 
										value="${templ.id}"
										<g:if test="${templ.id == sampleForm?.templateId?.Sample}">selected="selected"</g:if>
										title="${templ.description?.encodeAsHTML()}"
									>${templ.name?.encodeAsHTML()}</option>
								</g:each>
							</select>
					    </td>
					    <td width="40%">
						    <%
								def sampleTemplate = sampleForm?.template?.Sample ?: templates.Sample?.getAt(0)
								def sampleTemplateDescription = sampleTemplate?.description
							%>
							<div class="templatedescription" id="templateDescription_sample" <g:if test="${!sampleTemplateDescription}">style="display: none;"</g:if>>
								${sampleTemplateDescription?.encodeAsHTML()}
							</div>
					    </td>
					</tr>						
				</table>	
			</div>
		
		</g:form>
		
		<p class="options">
			<a href="#" onClick="submitForm( 'samples', 'previous' ); return false;" class="previous">Previous</a>
			<a href="#" onClick="submitForm( 'samples', 'next' ); return false;" class="next default">Next</a>

			<a class="skip separator" href="#" onClick="submitForm( 'samples', 'skip' ); return false;">Skip</a>
		</p>
	</div>	

</body>
</html>
