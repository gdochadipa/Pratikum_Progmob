package co.ocha.pratikum_progmob.remote;

import co.ocha.pratikum_progmob.model.AddressResponse;
import co.ocha.pratikum_progmob.model.BookResponse;
import co.ocha.pratikum_progmob.model.AddCartResponse;
import co.ocha.pratikum_progmob.model.CartResponse;
import co.ocha.pratikum_progmob.model.RegisterResponse;
import co.ocha.pratikum_progmob.model.TokenResponse;
import co.ocha.pratikum_progmob.model.AddTransactionResponse;
import co.ocha.pratikum_progmob.model.TransactionDetailResponse;
import co.ocha.pratikum_progmob.model.TransactionResponse;
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

    @POST("api/logout")
    Call<TokenResponse> logout(@Header("Authorization") String token);

    @GET("api/books")
    Call<BookResponse> apiGetBook();

    @FormUrlEncoded
    @POST("api/book/detail")
    Call<BookResponse> getBookDetail(
            @Field("id") int id
    );

    @GET("api/cart")
    Call<CartResponse> getCart(@Header("Authorization") String token);

    @GET("api/transaction")
    Call<TransactionResponse> getTransaction(@Header("Authorization") String token);

    @GET("api/address")
    Call<AddressResponse> getAddress(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/address")
    Call<AddCartResponse> addAddress(
            @Header("Authorization") String token,
            @Field("address") String address,
            @Field("district") String district,
            @Field("province") String province,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("api/updateToken")
    Call<TokenResponse> refreshFCMToken(
            @Header("Authorization") String token,
            @Field("fcm_token") String fcm_token
    );

    @FormUrlEncoded
    @POST("api/cart")
    Call<AddCartResponse> addToCart(
            @Header("Authorization") String token,
            @Field("book_id") int book_id,
            @Field("qty") int qty
    );

    @FormUrlEncoded
    @POST("api/transaction/detail")
    Call<TransactionDetailResponse> getTransactionDetail(
            @Header("Authorization") String token,
            @Field("id") int id
    );
}
