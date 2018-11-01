package com.mmall.payment.ali;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AlipayRefundDto implements Serializable {
    private static final long serialVersionUID = 2383199094976590371L;
    private String service;//接口名称
    private String partner;//合作者身份ID
    private String inputCharset;//参数编码字符集
    private String signType;//签名方式
    private String sign;//签名
    private String notifyUrl;//服务器异步通知页面路径
    private String sellerEmail;//卖家支付宝账号
    private String sellerUserId;//卖家用户ID

    @Override
    public String toString() {
        return "AlipayRefundDto{" +
                "service='" + service + '\'' +
                ", partner='" + partner + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", sellerUserId='" + sellerUserId + '\'' +
                ", refundDate='" + refundDate + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", batchNum='" + batchNum + '\'' +
                ", detailData='" + detailData + '\'' +
                ", ouCode='" + ouCode + '\'' +
                ", isSuccess='" + isSuccess + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    private String refundDate;//退款请求时间
    private String batchNo;//退款批次号
    private String batchNum;//总笔数
    private String detailData;//单笔数据集
    private String ouCode;

    private String isSuccess;
    private String error;

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIsSuccess() {

        return isSuccess;
    }

    public String getError() {
        return error;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getOuCode() {

        return ouCode;
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

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public String getDetailData() {
        return detailData;
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

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }
}
