package com.mmall.payment.ali;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AlipayReturnDto implements Serializable {

    private static final long serialVersionUID = -5137885932789504632L;

    private String isSuccess;//成功标识
    private String signType;//签名方式
    private String sign;//签名
    private String outTradeNo;//商户网站唯一订单号
    private String subject;//商品名称
    private String paymentType;//支付类型
    private String exterFace;//接口名称
    private String tradeNo;//支付宝交易号
    private String tradeStatus;//交易状态
    private String notifyId;//通知校验ID
    private String notifyTime;//通知时间
    private String notifyType;//通知类型
    private String sellerEmail;//卖家支付宝账号
    private String buyerEmail;//买家支付宝账号
    private String sellerId;//卖家支付宝账户号
    private String buyerId;//买家支付宝账户号
    private String totalFee;//交易金额
    private String body;//商品描述
    private String extraCommonParam;//公用回传参数

    public String getIsSuccess() {
        return isSuccess;
    }

    public String getSignType() {
        return signType;
    }

    public String getSign() {
        return sign;
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

    public String getExterFace() {
        return exterFace;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getBody() {
        return body;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public void setExterFace(String exterFace) {
        this.exterFace = exterFace;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    @Override
    public String toString() {
        return "AlipayReturnDto{" +
                "isSuccess='" + isSuccess + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", subject='" + subject + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", exterFace='" + exterFace + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", notifyId='" + notifyId + '\'' +
                ", notifyTime='" + notifyTime + '\'' +
                ", notifyType='" + notifyType + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", body='" + body + '\'' +
                ", extraCommonParam='" + extraCommonParam + '\'' +
                '}';
    }
}
