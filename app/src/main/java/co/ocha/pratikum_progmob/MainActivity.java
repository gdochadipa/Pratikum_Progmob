package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.remote.ApiInterface;


public class MainActivity extends AppCompatActivity {


    ApiInterface apiInterface;
    SharedPrefed sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager = new SharedPrefed(this);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnLogout)void onLogout(){
        sharedPrefManager.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}