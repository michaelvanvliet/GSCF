<g:if env="development">
	<% /* Nice buttons */ %>
	<link rel="stylesheet" href="<g:resource dir="css" file="buttons.css" />" type="text/css"/>

	<% /* Generic wizard stylesheets and javascripts */ %>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'studywizard.css')}" />
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'templates.css')}" />

	<script type="text/javascript" src="${resource(dir: 'js', file: 'studywizard.js' )}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'tooltips.js', plugin: 'gdt')}"></script>

	<% /* Table editor */ %>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'table-editor.css', plugin: 'gdt')}" />
	<script type="text/javascript" src="${resource(dir: 'js', file: 'table-editor.js', plugin: 'gdt')}"></script>

	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.qtip-1.0.0-rc3.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'ontology-chooser.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'publication-chooser.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'publication-chooser.pubmed.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'SelectAddMore.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'timepicker-0.2.1.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'ajaxupload.3.6.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.ui.autocomplete.html.js', plugin: 'gdt')}"></script>

	<% /* fuzzy matching */ %>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'fuzzyStringMatch.js', plugin: 'gdt')}"></script>

	<% /* Specific simplewizard stuff */ %>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'simpleWizard.js' )}"></script>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'simplewizard.css')}" />
</g:if>
<g:else>
	<link rel="stylesheet" href="<g:resource dir="css" file="buttons.min.css" />" type="text/css"/>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'studywizard.min.css')}" />
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'templates.min.css')}" />
	<script type="text/javascript" src="${resource(dir: 'js', file: 'studywizard.min.js' )}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'tooltips.min.js', plugin: 'gdt')}"></script>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'table-editor.min.css', plugin: 'gdt')}" />
	<script type="text/javascript" src="${resource(dir: 'js', file: 'table-editor.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.qtip-1.0.0-rc3.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'ontology-chooser.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'publication-chooser.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'publication-chooser.pubmed.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'SelectAddMore.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'timepicker-0.2.1.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'ajaxupload.3.6.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.ui.autocomplete.html.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'fuzzyStringMatch.min.js', plugin: 'gdt')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'simpleWizard.min.js' )}"></script>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'simplewizard.min.css')}" />
</g:else>

<g:if test="${flash.validationErrors}">
	<script type="text/javascript">
		$(function() {
			// mark error fields
			<g:each in="${flash.validationErrors}" var="error">
                var element = $("input[name='${error.key}'], input[name='${error.key.toLowerCase().replaceAll("([^a-z0-9])","_")}'], select[name='${error.key}'], select[name='${error.key.toLowerCase().replaceAll("([^a-z0-9])","_")}'], textarea[name='${error.key}'], textarea[name='${error.key.toLowerCase().replaceAll("([^a-z0-9])","_")}']");
				element.parent().parent().removeClass('required');
				element.parent().parent().addClass('error');
			</g:each>
		});
	</script>
</g:if>