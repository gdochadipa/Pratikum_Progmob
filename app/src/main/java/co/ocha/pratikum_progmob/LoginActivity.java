package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.QueryResponse;
import co.ocha.pratikum_progmob.model.TokenResponse;
import co.ocha.pratikum_progmob.model.TokenResult;
import co.ocha.pratikum_progmob.model.UserResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    SharedPrefed sharedPrefed;
    ApiInterface apiInterface;
    Context context;
    ProgressDialog progressDialog;
    String TAG = "LOGIN_RESPONSE";


    @BindView(R.id.etEmail)
    TextView txtEmail;

    @BindView(R.id.etPassword)
    TextView txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context = this;

        ButterKnife.bind(this);
        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);
        sharedPrefed = new SharedPrefed(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        progressDialog.show();
        if(sharedPrefed.getSPSudahLogin()){
            Log.d(TAG, "onResponse: Intent Now");
            startActivity(new Intent(context, BaseActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            progressDialog.dismiss();
        }else{
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.btnLogin) void login(){
        progressDialog.show();
        if(validation()){
            Call<TokenResponse> postLogin = apiInterface.login(txtEmail.getText().toString(),txtPassword.getText().toString());

            postLogin.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    progressDialog.dismiss();

                    if(response.code() == 200 ){
                        Log.d(TAG, "onResponse: "+response.code());
                        if(response.body().getStatus() == "error"){
                            Toast.makeText(context,response.body().getResult().toString() , Toast.LENGTH_SHORT).show();
                        }else{
                            TokenResult token = response.body().getResult();
                            sharedPrefed.saveSPString(SharedPrefed.SP_TOKEN,"Bearer "+token.getToken());
                            sharedPrefed.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,true);
                            startActivity(new Intent(context, BaseActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }

                    }else{
                        Toast.makeText(context, "Email atau Password salah", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.btnRegister)void toRegister(){
        startActivity(new Intent(context, RegisterActivity.class));
    }

    private boolean validation(){
        boolean valid = true;


        if(txtEmail.getText().toString().length()==0){
            valid = false;
            txtEmail.requestFocus();
            txtEmail.setError("Email tidak boleh kosong");
        }else if(txtEmail.getText().toString().matches("[a-zA-Z ]+")){
            valid = false;
            txtEmail.requestFocus();
            txtEmail.setError("Hanya boleh memasukan huruf");
        }

        if(txtPassword.getText().toString().length()<=6){
            valid = false;
            txtPassword.requestFocus();
            txtPassword.setError("Password harus 6 karakter lebih");
        }
        return valid;

    }

}