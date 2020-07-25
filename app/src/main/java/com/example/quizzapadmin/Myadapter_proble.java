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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class Myadapter_proble extends  RecyclerView.Adapter<Myadapter_proble.viewholder> {

private ArrayList<problemmodelclass>list;
private String categoryname;
private  int setNo;
private AlertDialog.Builder alertDailog;
    public Myadapter_proble(ArrayList<problemmodelclass> list, String categoryname, int setNo)
    {
        this.list = list;
        this.categoryname = categoryname;
        this.setNo = setNo;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problemlayout,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.SetData(list.get(position).getQuestion(),list.get(position).getCorrectoption(),position,list.get(position).getId());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView questiontext;
        TextView answertext;
        ImageButton back_btn;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            questiontext=itemView.findViewById(R.id.question);
            answertext=itemView.findViewById(R.id.answer);
        }
        private void SetData(String question, String answer, final int position, final String id){
            questiontext.setText(question);
            answertext.setText(answer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),changequestion.class);
                    intent.putExtra("category_name",categoryname);
                    intent.putExtra("setNo",setNo);
                    intent.putExtra("position",position);

                    itemView.getContext().startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    alertDailog=new AlertDialog.Builder(v.getContext());

                    showDailog(position, v);

                    return false;
                }
            });

        }
    }


    private void  showDailog(final int  position, final View v){
    alertDailog.setTitle("Confirm");
    alertDailog.setMessage("You want to Delect this item");

    alertDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myref=firebaseDatabase.getReference();
            myref.child("Sets").child(categoryname).child("questions").child(list.get(position).getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(v.getContext(),"Delected",Toast.LENGTH_LONG).show();
problems.list.remove(position);
problems.myadapter.notifyDataSetChanged();
               }
               else
                   Toast.makeText(v.getContext(),"something went wrong",Toast.LENGTH_LONG).show();


                }
            });

        }
    });

alertDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(v.getContext(),"Ok",Toast.LENGTH_LONG).show();

    }
});

alertDailog.setNeutralButton("cancaled", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(v.getContext(),"Cancaled",Toast.LENGTH_LONG).show();

    }
});

AlertDialog alert=alertDailog.create();
alert.show();




    }

}
