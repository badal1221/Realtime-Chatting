package com.example.chatt.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");
    private String chatkey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        CircleImageView pp;
        ImageView backbtn,send;
        TextView name,status;
        EditText txt;
        pp=findViewById(R.id.pp);
        backbtn=findViewById(R.id.backbtn);
        send=findViewById(R.id.send);
        name=findViewById(R.id.name);
        status=findViewById(R.id.status);
        txt=findViewById(R.id.txt);
        final String user1mob= MemoryData.getData(Chat.this);
        final String name1=getIntent().getStringExtra("Name");
        final String prp=getIntent().getStringExtra("profile_pic");
        chatkey=getIntent().getStringExtra("chat_key");
        final String user2mob=getIntent().getStringExtra("Mobile");
        name.setText(name1);
        if(!prp.equals(" ")) {
            Picasso.get().load(prp).into(pp);
        }
        else{
            pp.setImageResource(R.drawable.tst);
        }
        //if no chatkey generated =>no chat between these 2 user=>we need to give them a chat key and define their user1 and 2
        //for user 1 and 2 we need their mobile numbers
        if(chatkey.isEmpty()){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                     chatkey="1";
                    if(snapshot.hasChild("chat")){
                        chatkey=String.valueOf(snapshot.child("chat").getChildrenCount()+1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textmsg=txt.getText().toString();
                final String currenttimestamp=String.valueOf(System.currentTimeMillis()).substring(0,10);
                MemoryData.savelastmsgTs(currenttimestamp,chatkey,Chat.this);
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