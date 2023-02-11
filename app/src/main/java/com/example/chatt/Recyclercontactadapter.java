package com.example.chatt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recyclercontactadapter extends RecyclerView.Adapter<Recyclercontactadapter.viewholder> {
    Context context;
    ArrayList<MessageList> arr;
    public Recyclercontactadapter(Context context, ArrayList<MessageList> arr) {
        this.context = context;
        this.arr = arr;
    }
    @NotNull
    @Override
    public viewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.mycontacts_row,parent,false);
        viewholder holder=new viewholder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        //holder.imageView.setImageResource(arr.get(position).);
        if(!arr.get(position).profilepic.isEmpty()) {
            Picasso.get().load(arr.get(position).profilepic).into(holder.imageview);
        }
        holder.textname.setText(arr.get(position).getName());
        holder.textmsg.setText(arr.get(position).getLastmessage());
        holder.unmsgno.setText(arr.get(position).getUnseenmsg());
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        CircleImageView imageview;
        TextView textname,textmsg,unmsgno;
        public viewholder(@NotNull View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.img1);
            textname=itemView.findViewById(R.id.name);
            textmsg=itemView.findViewById(R.id.unmsg);
            unmsgno=itemView.findViewById(R.id.unmsgno);
        }
    }
}
