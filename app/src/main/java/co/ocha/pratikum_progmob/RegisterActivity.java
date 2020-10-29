package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ocha.pratikum_progmob.model.QueryResponse;
import co.ocha.pratikum_progmob.model.RegisterResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    Context context;
    ProgressDialog progressDialog;

    @BindView(R.id.etEmail)
    TextView txtEmail;

    @BindView(R.id.etPassword)
    TextView txtPassword;

    @BindView(R.id.etName)
    TextView txtName;

    @BindView(R.id.et_c_Password)
    TextView txtPasswordCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        context = this;

        ButterKnife.bind(this);
        apiInterface = RetrofitClient.getClient(getApplicationContext()).create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);


    }

    @OnClick(R.id.btnRegister) void register(){
        progressDialog.show();

        if(validation()){

            Call<RegisterResponse> postRegister = apiInterface.register(
                    txtName.getText().toString(),
                    txtEmail.getText().toString(),
                    txtPassword.getText().toString(),
                    txtPasswordCheck.getText().toString());
            postRegister.enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    progressDialog.dismiss();
//                    if(response.code() == 200){
//                        Toast.makeText(getApplicationContext()," Register berhasil, silahkan login",Toast.LENGTH_LONG).show();
//                        Log.d("RESPONSE", "onResponse: "+response.message().toString());
//                        startActivity(new Intent(context, LoginActivity.class)
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//
//                    }
                    //php artisan serve --host 0.0.0.0
                    try {
                        if(response.body()!=null){
                            Toast.makeText(getApplicationContext()," response message body" + response.body().toString(),Toast.LENGTH_LONG).show();
                        }if(response.errorBody()!=null) {
                            Toast.makeText(getApplicationContext(), response.errorBody().string(),Toast.LENGTH_LONG).show();
                            RegisterResponse registerResponse = new Gson().fromJson(response.errorBody().string(),RegisterResponse.class);
                            Log.d("MESSAGE",registerResponse.getStatus());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }else {
            progressDialog.dismiss();
        }
    }

    private boolean validation(){
        boolean valid = true;

        if(txtName.getText().toString().length()==0){
            valid = false;
            txtName.requestFocus();
            txtName.setError("Nama tidak boleh kosong");
        }else if(txtName.getText().toString().matches("[a-zA-Z ]+")){
            valid = false;
            txtName.requestFocus();
            txtName.setError("Hanya boleh memasukan huruf");
        }

        if(txtEmail.getText().toString().length()==0){
            valid = false;
            txtEmail.requestFocus();
            txtEmail.setError("Email tidak boleh kosong");
        }else if(txtEmail.getText().toString().matches("[a-zA-Z ]+")){
            valid = false;
            txtEmail.requestFocus();
            txtEmail.setError("Hanya boleh memasukan huruf");
        }

        if(txtPassword.getText().toString().length()>=6){
            valid = false;
            txtPassword.requestFocus();
            txtPassword.setError("Password harus diatas 6 karakter");
        }

        if(txtPassword.getText().toString().equals(txtPasswordCheck.getText().toString())){
            valid = false;
            txtPasswordCheck.requestFocus();
            txtPasswordCheck.setError("Password harus sama ");
        }

        return valid;

    }
}