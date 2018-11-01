package com.mmall.payment.ali;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25.
 */
public class AlipayUnifiedDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String detail_error_code;
    private String detail_error_des;
    private String display_message;
    private String out_trade_no;
    private String result_code;
    private String trade_no;

    public void setDetail_error_code(String detail_error_code) {
        this.detail_error_code = detail_error_code;
    }

    public void setDetail_error_des(String detail_error_des) {
        this.detail_error_des = detail_error_des;
    }

    public void setDisplay_message(String display_message) {
        this.display_message = display_message;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getDetail_error_code() {

        return detail_error_code;
    }

    @Override
    public String toString() {
        return "AlipayUnifiedDto{" +
                "detail_error_code='" + detail_error_code + '\'' +
                ", detail_error_des='" + detail_error_des + '\'' +
                ", display_message='" + display_message + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", trade_no='" + trade_no + '\'' +
                '}';
    }

    public String getDetail_error_des() {
        return detail_error_des;
    }

    public String getDisplay_message() {
        return display_message;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getTrade_no() {
        return trade_no;
    }
}
