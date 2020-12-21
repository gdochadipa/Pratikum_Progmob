package co.ocha.pratikum_progmob.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;

import co.ocha.pratikum_progmob.R;
import co.ocha.pratikum_progmob.model.BookModel;
import co.ocha.pratikum_progmob.model.CartModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.HolderData>{
    private Context ctx;
    private List<CartModel> listData;
    private RecyclerViewClickListener listener;

    public CartAdapter(Context ctx, List<CartModel> listData, RecyclerViewClickListener listener) {
        this.ctx = ctx;
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        CartModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvStatus.setText(dm.getStatus());
        holder.tvQty.setText(String.valueOf(dm.getQty()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvStatus, tvQty;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvQty = itemView.findViewById(R.id.tv_qty);
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
