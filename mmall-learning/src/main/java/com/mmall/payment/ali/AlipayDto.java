package com.mmall.payment.ali;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AlipayDto implements Serializable {
    private static final long serialVersionUID = 1579762997659067072L;

    private String service;//接口名称
    private String partner;//合作者身份ID
    private String inputCharset;//参数编码字符集
    private String signType;//签名方式
    private String sign;//签名
    private String notifyUrl;//服务器异步通知页面路径
    private String returnUrl;//页面跳转同步通知页面路径
    private String outTradeNo;//商户网站唯一订单号
    private String subject;//商品名称
    private String paymentType;//支付类型
    private String totalFee;//交易金额
    private String sellerId;//卖家支付宝用户号
    private String sellerEmail;//卖家支付宝账号
    private String sellerAccountName;//卖家支付宝账号别名
    private String buyerId;//买家支付宝用户号
    private String buyerEmail;//买家支付宝账号
    private String buyerAccountName;//买家支付宝账号别名
    private String price;//商品单价
    private String quantity;//购买数量
    private String body;//商品描述
    private String showUrl;//商品展示网址
    private String payMethod;//默认支付方式
    private String enablePaymethod;//支付渠道
    private String antiPhishingKey;//防钓鱼时间戳
    private String exterInvokeIp;//客户端IP
    private String extraCommonParam;//公用回传参数
    private String itBpay;//超时时间
    private String token;//快捷登录授权令牌
    private String qrPayMode;//扫码支付方式
    private String discount;
    private String ouCode;
    private String tradeStatus;
    private String transIn;//结算的银行卡编号，由BD添加银行卡号时填写，收单时传入
    private String  batchNo;//批次号，不可重复
    private String tradeNo;//支付宝流水号
    private String gmtCreate;//交易创建时间 yyyy-MM-dd HH:mm:ss
    private String zfbOpenId;
    private String authCode;//支付授权码 如：28763443825664394
    private String passbackParams;
    
    public String getPassbackParams() {
		return passbackParams;
	}

	public void setPassbackParams(String passbackParams) {
		this.passbackParams = passbackParams;
	}

	public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setZfbOpenId(String zfbOpenId) {
        this.zfbOpenId = zfbOpenId;
    }

    public String getZfbOpenId() {

        return zfbOpenId;
    }

    public String getTradeNo() {

        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {

        return batchNo;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public String getTransIn() {

        return transIn;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus() {

        return tradeStatus;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getOuCode() {

        return ouCode;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {

        return discount;
    }

    public String getService() {
        return service;
    }

    public String getPartner() {
        return partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public String getSign() {
        return sign;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBody() {
        return body;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public String getEnablePaymethod() {
        return enablePaymethod;
    }

    public String getAntiPhishingKey() {
        return antiPhishingKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public String getItBpay() {
        return itBpay;
    }

    public String getToken() {
        return token;
    }

    public String getQrPayMode() {
        return qrPayMode;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public void setEnablePaymethod(String enablePaymethod) {
        this.enablePaymethod = enablePaymethod;
    }

    public void setAntiPhishingKey(String antiPhishingKey) {
        this.antiPhishingKey = antiPhishingKey;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public void setItBpay(String itBpay) {
        this.itBpay = itBpay;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setQrPayMode(String qrPayMode) {
        this.qrPayMode = qrPayMode;
    }

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	
    
}
