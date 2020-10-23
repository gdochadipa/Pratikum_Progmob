package co.ocha.pratikum_progmob.model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse{

	@SerializedName("result")
	private TokenResult result;

	@SerializedName("status")
	private String status;

	public void setResult(TokenResult result){
		this.result = result;
	}

	public TokenResult getResult(){
		return result;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TokenResponse{" + 
			"result = '" + result + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}