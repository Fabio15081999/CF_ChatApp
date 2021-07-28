package com.example.cf_chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cf_chatapp.MessageActivity;
import com.example.cf_chatapp.R;
import com.example.cf_chatapp.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserModel> mUsers;

    public UsersAdapter(Context mcontext, List<UserModel> mUsers) {
        super();
        this.mcontext = mcontext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_users, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel userModel = mUsers.get(position);
        holder.username.setText(userModel.getUsername());
        holder.email.setText(userModel.getEmail());
        if (userModel.getAvatar().equals("default")) {
            holder.imProfile.setImageResource(R.mipmap.ic_launcher);

        } else {
            Picasso.get().load(userModel.getAvatar()).into(holder.imProfile);


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userId", userModel.getId());
                mcontext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        if (mUsers != null){
            return mUsers.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, email;
        ImageView imProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsernameContact);
            email = itemView.findViewById(R.id.tvEmailContacts);
            imProfile = itemView.findViewById(R.id.profile_img);
        }
    }

}
