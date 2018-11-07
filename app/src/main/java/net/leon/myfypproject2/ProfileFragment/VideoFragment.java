package net.leon.myfypproject2.ProfileFragment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.Video;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.VideoDetail;
import net.leon.myfypproject2.UserInterface.VideoPost;
import net.leon.myfypproject2.UserInterface.ViewVideo;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private RecyclerView videopostlist;
    private DatabaseReference VideoPostRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private FabSpeedDial fabSpeedDial;

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

        fabSpeedDial = (FabSpeedDial)view.findViewById(R.id.fabspeeddial1);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.Post_Video){
                    Intent i = new Intent(getContext(), VideoPost.class);
                    startActivity(i);

                }else if (id == R.id.Manage_Video){
                    String t = "1";
                    Intent i = new Intent(getContext(), ViewVideo.class);
                    i.putExtra("1", t);
                    startActivity(i);

                }else if (id == R.id.View_Video){
                    String t = "2";
                    Intent i = new Intent(getContext(), ViewVideo.class);
                    i.putExtra("1", t);
                    startActivity(i);

                }
                return true;

            }

            @Override
            public void onMenuClosed() {

            }
        });

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
                final String postkey = getRef(position).getKey();
                Uri uri = Uri.parse(model.getVideoUrl());
                viewHolder.setpostvideo(String.valueOf(uri));
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), VideoDetail.class);
                        i.putExtra("PostKey", postkey);
                        startActivity(i);

                    }
                });


            }
        };
        videopostlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class VideoPostHolder extends RecyclerView.ViewHolder{
        public View mView;
        private VideoView video;
        public CircleImageView deletevideo;
        public MediaController mediaC;
        public VideoPostHolder(View itemView){
            super(itemView);
            mView = itemView;
            mediaC = new MediaController(mView.getContext());
            video = (VideoView)mView.findViewById(R.id.MyVideolist);
            video.setMediaController(mediaC);
            video.start();

            deletevideo = (CircleImageView)mView.findViewById(R.id.deletevideo);
            deletevideo.setVisibility(View.INVISIBLE);

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



}
