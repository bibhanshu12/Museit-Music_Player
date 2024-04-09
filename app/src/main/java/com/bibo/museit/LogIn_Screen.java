package com.bibo.museit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn_Screen extends AppCompatActivity {
EditText mail,nameit;
Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        mail=findViewById(R.id.mail);
        nameit=findViewById(R.id.nameid);
        next=findViewById(R.id.next);
        String Name="";
        String maill="";

        Intent intent=new Intent(LogIn_Screen.this,MainActivity.class);
        intent.putExtra("Name",Name);
        intent.putExtra("mail",maill);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String Name=nameit.getText().toString();
//                String maill=mail.getText().toString();

                startActivity(intent);

            }
        });


    }
}