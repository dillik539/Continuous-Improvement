package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Idea {
	private final SimpleIntegerProperty userID;
	private final SimpleStringProperty ideaText;
	private final SimpleStringProperty benefits;
	private final SimpleStringProperty comments;
	private final SimpleStringProperty dateSubmitted;
	private final SimpleStringProperty status;

	public Idea(Integer userID, String ideaText,String benefits, String comments, String dateSubmitted, String status) {
		this.userID = new SimpleIntegerProperty(userID);
		this.ideaText = new SimpleStringProperty(ideaText);
		this.benefits = new SimpleStringProperty(benefits);
		this.comments = new SimpleStringProperty(comments);
		this.dateSubmitted = new SimpleStringProperty(dateSubmitted);
		this.status = new SimpleStringProperty(status);
	}
	
	public Integer getUserID() {
		return userID.get();
	}

	public String getIdeaText() {
		return ideaText.get();
	}

	public void setIdeaText(String value) {
		ideaText.set(value);
	}

	public String getBenefits() {
		return benefits.get();
	}

	public void setBenefits(String value) {
		benefits.set(value);
	}
	public String getComments() {
		return comments.get();
	}
	
	public void setComments(String value) {
		comments.set(value);
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
