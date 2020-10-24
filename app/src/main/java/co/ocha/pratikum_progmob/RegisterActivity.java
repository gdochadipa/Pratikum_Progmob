package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ocha.pratikum_progmob.model.QueryResponse;
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

    @BindView(R.id.etEmail)
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
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);


    }

    @OnClick(R.id.btnRegister) void register(){
        progressDialog.show();
        Call<QueryResponse> postRegister = apiInterface.register(txtEmail.getText().toString(),
                txtName.getText().toString(),
                txtPassword.getText().toString(),
                txtPasswordCheck.getText().toString());
        postRegister.enqueue(new Callback<QueryResponse>() {

            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                progressDialog.dismiss();
                if(response.code() == 200){
                    startActivity(new Intent(context, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }else if(response.code() == 401){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}