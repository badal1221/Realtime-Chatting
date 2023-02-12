package com.example.chatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ArrayList<MessageList> arr=new ArrayList<>();
    String name,email,mobno;
    RecyclerView recview;
    CircleImageView img1;
    int unseen=0;
    String last_msg="";
    private String chatkey="";
    private boolean dataset=false;
    private  Recyclercontactadapter adapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recview=findViewById(R.id.recview);
        img1=findViewById(R.id.img1);

        recview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Recyclercontactadapter(MainActivity.this,arr);
        recview.setAdapter(adapter);

        name=getIntent().getStringExtra("Name");
        email=getIntent().getStringExtra("Email");
        mobno=getIntent().getStringExtra("Mobno");
        //user's profile_pic set
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                  final String ppurl=snapshot.child("users").child(mobno).child("profile_pic").getValue(String.class);
                  if(!ppurl.equals(" ")) { Picasso.get().load(ppurl).into(img1); }
                  else{ img1.setImageResource(R.drawable.tst); }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        //recview set
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arr.clear();
                 for(DataSnapshot dataSnapshot:snapshot.child("users").getChildren()){
                     final String getmobile=dataSnapshot.getKey();
                     dataset=false;
                     if(!getmobile.equals(mobno)){
                         final String getname=dataSnapshot.child("name").getValue(String.class);
                         final String getprofile_pic=dataSnapshot.child("profile_pic").getValue(String.class);
                         databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot snapshot) {
                                 int getchatcount=(int)snapshot.getChildrenCount();
                                 for(DataSnapshot ds:snapshot.getChildren()){
                                     final String getkey=ds.getKey();
                                     chatkey=getkey;
                                     if(ds.hasChild("user_1") && ds.hasChild("user_2")){
                                         final String getuserone=ds.child("user_1").getValue(String.class);
                                         final String getusertwo=ds.child("user_2").getValue(String.class);
                                         if(getuserone.equals(getmobile) && getusertwo.equals(mobno) ||getusertwo.equals(getmobile) && getuserone.equals(mobno)){
                                             for(DataSnapshot dataSnapshot:ds.child("messages").getChildren()){
                                                 //msg key of msg we are now iterating
                                                 final long msgkey=Long.parseLong(dataSnapshot.getKey());
                                                 //getting the key of last seen message from memory
                                                 final long lastseenmsg=Long.parseLong(MemoryData.getlastmsgTs(MainActivity.this,getkey));
                                                 last_msg=dataSnapshot.child("msg").getValue(String.class);
                                                 if(msgkey > lastseenmsg){
                                                     unseen++;
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
                         if(!dataset){
                             dataset=true;
                             MessageList mesglist=new MessageList(getname,getmobile,last_msg,getprofile_pic,unseen,chatkey);
                             arr.add(mesglist);
                             adapter.updateData(arr);
                         }
                     }
                 }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}