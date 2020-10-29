package co.ocha.pratikum_progmob.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Message{

	@SerializedName("password")
	private List<String> password;

	@SerializedName("c_password")
	private List<String> cPassword;

	@SerializedName("name")
	private List<String> name;

	@SerializedName("email")
	private List<String> email;

	public List<String> getPassword(){
		return password;
	}

	public List<String> getCPassword(){
		return cPassword;
	}

	public List<String> getName(){
		return name;
	}

	public List<String> getEmail(){
		return email;
	}
}