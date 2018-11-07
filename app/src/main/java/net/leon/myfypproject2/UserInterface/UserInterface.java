package net.leon.myfypproject2.UserInterface;

import android.app.Dialog;
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
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.ProfileFragment.BioFragment;
import net.leon.myfypproject2.ProfileFragment.FanMessageFragment;
import net.leon.myfypproject2.ProfileFragment.ImagePostFragment;
import net.leon.myfypproject2.ProfileFragment.PostFragment;
import net.leon.myfypproject2.ProfileFragment.VideoFragment;
import net.leon.myfypproject2.Purchase_in_app_credit.PurchaseCredit;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserAccount.VerifyCelebrity;
import net.leon.myfypproject2.VipSubscription.VipMenu;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInterface extends AppCompatActivity {
    private static final String TAG = "UserInterface";
    private CircleImageView userprofile;
    private ImageView profilebgi;
    private TextView DisplayNameTv,AgeTV,GenderTV,FollowerTV,FollowingTV,AboutTV,test,UserMoney,vipstatus;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,UserProfileRef;
    private CircleImageView HomeBtn;
    private String currentUser_ID;
    private String User_ID;
    private Dialog myDialog;

    private int mFollowingCount = 0;
    private int mFollowersCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        myDialog = new Dialog(this);


        mAuth = FirebaseAuth.getInstance();
        currentUser_ID = mAuth.getCurrentUser().getUid();
        UserProfileRef = FirebaseDatabase.getInstance().getReference().child("User Profile");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        userprofile = (CircleImageView)findViewById(R.id.User_profile);
        DisplayNameTv = (TextView)findViewById(R.id.NameToShow);
        AgeTV = (TextView)findViewById(R.id.AgeView);
        GenderTV = (TextView)findViewById(R.id.GenderView);
        FollowerTV = (TextView)findViewById(R.id.FollowerView);
        FollowingTV = (TextView)findViewById(R.id.FollowingView);
        AboutTV = (TextView)findViewById(R.id.AboutView);
        UserMoney = (TextView)findViewById(R.id.UserMoney);
        vipstatus = (TextView)findViewById(R.id.vipstatus);


        HomeBtn = (CircleImageView)findViewById(R.id.ImagePOSTHome);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(UserInterface.this,MainActivity.class);
                startActivity(I);
                finish();
            }
        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.userprofile_nav);
        bnv.setOnNavigationItemSelectedListener(navlistener);

        UserProfileRef.child(currentUser_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("About")){
                        String about = dataSnapshot.child("About").getValue().toString();
                        AboutTV.setText(about);
                    }
                    if(dataSnapshot.hasChild("Age")){
                        String age = dataSnapshot.child("Age").getValue().toString();
                        AgeTV.setText(age + "y");
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

        UsersRef.child(currentUser_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("ProfilePicture")){
                        String profileimg = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(profileimg).placeholder(R.drawable.userprofile).into(userprofile);
                    }else {
                        Toast.makeText(UserInterface.this, "Profile name do not exists", Toast.LENGTH_SHORT).show();
                    }if(dataSnapshot.hasChild("username")){
                        String username = dataSnapshot.child("username").getValue().toString();
                        mTitle.setText(username);
                    }if(dataSnapshot.hasChild("InAppCredit")){
                        int credit =  dataSnapshot.child("InAppCredit").getValue(Integer.class);
                        UserMoney.setText(String.valueOf(credit));
                    }if(dataSnapshot.hasChild("Vip")){
                        int vip = dataSnapshot.child("Vip").getValue(Integer.class);
                        if(vip != 1){
                            vipstatus.setText("Not VIP");
                        }else {
                            vipstatus.setText("VIP");
                        }
                    }



                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getFollowingCount();
        getFollowerCount();



    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = null;

            switch (item.getItemId()) {
                case R.id.ProfileBio:
                    selectedfragment = new BioFragment();
                    break;
                case R.id.Profileimage:
                    selectedfragment = new ImagePostFragment();
                    break;
                case R.id.ProfilePost:
                    selectedfragment = new PostFragment();
                    break;
                case R.id.Profilevideo:
                    selectedfragment = new VideoFragment();
                    break;
                case R.id.FanMessage:
                    selectedfragment = new FanMessageFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_profile, selectedfragment).commit();
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
            Intent i = new Intent(UserInterface.this, EditUserBios.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void getFollowingCount(){
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Followings");
        Query query = reference.child(currentUser_ID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                FollowingTV.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowerCount(){
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Followers");
        Query query = reference.child(currentUser_ID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower user:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                FollowerTV.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void UserPagepopout(MenuItem item) {
        TextView editprofit,buyvip,buycredit,verifycelebrity;
        myDialog.setContentView(R.layout.userinterfacedialog);
        editprofit = (TextView)myDialog.findViewById(R.id.editUserProfile);
        buyvip = (TextView)myDialog.findViewById(R.id.userPurchasVip);
        buycredit = (TextView)myDialog.findViewById(R.id.userTopupCredit);
        verifycelebrity = (TextView)myDialog.findViewById(R.id.CelebrityVerify);

        editprofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInterface.this, EditUserBios.class);
                startActivity(i);
            }
        });
        buyvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInterface.this, VipMenu.class);
                startActivity(i);
            }
        });
        buycredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInterface.this, PurchaseCredit.class);
                startActivity(i);
            }
        });
        verifycelebrity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersRef.child(currentUser_ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String type = dataSnapshot.child("Type").getValue().toString();
                            if(type.equals("Celebrity")){
                                FancyToast.makeText(UserInterface.this,"You Are Already Celebrity !",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                            }else {
                                Intent i = new Intent(UserInterface.this, VerifyCelebrity.class);
                                startActivity(i);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.show();
    }
}
