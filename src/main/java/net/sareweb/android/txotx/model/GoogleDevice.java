package net.sareweb.android.txotx.model;

import net.sareweb.lifedroid.model.generic.LDObject;

public class GoogleDevice extends LDObject {

	private long googleDeviceId;
	
	private long userId;
	private String emailAddress;
	private String registrationId;
	
	private long createDate;
	private long modifiedDate;
	
	public long getGoogleDeviceId() {
		return googleDeviceId;
	}
	public void setGoogleDeviceId(long googleDeviceId) {
		this.googleDeviceId = googleDeviceId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
