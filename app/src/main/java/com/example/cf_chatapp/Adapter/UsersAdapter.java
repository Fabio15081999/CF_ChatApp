package com.example.cf_chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cf_chatapp.ChatActivity;
import com.example.cf_chatapp.R;
import com.example.cf_chatapp.Model.Chat;
import com.example.cf_chatapp.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserModel> mUsers;
    private boolean isonline;
    private String theLastMessage;

    public UsersAdapter(Context mcontext, List<UserModel> mUsers, boolean isonline) {
        super();
        this.mcontext = mcontext;
        this.mUsers = mUsers;
        this.isonline = isonline;
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
        if (isonline){
            lastMessage(userModel.getId(), holder.tvLastMessage);
        }else {
            holder.tvLastMessage.setVisibility(View.GONE);
        }
        if (isonline) {
            if (userModel.getStatus().equals("online")) {
                holder.im_status_on.setVisibility(View.VISIBLE);
                holder.im_status_off.setVisibility(View.GONE);
            } else {
                holder.im_status_on.setVisibility(View.GONE);
                holder.im_status_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.im_status_on.setVisibility(View.GONE);
            holder.im_status_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, ChatActivity.class);
                intent.putExtra("userId", userModel.getId());
                mcontext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, email;
        ImageView imProfile;
        private ImageView im_status_on;
        private ImageView im_status_off;
        private TextView tvLastMessage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsernameContact);
            email = itemView.findViewById(R.id.tvEmailContacts);
            imProfile = itemView.findViewById(R.id.profile_img);
            im_status_on = itemView.findViewById(R.id.img_on);
            im_status_off = itemView.findViewById(R.id.img_off);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }

    private void lastMessage(String userId, TextView last_msg) {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMessage = chat.getMessage();

                    }
                    switch (theLastMessage){
                        case "default":
                            last_msg.setText("no message");
                            break;
                        default:
                            last_msg.setText(theLastMessage);
                            break;
                    }
                    theLastMessage = "default";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}