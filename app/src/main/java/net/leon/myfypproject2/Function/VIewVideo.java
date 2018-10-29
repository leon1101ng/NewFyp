package net.leon.myfypproject2.Function;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.leon.myfypproject2.Model.Video;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VIewVideo extends Fragment {
    private RecyclerView videopostlist;
    private DatabaseReference VideoPostRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private VideoView video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        VideoPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Video_Posts");

        View view = inflater.inflate(R.layout.fragment_view_video, container, false);

        videopostlist = (RecyclerView)view.findViewById(R.id.video_post_list2);
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
        FirebaseRecyclerAdapter<Video,VideoPostHolder1> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, VideoPostHolder1>(
                Video.class,
                R.layout.all_videopost_layout,
                VideoPostHolder1.class,
                VideoPostRef
        ) {
            @Override
            protected void populateViewHolder(VideoPostHolder1 viewHolder, Video model, int position) {
                Uri uri = Uri.parse(model.getVideoUrl());
                viewHolder.setpostvideo(String.valueOf(uri));
                viewHolder.setUserFullname(model.getUserFullname());
                viewHolder.setUserPicture(getActivity(), model.getUserPicture());
                viewHolder.setVideoCaption(model.getVideoCaption());


            }
        };
        videopostlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class VideoPostHolder1 extends RecyclerView.ViewHolder{
        View mView;
        private VideoView video;
        public VideoPostHolder1(View itemView){
            super(itemView);
            mView = itemView;
            video = (VideoView)mView.findViewById(R.id.AllVideo);
            video.start();

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0f,0f);
                }
            });


        }
        public void setpostvideo( String postvideo){
            Uri uri = Uri.parse(postvideo);
            video.setVideoURI(uri);

        }
        public void setUserFullname(String userFullname) {
            TextView userfulln = (TextView)mView.findViewById(R.id.AllVideoUsername);
            userfulln.setText(userFullname);
        }

        public void setVideoCaption(String videoCaption) {
            TextView videocaption = (TextView)mView.findViewById(R.id.AllVideocaption);
            videocaption.setText(videoCaption);
        }

        public void setUserPicture(Context ctx,String userPicture) {

                CircleImageView videouserimage = (CircleImageView) mView.findViewById(R.id.AllVideoUserImage);
                Glide.with(ctx)
                        .load(userPicture)
                        .into(videouserimage);

        }


    }

}
