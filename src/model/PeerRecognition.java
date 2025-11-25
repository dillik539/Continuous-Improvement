
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.*;


/**
 * 
 * @author dilli
 * JavaFX Model for Peer Recognition records.
 *
 */

public class PeerRecognition {
	private IntegerProperty id;
	private IntegerProperty recognizedUserId;
	private IntegerProperty recognizerUserId;
	
	private StringProperty recognizedUsername;
	private StringProperty recognizerName;
	private StringProperty productLine;
	private StringProperty recognitionText;
	private StringProperty mindset;
	
	private ObjectProperty<LocalDate> recognitionDate;
	
	public PeerRecognition(int id, int recognizedUserId, int recognizerUserId, String recognizedUsername, String recognizerName, String productLine, String recognitionText, String mindset, LocalDate recognitionDate) {
		this.id = new SimpleIntegerProperty(id);
		this.recognizedUserId = new SimpleIntegerProperty(recognizedUserId);
		this.recognizerUserId = new SimpleIntegerProperty(recognizerUserId);
		
		this.recognizedUsername = new SimpleStringProperty(recognizedUsername);
		this.recognizerName = new SimpleStringProperty(recognizerName);
		this.productLine = new SimpleStringProperty(productLine);
		this.recognitionText = new SimpleStringProperty(recognitionText);
		this.mindset = new SimpleStringProperty(mindset);
		
		this.recognitionDate = new SimpleObjectProperty(recognitionDate);
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id.get();
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int value) {
		id.set(value);
	}

	/**
	 * @return the recognizedUserId
	 */
	public int getRecognizedUserId() {
		return recognizedUserId.get();
	}

	/**
	 * @param recognizedUserId the recognizedUserId to set
	 */
	public void setRecognizedUserId(int value) {
		recognizedUserId.set(value);
	}

	/**
	 * @return the recognizerUserId
	 */
	public int getRecognizerUserId() {
		return recognizerUserId.get();
	}

	/**
	 * @param recognizerUserId the recognizerUserId to set
	 */
	public void setRecognizerUserId(int value) {
		recognizerUserId.set(value);
	}

	/**
	 * @return the recognizedUsername
	 */
	public String getRecognizedUsername() {
		return recognizedUsername.get();
	}

	/**
	 * @param recognizedUsername the recognizedUsername to set
	 */
	public void setRecognizedUsername(String value) {
		recognizedUsername.set(value);
	}

	/**
	 * @return the recognizerName
	 */
	public String getRecognizerName() {
		return recognizerName.get();
	}

	/**
	 * @param recognizerName the recognizerName to set
	 */
	public void setRecognizerName(String value) {
		recognizerName.set(value);
	}

	/**
	 * @return the productLine
	 */
	public String getProductLine() {
		return productLine.get();
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(String value) {
		productLine.set(value);
	}

	/**
	 * @return the recognitionText
	 */
	public String getRecognitionText() {
		return recognitionText.get();
	}

	/**
	 * @param recognitionText the recognitionText to set
	 */
	public void setRecognitionText(String value) {
		recognitionText.set(value);
	}

	/**
	 * @return the mindset
	 */
	public String getMindset() {
		return mindset.get();
	}

	/**
	 * @param mindset the mindset to set
	 */
	public void setMindset(String value) {
		mindset.set(value);
	}

	/**
	 * @return the recognitionDate
	 */
	public LocalDate getRecognitionDate() {
		return recognitionDate.get();
	}

	/**
	 * @param recognitionDate the recognitionDate to set
	 */
	public void setRecognitionDate(LocalDate value) {
		recognitionDate.get();
	}

	//Property Accessors (These are required for tableView
	
	public IntegerProperty idProperty() {return id;}
	public IntegerProperty recognizedUserIdProperty() {return recognizedUserId;}
	public IntegerProperty recognizerUserIdProperty() {return recognizerUserId;}
	
	public StringProperty recognizedUsernameProperty() {return recognizedUsername;}
	public StringProperty recognizerNameProperty() {return recognizerName;}
	public StringProperty productLineProperty() {return productLine;}
	public StringProperty recognitionTextProperty() {return recognitionText;}
	public StringProperty mindsetProperty() {return mindset;}
	
	public ObjectProperty<LocalDate> recognitionDateProperty(){return recognitionDate;}

}
