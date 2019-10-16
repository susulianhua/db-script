package com.xquant.script.pojo.ReturnClass;


public class NormalResponse extends Response {

	public NormalResponse(Object data) {
		this.setCode(ResponseCode.OK);
		this.setMessage("");
		this.setData(data);
		this.setSuccess(true);
	}
	
	public NormalResponse(Object data, Long total) {
		this.setCode(ResponseCode.OK);
		this.setMessage("");
		this.setData(data);
		this.setSuccess(true);
		this.setTotal(total);
	}

	public NormalResponse() {
		this.setCode(ResponseCode.OK);
		this.setMessage("");
		this.setData(null);
		this.setSuccess(true);
	}

	public NormalResponse(int t){
		this.setCode(ResponseCode.OK);
		this.setMessage("");
		this.setData("");
		if(t == 1) this.setSuccess(true);
		else this.setSuccess(false);
	}
}
