package co.ocha.pratikum_progmob.FirebaseConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Field;

import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.TokenResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RefreshFCMToken {

    public static void getToken(final Context context){
        final String TAG = "RT_TOKEN";
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        final String token = task.getResult().getToken();
                        Log.d(TAG,"Refresh FCM Token ");
                        SharedPrefed sharedPrefed = new SharedPrefed(context);
                        ApiInterface apiInterface = RetrofitClient.getClient(context).create(ApiInterface.class);
                        String user_token = sharedPrefed.getSPToken();

                        Call<TokenResponse> postToken = apiInterface.refreshFCMToken(user_token,token);
                        postToken.enqueue(new Callback<TokenResponse>() {
                            @Override
                            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                                if(response.code() == 200 ){
                                    if(response.body().getStatus() == "error"){
                                        Log.e(TAG, "Found Error :"+response.body().getResult().toString());
                                    }
                                }else{
                                    Log.d(TAG, "Success Update Token at Server :"+response.body().getResult().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<TokenResponse> call, Throwable t) {
                                Log.e(TAG, "Found Error onFailure:"+t.getMessage());
                            }
                        });
                    }
                });
    }

}
