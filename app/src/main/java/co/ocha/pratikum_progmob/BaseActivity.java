package co.ocha.pratikum_progmob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import co.ocha.pratikum_progmob.FirebaseConfig.RefreshFCMToken;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.adapter.TransactionDetailAdapter;
import co.ocha.pratikum_progmob.model.TransactionDetailModel;
import co.ocha.pratikum_progmob.model.TransactionDetailResponse;
import co.ocha.pratikum_progmob.model.UserResponse;
import co.ocha.pratikum_progmob.model.UserResult;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPrefed sharedPrefManager;
    ApiInterface apiInterface;
    Context context;
    SharedPrefed sharedPrefed;

    String namaUser, emailUser, fotoUser;
    TextView tvNavNamaUser, tvNavEmailUser;
    ImageView ivNavFotoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);
        sharedPrefed = new SharedPrefed(this);
        sharedPrefManager = new SharedPrefed(this);

        retrieveData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_profile, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        RefreshFCMToken.getToken(getApplicationContext());

        View  navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvNavNamaUser = navHeaderView.findViewById(R.id.profileName);
        tvNavEmailUser = navHeaderView.findViewById(R.id.profileEmail);
        ivNavFotoUser = navHeaderView.findViewById(R.id.profileImage);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id==R.id.nav_logout){
                    sharedPrefManager.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,false);
                    startActivity(new Intent(BaseActivity.this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /*@OnClick(R.id.nav_logout)void onLogout(){
        sharedPrefManager.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,false);
        startActivity(new Intent(BaseActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void retrieveData(){
        String user_token = sharedPrefed.getSPToken();

        ApiInterface ardData = RetrofitClient.getClient(this).create(ApiInterface.class);
        Call<UserResponse> tampilData = ardData.getUser(user_token);

        tampilData.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                namaUser = response.body().getResult().getName();
                emailUser = response.body().getResult().getEmail();
                fotoUser = response.body().getResult().getPhoto_profile();
                sharedPrefed.saveSPString(SharedPrefed.SP_NAMA, namaUser);
                sharedPrefed.saveSPString(SharedPrefed.SP_EMAIL, emailUser);
                sharedPrefed.saveSPString(SharedPrefed.SP_PHOTO, emailUser);
                tvNavNamaUser.setText(namaUser);
                tvNavEmailUser.setText(emailUser);
                //Toast.makeText(getApplicationContext(), fotoUser, Toast.LENGTH_SHORT).show();
               // Glide.with(getApplicationContext()).load(fotoUser).into(ivNavFotoUser);
                Glide.with(getApplicationContext())
                        .load(fotoUser)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(ivNavFotoUser) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(),
                                        Bitmap.createScaledBitmap(resource, 100, 100, false));
                                drawable.setCircular(true);
                                ivNavFotoUser.setImageDrawable(drawable);
                            }
                        });
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                namaUser = sharedPrefed.getSPNama();
                emailUser = sharedPrefed.getSPEmail();
                fotoUser = sharedPrefed.getSPPhoto();
                tvNavNamaUser.setText(namaUser);
                tvNavEmailUser.setText(emailUser);
                //Glide.with(getApplicationContext()).load(fotoUser).into(ivNavFotoUser);
                Glide.with(getApplicationContext())
                        .load(fotoUser)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(ivNavFotoUser) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(),
                                        Bitmap.createScaledBitmap(resource, 50, 50, false));
                                drawable.setCircular(true);
                                ivNavFotoUser.setImageDrawable(drawable);
                            }
                        });
            }
        });
    }
}