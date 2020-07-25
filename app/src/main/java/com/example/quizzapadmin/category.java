package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;

public class category extends AppCompatActivity {
private RecyclerView recyclerView;
private Toolbar categorytb;
private Dialog catatgorydailog;
   private FirebaseDatabase database = FirebaseDatabase.getInstance();
     private  DatabaseReference myRef = database.getReference();
public static List<modelclass>list=new ArrayList<modelclass>();
private CircleImageView imageView;
private Button addbtn;
private  EditText categot_name;
  private   ProgressDialog progressDialog;
  private String downloadimageurl;
  private Uri Imageuri;
public static  Myadapter myadapter;

   // ***********************************************************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

progressDialog=new ProgressDialog(this);
progressDialog.setMessage("Loading..");
progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
progressDialog.setCancelable(false);

catatgorydailog=new Dialog(this);


        categorytb=findViewById(R.id.categorytoolbar);
setSupportActionBar(categorytb);

getSupportActionBar().setDisplayHomeAsUpEnabled(false);

setcatergorydialog();

        recyclerView=findViewById(R.id.categorylist);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
recyclerView.setLayoutManager(linearLayoutManager);


         myadapter=new Myadapter(list);
recyclerView.setAdapter(myadapter);
progressDialog.show();
        myRef.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String name = dataSnapshot1.child("name").getValue(String.class);
                    String url = dataSnapshot1.child("url").getValue(String.class);
                    int sets = dataSnapshot1.child("sets").getValue(Integer.class);
                    list.add(new modelclass(url, name, sets));


                }

                myadapter.notifyDataSetChanged();
progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(category.this, databaseError.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void setcatergorydialog() {

        catatgorydailog.setContentView(R.layout.addbtnlayout);
        catatgorydailog.setCancelable(true);
        catatgorydailog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

          addbtn =catatgorydailog.findViewById(R.id.addbtn);
          imageView=catatgorydailog.findViewById(R.id.imageview);
         categot_name=catatgorydailog.findViewById(R.id.categoryname);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
startActivityForResult(intent,101);

            }
        });

addbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(categot_name.getText().toString().isEmpty()){
            categot_name.setError("Required");
            return;
        }
        else if(imageView==null){

            Toast.makeText(category.this,"Image required",Toast.LENGTH_LONG).show();
            return;
        }
uplaodImage();

        catatgorydailog.dismiss();

    }
});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==101){
            if(resultCode== RESULT_OK){
                Imageuri=data.getData();
                imageView.setImageURI(Imageuri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.categorymenu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.addbtn==item.getItemId()){
            catatgorydailog.show();
        }
        if(R.id.Logout==item.getItemId()){
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private void uplaodImage(){

        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        final StorageReference storageReference=firebaseStorage.getReference();
        final StorageReference imageReference = storageReference.child("category").child(Imageuri.getLastPathSegment());
        UploadTask uploadTask = imageReference.putFile(Imageuri);


        progressDialog.show();



    }
    private  void uploadCategory(){

   final String name=categot_name.getText().toString();

        HashMap<String,Object>map=new HashMap<String,Object>();
        map.put("name",name);
        map.put("sets",0);
String id= UUID.randomUUID().toString();
        map.put("url",downloadimageurl);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();
        databaseReference.child("Category").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(category.this, "Suceessfull", Toast.LENGTH_LONG).show();

                }
                else
                    Toast.makeText(category.this,"unSuccessful",Toast.LENGTH_LONG).show();

            }
        });

        list.add(new modelclass(downloadimageurl,name,0));

        recyclerView.setAdapter(myadapter);


    }
}
