//package com.mmall.payment.ali;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.AlipayConstants;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.*;
//import com.alipay.api.response.*;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import sunbox.core.util.DateUtils;
//import sunbox.core.util.IOUtility;
//import sunbox.core.util.JSONUtility;
//import sunbox.core.util.StringUtils;
//import sunbox.core.util.io.DownLoadUtility;
//import sunbox.core.util.pay.ListTools;
//import sunbox.core.util.pay.PayOrderOrUserIdUtil;
//import sunbox.core.util.pay.alipay.AlipayConfig;
//import sunbox.core.util.pay.alipay.AlipaySubmit;
//import sunbox.core.util.pay.wechatpay.BillFileUtil;
//import sunbox.core.util.pay.wechatpay.EnumList;
//import sunbox.core.util.pay.wechatpay.PropertiesUtilWeChat;
//import sunbox.core.vo.fin.Deposite;
//import sunbox.core.vo.order.OrderRcardSale;
//import sunbox.core.vo.pay.*;
//import sunbox.core.vo.pay.payment.PaymentParamsVo;
//import sunbox.core.vo.pay.payment.wechat.ResultMsg;
//import sunbox.core.vo.sys.Dict;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
//public class AliPayment extends PayItem {
//	private Logger logger = Logger.getLogger(this.getClass());
//
//	/**
//	 * 生成 支付宝APP支付URL
//	 *
//	 * @Title: paymentApp
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: String
//	 */
//	public ResultMsg<String> paymentApp(AlipayDto alipayDto) throws Exception {
//		ResultMsg<String> result = new ResultMsg<String>();
//		logger.info(String.format("[%s]:: 支付宝APP支付入参[%s]", "paymentApp", JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//		if (StringUtils.isBlank(out_trade_no)) {
//			result.setMsg("订单号为空");
//			result.setResult(false);
//			return result;
//		}
//		String subject = String.valueOf(alipayDto.getSubject());
//		if (StringUtils.isBlank(subject)) {
//			result.setMsg("商品描述为空");
//			result.setResult(false);
//			return result;
//		}
//		// 付款金额，必填
//		String total_fee = String.valueOf(alipayDto.getTotalFee());
//		if (StringUtils.isBlank(total_fee)) {
//			result.setMsg("付款金额为空");
//			result.setResult(false);
//			return result;
//		}
//		String appId = "";
//		String notify_url = "";
//		if (out_trade_no.contains("X")) {
//			// 商户号、appid 至少传一个
//			 appId = getAppConfig().getConfig(EnumList.AliPayParam.APPID.value);
//			// 服务器异步通知URL 必填
//			 notify_url = getAppConfig().getConfig(EnumList.AliPayParam.NOTIFYURL.value);
//		}else if(out_trade_no.contains("E")){
//			// 商户号、appid 至少传一个
//			 appId = getAppConfig().getConfig(EnumList.EcardAliPayParam.APPID.value);
//			// 服务器异步通知URL 必填
//			 notify_url = getAppConfig().getConfig(EnumList.EcardAliPayParam.NOTIFYURL.value);
//		}
//
//		if (StringUtils.isBlank(appId)) {
//			result.setMsg("appid为空");
//			result.setResult(false);
//			return result;
//		}
//		if (StringUtils.isBlank(notify_url)) {
//			result.setMsg("服务器异步通知URL为空");
//			result.setResult(false);
//			return result;
//		}
//		Map<String, String> sParaTemp = new HashMap<String, String>();
//		// 公共参数 start
//		sParaTemp.put("app_id", appId);// 支付宝分配给开发者的应用ID
//		sParaTemp.put("method", AlipayConfig.serviceApp);// 接口名称
//		sParaTemp.put("charset", AlipayConfig.input_charset);// 请求使用的编码格式，如utf-8,gbk,gb2312等
//		sParaTemp.put("timestamp", DateUtils.getNowDate());// 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
//		sParaTemp.put("version", "1.0");// 调用的接口版本，固定为：1.0
//		sParaTemp.put("notify_url", notify_url);// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
//		sParaTemp.put("sign_type", alipayDto.getSignType());
//		// 业务参数 start
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//		jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//		jsonObject.put("total_amount", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//
//		if (out_trade_no.contains("X")) {
//			jsonObject.put("product_code", "UICK_MSECURITY_PAY");// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
//		}else if(out_trade_no.contains("E")){
//			jsonObject.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
//		}
//		// 业务参数 end
//
//		sParaTemp.put("biz_content", jsonObject.toString());// 业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
//		// 公共参数 end
//		String payStrResp ="";
//		if (out_trade_no.contains("X")) {
//			payStrResp = AlipaySubmit.buildRequest(sParaTemp, EnumList.PayModeSub.ALIAPP.value);
//		}else if(out_trade_no.contains("E")){
//			payStrResp = AlipaySubmit.EcardBuildRequest(sParaTemp, EnumList.PayModeSub.ALIAPP.value);
//		}
//
//		if (StringUtils.isNotBlank(payStrResp)) {
//			result.setData(payStrResp);
//			result.setResult(true);
//		}
//		logger.info(String.format("[%s]:: 支付宝APP支付响应[%s]", "paymentApp", payStrResp));
//		return result;
//	}
//
//	/**
//	 * 生成 支付宝即时到账支付URL
//	 *
//	 * @Title: paymentApp
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: String
//	 */
//	public ResultMsg<String> paymentInstantTransfer(AlipayDto alipayDto) throws Exception {
//		ResultMsg<String> result = new ResultMsg<String>();
//		logger.info(String.format("[%s]:: 支付宝即时到账支付入参[%s]", "paymentApp", JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//		if (StringUtils.isBlank(out_trade_no)) {
//			result.setMsg("订单号为空");
//			result.setResult(false);
//			return result;
//		}
//		String subject = String.valueOf(alipayDto.getSubject());
//		if (StringUtils.isBlank(subject)) {
//			result.setMsg("商品描述为空");
//			result.setResult(false);
//			return result;
//		}
//		// 付款金额，必填
//		String total_fee = String.valueOf(alipayDto.getTotalFee());
//		if (StringUtils.isBlank(total_fee)) {
//			result.setMsg("付款金额为空");
//			result.setResult(false);
//			return result;
//		}
//		// appid 必填
//		String partner = getAppConfig().getConfig(EnumList.AliPayParam.PARTNER.value);
//		if (StringUtils.isBlank(partner)) {
//			result.setMsg("partner为空");
//			result.setResult(false);
//			return result;
//		}
//		// 服务器异步通知URL 必填
//		String notify_url = getAppConfig().getConfig(EnumList.AliPayParam.PCNOTIFYURL.value);
//		if (StringUtils.isBlank(notify_url)) {
//			result.setMsg("服务器异步通知URL为空");
//			result.setResult(false);
//			return result;
//		}
//
//		Map<String, String> sParaTemp = new HashMap<String, String>();
//		// 公共参数 start
//		sParaTemp.put("partner", partner);// 支付宝分配给开发者的应用ID
//		sParaTemp.put("service", AlipayConfig.serviceInstantTransfer);// 接口名称
//		sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 请求使用的编码格式，如utf-8,gbk,gb2312等
//		sParaTemp.put("notify_url", notify_url);// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
//		sParaTemp.put("return_url", alipayDto.getReturnUrl());// 支付返回页面URL
//		sParaTemp.put("sign_type", alipayDto.getSignType());
//		// 公共参数 end
//
//		// 业务参数 start
//		sParaTemp.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//		sParaTemp.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//		sParaTemp.put("payment_type", "1");// 只支持取值为1（商品购买）
//		sParaTemp.put("total_fee", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//		sParaTemp.put("seller_id", partner);// 卖家支付宝用户号
//		sParaTemp.put("seller_account_name", alipayDto.getSellerAccountName());// 卖家支付宝账号别名
//		sParaTemp.put("seller_email", alipayDto.getSellerEmail());// 卖家支付宝账号
//		// 业务参数 end
//
//		String payStrResp = AlipaySubmit.buildRequest(sParaTemp, EnumList.PayModeSub.ALIINSTANTTRANSFER.value);
//		if (StringUtils.isNotBlank(payStrResp)) {
//			result.setData(payStrResp);
//			result.setResult(true);
//		}
//		logger.info(String.format("[%s]:: 支付宝即时到账支付响应[%s]", "paymentApp", payStrResp));
//		return result;
//	}
//
//	/**
//	 * 条码支付(收银员使用扫码设备读取用户手机支付宝“付款码”/声波获取设备（如麦克风）读取用户手机支付宝的声波信息后，将二维码或条码信息/
//	 * 声波信息通过本接口上送至支付宝发起支付。)
//	 *
//	 * @Title: paymentBarcode
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: ResultMsg<String>
//	 */
//	public ResultMsg<AlipayTradePayResponse> paymentBarcode(AlipayDto alipayDto){
//		ResultMsg<AlipayTradePayResponse> result = new ResultMsg<AlipayTradePayResponse>();
//		logger.info(
//				String.format("[%s]:: 支付宝条码支付入参[%s]", "paymentBarcode", JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		try {
//			String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//			if (StringUtils.isBlank(out_trade_no)) {
//				result.setMsg("订单号为空");
//				result.setResult(false);
//				return result;
//			}
//			String subject = String.valueOf(alipayDto.getSubject());
//			if (StringUtils.isBlank(subject)) {
//				result.setMsg("商品描述为空");
//				result.setResult(false);
//				return result;
//			}
//			// 付款金额，必填
//			String total_fee = String.valueOf(alipayDto.getTotalFee());
//			if (StringUtils.isBlank(total_fee)) {
//				result.setMsg("付款金额为空");
//				result.setResult(false);
//				return result;
//			}
//			// 支付授权码
//			String uthCode = String.valueOf(alipayDto.getAuthCode());
//			if (StringUtils.isBlank(uthCode)) {
//				result.setMsg("支付授权码为空");
//				result.setResult(false);
//				return result;
//			}
//
//			AlipayClient alipayClient = null;
//			if (out_trade_no.contains("X")) {
//				// appid 必填
//				String partner = getAppConfig().getConfig(EnumList.AliPayParam.PARTNER.value);
//				if (StringUtils.isBlank(partner)) {
//					result.setMsg("appid为空");
//					result.setResult(false);
//					return result;
//				}
//				 alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//						getAppConfig().getConfig(EnumList.AliPayParam.APPID.value),
//						getAppConfig().getConfig(EnumList.AliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//						AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.AliPayParam.PUBLICKEY.value),
//						AlipayConstants.SIGN_TYPE_RSA); // 获得初始化的AlipayClient
//			}else if(out_trade_no.contains("E")){
//				// appid 必填
//				String partner = getAppConfig().getConfig(EnumList.EcardAliPayParam.APPID.value);//感觉没用阿？？？
//				if (StringUtils.isBlank(partner)) {
//					result.setMsg("appid为空");
//					result.setResult(false);
//					return result;
//				}
//				 alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//						getAppConfig().getConfig(EnumList.EcardAliPayParam.APPID.value),
//						getAppConfig().getConfig(EnumList.EcardAliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//						AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.EcardAliPayParam.PUBLICKEY.value),
//						AlipayConstants.SIGN_TYPE_RSA2); // 获得初始化的AlipayClient
//			}
//			// 创建API对应的request类
//			AlipayTradePayRequest request = new AlipayTradePayRequest();
//
//			// 业务参数 start
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//			jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//			jsonObject.put("scene", "bar_code");// 支付场景 条码支付，取值：bar_code
//												// 声波支付，取值：wave_code
//			jsonObject.put("total_amount", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//			jsonObject.put("auth_code", uthCode);// 支付授权码
//
//			// 业务参数 end
//			request.setBizContent(jsonObject.toString()); // 设置业务参数
//			// 通过alipayClient调用API，获得对应的response类
//			AlipayTradePayResponse response = alipayClient.execute(request);
//			// 根据response中的结果继续业务逻辑处理
//			// 10000 支付成功 40004 支付失败 10003 等待用户付款 20000 未知异常
//			if (response != null) {
//				logger.info(String.format("[%s]:: 支付宝条码支付响应[%s]", "paymentBarcode", response.getCode()));
//				if (response.getCode().equals("10000")) {
//					result.setMsg("支付成功");
//					result.setResult(true);
//					result.setData(response);
//				} else if (response.getCode().equals("40004")) {
//					result.setMsg("支付失败");
//					result.setResult(false);
//					result.setData(response);
//				} else if (response.getCode().equals("10003")) {
//					result.setMsg("等待用户付款");
//					result.setNeedRetry(true);
//					result.setResult(false);
//					result.setData(response);
//				} else if (response.getCode().equals("20000")) {
//					result.setMsg("未知异常");
//					result.setNeedRetry(true);
//					result.setResult(false);
//					result.setData(response);
//				}
//			}
//			else {
//				result.setMsg("返回为空");
//				result.setResult(false);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			result.setMsg(e.getMessage());
//			result.setResult(false);
//			result.setNeedRetry(false);
//		}
//		logger.info(String.format("[%s]:: 支付宝条码支付响应[%s]", "paymentBarcode", JSONUtility.objectToJson(result)));
//		return result;
//	}
//
//
//	/**
//	 * 支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；如果用户支付成功，支付宝系统会将此订单资金退还给用户。 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
//	 * @param out_trade_no 商户订单号
//	 * @param transaction_id 第三方订单号
//	 * @return
//	 * @throws AlipayApiException
//	 */
//	public ResultMsg<String> paymentCancel(String out_trade_no, String transaction_id){
//		ResultMsg resultMsg = new ResultMsg();
//		logger.info(String.format("[%s]:: 支付宝撤销订单入参[out_trade_no=%s,transaction_id=%s]", "paymentCancel", out_trade_no, transaction_id));
//
//		try {
//			if(StringUtils.isBlank(out_trade_no) && StringUtils.isBlank(transaction_id)) {
//				logger.info(String.format("[%s]:: 支付宝无效的入参[out_trade_no=%s,transaction_id=%s]", "orderQuery", out_trade_no, transaction_id));
//				resultMsg.setResult(false);
//				resultMsg.setMsg("无效的入参");
//				return resultMsg;
//			}
//
//			// 获取商户号信息
//			AliPayMch aliPayMch = PaymentParamsVo.getAliParams();
//			if(aliPayMch == null) {
//				logger.info(String.format("[%s]:: 商户信息为空[out_trade_no=%s,transaction_id=%s]", "orderQuery", out_trade_no, transaction_id));
//				resultMsg.setResult(false);
//				resultMsg.setMsg("商户信息为空");
//				return resultMsg;
//			}
//			AlipayClient alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//					aliPayMch.getAppId(), aliPayMch.getPrivateKey(), AlipayConstants.FORMAT_JSON,
//					AlipayConstants.CHARSET_UTF8, aliPayMch.getPublicKey(), AlipayConstants.SIGN_TYPE_RSA);
//			AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
//
//			// 业务参数 start
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//			jsonObject.put("trade_no", transaction_id);// 支付宝交易流水号
//			request.setBizContent(jsonObject.toString());// 填充业务参数
//
//			AlipayTradeCancelResponse response = (AlipayTradeCancelResponse) alipayClient.execute(request);
//			if (response != null) {
//				if (response.getCode().equals("10000")) {
//					resultMsg.setMsg("撤销订单接口成功");
//					resultMsg.setData(JSONUtility.objectToJson(response));
//					resultMsg.setResult(true);
//				} else {
//					resultMsg.setMsg("撤销订单接口失败,code:" + response.getMsg());
//					resultMsg.setData(JSONUtility.objectToJson(response));
//					resultMsg.setResult(false);
//				}
//			} else {
//				resultMsg.setMsg("撤销订单接口返回为空");
//				resultMsg.setResult(false);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			resultMsg.setMsg(e.getMessage());
//			resultMsg.setResult(false);
//		}
//		logger.info(String.format("[%s]:: 支付宝撤销订单响应[%s]", "paymentCancel", JSONUtility.objectToJson(resultMsg)));
//
//		return resultMsg;
//	}
//
//	/**
//	 * 手机网站支付
//	 *
//	 * @Title: paymentApp
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: String
//	 */
//	public ResultMsg<String> paymentPhoneWeb(AlipayDto alipayDto) throws Exception {
//		ResultMsg<String> result = new ResultMsg<String>();
//		logger.info(String.format("[%s]:: 支付宝手机网站支付入参[%s]", "paymentPhoneWeb",
//				JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//		if (StringUtils.isBlank(out_trade_no)) {
//			result.setMsg("订单号为空");
//			result.setResult(false);
//			return result;
//		}
//		String subject = String.valueOf(alipayDto.getSubject());
//		if (StringUtils.isBlank(subject)) {
//			result.setMsg("商品描述为空");
//			result.setResult(false);
//			return result;
//		}
//		// 付款金额，必填
//		String total_fee = String.valueOf(alipayDto.getTotalFee());
//		if (StringUtils.isBlank(total_fee)) {
//			result.setMsg("付款金额为空");
//			result.setResult(false);
//			return result;
//		}
//		// 商户号、appid 至少传一个
//		String appId = getAppConfig().getConfig(EnumList.AliPayParam.APPID.value);
//		if (StringUtils.isBlank(appId)) {
//			result.setMsg("appid为空");
//			result.setResult(false);
//			return result;
//		}
//		// 服务器异步通知URL 手机网站支付 回调地址允许为空
//		String notify_url = getAppConfig().getConfig(EnumList.AliPayParam.NOTIFYURL.value);
//		if (StringUtils.isBlank(notify_url)) {
//			result.setMsg("服务器异步通知URL为空");
//			result.setResult(false);
//			return result;
//		}
//
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW, appId,
//				getAppConfig().getConfig(EnumList.AliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//				AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.AliPayParam.PUBLICKEY.value),
//				AlipayConstants.SIGN_TYPE_RSA); // 获得初始化的AlipayClient
//		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
//		alipayRequest.setReturnUrl(alipayDto.getReturnUrl());
//		alipayRequest.setNotifyUrl(notify_url);// 在公共参数中设置回跳和通知地址
//		logger.info("支付宝手机网站支付："+getAppConfig().getConfig(EnumList.AliPayParam.PRIVATEKEY.value));
////		Map<String, String> sParaTemp = new HashMap<String, String>();
////		// 公共参数 start
////		sParaTemp.put("app_id", appId);// 支付宝分配给开发者的应用ID
////		sParaTemp.put("method", AlipayConfig.serviceApp);// 接口名称
////		sParaTemp.put("charset", AlipayConfig.input_charset);// 请求使用的编码格式，如utf-8,gbk,gb2312等
////		sParaTemp.put("timestamp", DateUtils.getNowDate());// 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
////		sParaTemp.put("version", "1.0");// 调用的接口版本，固定为：1.0
////		sParaTemp.put("notify_url", notify_url);// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
////		sParaTemp.put("sign_type", alipayDto.getSignType());
//
//		// 业务参数 start
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//		jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//		jsonObject.put("total_amount", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//		jsonObject.put("product_code", "QUICK_WAP_WAY");// 销售产品码，商家和支付宝签约的产品码
//		alipayRequest.setBizContent(jsonObject.toString());// 填充业务参数
//		// 业务参数 end
//
//		logger.info("支付宝手机网站充值 调用SDK生成表单前的入参："+JSONUtility.objectToJson(alipayRequest));
//		String form = "";
//		try {
//			form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//		}
//
//		if (StringUtils.isNotBlank(form)) {
//			result.setData(form);
//			result.setResult(true);
//		}
//		logger.info(String.format("[%s]:: 支付宝手机网站支付响应[%s]", "paymentPhoneWeb", form));
//		return result;
//	}
//
//	/**
//	 * 支付宝当面付(用户用支付宝扫商户二维码支付)
//	 *
//	 * @Title: paymentApp
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: String
//	 */
//	public ResultMsg<String> paymentQrCode(AlipayDto alipayDto) throws Exception {
//		ResultMsg<String> result = new ResultMsg<String>();
//		logger.info(String.format("[%s]:: 支付宝当面付入参[%s]", "paymentQrCode", JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//		if (StringUtils.isBlank(out_trade_no)) {
//			result.setMsg("订单号为空");
//			result.setResult(false);
//			return result;
//		}
//		String subject = String.valueOf(alipayDto.getSubject());
//		if (StringUtils.isBlank(subject)) {
//			result.setMsg("商品描述为空");
//			result.setResult(false);
//			return result;
//		}
//		// 付款金额，必填
//		String total_fee = String.valueOf(alipayDto.getTotalFee());
//		if (StringUtils.isBlank(total_fee)) {
//			result.setMsg("付款金额为空");
//			result.setResult(false);
//			return result;
//		}
//		// 商户号、appid 至少传一个
//		String appId = getAppConfig().getConfig(EnumList.AliPayParam.APPID.value);
//		if (StringUtils.isBlank(appId)) {
//			result.setMsg("appid为空");
//			result.setResult(false);
//			return result;
//		}
//		// 服务器异步通知URL 必填
//		String notify_url = getAppConfig().getConfig(EnumList.AliPayParam.NOTIFYURL.value);
//		if (StringUtils.isBlank(notify_url)) {
//			result.setMsg("服务器异步通知URL为空");
//			result.setResult(false);
//			return result;
//		}
//
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW, appId,
//				getAppConfig().getConfig(EnumList.AliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//				AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.AliPayParam.PUBLICKEY.value),
//				AlipayConstants.SIGN_TYPE_RSA2); // 获得初始化的AlipayClient
//		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();// 创建API对应的request类
//
//		// 业务参数 start
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//		jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//		jsonObject.put("total_amount", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//		// jsonObject.put("store_id",
//		// "UICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
//		// jsonObject.put("timeout_express", "UICK_MSECURITY_PAY");//
//
//		request.setBizContent(jsonObject.toString());// 设置业务参数
//		// 业务参数 end
//		AlipayTradePrecreateResponse response = alipayClient.execute(request);
//		if (response != null) {
//			result.setResult(true);
//			result.setData(response);
//		}
//		logger.info(String.format("[%s]:: 支付宝当面付响应[%s]", "paymentQrCode", JSONUtility.objectToJson(result)));
//		return result;
//	}
//
//	/**
//	 * 生成 支付宝移动支付URL
//	 *
//	 * @Title: paymentApp
//	 * @Description: TODO
//	 * @param alipayDto
//	 * @return
//	 * @throws Exception
//	 * @return: String
//	 */
//	public ResultMsg<String> paymentMobile(AlipayDto alipayDto) throws Exception {
//		ResultMsg<String> result = new ResultMsg<String>();
//		logger.info(
//				String.format("[%s]:: 支付宝移动支付入参[%s]", "paymentMobile", JSONObject.fromObject(alipayDto).toString()));
//		// 商户订单号，商户网站订单系统中唯一订单号，必填
//		String out_trade_no = String.valueOf(alipayDto.getOutTradeNo());
//		if (StringUtils.isBlank(out_trade_no)) {
//			result.setMsg("订单号为空");
//			result.setResult(false);
//			return result;
//		}
//		String subject = String.valueOf(alipayDto.getSubject());
//		if (StringUtils.isBlank(subject)) {
//			result.setMsg("商品描述为空");
//			result.setResult(false);
//			return result;
//		}
//		// 付款金额，必填
//		String total_fee = alipayDto.getTotalFee();
//		if (StringUtils.isBlank(total_fee)) {
//			result.setMsg("付款金额为空");
//			result.setResult(false);
//			return result;
//		}
//		// appid 必填
//		String partner = getAppConfig().getConfig(EnumList.AliPayParam.PARTNER.value);
//		if (StringUtils.isBlank(partner)) {
//			result.setMsg("partner为空");
//			result.setResult(false);
//			return result;
//		}
//		// 服务器异步通知URL 必填
//		String notify_url = getAppConfig().getConfig(EnumList.AliPayParam.PCNOTIFYURL.value);
//		if (StringUtils.isBlank(notify_url)) {
//			result.setMsg("服务器异步通知URL为空");
//			result.setResult(false);
//			return result;
//		}
//
//		Map<String, String> sParaTemp = new HashMap<String, String>();
//		// 公共参数 start
//		sParaTemp.put("partner", partner);// 支付宝分配给开发者的应用ID
//		sParaTemp.put("service", AlipayConfig.serviceMobile);// 接口名称
//		sParaTemp.put("_input_charset", AlipayConstants.CHARSET_UTF8);// 请求使用的编码格式，如utf-8,gbk,gb2312等
//		sParaTemp.put("sign_type", alipayDto.getSignType());
//		sParaTemp.put("notify_url", notify_url);// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
//		// 公共参数 end
//
//		// 业务参数 start
//		sParaTemp.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//		sParaTemp.put("subject", subject);// 商品的标题/交易标题/订单标题/订单关键字等。
//		sParaTemp.put("payment_type", "1");// 只支持取值为1（商品购买）
//		sParaTemp.put("seller_id", "gwddqc@126.com");// 卖家支付宝用户号
//		sParaTemp.put("total_fee", total_fee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//		sParaTemp.put("body", subject);// 商品详情
//		sParaTemp.put("passback_params", alipayDto.getPassbackParams());
//		// 业务参数 end
//
//		String payStrResp = AlipaySubmit.buildRequest(sParaTemp, EnumList.PayModeSub.ALIMOBILE.value);
//		if (StringUtils.isNotBlank(payStrResp)) {
//			result.setData(payStrResp);
//			result.setResult(true);
//		}
//		logger.info(String.format("[%s]:: 支付宝移动支付响应[%s]", "paymentMobile", JSONUtility.objectToJson(result)));
//		return result;
//	}
//
//	/**
//	 * 退款
//	 *
//	 * @Title: refund
//	 * @Description: TODO
//	 * @param alipayRefundDto
//	 * @return
//	 * @throws Exception
//	 * @return: AlipayRefundDto
//	 */
//	public ResultMsg<String> refund(AlipayRefundReqDto alipayRefundReqDto) throws Exception {
//		ResultMsg<String> resultMsg = new ResultMsg<String>();
//		logger.info(String.format("[%s]:: 支付宝退款开始[%s]", "refund", JSONUtility.objectToJson(alipayRefundReqDto)));
//		AlipayClient alipayClient = null;
//		if (alipayRefundReqDto.getOut_trade_no().contains("X")) {
//			 alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//					getAppConfig().getConfig(EnumList.AliPayParam.APPID.value),
//					getAppConfig().getConfig(EnumList.AliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//					AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.AliPayParam.PUBLICKEY.value),
//					AlipayConstants.SIGN_TYPE_RSA);
//		}else if(alipayRefundReqDto.getOut_trade_no().contains("E")) {
//			 alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//					getAppConfig().getConfig(EnumList.EcardAliPayParam.APPID.value),
//					getAppConfig().getConfig(EnumList.EcardAliPayParam.PRIVATEKEY.value), AlipayConstants.FORMAT_JSON,
//					AlipayConstants.CHARSET_UTF8, getAppConfig().getConfig(EnumList.EcardAliPayParam.PUBLICKEY.value),
//					AlipayConstants.SIGN_TYPE_RSA2);
//		}
//		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//
//		// 业务参数 start
//		JSONObject jsonObject = new JSONObject();
//		/*String orderId = PayOrderOrUserIdUtil.getOrderId(alipayRefundReqDto.getOut_trade_no(),
//				alipayRefundReqDto.getUserId());*/
//		jsonObject.put("trade_no", alipayRefundReqDto.getTradeNo());// 支付宝交易流水号
//		jsonObject.put("out_trade_no", alipayRefundReqDto.getOut_trade_no());// 商户网站唯一订单号
//		jsonObject.put("refund_amount", alipayRefundReqDto.getRefund_amount());// 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
//		jsonObject.put("refund_reason", alipayRefundReqDto.getRefund_reason());// 退款原因
//		jsonObject.put("out_request_no", alipayRefundReqDto.getOut_request_no());
//
//		request.setBizContent(jsonObject.toString());// 设置业务参数
//		// 业务参数 end
//		AlipayTradeRefundResponse response = alipayClient.execute(request);
//		/*
//		 * 10000 支付成功 接口调用成功，调用结果请参考具体的API文档所对应的业务返回参数 20000 服务不可用 20001 授权权限不足 40001
//		 * 缺少必选参数 40002 非法的参数 40004 业务处理失败 40006 权限不足
//		 *
//		 */
//		if (response != null) {
//			if (response.getCode().equals("10000")) {
//				resultMsg.setMsg("退款成功");
//				resultMsg.setData(response.getTradeNo());
//				resultMsg.setResult(true);
//				resultMsg.setNeedRetry(false);
//			} else if(response.getCode().equals("20000")){
//				resultMsg.setMsg("未知异常");
//				resultMsg.setResult(true);
//				resultMsg.setNeedRetry(true);
//			}else{
//				resultMsg.setMsg("退款失败,code:" + response.getCode());
//				resultMsg.setResult(false);
//				resultMsg.setNeedRetry(false);
//				resultMsg.setErrorMsg(response.getSubMsg());
//			}
//		}
//
//		logger.info(String.format("[%s]:: 支付宝退款完成[%s]", "refund", JSONUtility.objectToJson(resultMsg)));
//		return resultMsg;
//	}
//
//	private volatile Integer abnormalCount = 0;// 异常笔数
//
//	private synchronized void addAbnormalCount() {
//		abnormalCount++;
//	}
//
//	public boolean isBillFileExist(Date billdate) {
//		String filePath = PropertiesUtilWeChat.get("alipay_local_path");
//		String fileName = String.format(ALIBILLFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//		File file = new File(filePath + fileName);
//		fileName = String.format(ALIBILLSUMFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//		File sumFile = new File(filePath + fileName);
//		return file.isFile() && file.exists() && sumFile.isFile() && sumFile.exists();
//	}
//
//	public void updateCheckStateToAbandon(Date billdate) {
//		AliPayMch aliPayMch = PaymentParamsVo.getAliParams();
//		PayBillTask payBillTask = new PayBillTask();
//		payBillTask
//				.setCheckDate(new sunbox.core.util.Date(DateUtils.formatDate(billdate, DateUtils.NORMAL_DATE_FORMAT)));
//		payBillTask.setCheckObject(EnumList.BillCheckObject.CHECKALIPAY.value);
//		payBillTask.setAcqmchild(aliPayMch.getAppId());
//		payBillTask.setCheckState(EnumList.TradeBillItemCheckState.CheckSuccess.value);
//		PayBillTask payBillTaskResp = payBillTask.load();
//		if(payBillTaskResp != null) {
//			payBillTaskResp.setCheckState(-1);
//			payBillTaskResp.update();
//		}
//	}
//
//	public ResultMsg processBill(Date billdate) throws Exception{
//		return processBill(billdate, true);
//	}
//	public ResultMsg onlyProcessBill(Date billdate) throws Exception{
//		return processBill(billdate, false);
//	}
//	private static final String ALIBILLFILENAME = "20882213528638750156_%s_业务明细.csv";
//	private static final String ALIBILLSUMFILENAME = "20882213528638750156_%s_业务明细(汇总).csv";
//	/**
//	 * 对账
//	 *
//	 * @Title: processBill 对账日期
//	 * @Description: TODO
//	 * @param billdate
//	 * @return
//	 * @throws Exception
//	 * @return: ResultMsg
//	 */
//	private ResultMsg processBill(Date billdate, boolean downFile) throws Exception {
//		Long time1 = new Date().getTime();
//		logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++开始时间：" + time1 + "," + new Date());
//		ResultMsg rmsg = new ResultMsg();
//		boolean isFinished = true;
//		logger.info(String.format("##########[%s]:: 开始自动对账--支付宝[%s]##########", "processBill",
//				DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//		// 获取商户号信息
//		AliPayMch aliPayMch = PaymentParamsVo.getAliParams();
//		if (aliPayMch == null) {
//			logger.info(String.format("[%s]:: 获取支付商信息为空", "processBill"));
//			rmsg.setResult(false);
//			rmsg.setMsg("获取支付商信息为空");
//			return rmsg;
//		}
//		String startmsg = DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT) + ";AppId:"
//				+ aliPayMch.getAppId() + "->";
//		// region 1.判断是否已对账 pay_bill_task
//		PayBillTask payBillTask = new PayBillTask();
//		payBillTask.setServiceType(PayBillTask.SERVICE_TYPE_RECHARGE);//充值
//		payBillTask
//				.setCheckDate(new sunbox.core.util.Date(DateUtils.formatDate(billdate, DateUtils.NORMAL_DATE_FORMAT)));
//		payBillTask.setCheckObject(EnumList.BillCheckObject.CHECKALIPAY.value);
//		payBillTask.setAcqmchild(aliPayMch.getAppId());
//		payBillTask.addConditions("notEqualCheckState", -1);
//		PayBillTask payBillTaskResp = payBillTask.load();
//		if (payBillTaskResp != null) {
//			if (payBillTaskResp.getCheckState() == EnumList.TradeBillItemCheckState.CheckSuccess.value) {
//				logger.info(String.format("##########[%s]:: 支付宝已对账，无须再次对账[%s]##########", "processBill",
//						DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//				rmsg.setMsg("支付宝已对账，无须再次对账");
//				rmsg.setResult(true);
//				return rmsg;
//			}
//		}
//		// endregion
//
//		// region 2.插入对账记录 pay_bill_task
//		payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//		payBillTask.setCheckTime(new sunbox.core.util.Date());
//		payBillTask.update();
//
//		// region 3.获取对账文件-支付宝对账接口
//		try {
//			List<AlipayBillDto> aliBillList = new ArrayList<AlipayBillDto>();
//			Map<String, String> gatherMap = new HashMap<String, String>();
//			String filePath = PropertiesUtilWeChat.get("alipay_local_path");
//
//			if(downFile) {
//				ResultMsg downloadBillFileResult = downloadBillFile(aliPayMch,
//						DateUtils.formatDate(billdate, DateUtils.NORMAL_DATE_FORMAT), filePath,AlipayConstants.SIGN_TYPE_RSA);
//
//				if (downloadBillFileResult.getResult()) {
//
//					List rows = downloadBillFileResult.getRows();
//					if (!rows.isEmpty()) {
//						for (Object obj : rows) {
//							String fileName = (String) obj;
//							if (fileName.indexOf("汇总") == -1) {
//								aliBillList = getAlipayBillList(filePath, IOUtility.filterFilePath(fileName));
//							} else {
//								gatherMap = getAlipayGather(filePath, fileName);
//							}
//						}
//					}
//				} else {
//					logger.info("下载支付宝账单失败");
//					rmsg.setMsg("下载支付宝账单失败");
//					rmsg.setResult(true);
//					return rmsg;
//				}
//			}
//			else {
//				String fileName = String.format(ALIBILLFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//				aliBillList = getAlipayBillList(filePath, IOUtility.filterFilePath(fileName));
//				fileName = String.format(ALIBILLSUMFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//				gatherMap = getAlipayGather(filePath, fileName);
//			}
//
//			if (aliBillList == null || aliBillList.isEmpty()) {
//				logger.info(DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT) + ",获取账单解析记录为空。");
//				payBillTask.setRemark("账单解析记录为空。");
//				payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//				payBillTask.update();
//				rmsg.setMsg("账单解析记录为空");
//				rmsg.setResult(true);
//				return rmsg;
//			}
//
//			abnormalCount = 0;
//			//开始解析
//			parseDZInfo(aliBillList, billdate, aliPayMch.getPartner(), payBillTask.getId(), startmsg);
//
//			// region **** 更新对账记录为已完成
//			payBillTask.setCheckState(EnumList.TradeBillItemCheckState.CheckSuccess.value);
//			payBillTask.setBillAmount(new BigDecimal(gatherMap.get("totalBillAmount")));// 账单金额
//			payBillTask.setBillCount(Integer.parseInt(gatherMap.get("totalBillCount")));// 账单笔数
//			payBillTask.setAccountAmount(new BigDecimal(gatherMap.get("amount")));//实收净额
//			payBillTask.setAbnormalCount(abnormalCount);// 异常笔数
//			payBillTask.setRemark("已对账");
//			payBillTask.setPoundage(new BigDecimal(gatherMap.get("totalProcedureFee")));//服务费or手续费
//			payBillTask.update();
//			// endregion
//			// 处理结款记录
//			if (abnormalCount == 0) {
//				productDeposite(billdate, new BigDecimal(gatherMap.get("totalBillAmount")), new BigDecimal(gatherMap.get("totalProcedureFee")), Integer.parseInt(gatherMap.get("totalBillCount")));
//			}
//
//		} catch (Exception ex) {
//			logger.error(startmsg + "获取支付宝对账列表失败。异常信息：" + ex.getMessage());
//			ex.printStackTrace();
//			payBillTask.setRemark("获取支付宝对账列表失败。异常信息:" + ex.getMessage());
//			payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//			payBillTask.update();
//		}
//
//		rmsg.setResult(isFinished);
//		rmsg.setMsg((isFinished ? "对账完成" : "对账未完成") + "。billdate:" + DateUtils.formatDate(billdate));
//		logger.info(String.format("##########[%s]:: 自动对账已完成--支付宝[%s]##########", "processBill",
//				DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//
//		Long time10 = new Date().getTime();
//		logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++支付宝对账结束时间：" + (time10 - time1) + "," + new Date());
//		return rmsg;
//	}
//
//	// endregion
//
//	private void productDeposite(Date billdate, BigDecimal depositeAmount, BigDecimal procedureFee, Integer billCount) {
//		Deposite deposite = new Deposite();
//		// 1.现金充值 2.押金 3.微信充值 4.支付宝充值 5.农行充值 6.电e宝充值 7.银联充值
//		deposite.setFeeType(EnumList.FeeType.ALI.value);
//		deposite.setDepositeDate(new sunbox.core.util.Date(DateUtils.getOnlyDate(billdate)));
//		deposite.load();
//		deposite.setDepositeBank(
//				getAppConfig().getConfig(EnumList.DepositeBankInfo.ALIPAY_DEPOSITE_BANK.value));
//		deposite.setBankAccountName(
//				getAppConfig().getConfig(EnumList.DepositeBankInfo.ALIPAY_DEPOSITE_BANK_ACCOUNT_NAME.value));
//		deposite.setBankAccountNo(
//				getAppConfig().getConfig(EnumList.DepositeBankInfo.ALIPAY_DEPOSITE_BANK_ACCOUNT_NO.value));
//		// 结款金额
//		deposite.setDepositeAmount(depositeAmount);
//		deposite.setPoundAge(procedureFee.abs());
//		deposite.setDepositeState("20");
//		deposite.setDepositeTime(new sunbox.core.util.Date());
//		deposite.setDepositeCount(billCount);
//		deposite.update();
//	}
//
//	/**
//	 * 获取本地汇总账单对象
//	 *
//	 * @Title: getAlipayGather
//	 * @Description: TODO
//	 * @param filePath
//	 * @param fileName
//	 * @return: void
//	 */
//	public Map<String, String> getAlipayGather(String filePath, String fileName) {
//		Map<String, String> respMap = null;
//		logger.info("filePath:" + filePath + File.separator + IOUtility.filterFilePath(fileName));
//		try {
//			List<String> lineList = BillFileUtil
//					.readLinesText(filePath + File.separator + IOUtility.filterFilePath(fileName));
//			if (lineList != null && !lineList.isEmpty()) {
//				respMap = new HashMap<String, String>();
//				int count = 0;
//				for (String lineText : lineList) {
//					if (count++ <= 4)
//						continue;
//					/*
//					 * 0门店编号,1门店名称,2交易订单总笔数,3退款订单总笔数,4订单金额（元）,5商家实收（元）,6支付宝优惠（元）,7商家优惠（元）,8卡消费金额（元）,
//					 * 9服务费（元）,10分润（元）,11实收净额（元）
//					 */
//
//					// CSV格式文件为逗号分隔符文件，根据逗号分隔符进行split
//					lineText = lineText.replaceAll("\t", "");
//					if (lineText.indexOf("合计") != -1) {
//						String[] records = lineText.split(",");
//						Integer billCount = Integer.parseInt(records[2]);
//						Integer refundCount = Integer.parseInt(records[3]);
//						respMap.put("totalBillCount", String.valueOf(billCount + refundCount));
//						respMap.put("totalBillAmount", records[4]);
//						respMap.put("totalRefundBillAmount", records[2]);
//						respMap.put("totalProcedureFee", records[9]);
//						respMap.put("amount", records[11]);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.info("汇总对账程序读取文件内容出错..................", e);
//			respMap = null;
//		}
//		return respMap;
//	}
//
//	/**
//	 * 获取本地账单对象集合
//	 *
//	 * @Title: getAlipayBillList
//	 * @Description: TODO
//	 * @param filePath
//	 * @param fileName
//	 * @return
//	 * @return: List<AlipayBillDto>
//	 */
//	public List<AlipayBillDto> getAlipayBillList(String filePath, String fileName) {
//		logger.info("filePath:" + filePath + File.separator + IOUtility.filterFilePath(fileName));
//		List<AlipayBillDto> list = null;
//		try {
//			List<String> lineList = BillFileUtil
//					.readLinesText(filePath + File.separator + IOUtility.filterFilePath(fileName));
//			if (lineList != null && !lineList.isEmpty()) {
//				list = new ArrayList<AlipayBillDto>();
//				int count = 0;
//				for (String lineText : lineList) {
//					if (count++ <= 4)
//						continue;
//					// 支付宝交易号 商户订单号 业务类型 商品名称 创建时间 完成时间 门店编号 门店名称 操作员 终端号 对方账户 订单金额（元） 商家实收（元）
//					// 支付宝红包（元） 集分宝（元） 支付宝优惠（元） 商家优惠（元） 券核销金额（元） 券名称 商家红包消费金额（元） 卡消费金额（元） 退款批次号/请求号
//					// 服务费（元） 分润（元） 备注
//
//					// 2017092221001004350249513682 20170922000018919150600961892022 交易 AlipayTrade
//					// 2017/9/22 0:00 2017/9/22 0:00 *相兰(177****1397) 50 50 0 0 0 0 0 0.00 0 -0.15 0
//					// 2017092221001004990508494329 500003X1168881 交易 充值 2017/9/22 0:48 2017/9/22
//					// 0:48 *卫(314***@qq.com) 1 1 0 0 0 0 0 0.00 0 0 0 充值
//
//					// "99788710528501 ","2016042121001004500210580411 ","1000028108468 ","中国石油昆仑加油卡
//					// ",2016/4/21
//					// 0:00,"徐卫明(13957690339) ",200,0,1336053.76,支付宝,在线支付,"中国石油昆仑加油卡 "
//
//					// CSV格式文件为逗号分隔符文件，根据逗号分隔符进行split
//					lineText = lineText.replaceAll("\"", "");
//					lineText = lineText.replaceAll("\t", "");
//					String[] records = lineText.split(",");
//					if (records.length >= 12) {
//						AlipayBillDto alipayBillDto = new AlipayBillDto();
//						alipayBillDto.setAliTradeNo(records[0].trim());
//						alipayBillDto.setShopTradeNo(records[1].trim());
//						alipayBillDto.setTradeProName(records[3].trim());
//						alipayBillDto.setTradeTime(records[5].trim());
//						alipayBillDto.setUserAccount(records[10].trim());
//
//						if (records[2].trim().equals("交易")) {
//							alipayBillDto.setIncomeAmount(new BigDecimal(records[11].trim()));
//							alipayBillDto.setTradeType(AlipayBillDto.ALI_TRADETYPE_PAID);
//						} else {
//							alipayBillDto.setRefundAmount(new BigDecimal(records[11].trim()));
//							alipayBillDto.setTradeType(AlipayBillDto.ALI_TRADETYPE_REFUND);
//							alipayBillDto.setShopRefundTradeNo(records[21].trim());
//						}
//						alipayBillDto.setBsnsType(records[2].trim());
//						if(records.length > 23) {
//							alipayBillDto.setRemark(records[23].trim());
//						}
//						list.add(alipayBillDto);
//					}
//					else {
//						logger.error("支付宝账单-解析记录异常-格式不正确:" + lineText);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.info("对账程序读取文件内容出错..................", e);
//			list = null;
//		}
//		return list;
//	}
//
//	/**
//	 * 下载对账单到指定目录
//	 *
//	 * @Title: downloadBillFile
//	 * @param aliPayMch
//	 *            商户信息
//	 * @param billDate
//	 *            账单日期 2016-04-05
//	 * @param filePath
//	 *            文件存储路径
//	 * @param fileName
//	 *            文件名称
//	 * @return
//	 * @throws AlipayApiException
//	 * @return: boolean
//	 * @throws IOException
//	 */
//	public ResultMsg downloadBillFile(AliPayMch aliPayMch, String billDate, String filePath,String signType)
//			throws AlipayApiException, IOException {
//		ResultMsg resultMsg = new ResultMsg();
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW, aliPayMch.getAppId(),
//				aliPayMch.getPrivateKey(), AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
//				aliPayMch.getPublicKey(), signType);
//		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("bill_date", billDate);
//		jsonObject.put("bill_type", "trade");// 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
//		request.setBizContent(jsonObject.toString());
//		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
//		if (response != null) {
//			if (response.getCode().equals("10000")) {
//				resultMsg.setMsg(response.getMsg());
//				String downloadUrl = response.getBillDownloadUrl();
//				logger.info("支付宝对账单下载地址:" + downloadUrl);
//				logger.info("文件存储路径:" + filePath);
//				File dic = new File(filePath);
//				if(!dic.exists()) {
//					logger.info("支付宝创建目录-" + dic.getAbsolutePath());
//					dic.mkdirs();
//					dic.setReadable(true, false);
//					dic.setWritable(true, false);
//				}
//				List<String> fileNames = DownLoadUtility.downLoadFromUrl(downloadUrl, filePath);
//				resultMsg.setRows(fileNames);
//				resultMsg.setResult(true);
//			} else {
//				resultMsg.setMsg(response.getCode() + ",Msg:" + response.getMsg());
//				resultMsg.setResult(false);
//			}
//		} else {
//			resultMsg.setMsg("调用支付宝对账接口异常");
//			resultMsg.setResult(false);
//		}
//
//		return resultMsg;
//	}
//
//	public boolean processBillSingle(Integer orderTradeBillTaskId, AlipayBillDto alipayBillDto, PayItem payItem) {
//		boolean isStateDiff = false;
//		boolean isAmountDiff = false;
//
//		Integer tradestate = payItem.getState();// 交易状态 '0待支付 1支付成功 2 支付失败 3支付取消
//												// 4退款中 5已退款',
//		BigDecimal shopAmount = payItem.getAmount();// 支付金额
//
//		Integer tradeType = alipayBillDto.getTradeType();// 交易类型 1消费； 2 退款
//		BigDecimal thirdAmount = (tradeType == AlipayBillDto.ALI_TRADETYPE_PAID ? alipayBillDto.getIncomeAmount()
//				: alipayBillDto.getRefundAmount().abs());
//		String thirdTradeNo = alipayBillDto.getAliTradeNo();
//		// region 分别判断积分、状态是否一致
//
//		// 判断交易状态
//		if (alipayBillDto.getShopTradeNo().contains("X")) {
//			if ((tradeType == AlipayBillDto.ALI_TRADETYPE_PAID && !tradestate.equals(Dict.PAY_ITEM_STATE_PAID_SUCCESS))
//					|| (tradeType == AlipayBillDto.ALI_TRADETYPE_REFUND && !tradestate.equals(Dict.PAY_ITEM_STATE_REFUND_SUCCESS))) {
//				logger.info(String.format("支付宝交易状态不一致,当前状态:[%s],消费成功:[%s],退款成功:[%s]", tradestate,
//						Dict.PAY_ITEM_STATE_PAID_SUCCESS, Dict.PAY_ITEM_STATE_REFUND_SUCCESS));
//				isStateDiff = true; // 状态不一致。后台管理，可以做后续处理，如：更新积分商城账单状态等
//			}
//		}else if(alipayBillDto.getShopTradeNo().contains("E")){
//			// 判断交易状态 （思考是否加一个 状态= 用户扣款成功但是未发货 而是发起退款的状态判断）
//			if ((tradeType == AlipayBillDto.ALI_TRADETYPE_PAID && !tradestate.equals(Dict.PAY_ITEM_STATE_PAID_SUCCESS))&&!tradestate.equals(Dict.PAY_STATE_REFUND_WHEN_FAIL)
//					|| (tradeType == AlipayBillDto.ALI_TRADETYPE_REFUND && !tradestate.equals(Dict.PAY_ITEM_STATE_REFUND_SUCCESS))) {
//				logger.info(String.format("支付宝交易状态不一致,当前状态:[%s],消费成功:[%s],退款成功:[%s]", tradestate,
//						Dict.PAY_ITEM_STATE_PAID_SUCCESS, Dict.PAY_ITEM_STATE_REFUND_SUCCESS));
//				isStateDiff = true; // 状态不一致。后台管理，可以做后续处理，如：更新积分商城账单状态等
//			}
//		}
//
//		// 判断金额是否相同
//		if (shopAmount.abs().compareTo(thirdAmount.abs()) != 0) {
//			logger.info(String.format("支付宝交易绝对值金额不同,积分商城:[%s],第三方:[%s]", shopAmount, thirdAmount));
//			isAmountDiff = true; // 金额不同
//		}
//
//		// endregion
//
//		// region 处理积分或状态不一致异常。插入异常记录
//		if (isStateDiff || isAmountDiff) {
//			PayDzexception dzException = new PayDzexception();
//			dzException.setTradeBillTaskId(orderTradeBillTaskId);
//			dzException.setPayItemId(payItem.getId());
//			dzException.load();
//			dzException.setIsRefund(payItem.getIsRefund() == null ? 0 : payItem.getIsRefund());
//			dzException.setProcessType(EnumList.ProcessType.UNDISPOSE.value);
//			dzException.setUserId(payItem.getUserId().longValue());
//			dzException.setDzObject(String.valueOf(EnumList.BillCheckObject.CHECKALIPAY.value));
//			dzException.setThirdBillTradeNo(alipayBillDto.getAliTradeNo());
//
//			if (isStateDiff && isAmountDiff) {
//				dzException.setExceptionMsg(EnumList.BillCheckExceptionCase.DiffAmountAndState.text);
//				dzException.setExceptionCase(EnumList.BillCheckExceptionCase.DiffAmountAndState.value);
//				dzException.setThirdBillAmount(thirdAmount);
//			} else {
//				if (isStateDiff) {
//					dzException.setExceptionMsg(EnumList.BillCheckExceptionCase.DiffState.text);
//					dzException.setExceptionCase(EnumList.BillCheckExceptionCase.DiffState.value);
//					dzException.setThirdBillAmount(thirdAmount);
//				}
//			}
//			// 插入异常记录
//			dzException.update();
//			return false;
//		}
//		// endregion
//
//		return true;
//	}
//
//	/**
//	 * 查询订单
//	 *
//	 * @Title: orderQuery
//	 * @Description:
//	 * @param out_trade_no
//	 *            商户订单号
//	 * @param transaction_id
//	 *            微信订单号
//	 * @return
//	 * @return: ResultMsg
//	 * @throws AlipayApiException
//	 */
//	public ResultMsg orderQuery(String out_trade_no, String transaction_id) {
//		ResultMsg resultMsg = new ResultMsg();
//		logger.info(String.format("[%s]:: 查询订单入参[out_trade_no=%s,transaction_id=%s]", "orderQuery", out_trade_no, transaction_id));
//		try {
//			if (StringUtils.isNotBlank(out_trade_no) || StringUtils.isNotBlank(transaction_id)) {
//				// 获取商户号信息
//				if (!StringUtils.isNotBlank(out_trade_no)) {
//					//合并根据orderId判断  X 充值的   E 购卡的
//					resultMsg.setMsg("区分充值和购卡订单 out_trade_no 不能为null");
//					resultMsg.setResult(false);
//				}
//				AliPayMch aliPayMch = null;
//				String signType = AlipayConstants.SIGN_TYPE_RSA;
//				if(out_trade_no.contains("X")){
//					aliPayMch = PaymentParamsVo.getAliParams();
//				}else{
//					aliPayMch = PaymentParamsVo.getEcardAliParams();
//					signType= AlipayConstants.SIGN_TYPE_RSA2;
//				}
//				if (aliPayMch != null) {
//					AlipayClient alipayClient = new DefaultAlipayClient(AlipaySubmit.ALIPAY_OPENAPI_NEW,
//							aliPayMch.getAppId(), aliPayMch.getPrivateKey(), AlipayConstants.FORMAT_JSON,
//							AlipayConstants.CHARSET_UTF8, aliPayMch.getPublicKey(), signType);
//					AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//
//					// 业务参数 start
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("out_trade_no", out_trade_no);// 商户网站唯一订单号
//					jsonObject.put("trade_no", transaction_id);// 支付宝交易流水号
//					request.setBizContent(jsonObject.toString());// 填充业务参数
//
//					AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) alipayClient.execute(request);
//					logger.info(String.format("[%s]:: 支付宝查询订单响应[%s]", "orderQuery", JSONUtility.objectToJson(response)));
//					if (response != null) {
//						if (response.getCode().equals("10000")) {
//							if(response.getTradeStatus().equals("WAIT_BUYER_PAY")) {
//								resultMsg.setMsg("查询订单接口:待支付");
//								resultMsg.setNeedRetry(true);
//								resultMsg.setResult(false);
//							}
//							else if(response.getTradeStatus().equals("TRADE_CLOSED")) {
//								resultMsg.setMsg("查询订单接口:支付关闭");
//								resultMsg.setResult(false);
//							}
//							else {
//								if(response.getTradeStatus().equals("TRADE_SUCCESS")) {
//									resultMsg.setMsg("查询订单接口:支付成功");
//								}
//								else if(response.getTradeStatus().equals("TRADE_FINISHED")) {
//									resultMsg.setMsg("查询订单接口:交易结束");
//								}
//								resultMsg.setData(response);
//								resultMsg.setResult(true);
//							}
//						} else {
//							resultMsg.setMsg("查询订单接口失败,code:" + response.getMsg());
//							resultMsg.setNeedRetry(true);
//							resultMsg.setResult(false);
//						}
//					} else {
//						resultMsg.setMsg("查询订单接口异常:" + response.getMsg());
//						resultMsg.setNeedRetry(true);
//						resultMsg.setResult(false);
//					}
//					logger.info(String.format("[%s]:: 查询订单响应[%s]", "orderQuery", JSONUtility.objectToJson(resultMsg)));
//				} else {
//					logger.info(String.format("[%s]:: 商户信息为空[out_trade_no=%s,transaction_id=%s]", "orderQuery", out_trade_no, transaction_id));
//				}
//			} else {
//				logger.info(String.format("[%s]:: 无效的入参[out_trade_no=%s,transaction_id=%s]", "orderQuery", out_trade_no, transaction_id));
//			}
//		} catch (AlipayApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			resultMsg.setResult(false);
//			resultMsg.setMsg(e.getMessage());
//		}
//		logger.info(String.format("[%s]:: 支付宝查询订单响应[%s]", "orderQuery", JSONUtility.objectToJson(resultMsg)));
//		return resultMsg;
//	}
//
//
//	/**
//	 * 插入未知单边帐
//	 * @param payBillTaskId
//	 * @param alipayBillDto
//	 */
//	private void insertUnknowPayDzException(Integer payBillTaskId, AlipayBillDto alipayBillDto) {
//		PayDzexception payDzexception = new PayDzexception();
//
//		Integer tradeType = alipayBillDto.getTradeType();// 交易类型 1消费； 2 退款
//		BigDecimal thirdAmount;
//
//		if(alipayBillDto.getTradeType() == AlipayBillDto.ALI_TRADETYPE_PAID) {
//			//支付宝充值有第三方交易流水号
//			payDzexception.setThirdBillTradeNo(alipayBillDto.getAliTradeNo());
//			payDzexception.setIsRefund(0);
//			thirdAmount = alipayBillDto.getIncomeAmount();
//		}
//		else {
//			payDzexception.setIsRefund(1);
//			thirdAmount = alipayBillDto.getRefundAmount();
//		}
//		payDzexception.load();
//		payDzexception.setTradeBillTaskId(payBillTaskId);
//		payDzexception.setExceptionCase(EnumList.BillCheckExceptionCase.UNKNOWN.value);
//		payDzexception.setExceptionMsg(EnumList.BillCheckExceptionCase.UNKNOWN.text);
//		payDzexception.setProcessType(EnumList.ProcessType.USERDISPOSE.value);
//		payDzexception.setDzObject(String.valueOf(EnumList.BillCheckObject.CHECKALIPAY.value));
//		payDzexception.setThirdBillAmount(thirdAmount);
//		payDzexception.update();
//	}
//
//	private void insertPayDzException(Long userId, Integer payBillTaskId, AlipayBillDto alipayBillDto) {
//		// 积分商城没有交易记录。插入异常记录
//		PayDzexception payDzexception = new PayDzexception();
//		payDzexception.setThirdBillTradeNo(alipayBillDto.getAliTradeNo());
//		BigDecimal thirdAmount;
//		if(alipayBillDto.getTradeType() == AlipayBillDto.ALI_TRADETYPE_PAID) {
//			payDzexception.setIsRefund(0);
//			thirdAmount = alipayBillDto.getIncomeAmount();
//		}else{
//			payDzexception.setIsRefund(1);
//			thirdAmount = alipayBillDto.getRefundAmount();
//		}
//		payDzexception.load();
//		payDzexception.setTradeBillTaskId(payBillTaskId);
//		payDzexception.setUserId(userId);
//		payDzexception.setExceptionCase(EnumList.BillCheckExceptionCase.NotFoundInShop.value);
//		payDzexception.setExceptionMsg(EnumList.BillCheckExceptionCase.NotFoundInShop.text);
//		payDzexception.setProcessType(EnumList.ProcessType.UNDISPOSE.value);
//		payDzexception.setDzObject(String.valueOf(EnumList.BillCheckObject.CHECKALIPAY.value));
//		payDzexception.setThirdBillAmount(thirdAmount);
//		payDzexception.update();
//	}
//
//	/**
//	 * 解析对账信息
//	 * @param aliBillList
//	 * @param billdate
//	 * @param partner 商户号
//	 * @param payBillTaskId
//	 * @param startmsg
//	 * @throws InterruptedException
//	 */
//	private void parseDZInfo(List<AlipayBillDto> aliBillList, Date billdate, String partner, Integer payBillTaskId, String startmsg) throws InterruptedException
//	{
//		String billThreadNum = PropertiesUtilWeChat.get("bill_thread_num");
//
//		sunbox.core.util.Date onlyDate = new sunbox.core.util.Date(sunbox.core.util.DateUtils.getOnlyDate(billdate));
//		// 创建线程池
//		ExecutorService pool = Executors.newCachedThreadPool();
//
//		logger.info("获取支付宝对账笔数:" + aliBillList.size());
//		List<List<AlipayBillDto>> alipayBillDtoList = ListTools.averageAssign(aliBillList,
//				Integer.parseInt(billThreadNum));
//
//		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
//		Callable<Integer> task = null;
//
//		for (List<AlipayBillDto> alipayBillDtos : alipayBillDtoList) {
//			task = new Callable<Integer>() {
//				@Override
//				public Integer call() throws Exception {
//					// 开始遍历解析对账记录
//					for (AlipayBillDto alipayBillDto : alipayBillDtos) {
//						try {
//							logger.info("支付宝对账TradeNo:" + alipayBillDto.getShopTradeNo());
//
//							String userIdStr = "";
//							Long orderId = null;
//							//充值查询充值业务流水号
//							if(alipayBillDto.getTradeType()==AlipayBillDto.ALI_TRADETYPE_PAID){
//								Map<String, String> orderIdOrUserIdMap = PayOrderOrUserIdUtil
//										.getOrderIdOrUserId(alipayBillDto.getShopTradeNo());
//								userIdStr = orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.userIdKey);
//								orderId = Long.parseLong(orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.orderIdKey));
//							}else if(alipayBillDto.getTradeType()==AlipayBillDto.ALI_TRADETYPE_REFUND){
//								//退费查询退费业务流水号
//								Map<String, String> orderIdOrUserIdMap = PayOrderOrUserIdUtil
//										.getOrderIdOrUserId(alipayBillDto.getShopRefundTradeNo());
//								userIdStr = orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.userIdKey);
//								orderId = Long.parseLong(orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.orderIdKey));
//							}
//
//							Integer userId = Integer.parseInt(userIdStr);
//							// region 4.插入账单解析信息到pay_bill_alipay
//							if (alipayBillDto.getShopTradeNo().contains("X")) {
//								PayBillAlipay payBillAlipay = new PayBillAlipay();
//								payBillAlipay.setAlipaytradeno(alipayBillDto.getAliTradeNo());
//								payBillAlipay.setTradeType(alipayBillDto.getTradeType());
//								payBillAlipay.load();
//								payBillAlipay.setOrderid(orderId.longValue());
//								payBillAlipay.setTradeTime(new sunbox.core.util.Date(alipayBillDto.getTradeTime()));
//								payBillAlipay.setUserAccount(alipayBillDto.getUserAccount());
//								payBillAlipay.setAmount(alipayBillDto.getIncomeAmount());
//								payBillAlipay.setRemark(alipayBillDto.getRemark());
//								payBillAlipay.setTradeProName(alipayBillDto.getTradeProName());
//								payBillAlipay.setBillDate(onlyDate);
//								payBillAlipay.setBsnsAccount(partner);
//								payBillAlipay.update();
//							}else if(alipayBillDto.getShopTradeNo().contains("E")){
//								PayBillRcardAlipay payBillAlipay = new PayBillRcardAlipay();
//								payBillAlipay.setAlipaytradeno(alipayBillDto.getAliTradeNo());
//								payBillAlipay.setTradeType(alipayBillDto.getTradeType());
//								payBillAlipay.load();
//								payBillAlipay.setOrderid(orderId.longValue());
//								payBillAlipay.setTradeTime(new sunbox.core.util.Date(alipayBillDto.getTradeTime()));
//								payBillAlipay.setUserAccount(alipayBillDto.getUserAccount());
//								if (alipayBillDto.getTradeType()==AlipayBillDto.ALI_TRADETYPE_PAID) {
//									payBillAlipay.setAmount(alipayBillDto.getIncomeAmount());
//								}else{
//									payBillAlipay.setAmount(alipayBillDto.getRefundAmount());
//								}
//								payBillAlipay.setRemark(alipayBillDto.getRemark());
//								payBillAlipay.setTradeProName(alipayBillDto.getTradeProName());
//								payBillAlipay.setBillDate(onlyDate);
//								payBillAlipay.setBsnsAccount(partner);
//								payBillAlipay.update();
//							}
//
//							// endregion
//
//							PayItem payItem = new PayItem();
//							payItem.setPayMode(Dict.PAY_MODE_ALIPAY);
//							payItem.setUserId(userId);
//							if (alipayBillDto.getShopTradeNo().contains("X")) {
//								payItem.setOrderId(orderId.intValue());
//							}else if(alipayBillDto.getShopTradeNo().contains("E")){
//								payItem.setThirdTradeNo(alipayBillDto.getAliTradeNo());
//								Integer tradeType = alipayBillDto.getTradeType();
//								if (tradeType == AlipayBillDto.ALI_TRADETYPE_PAID) {
//									// 交易 is_refund 0或者null为交易，1为退费
//									payItem.setSqlWhere(" and (is_refund is null or is_refund = 0)");
//								} else {
//									payItem.setIsRefund(1);
//								}
//							}
//
//							PayItem payItemResp = payItem.load();
//
//							//如果通过第三方交易流水号未查到，通过orderid查询
//							if (alipayBillDto.getShopTradeNo().contains("X")) {
//								if(payItemResp != null) {
//									if(payItemResp.getThirdTradeNo() == null) {
//											payItemResp.setThirdTradeNo(alipayBillDto.getAliTradeNo());
//									}
//								}
//							}else if(alipayBillDto.getShopTradeNo().contains("E")){
//								if(payItemResp == null) {
//									OrderRcardSale orderRcardSale = new OrderRcardSale();
//									orderRcardSale.setId(orderId);
//									orderRcardSale.setUserId(userId);
//									orderRcardSale.load();
//									if (orderRcardSale.getIsLoad()) {
//										payItem.setThirdTradeNo(null);
//										payItem.setPayId(orderRcardSale.getPayId());
//										payItemResp = payItem.load();
//										if(payItemResp != null) {
//											if(payItemResp.getThirdTradeNo() == null) {
//												payItemResp.setThirdTradeNo(alipayBillDto.getAliTradeNo());
//											}
//										}
//									}
//								}
//							}
//							// region 5.处理情况一，“第三方有，积分商城无”异常
//							if (payItemResp == null) {
//								insertPayDzException(userId.longValue(), payBillTaskId, alipayBillDto);
//
//								addAbnormalCount();
//								continue;
//							} else if (payItemResp
//									.getBillChkState() == EnumList.TradeBillItemCheckState.CheckSuccess.value) {// 已对账，无须再对
//								logger.info("支付宝-已对账,ThirdTradeNo:" + payItemResp.getThirdTradeNo());
//								continue;
//							}
//							// endregion
//
//							// region 6.处理情况二，“第三方有，积分商城有”异常
//							boolean b = processBillSingle(payBillTaskId, alipayBillDto, payItemResp);
//							if (!b) {// 统计异常交易笔数
//								addAbnormalCount();
//							}
//							// 更新账单日期
//							payItemResp.setBillDate(onlyDate);
//							payItemResp.setBillChkState(b ? EnumList.TradeBillItemCheckState.CheckSuccess.value
//									: EnumList.TradeBillItemCheckState.CheckDiff.value);
//							payItemResp.update();
//							// endregion
//						} catch (NumberFormatException e) {
//							logger.error(startmsg + "支付宝解析对账记录。NumberFormat异常信息：" + e.getMessage());
//
//							insertUnknowPayDzException(payBillTaskId, alipayBillDto);
//
//							addAbnormalCount();
//							continue;
//						} catch (Exception e) {
//							logger.error(startmsg + "支付宝解析对账记录。异常信息：" + e.getMessage());
//							e.printStackTrace();
//							insertUnknowPayDzException(payBillTaskId, alipayBillDto);
//
//							addAbnormalCount();
//							continue;
//						}
//					}
//
//					return new Random().nextInt(1000);
//				}
//			};
//
//			tasks.add(task);
//		}
//
//		pool.invokeAll(tasks);
//		pool.shutdown();
//	}
//
//
//	/**
//	 * ==============================================E卡相关添加===============================================
//	 * ==============================================E卡相关添加===============================================
//	 * ==============================================E卡相关添加===============================================
//	 * ==============================================E卡相关添加===============================================
//	 * ==============================================E卡相关添加===============================================
//	 * ==============================================E卡相关添加===============================================
//	 */
//
//
//	public ResultMsg EcardProcessBill(Date billdate) throws Exception{
//		return EcardProcessBill(billdate, true);
//	}
//
//	/**
//	 * Ecard支付宝支付对账
//	 *
//	 * @Title: processBill 对账日期
//	 * @Description: TODO
//	 * @param billdate
//	 * @return
//	 * @throws Exception
//	 * @return: ResultMsg
//	 */
//	private ResultMsg EcardProcessBill(Date billdate, boolean downFile) throws Exception {
//		Long time1 = new Date().getTime();
//		logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++开始时间：" + time1 + "," + new Date());
//		ResultMsg rmsg = new ResultMsg();
//		boolean isFinished = true;
//		logger.info(String.format("##########[%s]:: 开始自动对账--支付宝[%s]##########", "EcardProcessBill",
//				DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//		// 获取商户号信息
//		AliPayMch aliPayMch = PaymentParamsVo.getEcardAliParams();
//		if (aliPayMch == null) {
//			logger.info(String.format("[%s]:: 获取支付商信息为空", "processBill"));
//			rmsg.setResult(false);
//			rmsg.setMsg("获取支付商信息为空");
//			return rmsg;
//		}
//		String startmsg = DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT) + ";AppId:"
//				+ aliPayMch.getAppId() + "->";
//		// region 1.判断是否已对账 pay_bill_task
//		PayBillTask payBillTask = new PayBillTask();
//		payBillTask.setServiceType(PayBillTask.SERVICE_TYPE_RCARD);//业务类型 2 购卡
//		payBillTask
//				.setCheckDate(new sunbox.core.util.Date(DateUtils.formatDate(billdate, DateUtils.NORMAL_DATE_FORMAT)));
//		payBillTask.setCheckObject(EnumList.BillCheckObject.CHECKALIPAY.value);
//		payBillTask.setAcqmchild(aliPayMch.getAppId());
//		payBillTask.addConditions("notEqualCheckState", -1);
//		PayBillTask payBillTaskResp = payBillTask.load();
//		if (payBillTaskResp != null) {
//			if (payBillTaskResp.getCheckState() == EnumList.TradeBillItemCheckState.CheckSuccess.value) {
//				logger.info(String.format("##########[%s]:: 支付宝已对账，无须再次对账[%s]##########", "processBill",
//						DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//				rmsg.setMsg("支付宝已对账，无须再次对账");
//				rmsg.setResult(true);
//				return rmsg;
//			}
//		}
//		// endregion
//
//		// region 2.插入对账记录 pay_bill_task
//		payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//		payBillTask.setCheckTime(new sunbox.core.util.Date());
//		payBillTask.update();
//
//		// region 3.获取对账文件-支付宝对账接口
//		try {
//			List<AlipayBillDto> aliBillList = new ArrayList<AlipayBillDto>();
//			Map<String, String> gatherMap = new HashMap<String, String>();
//			String filePath = PropertiesUtilWeChat.get("ecard_alipay_local_path");
//			//String filePath = "D:/bill/ali/";
//			if(downFile) {
//				ResultMsg downloadBillFileResult = downloadBillFile(aliPayMch,
//						DateUtils.formatDate(billdate, DateUtils.NORMAL_DATE_FORMAT), filePath,AlipayConstants.SIGN_TYPE_RSA2);
//
//				if (downloadBillFileResult.getResult()) {
//
//					List rows = downloadBillFileResult.getRows();
//					if (!rows.isEmpty()) {
//						for (Object obj : rows) {
//							String fileName = (String) obj;
//							if (fileName.indexOf("汇总") == -1) {
//								aliBillList = getAlipayBillList(filePath, IOUtility.filterFilePath(fileName));
//							} else {
//								gatherMap = getAlipayGather(filePath, fileName);
//							}
//						}
//					}
//				} else {
//					logger.info("下载支付宝账单失败");
//					rmsg.setMsg("下载支付宝账单失败");
//					rmsg.setResult(true);
//					return rmsg;
//				}
//			}
//			/*else {
//				String fileName = String.format(ALIBILLFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//				aliBillList = getAlipayBillList(filePath, IOUtility.filterFilePath(fileName));
//				fileName = String.format(ALIBILLSUMFILENAME, DateUtils.formatDate(billdate, DateUtils.COMPACT_DATE_FORMAT));
//				gatherMap = getAlipayGather(filePath, fileName);
//			}*/
//
//			if (aliBillList == null || aliBillList.isEmpty()) {
//				logger.info(DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT) + ",获取账单解析记录为空。");
//				payBillTask.setRemark("账单解析记录为空。");
//				payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//				payBillTask.update();
//				rmsg.setMsg("账单解析记录为空");
//				rmsg.setResult(true);
//				return rmsg;
//			}
//
//			abnormalCount = 0;
//			//开始解析
//			parseDZInfo(aliBillList, billdate, aliPayMch.getPartner(), payBillTask.getId(), startmsg);
//
//			// region **** 更新对账记录为已完成
//			payBillTask.setCheckState(EnumList.TradeBillItemCheckState.CheckSuccess.value);
//			payBillTask.setBillAmount(new BigDecimal(gatherMap.get("totalBillAmount")));// 账单金额
//			payBillTask.setBillCount(Integer.parseInt(gatherMap.get("totalBillCount")));// 账单笔数
//			payBillTask.setAccountAmount(new BigDecimal(gatherMap.get("amount")));//实收净额
//			payBillTask.setAbnormalCount(abnormalCount);// 异常笔数
//			payBillTask.setRemark("已对账");
//			payBillTask.setPoundage(new BigDecimal(gatherMap.get("totalProcedureFee")));//服务费or手续费
//			payBillTask.update();
//			// endregion
//			// 处理结款记录
//			if (abnormalCount == 0) {
//				//productDeposite(billdate, new BigDecimal(gatherMap.get("totalBillAmount")), new BigDecimal(gatherMap.get("totalProcedureFee")), Integer.parseInt(gatherMap.get("totalBillCount")));
//
//				Deposite deposite = new Deposite();
//				// 1.现金充值 2.押金 3.微信充值 4.支付宝充值 5.农行充值 6.电e宝充值 7.银联充值
//				deposite.setFeeType(EnumList.FeeType.ECARDALI.value);
//				deposite.setDepositeDate(new sunbox.core.util.Date(DateUtils.getOnlyDate(billdate)));
//				deposite.load();
//				deposite.setDepositeBank(
//						getAppConfig().getConfig(EnumList.DepositeBankInfo.ECARD_DEPOSITE_BANK.value));
//				deposite.setBankAccountName(
//						getAppConfig().getConfig(EnumList.DepositeBankInfo.ECARD_DEPOSITE_BANK_ACCOUNT_NAME.value));
//				deposite.setBankAccountNo(
//						getAppConfig().getConfig(EnumList.DepositeBankInfo.ECARD_DEPOSITE_BANK_ACCOUNT_NO.value));
//				// 结款金额
//				deposite.setDepositeAmount( new BigDecimal(gatherMap.get("totalBillAmount")));
//				deposite.setPoundAge(new BigDecimal(gatherMap.get("totalProcedureFee")).abs());
//				deposite.setDepositeState("20");
//				deposite.setDepositeTime(new sunbox.core.util.Date());
//				deposite.setDepositeCount(Integer.parseInt(gatherMap.get("totalBillCount")));
//				deposite.update();
//			}
//
//		} catch (Exception ex) {
//			logger.error(startmsg + "获取支付宝对账列表失败。异常信息：" + ex.getMessage());
//			ex.printStackTrace();
//			payBillTask.setRemark("获取支付宝对账列表失败。异常信息:" + ex.getMessage());
//			payBillTask.setCheckState(EnumList.TradeBillItemCheckState.NotChecked.value);
//			payBillTask.update();
//		}
//
//		rmsg.setResult(isFinished);
//		rmsg.setMsg((isFinished ? "对账完成" : "对账未完成") + "。billdate:" + DateUtils.formatDate(billdate));
//		logger.info(String.format("##########[%s]:: 自动对账已完成--支付宝[%s]##########", "processBill",
//				DateUtils.formatDate(billdate, DateUtils.DEFAULT_DATE_FORMAT)));
//
//		Long time10 = new Date().getTime();
//		logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++支付宝对账结束时间：" + (time10 - time1) + "," + new Date());
//		return rmsg;
//	}
//
//
//	/**
//	 * 解析ECARD支付宝的对账信息
//	 * @param aliBillList
//	 * @param billdate
//	 * @param partner 商户号
//	 * @param payBillTaskId
//	 * @param startmsg
//	 * @throws InterruptedException
//	 *//*
//	private void EcardParseDZInfo(List<AlipayBillDto> aliBillList, Date billdate, String partner, Integer payBillTaskId, String startmsg) throws InterruptedException
//	{
//		String billThreadNum = PropertiesUtilWeChat.get("bill_thread_num");
//		//String billThreadNum = "1";
//
//		sunbox.core.util.Date onlyDate = new sunbox.core.util.Date(sunbox.core.util.DateUtils.getOnlyDate(billdate));
//		// 创建线程池
//		ExecutorService pool = Executors.newCachedThreadPool();
//
//		logger.info("获取支付宝对账笔数:" + aliBillList.size());
//		List<List<AlipayBillDto>> alipayBillDtoList = ListTools.averageAssign(aliBillList,
//				Integer.parseInt(billThreadNum));
//
//		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
//		Callable<Integer> task = null;
//
//		for (List<AlipayBillDto> alipayBillDtos : alipayBillDtoList) {
//			task = new Callable<Integer>() {
//				@Override
//				public Integer call() throws Exception {
//					// 开始遍历解析对账记录
//					for (AlipayBillDto alipayBillDto : alipayBillDtos) {
//						try {
//							logger.info("支付宝对账TradeNo:" + alipayBillDto.getShopTradeNo());
//							Map<String, String> orderIdOrUserIdMap = PayOrderOrUserIdUtil
//									.getOrderIdOrUserId(alipayBillDto.getShopTradeNo());
//							String userIdStr = orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.userIdKey);
//
//							Long orderId = Long.parseLong(orderIdOrUserIdMap.get(PayOrderOrUserIdUtil.orderIdKey));
//							Integer userId = Integer.parseInt(userIdStr);
//							// region 4.插入账单解析信息到pay_bill_rcard_alipay
//							PayBillRcardAlipay payBillAlipay = new PayBillRcardAlipay();
//							payBillAlipay.setAlipaytradeno(alipayBillDto.getAliTradeNo());
//							payBillAlipay.setTradeType(alipayBillDto.getTradeType());
//							payBillAlipay.load();
//							payBillAlipay.setOrderid(orderId.longValue());
//							payBillAlipay.setTradeTime(new sunbox.core.util.Date(alipayBillDto.getTradeTime()));
//							payBillAlipay.setUserAccount(alipayBillDto.getUserAccount());
//							payBillAlipay.setAmount(alipayBillDto.getIncomeAmount());
//							payBillAlipay.setRemark(alipayBillDto.getRemark());
//							payBillAlipay.setTradeProName(alipayBillDto.getTradeProName());
//							payBillAlipay.setBillDate(onlyDate);
//							payBillAlipay.setBsnsAccount(partner);
//							payBillAlipay.update();
//
//							// endregion
//
//							PayItem payItem = new PayItem();
//							payItem.setThirdTradeNo(alipayBillDto.getAliTradeNo());
//							payItem.setPayMode(Dict.PAY_MODE_ALIPAY);
//							payItem.setUserId(userId);
//							payItem.setOrderId(orderId.intValue());
//							Integer tradeType = alipayBillDto.getTradeType();
////							if (tradeType == AlipayBillDto.ALI_TRADETYPE_PAID) {
////								// 交易 is_refund 0或者null为交易，1为退费
////								payItem.setSqlWhere(" and (is_refund is null or is_refund = 0)");
////							} else {
////								payItem.setIsRefund(1);
////							}
//							PayItem payItemResp = payItem.load();
//
//							//如果通过第三方交易流水号未查到，通过orderid查询
//							if(payItemResp == null) {
//								OrderRcardSale orderRcardSale = new OrderRcardSale();
//								orderRcardSale.setId(orderId);
//								orderRcardSale.setUserId(userId);
//								orderRcardSale.load();
//								if (orderRcardSale.getIsLoad()) {
//									payItem.setThirdTradeNo(null);
//									payItem.setPayId(orderRcardSale.getPayId());
//									payItemResp = payItem.load();
//									if(payItemResp != null) {
//										if(payItemResp.getThirdTradeNo() == null) {
//											if(tradeType==AlipayBillDto.ALI_TRADETYPE_PAID){
//												payItemResp.setThirdTradeNo(alipayBillDto.getAliTradeNo());
//											}
//										}
//									}
//								}
//							}
//							// region 5.处理情况一，“第三方有，积分商城无”异常
//							if (payItemResp == null) {
//								insertPayDzException(userId.longValue(), payBillTaskId, alipayBillDto);
//
//								addAbnormalCount();
//								continue;
//							} else if (payItemResp
//									.getBillChkState() == EnumList.TradeBillItemCheckState.CheckSuccess.value) {// 已对账，无须再对
//								logger.info("支付宝-已对账,ThirdTradeNo:" + payItemResp.getThirdTradeNo());
//								continue;
//							}
//							// endregion
//
//							// region 6.处理情况二，“第三方有，积分商城有”异常
//							boolean b = EcardProcessBillSingle(payBillTaskId, alipayBillDto, payItemResp);
//							if (!b) {// 统计异常交易笔数
//								addAbnormalCount();
//							}
//							// 更新账单日期
//							payItemResp.setBillDate(onlyDate);
//							payItemResp.setBillChkState(b ? EnumList.TradeBillItemCheckState.CheckSuccess.value
//									: EnumList.TradeBillItemCheckState.CheckDiff.value);
//							payItemResp.update();
//							// endregion
//						} catch (NumberFormatException e) {
//							logger.error(startmsg + "支付宝解析对账记录。NumberFormat异常信息：" + e.getMessage());
//
//							insertUnknowPayDzException(payBillTaskId, alipayBillDto);
//
//							addAbnormalCount();
//							continue;
//						} catch (Exception e) {
//							logger.error(startmsg + "支付宝解析对账记录。异常信息：" + e.getMessage());
//							e.printStackTrace();
//							insertUnknowPayDzException(payBillTaskId, alipayBillDto);
//
//							addAbnormalCount();
//							continue;
//						}
//					}
//
//					return new Random().nextInt(1000);
//				}
//			};
//
//			tasks.add(task);
//		}
//
//		pool.invokeAll(tasks);
//		pool.shutdown();
//	}
//
//
//
//	public boolean EcardProcessBillSingle(Integer orderTradeBillTaskId, AlipayBillDto alipayBillDto, PayItem payItem) {
//		boolean isStateDiff = false;
//		boolean isAmountDiff = false;
//
//		Integer tradestate = payItem.getState();// 交易状态 '0待支付 1支付成功 2 支付失败 3支付取消
//												// 4退款中 5已退款',
//		BigDecimal shopAmount = payItem.getAmount();// 支付金额
//
//		Integer tradeType = alipayBillDto.getTradeType();// 交易类型 1消费； 2 退款
//		BigDecimal thirdAmount = (tradeType == AlipayBillDto.ALI_TRADETYPE_PAID ? alipayBillDto.getIncomeAmount()
//				: alipayBillDto.getRefundAmount().abs());
//		String thirdTradeNo = alipayBillDto.getAliTradeNo();
//		// region 分别判断积分、状态是否一致
//
//		// 判断交易状态 （思考是否加一个 状态= 用户扣款成功但是未发货 而是发起退款的状态判断）
//		if ((tradeType == AlipayBillDto.ALI_TRADETYPE_PAID && !tradestate.equals(Dict.PAY_ITEM_STATE_PAID_SUCCESS))&&!tradestate.equals(Dict.PAY_STATE_REFUND_WHEN_FAIL)
//				|| (tradeType == AlipayBillDto.ALI_TRADETYPE_REFUND && !tradestate.equals(Dict.PAY_ITEM_STATE_REFUND_SUCCESS))) {
//			logger.info(String.format("支付宝交易状态不一致,当前状态:[%s],消费成功:[%s],退款成功:[%s]", tradestate,
//					Dict.PAY_ITEM_STATE_PAID_SUCCESS, Dict.PAY_ITEM_STATE_REFUND_SUCCESS));
//			isStateDiff = true; // 状态不一致。后台管理，可以做后续处理，如：更新积分商城账单状态等
//		}
//
//		// 判断金额是否相同
//		if (shopAmount.abs().compareTo(thirdAmount.abs()) != 0) {
//			logger.info(String.format("支付宝交易绝对值金额不同,积分商城:[%s],第三方:[%s]", shopAmount, thirdAmount));
//			isAmountDiff = true; // 金额不同
//		}
//
//		// endregion
//
//		// region 处理积分或状态不一致异常。插入异常记录
//		if (isStateDiff || isAmountDiff) {
//			PayDzexception dzException = new PayDzexception();
//			dzException.setTradeBillTaskId(orderTradeBillTaskId);
//			dzException.setPayItemId(payItem.getId());
//			dzException.load();
//			dzException.setIsRefund(payItem.getIsRefund() == null ? 0 : payItem.getIsRefund());
//			dzException.setProcessType(EnumList.ProcessType.UNDISPOSE.value);
//			dzException.setUserId(payItem.getUserId().longValue());
//			dzException.setDzObject(String.valueOf(EnumList.BillCheckObject.CHECKALIPAY.value));
//			dzException.setThirdBillTradeNo(alipayBillDto.getAliTradeNo());
//
//			if (isStateDiff && isAmountDiff) {
//				dzException.setExceptionMsg(EnumList.BillCheckExceptionCase.DiffAmountAndState.text);
//				dzException.setExceptionCase(EnumList.BillCheckExceptionCase.DiffAmountAndState.value);
//				dzException.setThirdBillAmount(thirdAmount);
//			} else {
//				if (isStateDiff) {
//					dzException.setExceptionMsg(EnumList.BillCheckExceptionCase.DiffState.text);
//					dzException.setExceptionCase(EnumList.BillCheckExceptionCase.DiffState.value);
//					dzException.setThirdBillAmount(thirdAmount);
//				}
//			}
//			// 插入异常记录
//			dzException.update();
//			return false;
//		}
//		// endregion
//
//		return true;
//	}*/
//	public static void main(String[] args) {
//		String ss= "{gmt_create=2018-07-25 18:19:38, charset=UTF-8, seller_email=gwddqc@126.com, subject=充值, sign=hh1gQ+COvGPzLPIjbxNFo5h18p3clFw7ZccxY5N1jL6B33kiOXMsAV/nWNyrQ7YiXIXp/l9x4JbzcPkUZ9sqxcWlUDkQkRo+O6V5m2KV7pmqBqHGIZKX1ygLFAUN9SRkWE+JbP90XQV6dro0CsWBtyeX6tByCDYh+QuL92NcWh8=, buyer_id=2088422378962564, invoice_amount=0.01, notify_id=c6ac5e78a4d8bac820b47203b24e636kbp, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}], notify_type=trade_status_sync, trade_status=TRADE_SUCCESS, receipt_amount=0.01, buyer_pay_amount=0.01, app_id=2016033001252816, sign_type=RSA, seller_id=2088221352863875, gmt_payment=2018-07-25 18:19:40, notify_time=2018-07-25 18:19:40, version=1.0, out_trade_no=102600153X200336833, total_amount=0.01, trade_no=2018072521001004560568354430, auth_app_id=2016033001252816, buyer_logon_id=159****6485, point_amount=0.00}";
//		System.err.println(ss.replace(", ", "&"));
//	}
//}
