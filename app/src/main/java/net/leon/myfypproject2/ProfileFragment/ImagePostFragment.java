package net.leon.myfypproject2.ProfileFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.Imagepost;
import net.leon.myfypproject2.Model.UserPostsImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePostFragment extends Fragment {
    private RecyclerView imagepostlist;
    private DatabaseReference UserPostedImgRef;
    private FirebaseAuth mAuth;
    private String current_UserID;

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
                viewHolder.setMpostimage(getContext(), model.getImageUrl());



            }
        };
        imagepostlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostedImageHolder extends RecyclerView.ViewHolder{
        View mView;
        public PostedImageHolder(View itemView){
            super(itemView);
            mView = itemView;
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



    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.imagepostfragmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Drawable drawable = menu.findItem(R.id.add_images).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_images:
                Intent addimge = new Intent(getContext(), Imagepost.class);
                startActivity(addimge);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("");
    }

}
