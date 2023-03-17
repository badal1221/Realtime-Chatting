package com.example.chatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private static String chatkey = "";
    ArrayList<MessageList> arr = new ArrayList<>();
    String name, email, mobno;
    RecyclerView recview;
    CircleImageView img1;
    static int unseen = 0;
    private String last_msg = "";
    private boolean dataset = false;
    private Recyclercontactadapter adapter;
    ProgressBar prgbr;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        prgbr = findViewById(R.id.prgbr);
        recview = findViewById(R.id.recview);
        img1 = findViewById(R.id.img1);
        recview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Recyclercontactadapter(MainActivity.this, arr);
        recview.setAdapter(adapter);
        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        mobno = getIntent().getStringExtra("Mobno");
        //user's profile_pic set
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final String ppurl = snapshot.child("users").child(mobno).child("profile_pic").getValue(String.class);
                if (!ppurl.equals(" ")) {
                    Picasso.get().load(ppurl).into(img1);
                } else {
                    img1.setImageResource(R.drawable.tst);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        //recview set
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                prgbr.setVisibility(View.VISIBLE);
                arr.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    final String getmobile = dataSnapshot.getKey();
                    dataset = false;
                    if (!getmobile.equals(mobno)) {
                        final String getname = dataSnapshot.child("name").getValue(String.class);
                        final String getprofile_pic = dataSnapshot.child("profile_pic").getValue(String.class);
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot1) {
                                for (DataSnapshot ds : snapshot1.getChildren()) {
                                    if (ds.hasChild("user_1") && ds.hasChild("user_2")) {
                                        final String getuserone = ds.child("user_1").getValue(String.class);
                                        final String getusertwo = ds.child("user_2").getValue(String.class);
                                        if ((getuserone.equals(getmobile) && getusertwo.equals(mobno)) || (getusertwo.equals(getmobile) && getuserone.equals(mobno))) {
                                            chatkey = ds.getKey();

                                            MemoryData.savechatkey(chatkey,MainActivity.this,getname);

                                            for (DataSnapshot dataSnapshot1 : ds.child("messages").getChildren()) {
                                                //msg key of msg we are now iterating
                                                final long msgkey = Long.parseLong(dataSnapshot1.getKey());
                                                //getting the key of last seen message from memory
                                                final long lastseenmsg = Long.parseLong(MemoryData.getlastmsgTs(MainActivity.this, chatkey));
                                                last_msg = dataSnapshot1.child("msg").getValue(String.class);
                                                if (msgkey > lastseenmsg) {
                                                    unseen++;
                                                }
                                            }
                                            MemoryData.saveunseenno(String.valueOf(unseen),MainActivity.this,getname+"a");
                                            MemoryData.savelastmsg(last_msg,MainActivity.this,getname+"b");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                        if (!dataset) {
                            dataset = true;
                            String ck=MemoryData.getchatkey(MainActivity.this,getname);
                            Log.d("arr",ck);
//                            int us=Integer.parseInt(MemoryData.getunseenno(MainActivity.this,getname+"a"));
//                            String last=MemoryData.getlastmsg(MainActivity.this,getname+"b");
                            MessageList mesglist = new MessageList(getname, getmobile, last_msg, getprofile_pic, unseen,ck);
                            arr.add(mesglist);
                            adapter.updateData(arr);
                        }
                    }
                }
                prgbr.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

}