package net.leon.myfypproject2.FanClub;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.FanClubUser;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserAccount.UserSetup;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClubUser extends Fragment {
    private String CurrentUser,i,clubcreatorID;
    private DatabaseReference fanClubUserRef,FanClubRef;
    private FirebaseAuth mAuth;
    private RecyclerView fanclubuser_list;


    public ClubUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_club_user, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            i = bundle.getString("ClubName");
        }

        fanclubuser_list = (RecyclerView)view.findViewById(R.id.fanclubuser_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fanclubuser_list.setLayoutManager(linearLayoutManager);

        fanClubUserRef = FirebaseDatabase.getInstance().getReference().child("FanClubUser").child(i);
        FanClubRef = FirebaseDatabase.getInstance().getReference().child("Fan Club").child(i);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();

        FanClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    clubcreatorID = dataSnapshot.child("UserID").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        UserChecking();
        return view;

    }
    private void UserChecking() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        fanClubUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id)){
                    UserSetup();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UserSetup() {
        Intent setuppage = new Intent(getContext(), FanClubMainMenu.class);
        Toast.makeText(getContext(),"You Has Been Kick By Host", Toast.LENGTH_SHORT).show();
        startActivity(setuppage);

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FanClubUser,FanclubUserHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FanClubUser, FanclubUserHolder>(
                FanClubUser.class,
                R.layout.fanuserlayout,
                FanclubUserHolder.class,
                fanClubUserRef
        ) {
            @Override
            protected void populateViewHolder(FanclubUserHolder viewHolder, FanClubUser model, int position) {

                final String postkey = getRef(position).getKey();

                viewHolder.setUsername(model.getUsername());
                viewHolder.setUserImage(getContext(),model.getUserImage());
                final int fanuservip = model.getVip();
                final String fanuserID = model.getUserID();
                if (fanuservip== 1){
                    viewHolder.vipgifview.setVisibility(View.VISIBLE);

                }


                if(clubcreatorID.equals(fanuserID)){
                    viewHolder.FanClubHost.setVisibility(View.VISIBLE);
                    viewHolder.removeFanClubUser.setVisibility(View.INVISIBLE);
                }

                viewHolder.removeFanClubUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CurrentUser.equals(clubcreatorID)){
                            fanClubUserRef.child(postkey).removeValue();

                        }else {
                            Toast.makeText(getContext(),"Only Host Can Kick User", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        };
        fanclubuser_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FanclubUserHolder extends RecyclerView.ViewHolder {
        View mView;
        private GifImageView vipgifview,FanClubHost;
        private CircleImageView removeFanClubUser;
        public FanclubUserHolder (View itemView) {
            super(itemView);
            mView = itemView;
            vipgifview = (GifImageView)mView.findViewById(R.id.FanVip);
            FanClubHost = (GifImageView)mView.findViewById(R.id.FanClubHost);
            removeFanClubUser = (CircleImageView)mView.findViewById(R.id.removeFanClubUser);
            vipgifview.setVisibility(View.INVISIBLE);
            FanClubHost.setVisibility(View.INVISIBLE);
        }
        public void setUsername(String username) {
            TextView FCUser_name = (TextView)mView.findViewById(R.id.FCUser_name);
            FCUser_name.setText(username);

        }

        public void setUserImage(Context ctx,String userImage) {
            CircleImageView FCUser_Image = (CircleImageView)mView.findViewById(R.id.FCUser_Image);
            Glide.with(ctx).load(userImage).into(FCUser_Image);

        }


    }
}
