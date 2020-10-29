package co.ocha.pratikum_progmob.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse{

	@SerializedName("message")
	private Message message;

	@SerializedName("status")
	private String status;

	public Message getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}