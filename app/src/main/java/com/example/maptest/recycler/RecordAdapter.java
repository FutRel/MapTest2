package com.example.maptest.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.maptest.R;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

     public interface OnRecordClickListener{
        void onRecordClick(RecordForRecycler record, int position);
    }

    private final OnRecordClickListener onRecordClickListener;
    private final LayoutInflater inflater;
    private final List<RecordForRecycler> records;

    public RecordAdapter(Context context, List<RecordForRecycler> records, OnRecordClickListener onRecordClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.records = records;
        this.onRecordClickListener = onRecordClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_list_item_1, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordForRecycler record = records.get(position);
        holder.tvDistance.setText(record.getDistance());
        holder.tvTime.setText(record.getTime());
        holder.tvDate.setText(record.getDate());

        holder.itemView.setOnClickListener(view -> onRecordClickListener.onRecordClick(record, position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDistance;
        final TextView tvTime;
        final TextView tvDate;
        ViewHolder(View view){
            super(view);
            tvDistance = view.findViewById(R.id.tv_dist);
            tvTime = view.findViewById(R.id.tv_time);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }
}
