package co.ocha.pratikum_progmob.remote;

import co.ocha.pratikum_progmob.model.QueryResponse;
import co.ocha.pratikum_progmob.model.RegisterResponse;
import co.ocha.pratikum_progmob.model.TokenResponse;
import co.ocha.pratikum_progmob.model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/login")
    Call<TokenResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<RegisterResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("c_password") String c_password
    );

    @GET("api/user/detail")
    Call<UserResponse> getUser(@Header("Authorization") String token);

    @GET("api/user/token")
    Call<TokenResponse> refreshToken(@Header("Authorization") String token);

}
