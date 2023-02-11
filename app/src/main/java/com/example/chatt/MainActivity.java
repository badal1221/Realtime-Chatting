package com.example.chatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recview=findViewById(R.id.recview);
        img1=findViewById(R.id.img1);
        recview.setLayoutManager(new LinearLayoutManager(this));
        name=getIntent().getStringExtra("Name");
        email=getIntent().getStringExtra("Email");
        mobno=getIntent().getStringExtra("Mobno");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                  final String ppurl=snapshot.child("users").child(mobno).child("profile_pic").getValue(String.class);
                  if(!ppurl.isEmpty()) {
                      Picasso.get().load(ppurl).into(img1);
                  }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                 for(DataSnapshot dataSnapshot:snapshot.child("users").getChildren()){
                     final String getmobile=dataSnapshot.getKey();
                     if(!getmobile.equals(mobno)){
                         final String getname=dataSnapshot.child("name").getValue(String.class);
                         final String getprofile_pic=dataSnapshot.child("profile_pic").getValue(String.class);
                           MessageList mesglist=new MessageList(getname,getmobile,"",getprofile_pic,0);
                           arr.add(mesglist);
                     }
                 }
                recview.setAdapter(new Recyclercontactadapter(MainActivity.this,arr));
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });









    }
}