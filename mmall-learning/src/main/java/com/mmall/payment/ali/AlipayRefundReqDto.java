package com.mmall.payment.ali;

/**
 * 退款入参
 * @ClassName: AlipayRefundReqDto 
 * @Description: TODO
 * @author: Administrator
 * @date: 2017年5月22日 下午5:05:20
 */
public class AlipayRefundReqDto{
	
	private String tradeNo;//支付宝交易流水号    （和商户号二选一必填）
	private String out_trade_no;//商户订单号  （和交易流水号二选一必填）
	private String refund_amount;//退款金额   (必填)
	private String out_request_no;//退款号    (标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传)
	private String refund_reason;//退款原因
	private String userId;
	private String payId;
	
	
	
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	public String getOut_request_no() {
		return out_request_no;
	}
	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}
	public String getRefund_reason() {
		return refund_reason;
	}
	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	
	
}
