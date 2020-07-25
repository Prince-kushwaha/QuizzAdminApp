package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class addquestion extends AppCompatActivity {
private Toolbar toolbar;
private EditText question;
private EditText optionA;
private EditText optionB;
private EditText optionC;
private EditText optionD;
private EditText answer;
private Button upload;
private String category_name;
private int setNo;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);
toolbar=findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);

progressDialog=new ProgressDialog(this);
progressDialog.setMessage("Uplaoding..");

setNo=getIntent().getIntExtra("setNo",0);
category_name=getIntent().getStringExtra("category_name");
getSupportActionBar().setTitle(category_name+"-"+String.valueOf(setNo));

question=findViewById(R.id.question_name);
optionA=findViewById(R.id.optionA);
optionB=findViewById(R.id.optionB);
optionC=findViewById(R.id.optionC);
optionD=findViewById(R.id.optionD);
answer=findViewById(R.id.answer);
upload=findViewById(R.id.Uplaod_btn);

upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        uploaddata();
    }
});

    }

    private void uploaddata() {

        final String ques=question.getText().toString();
        final String a=optionA.getText().toString();
        final String b=optionB.getText().toString();
        final String c=optionC.getText().toString();
        final String d=optionD.getText().toString();
        final String ans=answer.getText().toString();
        final int set_No=setNo;

        if(ques.isEmpty()||a.isEmpty()||b.isEmpty()||c.isEmpty()||d.isEmpty()||ans.isEmpty()){
         Toast.makeText(this,"Data not filled",Toast.LENGTH_LONG).show();
            return;
        }


progressDialog.show();





        HashMap<String,Object>map=new HashMap<String,Object>();


        map.put("question",ques);
        map.put("optionA",a);
        map.put("optionB",b);
        map.put("optionC",c);
        map.put("optionD",d);
        map.put("correctoption",ans);
        map.put("setNo",setNo);

final String id=UUID.randomUUID().toString();
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();
        databaseReference.child("Sets").child(category_name).child("questions").child(id).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                problems.list.add(new problemmodelclass(id,ans,a,b,c,d,ques,setNo));
problems.myadapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();;
        }
        return super.onOptionsItemSelected(item);

    }


}
