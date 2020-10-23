package co.ocha.pratikum_progmob.model;

import com.google.gson.annotations.SerializedName;

public class TokenResult{

	@SerializedName("token")
	private String token;

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Result{" + 
			"token = '" + token + '\'' + 
			"}";
		}
}