package model;

import javafx.beans.property.SimpleStringProperty;

public class Idea {
	private final SimpleStringProperty shortDescription;
	private final SimpleStringProperty fullIdea;
	private final SimpleStringProperty dateSubmitted;
	private final SimpleStringProperty status;

	public Idea(String shortDescription, String fullIdea, String dateSubmitted, String status) {
		this.shortDescription = new SimpleStringProperty(shortDescription);
		this.fullIdea = new SimpleStringProperty(fullIdea);
		this.dateSubmitted = new SimpleStringProperty(dateSubmitted);
		this.status = new SimpleStringProperty(status);
	}

	public String getShortDescription() {
		return shortDescription.get();
	}

	public void setShortDescription(String value) {
		shortDescription.set(value);
	}

	public String getFullIdea() {
		return fullIdea.get();
	}

	public void setFullIdea(String value) {
		fullIdea.set(value);
	}

	public String getDateSubmitted() {
		return dateSubmitted.get();
	}

	public void setDateSubmitted(String value) {
		dateSubmitted.set(value);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String value) {
		status.set(value);
	}
}
