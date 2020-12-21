package co.ocha.pratikum_progmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.model.TransactionModel;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.HolderData>{
    private Context ctx;
    private List<TransactionModel> listData;
    private TransactionAdapter.RecyclerViewClickListener listener;

    public TransactionAdapter(Context ctx, List<TransactionModel> listData, RecyclerViewClickListener listener) {
        this.ctx = ctx;
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        TransactionModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvStatus.setText(dm.getStatus());
        holder.tvTimeout.setText(dm.getTimeout());
        holder.tvTotal.setText(String.valueOf(dm.getTotal()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvStatus, tvTimeout, tvTotal;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTimeout = itemView.findViewById(R.id.tv_timeout);
            tvTotal = itemView.findViewById(R.id.tv_total);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
