package net.leon.myfypproject2.Function;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Comment.ImageComment;
import net.leon.myfypproject2.Model.UserPostsImage;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.UserInterface;
import net.leon.myfypproject2.UserInterface.ViewUserProfile;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImage extends Fragment {


    private RecyclerView imagepostlist;
    private DatabaseReference UserPostedImgRef, LikePostRef, FollowingRef , UserRef;
    private FirebaseAuth mAuth;
    private String current_UserID, username;
    Boolean CheckLike = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        UserPostedImgRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts");
        LikePostRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        FollowingRef = FirebaseDatabase.getInstance().getReference().child("Followings").child(current_UserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_UserID);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username = dataSnapshot.child("username").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        View view = inflater.inflate(R.layout.fragment_view_image, container, false);
        imagepostlist = (RecyclerView)view.findViewById(R.id.Image_post_list2);
        imagepostlist.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        imagepostlist.setLayoutManager(linearLayoutManager);
        displayresult();


        return view;




    }



    private void displayresult() {
        FirebaseRecyclerAdapter<UserPostsImage, ViewImage.PostedImageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPostsImage, ViewImage.PostedImageHolder>(
                UserPostsImage.class,
                R.layout.allimagepost,
                ViewImage.PostedImageHolder.class,
                UserPostedImgRef
        ) {
            @Override
            protected void populateViewHolder(final ViewImage.PostedImageHolder viewHolder, UserPostsImage model, int position) {


                final String userid = model.getUserID();

                viewHolder.setpostimage(getContext(), model.getImageUrl());
                viewHolder.setuserprofile(getContext(), model.getPostImage());
                viewHolder.setdate(model.getDate() + " "+ model.getTime());
                viewHolder.setusername(model.getFullname());
                viewHolder.setimagecaption(model.getImageCaption());
                viewHolder.setImagePostlocation(model.getImagePostLocation());

                final String postkey = getRef(position).getKey();
                final String User_ID = model.getUserID();


                viewHolder.setLikeButtonStatus(postkey);

                viewHolder.username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userid.equals(current_UserID)) {
                            Intent i = new Intent(getActivity(), UserInterface.class);
                            startActivity(i);
                        }else {
                            goViewUserProfile();


                        }
                    }

                    private void goViewUserProfile() {
                        Intent i = new Intent(getActivity(), ViewUserProfile.class);
                        i.putExtra("UserID", User_ID);
                        startActivity(i);
                    }
                });
                viewHolder.comment_img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent commentsIntent = new Intent(getActivity(), ImageComment.class);
                        commentsIntent.putExtra("PostKey", postkey);
                        startActivity(commentsIntent);
                    }
                });
                viewHolder.like_imgpost_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckLike = true;

                        LikePostRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(CheckLike.equals(true)){
                                    if(dataSnapshot.child(postkey).hasChild(current_UserID)){
                                        LikePostRef.child(postkey).child(current_UserID).removeValue();
                                        CheckLike = false;
                                    }else {
                                        LikePostRef.child(postkey).child(current_UserID).setValue(true);
                                        CheckLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });



            }
        };
        imagepostlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostedImageHolder extends RecyclerView.ViewHolder{
        View mView;

        private ImageButton like_imgpost_btn,comment_img_btn;
        private TextView no_likes, no_comments, username;
        int Count_postlikes,count_postcm;
        private CircleImageView UserprofileCI;
        private TextView UsernameTV;
        String currentuserid;
        DatabaseReference LikesRef,CommentRef, UserRef;
        public PostedImageHolder(final View itemView){
            super(itemView);
            mView = itemView;
            no_likes = (TextView)mView.findViewById(R.id.totalno_likes);
            like_imgpost_btn = (ImageButton)mView.findViewById(R.id.like);
            comment_img_btn = (ImageButton)mView.findViewById(R.id.comment);
            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            username = (TextView)mView.findViewById(R.id.allpostusername);



        }



        public void setLikeButtonStatus(final String postkey){
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postkey).hasChild(currentuserid)){
                        Count_postlikes = (int) dataSnapshot.child(postkey).getChildrenCount();
                        like_imgpost_btn.setImageResource(R.drawable.liked);
                        no_likes.setText(Integer.toString(Count_postlikes) + "Likes");


                    }else {
                        Count_postlikes = (int) dataSnapshot.child(postkey).getChildrenCount();
                        like_imgpost_btn.setImageResource(R.drawable.like);
                        no_likes.setText(Integer.toString(Count_postlikes) + "Likes");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        }

        public void setpostimage(Context ctx, String Postimg){
            ImageView postedimg = (ImageView) mView.findViewById(R.id.allpostuserimage);
            Glide.with(ctx)
                    .load(Postimg)
                    .into(postedimg);
        }
        public void setuserprofile(Context ctx, String setuserprofile){
            CircleImageView userprofile = (CircleImageView) mView.findViewById(R.id.allpostuserprofile);
            Glide.with(ctx)
                    .load(setuserprofile)
                    .into(userprofile);
        }
        public void setusername(String Username){
            TextView username = (TextView)mView.findViewById(R.id.allpostusername);
            username.setText(Username);
        }

        public void setdate(String Timedate){
            TextView date = (TextView)mView.findViewById(R.id.allposttimedate);
            date.setText(Timedate);
        }
        public void setimagecaption(String Caption){
            TextView caption = (TextView)mView.findViewById(R.id.allpostimagedesp);
            caption.setText(Caption);
        }
        public void setImagePostlocation(String imagePostlocation) {
           TextView location = (TextView)mView.findViewById(R.id.allpostlocation);
           location.setText(imagePostlocation);
        }



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getActivity().setTitle("The Starry");
    }

}
