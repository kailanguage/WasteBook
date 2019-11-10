package com.kailang.wastebook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.detail.DetailViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WasteBookAdapter extends RecyclerView.Adapter<WasteBookAdapter.MyViewHolder> {
    private List<WasteBook> allWasteBook = new ArrayList<>();
    private DetailViewModel detailViewModel;
    private static WasteBookClickListener clickListener;
    private DecimalFormat amountFormat = new DecimalFormat("#.##");

    public WasteBookAdapter(DetailViewModel detailViewModel) {
        this.detailViewModel = detailViewModel;
    }

    public void setAllWasteBook(List<WasteBook> allWasteBook) {
        this.allWasteBook = allWasteBook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.wastebook_card,parent,false);

        final MyViewHolder holder = new MyViewHolder(itemView);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到编辑memo
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final WasteBook wasteBook = allWasteBook.get(position);
        holder.tv_type.setText(wasteBook.getCategories());

        holder.tv_amount.setText(amountFormat.format(wasteBook.getAmount()));
    }

    @Override
    public int getItemCount() {
        return allWasteBook.size();
    }

    //自定义ViewHolder:内部类，static 防止内存泄露
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_type,tv_desc,tv_amount;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=itemView.findViewById(R.id.wastebook_type);
            tv_amount=itemView.findViewById(R.id.wastebook_amount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

    public void setOnItemClickListener(WasteBookClickListener clickListener){
        this.clickListener=clickListener;
    }

    //点击事件接口
    public interface WasteBookClickListener {
        void onItemClick(int position, View v);
        //void onItemLongClick(int position, View v);
    }

}
