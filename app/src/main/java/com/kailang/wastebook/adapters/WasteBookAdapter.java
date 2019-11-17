package com.kailang.wastebook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.add.AddFragment;
import com.kailang.wastebook.ui.detail.DetailViewModel;
import com.kailang.wastebook.utils.DateToLongUtils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WasteBookAdapter extends RecyclerView.Adapter<WasteBookAdapter.MyViewHolder> {
    private List<WasteBook> allWasteBook = new ArrayList<>();
    private static WasteBookClickListener clickListener;
    private DecimalFormat amountFormat = new DecimalFormat("#.##");
    private Context context;

    public WasteBookAdapter(Context context) {
        this.context=context;

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

        holder.tv_type.setText(wasteBook.getCategory());
        holder.imageView.setImageDrawable(context.getDrawable(getDrawableId(wasteBook.getIcon())));
        holder.tv_date.setText(DateToLongUtils.longToDate(wasteBook.getTime()));
        if(wasteBook.isType())
            holder.tv_amount.setText("- "+amountFormat.format(wasteBook.getAmount()));
        else holder.tv_amount.setText(amountFormat.format(wasteBook.getAmount()));
    }

    @Override
    public int getItemCount() {
        return allWasteBook.size();
    }

    //自定义ViewHolder:内部类，static 防止内存泄露
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_type,tv_date,tv_amount;
        ImageView imageView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=itemView.findViewById(R.id.wastebook_type);
            tv_date=itemView.findViewById(R.id.wastebook_date);
            tv_amount=itemView.findViewById(R.id.wastebook_amount);
            imageView=itemView.findViewById(R.id.imageView_wasteBook_category);
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

    private int getDrawableId(String iconName){
        Field field = null;
        int res_ID;
        try {
            field = R.drawable.class.getField(iconName);
            res_ID = field.getInt(field.getName());
        } catch (NoSuchFieldException e) {
            res_ID = R.drawable.ic_category_out_1;
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            res_ID = R.drawable.ic_category_out_1;
            e.printStackTrace();
        }
        return res_ID;
    }

}
