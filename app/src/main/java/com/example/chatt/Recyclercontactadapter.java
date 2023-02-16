package com.example.chatt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatt.chat.Chat;
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
    public void onBindViewHolder(viewholder holder,int position) {
        MessageList list=arr.get(position);

        //update image settings
//        if(!arr.get(position).profilepic.equals("")) {
//            Picasso.get().load(list.profilepic).into(holder.imageview);
//        }
//        else{ holder.imageview.setImageResource(R.drawable.tst); }
        holder.imageview.setImageResource(R.drawable.tst);
        holder.textname.setText(list.getName());
        holder.textmsg.setText(list.getLastmessage());
        holder.unmsgno.setText(String.valueOf(list.getUnseenmsg()));
        if(list.getLastmessage().equals("")){
            holder.textmsg.setVisibility(View.GONE);
        }
        else{
            holder.textmsg.setVisibility(View.VISIBLE);
        }
        holder.unmsgno.setText(String.valueOf(list.getUnseenmsg()));
        if(list.getUnseenmsg()==0){
            holder.unmsgno.setVisibility(View.GONE); }
        else{
            holder.unmsgno.setVisibility(View.VISIBLE);
        }
        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Chat.class);
                intent.putExtra("Name",list.getName());
                intent.putExtra("profile_pic",list.getProfilepic());
                intent.putExtra("chat_key",list.getChatkey());
                intent.putExtra("Mobile",list.getMobile());
  //              Log.d("arr",String.valueOf(list.getChatkey().isEmpty()));
                context.startActivity(intent);
            }
        });
        Dialog dialog = new Dialog(context);
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.custom_dp);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageView img= dialog.findViewById(R.id.img);
                TextView name=dialog.findViewById(R.id.name);
//                if(!arr.get(position).profilepic.equals("")) {
//                    Picasso.get().load(list.profilepic).into(img);
//                }
//                else{img.setImageResource(R.drawable.tst); }
                name.setText(list.getName());
                img.setImageResource(R.drawable.tst);
                dialog.show();
            }
        });
    }
    public void updateData(ArrayList<MessageList> arr){
            this.arr=arr;
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        CircleImageView imageview;
        TextView textname,textmsg,unmsgno;
        LinearLayout ll1;
        public viewholder(@NotNull View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.img1);
            textname=itemView.findViewById(R.id.name);
            textmsg=itemView.findViewById(R.id.unmsg);
            unmsgno=itemView.findViewById(R.id.unmsgno);
            ll1=itemView.findViewById(R.id.ll1);
        }
    }
}
