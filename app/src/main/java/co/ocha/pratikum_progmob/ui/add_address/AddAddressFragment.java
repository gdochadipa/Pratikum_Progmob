package co.ocha.pratikum_progmob.ui.add_address;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.AddCartResponse;
import co.ocha.pratikum_progmob.model.AddressResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressFragment extends Fragment {

    ApiInterface apiInterface;
    Context context;
    ProgressDialog progressDialog;
    SharedPrefed sharedPrefed;

    EditText address, district, province;
    Button addAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_address, container, false);

        address = (EditText) root.findViewById(R.id.etAddress);
        district = (EditText) root.findViewById(R.id.etDistrict);
        province = (EditText) root.findViewById(R.id.etProvince);
        addAddress = (Button) root.findViewById(R.id.btnAddAddress);

        sharedPrefed = new SharedPrefed(getContext());
        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressText, districtText, provinceText;
                String user_token = sharedPrefed.getSPToken();
                addressText = address.getText().toString();
                districtText = district.getText().toString();
                provinceText = province.getText().toString();
                //Toast.makeText(getActivity(), addressText, Toast.LENGTH_SHORT).show();
                Call<AddCartResponse> postCart = apiInterface.addAddress(
                        user_token,
                        addressText,
                        districtText,
                        provinceText,
                        1);
                postCart.enqueue(new Callback<AddCartResponse>() {
                    @Override
                    public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                        if(response.code() == 200){
                            Toast.makeText(getActivity()," Successfully added new address!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddCartResponse> call, Throwable t) {
                        //Toast.makeText(getApplicationContext()," Cant connect to server!",Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return root;
    }
}