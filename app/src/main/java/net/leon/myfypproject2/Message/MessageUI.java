package net.leon.myfypproject2.Message;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Initializable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.Adapter.MessageAdapter;
import net.leon.myfypproject2.Model.Message;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.StatusPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageUI extends AppCompatActivity {
    private String UserID,Current_user,message,CurrentDate,CurrentTime,Postrandomname;
    private DatabaseReference MessagesRef,UserRef,RootRef;
    private FirebaseAuth mAuth;
    private CircleImageView UserImage,BackBtn;
    private TextView Username;
    private EditText messagetext;
    private ImageView sendMessage;
    private RecyclerView userMessageList;
    private final List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_ui);
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef = FirebaseDatabase.getInstance().getReference();

        UserImage = (CircleImageView)findViewById(R.id.ChatUserImage);
        BackBtn = (CircleImageView)findViewById(R.id.ToUserProfile);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Username = (TextView)findViewById(R.id.chatusername);
        messagetext = (EditText)findViewById(R.id.message_text);
        sendMessage = (ImageView)findViewById(R.id.sendmessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateInput();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        UserID = getIntent().getExtras().get("UserID").toString();

        UserRef.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userimage = dataSnapshot.child("ProfilePicture").getValue().toString();
                    Picasso.get().load(userimage).placeholder(R.drawable.addphoto).into(UserImage);
                    String username = dataSnapshot.child("username").getValue().toString();
                    Username.setText(username);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        messageAdapter = new MessageAdapter(messageList);
        userMessageList = (RecyclerView)findViewById(R.id.userMessageList);
        linearLayoutManager = new LinearLayoutManager(this);

        userMessageList.setHasFixedSize(true);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messageAdapter);
        
        FetchMessage();

    }

    private void FetchMessage() {
        RootRef.child("Messages").child(Current_user).child(UserID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()){
                            Message message = dataSnapshot.getValue(Message.class);
                            messageList.add(message);
                            messageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void ValidateInput() {
        message = messagetext.getText().toString();
        if(TextUtils.isEmpty(message)){
            
        }else {
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            storeMessagetodb();
        }
    }

    private void storeMessagetodb() {
        String type = "Text";
        HashMap sendMessage = new HashMap();
        sendMessage.put("Date", CurrentDate);
        sendMessage.put("Time", CurrentTime);
        sendMessage.put("Sender", Current_user);
        sendMessage.put("Message", message);
        sendMessage.put("Type", type);
        MessagesRef.child(Current_user).child(UserID).child(Postrandomname).updateChildren(sendMessage)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            messagetext.setText("");

                        }else {

                        }

                    }
                });
        MessagesRef.child(UserID).child(Current_user).child(Postrandomname).updateChildren(sendMessage)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){

                        }else {

                        }

                    }
                });

    }
}
