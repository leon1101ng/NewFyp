package net.leon.myfypproject2.ProfileFragment;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.UserPostsImage;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.ImageDetail;
import net.leon.myfypproject2.UserInterface.Imagepost;
import net.leon.myfypproject2.UserInterface.ViewImage;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePostFragment extends Fragment {
    private RecyclerView imagepostlist;
    private DatabaseReference UserPostedImgRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private FabSpeedDial fabSpeedDial;
    private Animator mCurrentAnimator;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        UserPostedImgRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts");

        View view = inflater.inflate(R.layout.fragment_image_post, container, false);


        fabSpeedDial = (FabSpeedDial)view.findViewById(R.id.fabspeeddial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.Post_Image){
                    Intent i = new Intent(getContext(), Imagepost.class);
                    startActivity(i);

                }else if (id == R.id.Manage_Image){
                    String t = "1";
                    Intent i = new Intent(getContext(), ViewImage.class);
                    i.putExtra("1", t);
                    startActivity(i);






                }else if (id == R.id.View_Image){
                    String t = "2";
                    Intent i = new Intent(getContext(), ViewImage.class);
                    i.putExtra("1", t);
                    startActivity(i);

                }
                return true;

            }



            @Override
            public void onMenuClosed() {

            }
        });

        imagepostlist = (RecyclerView)view.findViewById(R.id.Image_post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        imagepostlist.setLayoutManager(linearLayoutManager);
        imagepostlist.setLayoutManager(new GridLayoutManager(getContext(),3));


        return view;

    }




    public void onStart() {
        super.onStart();


        Query ArtistArt = UserPostedImgRef.orderByChild("UserID").startAt(current_UserID).endAt(current_UserID + "\uf8ff");
        FirebaseRecyclerAdapter<UserPostsImage, PostedImageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPostsImage, PostedImageHolder>(
                UserPostsImage.class,
                R.layout.user_imagepost_layout,
                PostedImageHolder.class,
                ArtistArt

        ) {
            @Override
            protected void populateViewHolder(PostedImageHolder viewHolder, UserPostsImage model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setMpostimage(getContext(), model.getImageUrl());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), ImageDetail.class);
                        i.putExtra("PostKey", postkey);
                        startActivity(i);

                    }
                });


            }


        };
        imagepostlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostedImageHolder extends RecyclerView.ViewHolder{
        public View mView;
        public CircleImageView deleteimage;
        public PostedImageHolder(View itemView){
            super(itemView);

            mView = itemView;
        deleteimage = (CircleImageView)mView.findViewById(R.id.deleteimage);
        deleteimage.setVisibility(View.INVISIBLE);


        }

        public void setMpostimage(Context ctx,String mpostimage){
            ImageView postedimg = (ImageView) mView.findViewById(R.id.Image_post);
            Glide.with(ctx)
                    .load(mpostimage)
                    .into(postedimg);

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("");
    }


}
