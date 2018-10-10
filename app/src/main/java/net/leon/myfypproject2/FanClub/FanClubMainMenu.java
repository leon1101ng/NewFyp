package net.leon.myfypproject2.FanClub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.Model.FanclubClass;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.UserInterface;
import net.leon.myfypproject2.VipSubscription.VipMenu;

import de.hdodenhof.circleimageview.CircleImageView;

public class FanClubMainMenu extends AppCompatActivity {
    private RecyclerView Fanclubcreatedlist;
    private DatabaseReference FanclubcreatedRef, UserRef;
    private FirebaseAuth mAuth;
    private CircleImageView back,ToCreateClub;
    private String Current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_club_main_menu);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        FanclubcreatedRef = FirebaseDatabase.getInstance().getReference().child("Fan Club");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Fanclubcreatedlist =(RecyclerView)findViewById(R.id.Fanclubcreatedlist);
        Fanclubcreatedlist.setHasFixedSize(true);
        back = (CircleImageView)findViewById(R.id.BackToMenu_FanclubPage);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToCreateClub = (CircleImageView)findViewById(R.id.ToCreateClubV);
        ToCreateClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckUser();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        Fanclubcreatedlist.setLayoutManager(linearLayoutManager);
        
        DisplayAllFanClubCreated();

    }

    private void CheckUser() {

        UserRef.child(Current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("Vip")){
                        int vip = 1;
                        int UserVip =  dataSnapshot.child("Vip").getValue(Integer.class);
                        if(UserVip == vip){
                                    ToCreateClubMenu();
                        }else {
                            Toast.makeText(FanClubMainMenu.this,"Only Vip Can Create Fan Club", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ToCreateClubMenu() {
        Intent i = new Intent(FanClubMainMenu.this, CreateClub.class);
        startActivity(i);
    }

    private void DisplayAllFanClubCreated() {
        FirebaseRecyclerAdapter<FanclubClass, FanClubViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FanclubClass, FanClubViewHolder>(
                        FanclubClass.class,
                        R.layout.all_fanclub_created,
                        FanClubViewHolder.class,
                        FanclubcreatedRef
                ) {
                    @Override
                    protected void populateViewHolder(FanClubViewHolder viewHolder, FanclubClass model, int position) {
                        viewHolder.setClubCreator(model.getClubCreator());
                        viewHolder.setClubName(model.getClubName());
                        viewHolder.setClubTopic(model.getClubTopic());
                        viewHolder.setClubPicture(getApplication(), model.getClubPicture());

                        final String ClubName = getRef(position).getKey();
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent fanclubchat = new Intent(FanClubMainMenu.this, FanClubChat.class);
                                fanclubchat.putExtra("ClubName", ClubName);
                                startActivity(fanclubchat);

                            }
                        });

                    }
                };
        Fanclubcreatedlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FanClubViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FanClubViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setClubName(String clubName){
            TextView clubname = (TextView) mView.findViewById(R.id.fanclub_name);
            clubname.setText(clubName);
        }

        public void setClubCreator(String clubCreator) {
            TextView clubcreator = (TextView) mView.findViewById(R.id.fanclub_creator);
            clubcreator.setText("created By "+clubCreator);
        }

        public void setClubTopic(String clubTopic) {
            TextView clubtopic = (TextView) mView.findViewById(R.id.fanclub_topic);
            clubtopic.setText(clubTopic);
        }

        public void setClubPicture(Context ctx, String clubPicture) {
            ImageView clubpicture = (ImageView)mView.findViewById(R.id.fanclub_picture);
            Glide.with(ctx)
                    .load(clubPicture)
                    .into(clubpicture);
        }
    }



}
