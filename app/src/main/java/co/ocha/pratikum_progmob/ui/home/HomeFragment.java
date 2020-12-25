package co.ocha.pratikum_progmob.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import co.ocha.pratikum_progmob.BookDetailActivity;
import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.SQLite.DBBooks;
import co.ocha.pratikum_progmob.SQLite.DBCarts;
import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.adapter.BookAdapter;
import co.ocha.pratikum_progmob.model.BookModel;
import co.ocha.pratikum_progmob.model.BookResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<BookModel> listData = new ArrayList<>();
    private SwipeRefreshLayout srData;
    private ProgressBar pbData;
    private BookAdapter.RecyclerViewClickListener listener;
    private DBBooks dbBooks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvData = root.findViewById(R.id.rv_data);
        srData = root.findViewById(R.id.srl_data);
        pbData = root.findViewById(R.id.pb_data);

        setOnClickListener();

        /*DBBooks dbBooks = new DBBooks(getContext());
        Toast.makeText(getActivity(), dbBooks.getDatabaseName(), Toast.LENGTH_SHORT).show();*/

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
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = RetrofitClient.getClient(getContext()).create(ApiInterface.class);
        Call<BookResponse> tampilData = ardData.apiGetBook();

        tampilData.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {

                listData = response.body().getResult();

                // SQLite Teritory
                dbBooks = new DBBooks(getContext());
                SQLiteDatabase create = dbBooks.getWritableDatabase();
                ContentValues values = new ContentValues();

                if (listData.size() == 0){
                    Toast.makeText(getActivity(), "Your cart is empty, Lets add some!", Toast.LENGTH_SHORT).show();

                    SQLiteDatabase deleteData = dbBooks.getWritableDatabase();
                    deleteData.delete(DBBooks.MyColumns.NamaTabel, null, null);
                }

                for(int i = 0; i < listData.size(); i++){
                    values.put(DBBooks.MyColumns.id, listData.get(i).getId());
                    values.put(DBBooks.MyColumns.title, listData.get(i).getTitle());
                    values.put(DBBooks.MyColumns.description, listData.get(i).getDescription());
                    values.put(DBBooks.MyColumns.writer, listData.get(i).getWriter());
                    values.put(DBBooks.MyColumns.cover, listData.get(i).getCover());
                    values.put(DBBooks.MyColumns.language, listData.get(i).getLanguage());
                    values.put(DBBooks.MyColumns.price, listData.get(i).getPrice());
                    values.put(DBBooks.MyColumns.stock, listData.get(i).getStock());
                    create.insert(DBBooks.MyColumns.NamaTabel, null, values);
                }

                // Adapter Teritory
                adData = new BookAdapter(getActivity(), listData, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                //Toast.makeText(getActivity(), "Gagal Menghubungi server", Toast.LENGTH_SHORT).show();
                getDataSQLite();

                adData = new BookAdapter(getActivity(), listData, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }

    protected void getDataSQLite(){
        //Mengambil Repository dengan Mode Membaca
        listData = new ArrayList<>();
        dbBooks = new DBBooks(getContext());
        SQLiteDatabase ReadData = dbBooks.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM "+ DBBooks.MyColumns.NamaTabel,null);

        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal

        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for(int count=0; count < cursor.getCount(); count++){
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir

            listData.add(new BookModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7)));
        }
    }

    private void setOnClickListener() {
        listener = new BookAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("title", listData.get(position).getTitle());
                intent.putExtra("cover", listData.get(position).getCover());
                intent.putExtra("writer", listData.get(position).getWriter());
                intent.putExtra("description", listData.get(position).getDescription());
                intent.putExtra("language", listData.get(position).getLanguage());
                intent.putExtra("stock", listData.get(position).getStock());
                intent.putExtra("price", listData.get(position).getPrice());
                startActivity(intent);
            }
        };
    }
}