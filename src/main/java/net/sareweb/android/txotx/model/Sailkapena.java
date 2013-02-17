package net.sareweb.android.txotx.model;

import net.sareweb.lifedroid.model.generic.LDObject;

public class Sailkapena extends LDObject {

	private long sailkapenaId;
	private long userId;
	private String screenName;

	private long iruzkinKopurua;
	private long balorazioKopurua;
	private long argazkiKopurua;
	private long gertaeraKopurua;
	public long getSailkapenaId() {
		return sailkapenaId;
	}
	public void setSailkapenaId(long sailkapenaId) {
		this.sailkapenaId = sailkapenaId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public long getIruzkinKopurua() {
		return iruzkinKopurua;
	}
	public void setIruzkinKopurua(long iruzkinKopurua) {
		this.iruzkinKopurua = iruzkinKopurua;
	}
	public long getBalorazioKopurua() {
		return balorazioKopurua;
	}
	public void setBalorazioKopurua(long balorazioKopurua) {
		this.balorazioKopurua = balorazioKopurua;
	}
	public long getArgazkiKopurua() {
		return argazkiKopurua;
	}
	public void setArgazkiKopurua(long argazkiKopurua) {
		this.argazkiKopurua = argazkiKopurua;
	}
	public long getGertaeraKopurua() {
		return gertaeraKopurua;
	}
	public void setGertaeraKopurua(long gertaeraKopurua) {
		this.gertaeraKopurua = gertaeraKopurua;
	}
	
}
