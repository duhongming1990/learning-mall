package com.mmall.payment.ali;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by kwg on 2016/5/4.
 */
public class AlipayBillDto implements Serializable {
	public static final Integer ALI_TRADETYPE_PAID = 1;
	public static final Integer ALI_TRADETYPE_REFUND = 2;

    private static final long serialVersionUID = 110009845787328624L;

    //    private String billTradeNo; //账务流水号
    private String aliTradeNo; // 业务流水号
    private String shopTradeNo; // 商户订单号
    private String shopBsnsAccount; //支付宝商务号
    private String userAccount; // 对方支付宝账号
    private String tradeProName; // 商品名称
    private String tradeTime; // 发生时间2016/4/21 0:00
    private BigDecimal incomeAmount; // 金额（+元）
    private BigDecimal refundAmount; //支出金额（-元）
    private BigDecimal accountBalance; // 账户余额（元）
    private String bsnsType; // 业务类型(在线支付)
    private String remark; // 备注
    private Integer tradetype; //1收入； 2支出
    private String shopRefundTradeNo;//商户退款订单号
	public String getAliTradeNo() {
		return aliTradeNo;
	}
	public void setAliTradeNo(String aliTradeNo) {
		this.aliTradeNo = aliTradeNo;
	}
	public String getShopTradeNo() {
		return shopTradeNo;
	}
	public void setShopTradeNo(String shopTradeNo) {
		this.shopTradeNo = shopTradeNo;
	}
	public String getShopBsnsAccount() {
		return shopBsnsAccount;
	}
	public void setShopBsnsAccount(String shopBsnsAccount) {
		this.shopBsnsAccount = shopBsnsAccount;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getTradeProName() {
		return tradeProName;
	}
	public void setTradeProName(String tradeProName) {
		this.tradeProName = tradeProName;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}
	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getBsnsType() {
		return bsnsType;
	}
	public void setBsnsType(String bsnsType) {
		this.bsnsType = bsnsType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getTradeType() {
		return tradetype;
	}
	public void setTradeType(Integer tradetype) {
		this.tradetype = tradetype;
	}
	public String getShopRefundTradeNo() {
		return shopRefundTradeNo;
	}
	public void setShopRefundTradeNo(String shopRefundTradeNo) {
		this.shopRefundTradeNo = shopRefundTradeNo;
	}
    
}
