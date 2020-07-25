package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class problems extends AppCompatActivity {
    private Toolbar toolbar;
private RecyclerView recyclerView;
private Button add_btn;
private Button excel_btn;
public static Myadapter_proble myadapter;
public  static List<problemmodelclass> list;
private String category_name;
private int setsNo;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

toolbar=findViewById(R.id.problemstoolbar);
setSupportActionBar(toolbar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
category_name=getIntent().getStringExtra("category_name");
setsNo=getIntent().getIntExtra("setNo",0);
Toast.makeText(this,category_name,Toast.LENGTH_LONG).show();
        Toast.makeText(this,String.valueOf(setsNo),Toast.LENGTH_LONG).show();


        getSupportActionBar().setTitle(category_name);

recyclerView=findViewById(R.id.recyclerview);
excel_btn=findViewById(R.id.excel_btn);
add_btn=findViewById(R.id.add_btn);
list=new ArrayList<problemmodelclass>();



myadapter=new Myadapter_proble((ArrayList<problemmodelclass>) list, category_name, setsNo);

        getData();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myadapter);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(problems.this,addquestion.class);
                intent.putExtra("category_name",category_name);
           intent.putExtra("setNo",setsNo);
                startActivity(intent);

            }
        });

        excel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(problems.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
selectFile();
                }
                else{
                    ActivityCompat.requestPermissions(problems.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }


            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectFile();
            }
        }
    }


    private void selectFile() {

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent,"select the file"),102);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102){
            if(resultCode==RESULT_OK){
                String path=data.getData().getPath();
                if(path.endsWith(".xmls")){
                    Toast.makeText(this,"file selected ",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this,"select the xmls file",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

private void getData(){
progressDialog.show();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference();

        myRef.child("Sets").child(category_name).child("questions").orderByChild("setNo").equalTo(setsNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String cor=dataSnapshot1.child("correctoption").getValue(String.class);
                    String a=dataSnapshot1.child("optionA").getValue(String.class);
                    String b=dataSnapshot1.child("optionB").getValue(String.class);
                    String c=dataSnapshot1.child("optionC").getValue(String.class);
                    String d=dataSnapshot1.child("optionD").getValue(String.class);
                    String que=dataSnapshot1.child("question").getValue(String.class);
                    int setn=dataSnapshot1.child("setNo").getValue(Integer.class);


list.add(new problemmodelclass(dataSnapshot1.getKey(),cor,a,b,c,d,que,setn));
                }
                progressDialog.dismiss();
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
