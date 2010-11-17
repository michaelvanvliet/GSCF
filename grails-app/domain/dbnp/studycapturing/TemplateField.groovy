package dbnp.studycapturing

import dbnp.data.Ontology

/**
 * A TemplateField is a specification for either a 'domain field' of a subclass of TemplateEntity or a
 * 'template field' for a specific Template. See the Template class for an explanation of these terms.
 * The TemplateField class contains all information which is needed to specify what kind of data can be stored
 * in this particular field, such as the TemplateFieldType, the name, the ontologies from which terms can derive
 * in case of an ONTOLOGYTERM field, the list entries in case of a STRINGLIST fields, and so on.
 * The actual values of the template fields are stored in instances of subclasses of the TemplateEntity class.
 * For example, if there exists a Study template with a 'description' TemplateField as a member of Template.fields,
 * the actual description for each Study would be stored in the inherited templateStringFields map of that Study instance.
 *
 * One TemplateField can belong to many Templates, but they have to be the same entity as the TemplateField itself.
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */
class TemplateField implements Serializable {

	/** The name of the TemplateField, by which it is represented to the user.  */
	String name

	/** The type of this TemplateField, such as STRING, ONTOLOGYTERM etc. */
	TemplateFieldType type

	/** The entity for which this TemplateField is meant. Only Templates for this entity can contain this TemplateField */
	Class entity

	/** The unit of the values of this TemplateField (optional) */
	String unit

	/** The help string which is shown in the user interface to describe this template field (optional, TEXT) */
	String comment

	/** The different list entries for a STRINGLIST TemplateField. This property is only used if type == TemplateFieldType.STRINGLIST */
	List listEntries

	/** Indicates whether this field is required to be filled out or not */
	boolean required

	/** Indicates whether this field is the preferred identifier for the resulting templated entity.
		This is for example used when importing to match entries in the database against the ones that are being imported. */
	boolean preferredIdentifier

	static hasMany = [
		listEntries: TemplateFieldListItem,	// to store the entries to choose from when the type is 'item from predefined list'
		ontologies: Ontology				// to store the ontologies to choose from when the type is 'ontology term'
	]

	static constraints = {
		// outcommented for now due to bug in Grails / Hibernate
		// see http://jira.codehaus.org/browse/GRAILS-6020
		// This is to verify that TemplateField names are unique within templates of each super entity
		// TODO: this probably has to change in the case of private templates of different users,
		// which can co-exist with the same name. See also Template
		// name(unique:['entity'])

		name(nullable: false, blank: false)
		type(nullable: false, blank: false)
		entity(nullable: false, blank: false)
		unit(nullable: true, blank: true)
		comment(nullable: true, blank: true)
		required(default: false)
		preferredIdentifier(default: false)
	}

	static mapping = {
		// Make sure the comments can be Strings of arbitrary length
		comment type: 'text'
                name column:"templatefieldname"
                type column:"templatefieldtype"
                entity column:"templatefieldentity"
                unit column:"templatefieldunit"
                comment column:"templatefieldcomment"                
	}

	String toString() {
		return name
	}

	/**
	 * return an escaped name which can be used in business logic
	 * @return String
	 */
	def String escapedName() {
		return name.toLowerCase().replaceAll("([^a-z0-9])", "_")
	}

	/**
	 * overloading the findAllByEntity method to make it function as expected
	 * @param Class entity (for example: dbnp.studycapturing.Subject)
	 * @return ArrayList
	 */
	public static findAllByEntity(java.lang.Class entity) {
		def results = []
		// 'this' should not work in static context, so taking Template instead of this
		TemplateField.findAll().each() {
			if (entity.equals(it.entity)) {
				results[results.size()] = it
			}
		}

		return results
	}

	/**
	 * Checks whether this template field is used in a template
	 *
	 * @returns		true iff this template field is used in a template (even if the template is never used), false otherwise
	 */
	def inUse() {
		return numUses() > 0;
	}

	/**
	 * The number of templates that use this template field
	 *
	 * @returns		the number of templates that use this template field.
	 */
	def numUses() {
		return getUses().size();
	}

	/**
	 * Retrieves the templates that use this template field
	 *
	 * @returns		a list of templates that use this template field.
	 */
	def getUses() {
		def templates = Template.findAll();
		def elements;

		if( templates && templates.size() > 0 ) {
			elements = templates.findAll { template -> template.fields.contains( this ) };
		} else {
			return [];
		}

		return elements;
	}

	/**
	 * Checks whether this template field is used in a template and also filled in any instance of that template
	 *
	 * @returns		true iff this template field is used in a template, the template is instantiated
	 *				and an instance has a value for this field. false otherwise
	 */
	def isFilled() {
		// Find all entities that use this template
		def templates = getUses();

		if( templates.size() == 0 )
			return false;

		def c = this.entity.createCriteria()
		def entities = c {
			'in'("template",templates)
		}

		def filledEntities = entities.findAll { entity -> entity.getFieldValue( this.name ) }

		return filledEntities.size() > 0;
	}

	/**
	 * Checks whether this template field is used in the given template and also filled in an instance of that template
	 *
	 * @returns		true iff this template field is used in the given template, the template is instantiated
	 *				and an instance has a value for this field. false otherwise
	 */
	def isFilledInTemplate(Template t) {
		println( "Checking field " + this.name )
		println( "Filled in template: " + t)
		if( t == null ) 
			return false;
			
		// If the template is not used, if can never be filled
		if( !t.fields.contains( this ) )
			return false;

		// Find all entities that use this template
		def entities = entity.findAllByTemplate( t );

		println( "Num entities: " + entities.size() )

		def filledEntities = entities.findAll { entity -> entity.getFieldValue( this.name ) }

		println( "Num filled entities: " + filledEntities.size() )
		println( "Values: " + filledEntities*.getFieldValue( this.name ).join( ', ' ) )
		return filledEntities.size() > 0;
	}

	/**
	 * Check whether a templatefield that is used in a template may still be edited or deleted.
	 * That is possible if the templatefield is never filled and the template is only used in one template
	 *
	 * This method should only be used for templatefields used in a template that is currently shown. Otherwise
	 * the user may edit this template field, while it is also in use in another template than is currently shown.
	 * That lead to confusion.
	 *
	 * @returns true iff this template may still be edited or deleted.
	 */
	def isEditable() {
		return !isFilled() && numUses() == 1;
	}

	/**
	 * Checks whether the given list item is selected in an entity where this template field is used
	 *
	 * @param	item	ListItem to check.
	 * @returns			true iff the list item is part of this template field and the given list
	 *					item is selected in an entity where this template field is used. false otherwise
	 *					Returns false if the type of this template field is other than STRINGLIST
	 */
	def entryUsed(TemplateFieldListItem item) {
		//return numUses() > 0;
	}

	/**
	 * Checks whether a term from the given ontology is selected in an entity where this template field is used
	 *
	 * @param	item	ListItem to check.
	 * @returns			true iff the ontology is part of this template field and a term from the given
	 *					ontology is selected in an entity where this template field is used. false otherwise
	 *					Returns false if the type of this template field is other than ONTOLOGY
	 */
	def entryUsed(Ontology item) {
		//return numUses() > 0;
	}

}