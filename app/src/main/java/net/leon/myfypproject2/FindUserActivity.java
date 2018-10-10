package net.leon.myfypproject2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.UserClass;
import net.leon.myfypproject2.UserInterface.ViewUserProfile;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindUserActivity extends AppCompatActivity {
    private EditText searchUser;
    private TextView findfriend;
    private RecyclerView userlist;
    private DatabaseReference AllUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.FindUser_Toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Find Friend");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AllUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userlist = (RecyclerView)findViewById(R.id.RV_FindUserList);
        userlist.setLayoutManager(new LinearLayoutManager(this));

        findfriend = (TextView)findViewById(R.id.fin_friend);
        searchUser = (EditText)findViewById(R.id.Search_UserID);

        findfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputUsername = searchUser.getText().toString();
                if(TextUtils.isEmpty(inputUsername)){
                    Toast.makeText(FindUserActivity.this, "Please Enter A Usernaame", Toast.LENGTH_SHORT).show();
                }else {
                SearchOtherUser(inputUsername);
                }

            }
        });
    }

    private void SearchOtherUser(String inputUsername) {
        Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show();

        Query searchuserquery = AllUserRef.orderByChild("username")
                .startAt(inputUsername).endAt(inputUsername + "\uf8ff");
        FirebaseRecyclerAdapter<UserClass, FindUserActivity.FindUserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserClass, FindUserActivity.FindUserViewHolder>(
                UserClass.class,
                R.layout.user_friend_layout,
                FindUserActivity.FindUserViewHolder.class,
                searchuserquery
        ) {
            @Override
            protected void populateViewHolder(FindUserViewHolder viewHolder, UserClass model, int position) {
                viewHolder.setProfileimage(getApplication(), model.getProfilePicture());
                viewHolder.setFindUsername(model.getUsername());
                viewHolder.setFindname(model.getFullname());
                final String User_ID = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userprofile = new Intent(FindUserActivity.this, ViewUserProfile.class);
                        userprofile.putExtra("UserID", User_ID);
                        startActivity(userprofile);
                    }
                });

            }

        };
        userlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class FindUserViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FindUserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProfileimage(Context ctx,String profileimage){
            CircleImageView userimg = (CircleImageView)mView.findViewById(R.id.FindUser_Profile);
            Glide.with(ctx)
                    .load(profileimage)
                    .into(userimg);
        }

        public void setFindUsername(String findusername){
            TextView finduser = (TextView)mView.findViewById(R.id.FindUser_Username);
            finduser.setText(findusername);

        }

        public void setFindname(String findname){
            TextView findName = (TextView)mView.findViewById(R.id.FindUser_Name);
            findName.setText(findname);

        }
    }
}
