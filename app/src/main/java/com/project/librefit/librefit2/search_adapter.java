package com.project.librefit.librefit2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class search_adapter extends RecyclerView.Adapter<search_adapter.search_viewHolder>  {
    Context context;
    List <account_info> mList;

    public search_adapter(Context context, List<account_info> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public search_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_item,parent,false);
        return new search_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull search_viewHolder holder, final int position) {
        if(mList.get(position).image_url.equals("empty"))
        {
            holder.profile_img.setImageResource(R.drawable.unknown);
        }
        else {
            Picasso.get().load(mList.get(position).image_url).into(holder.profile_img);
        }
        holder.username.setText(mList.get(position).name);
        holder.setonCLicklistner(new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent=new Intent(context.getApplicationContext(),profiledemo.class);
                intent.putExtra("search",mList.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class search_viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile_img;
        TextView username;
        CustomItemClickListener listener;
        public search_viewHolder(View itemView) {
            super(itemView);
            profile_img=(ImageView)itemView.findViewById(R.id.profile_image);
            username=(TextView)itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

public void setonCLicklistner(CustomItemClickListener clickListener){
            listener=clickListener;
}
        @Override
        public void onClick(View v) {
              listener.onItemClick(v,getAdapterPosition());
        }
    }
    public interface CustomItemClickListener {
         void onItemClick(View v, int position);
    }
}
