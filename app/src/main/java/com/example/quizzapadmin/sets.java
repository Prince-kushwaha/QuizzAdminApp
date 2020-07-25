package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sets extends AppCompatActivity {
private GridView gridView;
private Toolbar toolbar;
private String category_name;
private int setsnumber ;
public static Myadaptersets myadaptersets;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
toolbar=findViewById(R.id.settoolbar);
gridView=findViewById(R.id.setsview);
setSupportActionBar(toolbar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);



 category_name=getIntent().getStringExtra("categoryname");
 setsnumber=getIntent().getIntExtra("setnumber",0);
getSupportActionBar().setTitle(category_name);

myadaptersets=new Myadaptersets(setsnumber, category_name);
        gridView.setAdapter(myadaptersets);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.categorymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==(android.R.id.home)){
            finish();;
        }


if(item.getItemId()==R.id.addbtn){

setsnumber++;
   FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
   DatabaseReference myRef=firebaseDatabase.getReference();

myRef.child("Category").child(category_name).child("sets").setValue(setsnumber).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        Toast.makeText(sets.this,"successful",Toast.LENGTH_LONG);
    }
});

    myadaptersets=new Myadaptersets(setsnumber, category_name);
    gridView.setAdapter(myadaptersets);
    myadaptersets.notifyDataSetChanged();



}



        return super.onOptionsItemSelected(item);
    }






}
