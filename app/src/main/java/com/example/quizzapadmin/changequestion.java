package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class changequestion extends AppCompatActivity {

private EditText question;
private EditText optionA;
private EditText optionB;
private EditText optionC;
private EditText optionD;
private EditText answer;
private Toolbar toolbar;
private Button upload;
private String category_name;
private int set_No;
private int position;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changequestion);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        toolbar=findViewById(R.id.toolbar);
 setSupportActionBar(toolbar);
 getSupportActionBar().setTitle("update the question");
 category_name=getIntent().getStringExtra("category_name");
 set_No=getIntent().getIntExtra("setNo",-1);
 position=getIntent().getIntExtra("position",-1);
  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        question=findViewById(R.id.question_name);
        optionA=findViewById(R.id.optionA);
        optionB=findViewById(R.id.optionB);
        optionC=findViewById(R.id.optionC);
        optionD=findViewById(R.id.optionD);
        answer=findViewById(R.id.answer);
        upload=findViewById(R.id.Uplaod_btn);
        setData();
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(position!=-1) {
Uploaddata();
        }


    }
});



    }

    private void setData() {

problemmodelclass  questionmodel=problems.list.get(position);
question.setText(questionmodel.getQuestion());
optionA.setText(questionmodel.getOptionA());
optionB.setText(questionmodel.getOptionB());
optionC.setText(questionmodel.getOptionC());
optionD.setText(questionmodel.getOptionD());
answer.setText(questionmodel.getCorrectoption());

    }


    private void Uploaddata(){
progressDialog.show();
        final String ques=question.getText().toString();
        final String a=optionA.getText().toString();
        final String b=optionB.getText().toString();
        final String c=optionC.getText().toString();
        final String d=optionD.getText().toString();
        final String ans=answer.getText().toString();
        final int setNo=set_No;
        HashMap<String ,Object>map=new HashMap<>();

        map.put("question",ques);
        map.put("optionA",a);
        map.put("optionB",b);
        map.put("optionC",c);
        map.put("optionD",d);
        map.put("correctoption",ans);
        map.put("setNo",setNo);
        final String id=problems.list.get(position).getId();



        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference();
myref.child("Sets").child(category_name).child("questions").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
problems.list.set(position,new problemmodelclass(id,ans,a,b,c,d,ques,setNo));
problems.myadapter.notifyDataSetChanged();
progressDialog.dismiss();
finish();
        }
        else {
            Toast.makeText(changequestion.this,"failed",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
});



    }

}
