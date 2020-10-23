package co.ocha.pratikum_progmob.remote;

import co.ocha.pratikum_progmob.model.QueryResponse;
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
    @POST("/login")
    Call<TokenResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/register")
    Call<QueryResponse> register(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("password") String password,
            @Field("c_password") String c_password
    );

    @GET("/user/detail")
    Call<TokenResponse> refreshToken(@Header("Authorization") String token);

}
