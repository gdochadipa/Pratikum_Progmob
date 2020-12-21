package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.AddCartResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {
    int num;
    ImageView imgMinus;
    ImageView imgPlus;
    TextView txtNumber;

    private int book_id, qty;

    ApiInterface apiInterface;
    Context context;
    SharedPrefed sharedPrefed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        TextView tvTitle = findViewById(R.id.tv_title);
        ImageView ivCover = findViewById(R.id.iv_cover);
        TextView tvWriter = findViewById(R.id.tv_writer);
        TextView tvDesc = findViewById(R.id.tv_desc);
        TextView tvLanguage = findViewById(R.id.tv_language);
        TextView tvStock = findViewById(R.id.tv_stock);
        TextView tvPrice = findViewById(R.id.tv_price);

        apiInterface = RetrofitClient.getClient(this.context).create(ApiInterface.class);
        sharedPrefed = new SharedPrefed(this);

        String title = "title not set";
        String cover = "cover not set";
        String writer = "writer not set";
        String description = "description not set";
        String language = "language not set";
        int stock = 0;
        int price = 0;
        int id = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            title = extras.getString("title");
            cover = extras.getString("cover");
            writer = extras.getString("writer");
            description = extras.getString("description");
            language = extras.getString("language");
            stock = extras.getInt("stock");
            price = extras.getInt("price");
        }

        tvTitle.setText(title);
        Glide.with(this).load(cover).into(ivCover);
        tvWriter.setText(writer);
        tvDesc.setText(description);
        tvLanguage.setText(language);
        tvStock.setText(String.valueOf(stock));
        tvPrice.setText(String.valueOf(price));

        if (stock == 0){
            num = 0;
        } else {
            num = 1;
        }

        txtNumber = (TextView) findViewById(R.id.txtNumbers);
        txtNumber.setText(String.valueOf(num));

        final int stockFinal = stock;
        final int idFinal = id;

        imgMinus = (ImageView) findViewById(R.id.imgMinus);
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockFinal != 0) {
                    if (num > 1) {
                        num--;
                    }
                    setText();
                }
            }
        });

        imgPlus = (ImageView) findViewById(R.id.imgPlus);
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockFinal != 0){
                    if (num == stockFinal){
                        num = 1;
                    } else {
                        num++;
                    }
                }
                setText();
            }
        });

        /*Button btn1 = (Button) findViewById(R.id.purchaseBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num1 = num;
                String str2 = " Purchased!";
                String concatenatedText = num1 + str2;
                Toast.makeText(getBaseContext(), concatenatedText, Toast.LENGTH_SHORT ).show();
            }
        });*/

        Button btn2 = (Button) findViewById(R.id.addCartBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = num;
                book_id = idFinal;

                if (stockFinal != 0){
                    createCart();
                } else {
                    Toast.makeText(getApplicationContext()," We're sorry, Item out of stock",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setText(){
        txtNumber = (TextView) findViewById(R.id.txtNumbers);
        txtNumber.setText(num+"");
    }

    private void createCart(){
        String user_token = sharedPrefed.getSPToken();

       Call<AddCartResponse> postCart = apiInterface.addToCart(
                user_token,
                book_id,
                qty);
        postCart.enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext()," Successfully added to cart!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                //Toast.makeText(getApplicationContext()," Cant connect to server!",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}