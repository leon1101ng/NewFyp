package net.leon.myfypproject2.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Model.Message;
import net.leon.myfypproject2.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Message> userMessagelist;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    public MessageAdapter(List<Message> userMessagelist){
        this.userMessagelist = userMessagelist;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView sdmessageText, rcmessageText;
        public MessageViewHolder(View itemView){
            super(itemView);
            sdmessageText = (TextView) itemView.findViewById(R.id.sender_msg);
            rcmessageText = (TextView)itemView.findViewById(R.id.receiver_msg);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Message message = userMessagelist.get(position);

        String fromUserID = message.getSender();
        String fromMessagetype = message.getType();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        if(fromMessagetype.equals("Text")){
            holder.rcmessageText.setVisibility(View.INVISIBLE);

            if(fromUserID.equals(messageSenderId)){
                holder.sdmessageText.setBackgroundResource(R.drawable.layer10);
                holder.sdmessageText.setTextColor(Color.WHITE);
                holder.sdmessageText.setGravity(Gravity.LEFT);
                holder.sdmessageText.setText(message.getMessage());

            }else {
                holder.sdmessageText.setVisibility(View.INVISIBLE);
                holder.rcmessageText.setVisibility(View.VISIBLE);
                holder.rcmessageText.setBackgroundResource(R.drawable.layer9);
                holder.rcmessageText.setTextColor(Color.BLACK);
                holder.rcmessageText.setGravity(Gravity.LEFT);
                holder.rcmessageText.setText(message.getMessage());

            }
        }

    }

    @Override
    public int getItemCount() {
        return userMessagelist.size();
    }
}
