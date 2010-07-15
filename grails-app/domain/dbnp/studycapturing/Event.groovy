package dbnp.studycapturing

import groovy.time.*

/**
 * The Event class describes an actual event, as it has happened to a certain subject. Often, the same event occurs
 * to multiple subjects at the same time. That is why the actual description of the event is factored out into a more
 * general EventDescription class. Events that also lead to sample(s) should be instantiated as SamplingEvents.
 *
 * Revision information:
 * $Rev$
 * $Author$
 * $Date$
 */
class Event extends TemplateEntity implements Serializable {
	long startTime
	long endTime

	// TODO: assure the Event has a parent study in validate()

	/**
	 * Constraints
	 */
	static constraints = {
		endTime(validator: { fields, obj, errors ->
			def error = false

			// endTime must be >= the startTime
			if (fields && fields.compareTo(obj.startTime) < 0) {
				error = true
				errors.rejectValue(
					'endTime',
					'event.endTime.greaterThanStartTime',
					['endTime', fields] as Object[],
					'End time should be greater than or equal to the Start Time'
				)
			}

			return (!error)
		})
	}

	/**
	 * return the domain fields for this domain class
	 * @return List
	 */
	static List<TemplateField> giveDomainFields() { return Event.domainFields }

	static final List<TemplateField> domainFields = [
		new TemplateField(
			name: 'startTime',
			type: TemplateFieldType.RELTIME,
			comment: "Please enter the start time as a relative time from study start date. "+RelTime.getHelpText()),
		new TemplateField(
			name: 'endTime',
			type: TemplateFieldType.RELTIME,
			comment: "Please enter the end time as a relative time from study start date. "+RelTime.getHelpText())
	]

	/**
	 * get the duration
	 * @return RelTime
	 */
	def getDuration() {
		return new RelTime(startTime, endTime)
	}

	/**
	 * Get extended, human readable string representing the duration between startTime and endTime 
     *
	 * @return String
	 */
	def getDurationString() {
		return new RelTime(startTime, endTime).toPrettyRoundedString();
	}

	/**
	 * get a prettified duration
	 * @return String
	 */
	static def getPrettyDuration(RelTime duration) {
		return duration.toPrettyRoundedString();
	}

	def getPrettyDuration() {
		getPrettyDuration(getDuration())
	}

	/**
	 * Get short, human readable string representing the duration between startTime and endTime 
     *
	 * @return String
	 */
	def getShortDuration() {
		return new RelTime(startTime, endTime).toPrettyRoundedString();
	}

	/**
	 *    Return whether this is SamplingEvent
	 * @return    boolean
	 */
	def isSamplingEvent() {
		return (this instanceof SamplingEvent)
	}

	def belongsToGroup(Collection<EventGroup> groups) {
		def eventFound = false;
		def that = this;
		groups.each { eventgroup ->
			if (!eventFound) {
				eventFound = (that.id in eventgroup.events.id);
			}
		}

		return eventFound;
	}

	def String toString() {
		return fieldExists('Description') ? getFieldValue('Description') : ""
	}
}
