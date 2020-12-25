package co.ocha.pratikum_progmob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.ocha.pratikum_progmob.SQLite.DBCarts;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.adapter.TransactionAdapter;
import co.ocha.pratikum_progmob.adapter.TransactionDetailAdapter;
import co.ocha.pratikum_progmob.model.BookModel;
import co.ocha.pratikum_progmob.model.BookResponse;
import co.ocha.pratikum_progmob.model.TransactionDetailModel;
import co.ocha.pratikum_progmob.model.TransactionDetailResponse;
import co.ocha.pratikum_progmob.model.TransactionModel;
import co.ocha.pratikum_progmob.model.TransactionResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailActivity extends AppCompatActivity {

    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<TransactionDetailModel> listDetail = new ArrayList<>();
    private List<BookModel> listBook = new ArrayList<>();
    private SwipeRefreshLayout srData;
    private ProgressBar pbData;
    private TransactionDetailAdapter.RecyclerViewClickListener listener;
    SharedPrefed sharedPrefed;
    private DBCarts dbCarts;
    ApiInterface apiInterface;

    int transaction_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        //TextView tvAddress = findViewById(R.id.tv_address);

        String address = "address not set";
        int id = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            address = extras.getString("address");
        }

        transaction_id = id;
        //tvAddress.setText(address);

        rvData = findViewById(R.id.rv_data);
        srData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        sharedPrefed = new SharedPrefed(this);

        setOnClickListener();

        lmData =  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        srData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srData.setRefreshing(true);
                retrieveData();
                srData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveData();
    }

    public void retrieveData(){
        String user_token = sharedPrefed.getSPToken();
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = RetrofitClient.getClient(this).create(ApiInterface.class);
        Call<TransactionDetailResponse> tampilData = ardData.getTransactionDetail(user_token, transaction_id);

        tampilData.enqueue(new Callback<TransactionDetailResponse>() {
            @Override
            public void onResponse(Call<TransactionDetailResponse> call, Response<TransactionDetailResponse> response) {

                listDetail = response.body().getResult();
                Log.d("myTag", listDetail.toString());

                // SQLite Teritory
                /*dbCarts = new DBCarts(getContext());
                SQLiteDatabase create = dbCarts.getWritableDatabase();
                ContentValues values = new ContentValues();

                for(int i = 0; i < listData.size(); i++){
                    Log.d("myTag", listData.get(i).getStatus());
                    values.put(DBCarts.MyColumns.id, listData.get(i).getId());
                    values.put(DBCarts.MyColumns.user_id, listData.get(i).getUser_id());
                    values.put(DBCarts.MyColumns.book_id, listData.get(i).getBook_id());
                    values.put(DBCarts.MyColumns.qty, listData.get(i).getQty());
                    values.put(DBCarts.MyColumns.status, listData.get(i).getStatus());
                    create.insert(DBCarts.MyColumns.NamaTabel, null, values);
                }*/

                // Adapter Teritory
                adData = new TransactionDetailAdapter(getApplication(), listDetail, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<TransactionDetailResponse> call, Throwable t) {
                //getDataSQLite();
                //Toast.makeText(getApplicationContext(), "faillureee", Toast.LENGTH_SHORT).show();

                /*adData = new CartAdapter(getActivity(), listData, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();*/

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*protected void getDataSQLite(){
        //Mengambil Repository dengan Mode Membaca
        listData = new ArrayList<>();
        SQLiteDatabase ReadData = dbCarts.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM "+ DBCarts.MyColumns.NamaTabel,null);

        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal

        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for(int count=0; count < cursor.getCount(); count++){
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir

            listData.add(new CartModel(cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4)));
        }
    }*/

    private void setOnClickListener() {
        listener = new TransactionDetailAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                int book_id = listDetail.get(position).getBook_id();

                ApiInterface ardData = RetrofitClient.getClient(getApplicationContext()).create(ApiInterface.class);
                Call<BookResponse> tampilData = ardData.getBookDetail(book_id);

                tampilData.enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        listBook = response.body().getResult();
                        if(response.code() == 200){
                            Intent intent = new Intent(getApplicationContext(), TransactionDetailBookActivity.class);
                            intent.putExtra("id", listDetail.get(position).getId());
                            intent.putExtra("qty", listDetail.get(position).getQty());
                            //intent.putExtra("status", listDetail.get(position).getStatus());

                            intent.putExtra("book_id", listBook.get(0).getId());
                            intent.putExtra("book_title", listBook.get(0).getTitle());
                            intent.putExtra("book_description", listBook.get(0).getDescription());
                            intent.putExtra("book_writer", listBook.get(0).getWriter());
                            intent.putExtra("book_cover", listBook.get(0).getCover());
                            intent.putExtra("book_language", listBook.get(0).getLanguage());
                            intent.putExtra("book_price", listBook.get(0).getPrice());
                            intent.putExtra("book_stock", listBook.get(0).getStock());
                            startActivity(intent);
                        }

                        pbData.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<BookResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                        pbData.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
    }
}