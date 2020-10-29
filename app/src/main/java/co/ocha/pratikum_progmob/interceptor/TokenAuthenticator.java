package co.ocha.pratikum_progmob.interceptor;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import co.ocha.pratikum_progmob.LoginActivity;
import co.ocha.pratikum_progmob.MyApp;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.TokenResponse;
import co.ocha.pratikum_progmob.model.UserResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class TokenAuthenticator implements Interceptor {

    SharedPrefed sharedPrefed;
    Context mcontext;

    public TokenAuthenticator(Context context){
        this.mcontext = context;
        sharedPrefed = new SharedPrefed(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response mainResponse = chain.proceed(chain.request());
        Request mainRequest = chain.request();
        ApiInterface apiInterface = RetrofitClient.getClient(mcontext).create(ApiInterface.class);

        if(mainResponse.code() == 401 || mainResponse.code() == 403){
            String token = sharedPrefed.getSPToken();
            retrofit2.Response<TokenResponse> refreshToken = apiInterface.refreshToken(token).execute();
            if(refreshToken.isSuccessful()){
                sharedPrefed.saveSPString(SharedPrefed.SP_TOKEN,"Bearer "+refreshToken.body().getResult().getToken());
                Request.Builder builder  =  mainRequest.newBuilder().header("Authorization",sharedPrefed.getSPToken())
                        .method(mainRequest.method(), mainRequest.body());
                mainResponse = chain.proceed(builder.build());
            }
        }else if(mainResponse.code() == 500){
            sharedPrefed.saveSPBoolean(sharedPrefed.SP_SUDAH_LOGIN, false);
            Intent i = new Intent(MyApp.getContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApp.getContext().startActivity(i);
        }

        return mainResponse;
    }
}
