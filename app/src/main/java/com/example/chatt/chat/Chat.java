package com.example.chatt.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatt.MemoryData;
import com.example.chatt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");
    private String chatkey = "";
    private ArrayList<ChatList> arr=new ArrayList<>();
    private  ChatAdapter adapter;
    private  boolean firsttimeloading=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        CircleImageView pp;
        ImageView backbtn, send;
        TextView name, status;
        EditText txt;
        RecyclerView chatrecview;
        pp = findViewById(R.id.pp);
        backbtn = findViewById(R.id.backbtn);
        send = findViewById(R.id.send);
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        txt = findViewById(R.id.txt);
        chatrecview = findViewById(R.id.chatrecview);
        final String user1mob = MemoryData.getData(Chat.this);
        final String name1 = getIntent().getStringExtra("Name");
        final String prp = getIntent().getStringExtra("profile_pic");
       chatkey = getIntent().getStringExtra("chat_key");
        final String user2mob = getIntent().getStringExtra("Mobile");
        name.setText(name1);
        if (!prp.equals(" ")) { Picasso.get().load(prp).into(pp); }
        else { pp.setImageResource(R.drawable.tst); }
        chatrecview.setHasFixedSize(true);
        chatrecview.setLayoutManager(new LinearLayoutManager(Chat.this));
        adapter=new ChatAdapter(Chat.this,arr);
        chatrecview.setAdapter(adapter);
        //if no chatkey generated =>no chat between these 2 user=>we need to give them a chat key and define their user1 and 2
        //for user 1 and 2 we need their mobile numbers
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (chatkey.isEmpty()) {
                    chatkey="1";
                    if (snapshot.hasChild("chat")) {
                        chatkey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }
                if(snapshot.hasChild("chat")){
                    if(snapshot.child("chat").child(chatkey).hasChild("message")){
                        arr.clear();
                        for(DataSnapshot ds:snapshot.child("chat").child(chatkey).child("message").getChildren()){
                            if(ds.hasChild("msg") && ds.hasChild("mobile")){
                                String msgtimestamp=ds.getKey();
                                String msg=ds.child("msg").getValue(String.class);
                                String sender=ds.child("mobile").getValue(String.class);
                                Timestamp timestamp=new Timestamp(Long.parseLong(msgtimestamp));
                                Date date=new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpletimeFormat=new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                arr.add(new ChatList(sender,name1,msg,simpleDateFormat.format(date),simpletimeFormat.format(date)));
                                if(firsttimeloading || Long.parseLong(msgtimestamp) > Long.parseLong(MemoryData.getlastmsgTs(Chat.this,chatkey))){
                                    MemoryData.savelastmsgTs(msgtimestamp, chatkey, Chat.this);
                                    firsttimeloading=false;
                                    adapter.updateData(arr);
                                    chatrecview.scrollToPosition(arr.size()-1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textmsg = txt.getText().toString();
                final String currenttimestamp = String.valueOf(System.currentTimeMillis());
                //here the last seen message time is the time for last sent message
                //update soon
                databaseReference.child("chat").child(chatkey).child("user_1").setValue(user1mob);
                databaseReference.child("chat").child(chatkey).child("user_2").setValue(user2mob);
                //the sent message
                databaseReference.child("chat").child(chatkey).child("message").child(currenttimestamp).child("msg").setValue(textmsg);
                //message sender
                databaseReference.child("chat").child(chatkey).child("message").child(currenttimestamp).child("mobile").setValue(user1mob);
                txt.setText("");
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}