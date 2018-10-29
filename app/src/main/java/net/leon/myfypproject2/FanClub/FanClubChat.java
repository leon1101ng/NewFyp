package net.leon.myfypproject2.FanClub;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.FanPost;
import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.FanClubChatClass;
import net.leon.myfypproject2.ProfileFragment.BioFragment;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FanClubChat extends AppCompatActivity {
    private EditText fcchattext;
    private TextView postfcchat,fanclubtopicView;
    private RecyclerView FanClubChatlist;
    private DatabaseReference UserRef, FanClubRef, FanClubRef1, FanClubUserRef, FanClubUserRef1;
    private FirebaseAuth mAuth;
    private CircleImageView BackToMenu;
    private ImageView fanclubimageView;

    String postKey, Current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_club_chat);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.FanClubTitle);
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("ClubName").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FanClubRef = FirebaseDatabase.getInstance().getReference().child("Fan Club").child(postKey).child("Chat");
        FanClubRef1 = FirebaseDatabase.getInstance().getReference().child("Fan Club").child(postKey);
        fanclubtopicView = (TextView)findViewById(R.id.fanclubtopicView);
        FanClubUserRef = FirebaseDatabase.getInstance().getReference().child("FanClubUser").child(postKey).child(Current_user);
        FanClubUserRef1 = FirebaseDatabase.getInstance().getReference().child("FanClubUser").child(postKey);
        fanclubimageView = (ImageView)findViewById(R.id.fanclubimageView);

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.fanUserBNV);
        bnv.setOnNavigationItemSelectedListener(navlistener);

        BackToMenu = (CircleImageView)findViewById(R.id.BackToMenu);
        BackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FanClubChat.this, MainActivity.class);
                startActivity(i);
                finish();


            }
        });



        FanClubRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String clubname = dataSnapshot.child("ClubName").getValue().toString();
                    mTitle.setText(clubname);
                    String clubtopic = dataSnapshot.child("ClubTopic").getValue().toString();
                    fanclubtopicView.setText(clubtopic);
                    String clubimage = dataSnapshot.child("ClubPicture").getValue().toString();
                    Picasso.get().load(clubimage).placeholder(R.drawable.layer11).into(fanclubimageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fcchattext = (EditText) findViewById(R.id.fanclubchatText);
        postfcchat = (TextView) findViewById(R.id.postfcchat);

        FanClubChatlist = (RecyclerView)findViewById(R.id.FanClubChatlist);
        FanClubChatlist.setHasFixedSize(true);
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
                            fcchattext.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        UserChecking();

    }

    private void UserChecking() {
        FanClubUserRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(Current_user)){
                    KickUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void KickUser() {
        Intent setuppage = new Intent(FanClubChat.this, FanClubMainMenu.class);
        Toast.makeText(FanClubChat.this,"You Has Been Kick By Host", Toast.LENGTH_SHORT).show();
        startActivity(setuppage);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = new Fragment();

            switch (item.getItemId()) {
                case R.id.fanclubmsg:

                    break;
                case  R.id.fanclubuser:
                    selectedfragment = new ClubUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("ClubName", postKey);
                    selectedfragment.setArguments(bundle);
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.FanClubLy, selectedfragment).commit();
            return true;
        }

    };




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

            final String RandomKey = "FC" + CurrentDate + CurrentTime;

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
