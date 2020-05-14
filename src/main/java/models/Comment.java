package models;

import java.io.Serializable;

public class Comment implements Serializable {
	
	private static final long serialVersionUID = 3004879294918214266L;
	
	public String content;
	public Person person;
	
	public Comment(String newContent, Person newPerson) {
		this.content = newContent;
		this.person = newPerson;
	}

	@Override
	public String toString() {
		return person.username + ": " + content;
	}

	//getter & setter
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
}
