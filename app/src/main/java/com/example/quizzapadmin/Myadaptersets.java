package com.example.quizzapadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.os.Build.VERSION_CODES.M;

public class Myadaptersets extends BaseAdapter {
    private  int setnumber=0;
    private String Category_name;
    private ProgressDialog progressDialog;
    public Myadaptersets(int setnumber, String category_name) {
        this.setnumber = setnumber;
        Category_name = category_name;
    }

    @Override
    public int getCount() {
        return setnumber;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView textView;
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.setlayout,parent,false);
        textView=view.findViewById(R.id.number);
        textView.setText(String.valueOf(position+1));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(parent.getContext(),problems.class);
                intent.putExtra("category_name",Category_name);
                intent.putExtra("setNo",(position+1));
                parent.getContext().startActivity(intent);

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                progressDialog=new ProgressDialog(v.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Removing.....");

                showDailog(position,v);
                return false;
            }
        });
        return view;



    }

    private void showDailog(final int position, View v) {

        AlertDialog.Builder alBuilder= new AlertDialog.Builder(v.getContext());
        alBuilder.setTitle("Comfirm");
        alBuilder.setMessage("Delect the selected itmes");
        alBuilder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                FirebaseDatabase  firebaseDatabase=FirebaseDatabase.getInstance();
                final DatabaseReference myref=firebaseDatabase.getReference();
                myref.child("Sets").child(Category_name).child("questions").orderByChild("setNo").equalTo(position+1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            String id=dataSnapshot1.getKey();
                            myref.child("Sets").child(Category_name).child("questions").child(id).removeValue();

                        }
                        progressDialog.dismiss();
                        setnumber--;
sets.myadaptersets.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        alBuilder.setNeutralButton("Canceled", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog=alBuilder.create();
        dialog.show();
    }

}
