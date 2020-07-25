package com.example.quizzapadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button button;

    private List<modelclass> modelclassList;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        modelclassList = new ArrayList<modelclass>();

        firebaseAuth = FirebaseAuth.getInstance();

if(firebaseAuth.getCurrentUser()==null){
  Intent intent=new Intent(MainActivity.this,category.class);
  startActivity(intent);

}

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,category.class);

                if(email.getText().toString().isEmpty()&&password.getText().toString().isEmpty()){
    email.setError("Required");
    password.setError("Required");
    return;
}
else  if(email.getText().toString().isEmpty()){
    email.setError("Required");
    return;
}
else if(password.getText().toString().isEmpty()){
    password.setError("Required");
    return;
}
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,category.class);
startActivity(intent);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                        }

                        progressBar.setVisibility(View.INVISIBLE);

                    }

                });
            }


        });


    }

}
