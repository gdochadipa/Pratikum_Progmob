package co.ocha.pratikum_progmob.ui.home;

import android.content.Intent;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvData = root.findViewById(R.id.rv_data);
        srData = root.findViewById(R.id.srl_data);
        pbData = root.findViewById(R.id.pb_data);

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
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = RetrofitClient.getClient(getContext()).create(ApiInterface.class);
        Call<BookResponse> tampilData = ardData.apiGetBook();

        tampilData.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {

                listData = response.body().getResult();

                adData = new BookAdapter(getActivity(), listData, listener);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal Menghubungi server", Toast.LENGTH_SHORT).show();

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setOnClickListener() {
        listener = new BookAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("title", listData.get(position).getTitle());
                intent.putExtra("description", listData.get(position).getDescription());
                startActivity(intent);
            }
        };
    }
}