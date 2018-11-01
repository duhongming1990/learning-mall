package com.mmall.payment.ali;

import java.io.Serializable;


public class AliPayMch implements Serializable{
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 6550410638192217709L;
	private String appId ;
	private String partner ;
	private String privateKey;
	private String publicKey ;
	private String notifyUrl ;
	private String pcprivateKey;
	private String pcnotifyUrl;
	private String pcpublicKey;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getPcprivateKey() {
		return pcprivateKey;
	}
	public void setPcprivateKey(String pcprivateKey) {
		this.pcprivateKey = pcprivateKey;
	}
	public String getPcnotifyUrl() {
		return pcnotifyUrl;
	}
	public void setPcnotifyUrl(String pcnotifyUrl) {
		this.pcnotifyUrl = pcnotifyUrl;
	}
	public String getPcpublicKey() {
		return pcpublicKey;
	}
	public void setPcpublicKey(String pcpublicKey) {
		this.pcpublicKey = pcpublicKey;
	}
	
	

}
