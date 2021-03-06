package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.AddCartResponse;
import co.ocha.pratikum_progmob.model.AddTransactionResponse;
import co.ocha.pratikum_progmob.model.AddressResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartDetailActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    Context context;
    SharedPrefed sharedPrefed;

    int cart_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);

        TextView tvTitle = findViewById(R.id.tv_title);
        ImageView ivCover = findViewById(R.id.iv_cover);
        TextView tvWriter = findViewById(R.id.tv_writer);
        TextView tvDesc = findViewById(R.id.tv_desc);
        TextView tvLanguage = findViewById(R.id.tv_language);

        TextView tvQty = findViewById(R.id.tv_qty);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvQtyInPrice = findViewById(R.id.tv_qty_in_price);

        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);
        sharedPrefed = new SharedPrefed(this);

        int id = 0;
        String title = "title not set";
        String cover = "cover not set";
        String writer = "writer not set";
        String description = "description not set";
        String language = "language not set";

        int qty = 0;
        int price = 0;
        int total_price = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("book_id");
            cart_id = extras.getInt("id");
            title = extras.getString("book_title");
            cover = extras.getString("book_cover");
            writer = extras.getString("book_writer");
            description = extras.getString("book_description");
            language = extras.getString("book_language");
            qty = extras.getInt("qty");
            price = extras.getInt("book_price");
            total_price = price * qty;
        }

        tvTitle.setText(title);
        Glide.with(this).load(cover).into(ivCover);
        tvWriter.setText(writer);
        tvDesc.setText(description);
        tvLanguage.setText(language);

        tvQty.setText(String.valueOf(qty));
        tvQtyInPrice.setText(String.valueOf(qty));
        tvPrice.setText(String.valueOf(total_price));

        Button btn1 = (Button) findViewById(R.id.btnCheckout);
        Button btn2 = (Button) findViewById(R.id.btnRemoveCart);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createCart();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCart();
            }
        });
    }

    private void createCart(){
        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
        startActivity(intent);
    }

    private void removeCart(){
        //Toast.makeText(getApplicationContext(), String.valueOf(cart_id), Toast.LENGTH_SHORT).show();
        String user_token = sharedPrefed.getSPToken();

        Call<AddCartResponse> postTransaction = apiInterface.deleteCart(
                cart_id,
                user_token);
        postTransaction.enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext()," Successfully deleted!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}