package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.AddressModel;
import co.ocha.pratikum_progmob.model.AddressResponse;
import co.ocha.pratikum_progmob.model.AddTransactionResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {

    SharedPrefed sharedPrefed;
    private List<AddressModel> listData = new ArrayList<>();
    Spinner spinnerAddress;
    int addressId;
    ApiInterface apiInterface;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        spinnerAddress = findViewById(R.id.listAddress);

        sharedPrefed = new SharedPrefed(this);
        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);

        initSpinnerAddress();

        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
                addressId = listData.get(position).getId();
                //Toast.makeText(getApplicationContext(), String.valueOf(addressId), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn1 = (Button) findViewById(R.id.btnSelectAddress);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTransaction();
            }
        });
    }

    private void createTransaction(){
        String user_token = sharedPrefed.getSPToken();
        //Toast.makeText(getApplicationContext(), user_token, Toast.LENGTH_LONG).show();

        Call<AddressResponse> postTransaction = apiInterface.getAddress(
                user_token);
        postTransaction.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext()," Successfully purchased!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinnerAddress(){
        String user_token = sharedPrefed.getSPToken();

        ApiInterface ardData = RetrofitClient.getClient(getApplicationContext()).create(ApiInterface.class);
        Call<AddressResponse> tampilData = ardData.getAddress(user_token);

        tampilData.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {

                listData = response.body().getResult();
                Log.d("myTag", listData.toString());

                if (listData.size() == 0){
                    Toast.makeText(getApplicationContext(), "You dont have an address to select, add your address in main menu!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
                    startActivity(intent);
                }

                if (response.isSuccessful()) {
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < listData.size(); i++){
                        listSpinner.add(listData.get(i).getAddress());
                    }

                    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listSpinner);*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.custom_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAddress.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                //getDataSQLite();
                Toast.makeText(getApplicationContext(), "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}