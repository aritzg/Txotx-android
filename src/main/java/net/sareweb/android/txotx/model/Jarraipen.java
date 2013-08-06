package net.sareweb.android.txotx.model;

import net.sareweb.lifedroid.model.generic.LDObject;

public class Jarraipen extends LDObject {

	private long jarraipenId;
	private long jarraitzaileUserId;
	private long jarraituaId;
	private String jarraipenMota;
	private long createDate;
	
	public long getJarraipenId() {
		return jarraipenId;
	}
	public void setJarraipenId(long jarraipenId) {
		this.jarraipenId = jarraipenId;
	}
	public long getJarraitzaileUserId() {
		return jarraitzaileUserId;
	}
	public void setJarraitzaileUserId(long jarraitzaileUserId) {
		this.jarraitzaileUserId = jarraitzaileUserId;
	}
	public long getJarraituaId() {
		return jarraituaId;
	}
	public void setJarraituaId(long jarraituaId) {
		this.jarraituaId = jarraituaId;
	}
	public String getJarraipenMota() {
		return jarraipenMota;
	}
	public void setJarraipenMota(String jarraipenMota) {
		this.jarraipenMota = jarraipenMota;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	public static final String JARRAIPEN_MOTA_PERTSONA = "JARRAIPEN_MOTA_PERTSONA";
	public static final String JARRAIPEN_MOTA_SAGARDOTEGIA = "JARRAIPEN_MOTA_SAGARDOTEGIA";
	public static final String JARRAIPEN_MOTA_SAGARDO_EGUNA ="JARRAIPEN_MOTA_SAGARDO_EGUNA";
	
}
