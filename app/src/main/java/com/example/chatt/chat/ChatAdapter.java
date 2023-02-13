package com.example.chatt.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatt.MemoryData;
import com.example.chatt.MessageList;
import com.example.chatt.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {
    Context context;
    ArrayList<ChatList> arr;

    public ChatAdapter(Context context, ArrayList<ChatList> arr) {
        this.context = context;
        this.arr = arr;
    }
    @NotNull
    @Override
    public ChatAdapter.viewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.mychat_recadapter,parent,false);
        viewholder holder=new viewholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        ChatList list=arr.get(position);
        if(list.getMobile().equals(MemoryData.getData(context))){
            holder.ll2.setVisibility(View.VISIBLE);
            holder.ll1.setVisibility(View.GONE);
            holder.mymsg.setText(list.getMessage());
            holder.mytime.setText(list.getDate()+" "+list.getTime());
        }
        else{
            holder.ll2.setVisibility(View.GONE);
            holder.ll1.setVisibility(View.VISIBLE);
            holder.opponentmsg.setText(list.getMessage());
            holder.opponenttime.setText(list.getDate()+" "+list.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    public void updateData(ArrayList<ChatList> arr){
        this.arr=arr;
        notifyDataSetChanged();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        LinearLayout ll1,ll2;
        TextView opponentmsg,opponenttime;
        TextView mymsg,mytime;
        public viewholder( View itemView) {
            super(itemView);
             ll1=itemView.findViewById(R.id.ll1);
             ll2=itemView.findViewById(R.id.ll2);
             opponentmsg=itemView.findViewById(R.id.opponentmsg);
             opponenttime=itemView.findViewById(R.id.opponenttime);
             mymsg=itemView.findViewById(R.id.mymsg);
             mytime=itemView.findViewById(R.id.mytime);
        }
    }
}
