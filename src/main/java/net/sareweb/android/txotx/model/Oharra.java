package net.sareweb.android.txotx.model;

import net.sareweb.lifedroid.model.generic.LDObject;

public class Oharra extends LDObject {

	private long oharraId;
	private String oharMota;
	private long irudiKarpetaId;
	private String irudia;
	private String izenburua;
	private String testua;
	private String esteka;
	private String estekaTestua;

	private long createDate;

	public long getOharraId() {
		return oharraId;
	}

	public void setOharraId(long oharraId) {
		this.oharraId = oharraId;
	}

	public String getOharMota() {
		return oharMota;
	}

	public void setOharMota(String oharMota) {
		this.oharMota = oharMota;
	}

	public long getIrudiKarpetaId() {
		return irudiKarpetaId;
	}

	public void setIrudiKarpetaId(long irudiKarpetaId) {
		this.irudiKarpetaId = irudiKarpetaId;
	}

	public String getIrudia() {
		return irudia;
	}

	public void setIrudia(String irudia) {
		this.irudia = irudia;
	}

	public String getIzenburua() {
		return izenburua;
	}

	public void setIzenburua(String izenburua) {
		this.izenburua = izenburua;
	}

	public String getTestua() {
		return testua;
	}

	public void setTestua(String testua) {
		this.testua = testua;
	}

	public String getEsteka() {
		return esteka;
	}

	public void setEsteka(String esteka) {
		this.esteka = esteka;
	}

	public String getEstekaTestua() {
		return estekaTestua;
	}

	public void setEstekaTestua(String estekaTestua) {
		this.estekaTestua = estekaTestua;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
}
