package com.example.quizzapadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Myadapter  extends RecyclerView.Adapter<Myadapter.viewholder> {
private List<modelclass>list;
    AlertDialog.Builder alertDailog;
    public Myadapter(List<modelclass> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylistlayout,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
holder.setdata(list.get(position).getUrl(),list.get(position).getName(),list.get(position).getSets());

        holder.delect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alertDailog=new AlertDialog.Builder(v.getContext());
                showDailog(position,v);


            }

        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
CircleImageView imageView;
TextView textView;
ImageButton delect_btn;

        public viewholder(@NonNull final View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.categoryname);
     delect_btn=itemView.findViewById(R.id.delect_btn);


        }



         private  void setdata(String imageurl, final String name, final int setsnumber){
            textView.setText(name);
             Glide.with(itemView.getContext()).load(imageurl).into(imageView);
             itemView.setOnClickListener(
                     new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Intent intent=new Intent(itemView.getContext(),sets.class);
                             intent.putExtra("setnumber",setsnumber);
                             intent.putExtra("categoryname",name);
                             itemView.getContext().startActivity(intent);
                         }
                     });
        }


    }

private    void showDailog(final int position, final View v){

        alertDailog.setTitle("Confirm");
        alertDailog.setMessage("You want to Delect the this Category");
        alertDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference myref=firebaseDatabase.getReference();
                myref.child("Category").child(list.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()==true) {
                            Toast.makeText(v.getContext(), "successfull", Toast.LENGTH_LONG).show();
                            category.list.remove(position);
                            category.myadapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(v.getContext(),"unsuccessfull",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "failed", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });
        alertDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(v.getContext(), "Ok", Toast.LENGTH_LONG).show();

                return;
            }
        });
        alertDailog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(v.getContext(), "Cancel", Toast.LENGTH_LONG).show();

                return;

            }
        });

        AlertDialog dialog=alertDailog.create();
        dialog.show();

    }

}
