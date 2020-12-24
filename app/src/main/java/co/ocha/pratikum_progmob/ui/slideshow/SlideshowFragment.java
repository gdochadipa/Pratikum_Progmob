package co.ocha.pratikum_progmob.ui.slideshow;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import co.ocha.pratikum_progmob.BaseActivity;
import co.ocha.pratikum_progmob.BookDetailActivity;
import co.ocha.pratikum_progmob.CartDetailActivity;
import co.ocha.pratikum_progmob.LoginActivity;
import co.ocha.pratikum_progmob.MainActivity;
import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.SQLite.DBCarts;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.TransactionDetailActivity;
import co.ocha.pratikum_progmob.adapter.CartAdapter;
import co.ocha.pratikum_progmob.adapter.TransactionAdapter;
import co.ocha.pratikum_progmob.model.BookModel;
import co.ocha.pratikum_progmob.model.BookResponse;
import co.ocha.pratikum_progmob.model.CartModel;
import co.ocha.pratikum_progmob.model.CartResponse;
import co.ocha.pratikum_progmob.model.TransactionModel;
import co.ocha.pratikum_progmob.model.TransactionResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    //SharedPrefed sharedPrefManager;
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<TransactionModel> listData = new ArrayList<>();
    private List<BookModel> listBook = new ArrayList<>();
    private SwipeRefreshLayout srData;
    private ProgressBar pbData;
    private TransactionAdapter.RecyclerViewClickListener listener;
    SharedPrefed sharedPrefed;
    private DBCarts dbCarts;
    ApiInterface apiInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        rvData = root.findViewById(R.id.rv_data);
        srData = root.findViewById(R.id.srl_data);
        pbData = root.findViewById(R.id.pb_data);
        sharedPrefed = new SharedPrefed(getContext());

        setOnClickListener();

        lmData =  new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        srData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srData.setRefreshing(true);
                retrieveData();
                srData.setRefreshing(false);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveData();
    }

    public void retrieveData(){
        String user_token = sharedPrefed.getSPToken();
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = RetrofitClient.getClient(getContext()).create(ApiInterface.class);
        Call<TransactionResponse> tampilData = ardData.getTransaction(user_token);

        tampilData.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                listData = response.body().getResult();
                Log.d("myTag", listData.toString());

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
                adData = new TransactionAdapter(getActivity(), listData, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                //getDataSQLite();

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
        listener = new TransactionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TransactionDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("address", listData.get(position).getAddress_id());
                startActivity(intent);
            }
        };
    }

    /*@OnClick(R.id.btnLogout)void onLogout(){
        sharedPrefManager.saveSPBoolean(SharedPrefed.SP_SUDAH_LOGIN,false);
        startActivity(new Intent(getActivity(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
    }*/
}