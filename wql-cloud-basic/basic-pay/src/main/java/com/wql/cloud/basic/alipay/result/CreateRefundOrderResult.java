package com.wql.cloud.basic.alipay.result;

public class CreateRefundOrderResult {

	private Boolean result;
	
	/**退款结果*/
	private Boolean refundResult;
	
	private String resultMsg;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Boolean getRefundResult() {
		return refundResult;
	}

	public void setRefundResult(Boolean refundResult) {
		this.refundResult = refundResult;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
}
