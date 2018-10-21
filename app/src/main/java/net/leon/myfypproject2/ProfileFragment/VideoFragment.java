package net.leon.myfypproject2.ProfileFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.Video;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.StatusPost;
import net.leon.myfypproject2.UserInterface.VideoPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private RecyclerView videopostlist;
    private DatabaseReference VideoPostRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private VideoView video;

    public VideoFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        VideoPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Video_Posts");

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videopostlist = (RecyclerView)view.findViewById(R.id.video_post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        videopostlist.setLayoutManager(linearLayoutManager);

        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query Videopost = VideoPostRef.orderByChild("UserID").startAt(current_UserID).endAt(current_UserID + "\uf8ff");
        FirebaseRecyclerAdapter<Video,VideoPostHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, VideoPostHolder>(
                Video.class,
                R.layout.allvideopost,
                VideoPostHolder.class,
                Videopost
        ) {
            @Override
            protected void populateViewHolder(VideoPostHolder viewHolder, Video model, int position) {
                Uri uri = Uri.parse(model.getVideoUrl());
                viewHolder.setpostvideo(String.valueOf(uri));


            }
        };
        videopostlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class VideoPostHolder extends RecyclerView.ViewHolder{
        View mView;
        private VideoView video;
        public VideoPostHolder(View itemView){
            super(itemView);
            mView = itemView;
            video = (VideoView)mView.findViewById(R.id.MyVideolist);
            video.start();

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });






        }

        public void setpostvideo( String postvideo){
            Uri uri = Uri.parse(postvideo);
            video.setVideoURI(uri);





        }


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addvideofragmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Drawable drawable = menu.findItem(R.id.add_video).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_video:
                Intent addimge = new Intent(getContext(), VideoPost.class);
                startActivity(addimge);

                return true;


        }
        return super.onOptionsItemSelected(item);
    }


}
