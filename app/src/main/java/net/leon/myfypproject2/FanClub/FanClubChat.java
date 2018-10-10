package net.leon.myfypproject2.FanClub;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Model.FanClubChatClass;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FanClubChat extends AppCompatActivity {
    private EditText fcchattext;
    private TextView postfcchat;
    private RecyclerView FanClubChatlist;
    private DatabaseReference UserRef, FanClubRef;
    private FirebaseAuth mAuth;

    String postKey, Current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_club_chat);
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("ClubName").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FanClubRef = FirebaseDatabase.getInstance().getReference().child("Fan Club").child(postKey).child("Chat");

        fcchattext = (EditText) findViewById(R.id.fanclubchatText);
        postfcchat = (TextView) findViewById(R.id.postfcchat);

        FanClubChatlist = (RecyclerView)findViewById(R.id.FanClubChatlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        FanClubChatlist.setLayoutManager(linearLayoutManager);

        postfcchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(Current_user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String userName = dataSnapshot.child("username").getValue().toString();

                            ValidateChat(userName);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void ValidateChat(String userName) {
        String chat = fcchattext.getText().toString();
        if (TextUtils.isEmpty(chat)) {
            Toast.makeText(this, "Please Enter Text To chat...", Toast.LENGTH_SHORT).show();
        } else {
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            final String CurrentTime = currenttime.format(caltime.getTime());

            final String RandomKey = Current_user + CurrentDate + CurrentTime;

            HashMap chatmap = new HashMap();
            chatmap.put("Username", userName);
            chatmap.put("Chat", chat);
            chatmap.put("ChatTime", CurrentTime);
            chatmap.put("ChatDate", CurrentDate);
            FanClubRef.child(RandomKey).updateChildren(chatmap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FanClubChat.this, "Done Comment...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FanClubChat.this, "Failed To Comment", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<FanClubChatClass,FanClubChatViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FanClubChatClass, FanClubChatViewHolder>(
                        FanClubChatClass.class,
                        R.layout.fanclubchat_layout,
                        FanClubChatViewHolder.class,
                        FanClubRef

                ) {
                    @Override
                    protected void populateViewHolder(FanClubChatViewHolder viewHolder, FanClubChatClass model, int position) {
                        viewHolder.setChat(model.getChat());
                        viewHolder.setUsername(model.getUsername());

                    }
                };
        FanClubChatlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FanClubChatViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FanClubChatViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setChat(String chat) {
            TextView content = (TextView) mView.findViewById(R.id.FCChat_content);
            content.setText(chat);
        }

        public void setUsername(String username) {
            TextView Username = (TextView) mView.findViewById(R.id.FCChat_username);
            Username.setText(username);
        }
    }
}
