package co.ocha.pratikum_progmob.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse{

	@SerializedName("result")
	private UserResult result;

	@SerializedName("status")
	private String status;

	public void setResult(UserResult result){
		this.result = result;
	}

	public UserResult getResult(){
		return result;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}