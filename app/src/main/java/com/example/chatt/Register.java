package com.example.chatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            intent.putExtra("Email"," ");
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
                else if(!isNumeric(mobno) || mobno.length()!=10){
                    Toast.makeText(Register.this,"Enter a valid mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(!isValid(email)){
                    Toast.makeText(Register.this,"Enter a valid email id",Toast.LENGTH_SHORT).show();
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
                                databaseReference.child("users").child(mobno).child("profile_pic").setValue(" ");
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
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static boolean isValid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}