
package dbnp.user

class User extends grails.plugins.nimble.core.UserBase implements Serializable {

	// Extend UserBase with your custom values here

	def String toString() {
		return username;
	}

	long giveId() {
		//Long val = this.id;
		return (long)ident();
	}

}