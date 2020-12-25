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
import co.ocha.pratikum_progmob.model.TransactionDetailModel;

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.HolderData>{
    private Context ctx;
    private List<TransactionDetailModel> listData;
    private TransactionDetailAdapter.RecyclerViewClickListener listener;

    public TransactionDetailAdapter(Context ctx, List<TransactionDetailModel> listData, RecyclerViewClickListener listener) {
        this.ctx = ctx;
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionDetailAdapter.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_detail_item, parent, false);
        TransactionDetailAdapter.HolderData holder = new TransactionDetailAdapter.HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailAdapter.HolderData holder, int position) {
        TransactionDetailModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvQty.setText(String.valueOf(dm.getQty()));
        holder.tvPrice.setText(String.valueOf(dm.getPrice()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvQty, tvPrice;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvPrice = itemView.findViewById(R.id.tv_price);
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
