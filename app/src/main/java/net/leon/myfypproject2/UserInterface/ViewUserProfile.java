package net.leon.myfypproject2.UserInterface;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.ProfileFragment.BioFragment;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUserProfile extends AppCompatActivity {
    private static final String TAG = "ViewUserProfile";
    private CircleImageView userprofile, ufollow;
    private ImageView profilebgi;
    private TextView DisplayNameTv,AgeTV,GenderTV,FollowerTV,FollowingTV,AboutTV,test;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,UserProfileRef, FollowingRef , FollowerRef;
    private CircleImageView HomeBtn;
    private String currentUser_ID;
    private String User_ID;

    private int mFollowersCount =0;
    private int mFollowingsCount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title1);
        User_ID = getIntent().getExtras().get("UserID").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUser_ID = mAuth.getCurrentUser().getUid();
        UserProfileRef = FirebaseDatabase.getInstance().getReference().child("User Profile").child(User_ID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FollowingRef = FirebaseDatabase.getInstance().getReference().child("Followings");
        FollowerRef = FirebaseDatabase.getInstance().getReference().child("Followers");



        userprofile = (CircleImageView)findViewById(R.id.User_profile1);
        DisplayNameTv = (TextView)findViewById(R.id.NameToShow1);
        AgeTV = (TextView)findViewById(R.id.AgeView1);
        GenderTV = (TextView)findViewById(R.id.GenderView1);
        FollowerTV = (TextView)findViewById(R.id.FollowerView1);
        FollowingTV = (TextView)findViewById(R.id.FollowingView1);
        AboutTV = (TextView)findViewById(R.id.AboutView1);
        test = (TextView)findViewById(R.id.Test1);
        ufollow = (CircleImageView)findViewById(R.id.UFollow);






        ufollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: now following: " + DisplayNameTv.getText().toString());
                FollowingRef.child(currentUser_ID).child(User_ID).child(getString(R.string.field_user_id)).setValue(User_ID);
                FollowerRef.child(User_ID).child(currentUser_ID).child(getString(R.string.field_user_id)).setValue(currentUser_ID);
                ufollow.setVisibility(view.INVISIBLE);

            }
        });
        HomeBtn = (CircleImageView)findViewById(R.id.ImagePOSTHome1);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(ViewUserProfile.this,MainActivity.class);
                startActivity(I);
                finish();
            }
        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.userprofile_nav1);
        bnv.setOnNavigationItemSelectedListener(navlistener);

        UserProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("About")){
                        String about = dataSnapshot.child("About").getValue().toString();
                        AboutTV.setText(about);
                    }
                    if(dataSnapshot.hasChild("Age")){
                        String age = dataSnapshot.child("Age").getValue().toString();
                        AgeTV.setText(age);
                    }
                    if(dataSnapshot.hasChild("Country")){
                        String country = dataSnapshot.child("Country").getValue().toString();

                    }
                    if(dataSnapshot.hasChild("DisplayName")){
                        String displayname = dataSnapshot.child("DisplayName").getValue().toString();
                        DisplayNameTv.setText(displayname);
                    }
                    if(dataSnapshot.hasChild("Follower")){
                        String follower = dataSnapshot.child("Follower").getValue().toString();
                        int newfollower = Integer.parseInt(follower);
                        FollowerTV.setText(""+ newfollower );
                    }
                    if(dataSnapshot.hasChild("Following")){
                        String following = dataSnapshot.child("Following").getValue().toString();
                        FollowingTV.setText(following);
                    }
                    if(dataSnapshot.hasChild("Gender")){
                        String gender = dataSnapshot.child("Gender").getValue().toString();
                        GenderTV.setText(gender);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        UsersRef.child(User_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("ProfilePicture")){
                        String profileimg = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(profileimg).placeholder(R.drawable.userprofile).into(userprofile);
                    }else {
                        Toast.makeText(ViewUserProfile.this, "Profile name do not exists", Toast.LENGTH_SHORT).show();
                    }if(dataSnapshot.hasChild("username")){
                        String username = dataSnapshot.child("username").getValue().toString();
                        mTitle.setText(username);
                    }

                    if(dataSnapshot.hasChild("InAppCredit")){
                        Double total;
                        Double AddMoney = 20.0;
                        Double credit = (Double) dataSnapshot.child("InAppCredit").getValue();
                        total = AddMoney + credit;
                        test.setText(String.valueOf(total));
                    }


                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        getFollowersCount();
        getFolloweringsCount();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = null;

            switch (item.getItemId()) {
                case R.id.ProfileBio1:
                    selectedfragment = new BioFragment();
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_profile1, selectedfragment).commit();
            return true;
        }

    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userinterfacemenu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.User_Setting) {

        }

        return super.onOptionsItemSelected(item);
    }

    private void getFollowersCount(){
        mFollowersCount = 0;

        Query query = FollowerRef.child(User_ID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                FollowerTV.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFolloweringsCount() {
        mFollowingsCount = 0;

        Query query = FollowingRef.child(User_ID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following:" + singleSnapshot.getValue());
                    mFollowingsCount++;
                }
                FollowingTV.setText(String.valueOf(mFollowingsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
