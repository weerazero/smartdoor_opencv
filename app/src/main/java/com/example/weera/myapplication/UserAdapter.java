package com.example.weera.myapplication;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<log_entrance> list;

    public UserAdapter(List<log_entrance> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

         log_entrance user = list.get(position);
         holder.t_name.setText(user.getName());
         holder.t_time.setText(user.getTimestamp());
        if(user.getFrom().equals("raspberry")) {
            holder.im_form.setImageResource(R.drawable.raspberry);
        }
        else if(user.getFrom().equals("Android")){

            holder.im_form.setImageResource(R.drawable.android);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder  extends RecyclerView.ViewHolder{

       TextView t_name,t_time;
       ImageView im_form;


       public UserViewHolder(View itemView) {
           super(itemView);

           im_form = itemView.findViewById(R.id.imageViewk);
           t_name = itemView.findViewById(R.id.textname);
           t_time = itemView.findViewById(R.id.texttime);
       }
   }

}
