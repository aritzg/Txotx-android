package net.sareweb.android.txotx.model;

import net.sareweb.lifedroid.model.generic.LDObject;

public class APKVersion extends LDObject {

	private long apkVersionId;
	private Integer supportedMinVersion;
	private String supportedMinVersionName;
	private Integer currentVersion;
	private String currentVersionName;
	private long createDate;
	
	public long getApkVersionId() {
		return apkVersionId;
	}
	public void setApkVersionId(long apkVersionId) {
		this.apkVersionId = apkVersionId;
	}
	public Integer getSupportedMinVersion() {
		return supportedMinVersion;
	}
	public void setSupportedMinVersion(Integer supportedMinVersion) {
		this.supportedMinVersion = supportedMinVersion;
	}
	public String getSupportedMinVersionName() {
		return supportedMinVersionName;
	}
	public void setSupportedMinVersionName(String supportedMinVersionName) {
		this.supportedMinVersionName = supportedMinVersionName;
	}
	public Integer getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(Integer currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getCurrentVersionName() {
		return currentVersionName;
	}
	public void setCurrentVersionName(String currentVersionName) {
		this.currentVersionName = currentVersionName;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

}
