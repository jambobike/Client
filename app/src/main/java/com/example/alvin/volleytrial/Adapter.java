package com.example.alvin.volleytrial;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    List<Users> users;
    Context context;
    Dialog myDialog;

    public Adapter(Context context, List<Users> users){
        this.users = users;
        this.context = context;
    }

    public Adapter() {

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.item, null);
//        return new MyViewHolder(view);

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Test CLick"+String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Users user = users.get(position);

        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        Glide.with(context)
                .load(user.getPhoto())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name, email;
        private ImageView img;
        private LinearLayout item;

        public MyViewHolder(View itemView){
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.item);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            img = (ImageView) itemView.findViewById(R.id.user_image);
        }
    }
}
