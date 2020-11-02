package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}