package com.example.chatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapplication-73bf2-default-rtdb.firebaseio.com/");
        TextView nametxt,mobtxt,emailtxt;
        Button submit;
        nametxt=findViewById(R.id.nametxt);
        mobtxt=findViewById(R.id.mobtxt);
        emailtxt=findViewById(R.id.emailtxt);
        submit=findViewById(R.id.submit);
        if(!MemoryData.getData(Register.this).isEmpty()){
            Intent intent=new Intent(Register.this,MainActivity.class);
            intent.putExtra("Name",MemoryData.getName(this));
            intent.putExtra("Mobno",MemoryData.getData(this));
            intent.putExtra("Email","");
            startActivity(intent);
            finish();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nametxt.getText().toString();
                String mobno=mobtxt.getText().toString();
                String email=emailtxt.getText().toString();
                if(name.isEmpty()||mobno.isEmpty()||email.isEmpty()){
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.child("users").hasChild(mobno)){
                                Toast.makeText(Register.this, "Mobile number exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(mobno).child("name").setValue(name);
                                databaseReference.child("users").child(mobno).child("email").setValue(email);
                                databaseReference.child("users").child(mobno).child("profile_pic").setValue("");
                                MemoryData.saveData(mobno,Register.this);
                                MemoryData.saveName(name,Register.this);
                                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Register.this,MainActivity.class);
                                intent.putExtra("Name",name);
                                intent.putExtra("Mobno",mobno);
                                intent.putExtra("Email",email);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}