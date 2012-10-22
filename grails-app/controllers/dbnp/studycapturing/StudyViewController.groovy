/**
 * StudyViewController
 *
 * This controller provides the possibility to view
 * and modify a study
 */
package dbnp.studycapturing

import dbnp.authentication.SecUser
import grails.converters.JSON

class StudyViewController {
	def authenticationService
	def studyViewService

	/**
	 * list the studies where the viewer has access to
	 */
	def index = {
		SecUser user = authenticationService.getLoggedInUser()
		def studies = Study.giveReadableStudies( user );

		render(view: "list", model: [studies: studies])
	}

	def view = {
		Long id = (params.containsKey('id') && (params.get('id').toLong()) > 0) ? params.get('id').toLong() : 0

		SecUser user = authenticationService.getLoggedInUser()
		Study study = studyViewService.fetchStudyForCurrentUserWithId(id)

		// got a study?
		if (study) {
			// yes, render the study view page
			render(view: "view", model: [
					study   : study,
					canRead : study.canRead(user),
					canWrite: study.canWrite(user)
			])
		} else {
			// no user and/or no study. As only users can create
			// a new study show the 401 page
			render(view: "/error/_401")
		}
	}

	def ajaxTimeline = {
		SecUser user = authenticationService.getLoggedInUser()
		studyViewService.wrap(params, { study, summary ->
			render(view: "elements/timeline", model: [study: study, summary: summary, canRead: study.canRead(user), canWrite: study.canWrite(user)])
		})
	}

	def ajaxDetails = {
		SecUser user = authenticationService.getLoggedInUser()
		Integer cleanupInDays = RemoveExpiredStudiesJob.studyExpiry
		studyViewService.wrap(params, { study, summary ->
			render(view: "elements/details", model: [
					study: study,
					summary: summary,
					canRead: study.canRead(user),
					canWrite: study.canWrite(user),
					cleanupInDays: cleanupInDays
			])
		})
	}

	def ajaxSubjects = {
		SecUser user = authenticationService.getLoggedInUser()
		studyViewService.wrap(params, { study, summary ->
			render(view: "elements/subjects", model: [subjects: study.subjects, canRead: study.canRead(user), canWrite: study.canWrite(user)])
		})
	}

	def ajaxUpdateStudy = {
		println "update study: ${params}"

		SecUser user = authenticationService.getLoggedInUser()
		String name = (params.containsKey('name')) ? params.get('name') : ''
		String value = (params.containsKey('value')) ? params.get('value') : ''
		String uuid = (params.containsKey('identifier')) ? params.get('identifier') : ''
		List result = []

		def criteria = Study.createCriteria()
		List studies = criteria.list {
			and {
				eq("UUID", uuid)
				and {
					or {
						eq("owner", user)
						writers {
							eq("id", user.id)
						}
					}
				}
			}
		}

		// got a
		if (studies.size()) {
			Study study = studies[0]
			study.cleanup = false

			// update field
			study.setFieldValue(name, value)

			// validate instance
			if (study.validate()) {
				if (study.save()) {
					response.status = 200
				} else {
					response.status = 409
				}
			} else {
				response.status = 412
			}
		} else {
			response.status = 401
		}

		// todo...
		result = []

		// set output headers
		response.contentType = 'application/json;charset=UTF-8'

		if (params.containsKey('callback')) {
			render "${params.callback}(${result as JSON})"
		} else {
			render result as JSON
		}
	}

	def ajaxUpdateSubject = {
		println params

		SecUser user = authenticationService.getLoggedInUser()
		String name = (params.containsKey('name')) ? params.get('name') : ''
		String value = (params.containsKey('value')) ? params.get('value') : ''
		String uuid = (params.containsKey('identifier')) ? params.get('identifier') : ''

		Subject subject = Subject.findWhere(UUID: uuid)
		if (subject) {
			Study study = subject.parent
			if (study.canWrite(user)) {
				// update the subject
				subject.setFieldValue(name, value)

				// validate subject
				if (subject.validate()) {
					if (subject.save()) {
						response.status = 200
					} else {
						response.status = 409
					}
				} else {
					response.status = 412
				}
			} else {
				response.status = 401
			}
		} else {
			response.status = 401
		}

		def result = []

		// set output headers
		response.contentType = 'application/json;charset=UTF-8'

		if (params.containsKey('callback')) {
			render "${params.callback}(${result as JSON})"
		} else {
			render result as JSON
		}
	}
}