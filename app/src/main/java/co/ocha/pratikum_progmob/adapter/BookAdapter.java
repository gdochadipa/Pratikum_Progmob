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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.HolderData>{
    private Context ctx;
    private List<BookModel> listData;
    private RecyclerViewClickListener listener;

    public BookAdapter(Context ctx, List<BookModel> listData, RecyclerViewClickListener listener) {
        this.ctx = ctx;
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        BookModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvTitle.setText(dm.getTitle());
        holder.tvDescription.setText(dm.getDescription());
        holder.tvWriter.setText(dm.getWriter());

        Glide.with(ctx).load(dm.getCover()).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvId, tvTitle, tvDescription, tvWriter;
        ImageView ivCover;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_desc);
            tvWriter = itemView.findViewById(R.id.tv_writer);
            ivCover = itemView.findViewById(R.id.iv_cover);
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
