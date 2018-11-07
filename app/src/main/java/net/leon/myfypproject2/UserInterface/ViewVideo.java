package net.leon.myfypproject2.UserInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.Model.Video;
import net.leon.myfypproject2.ProfileFragment.VideoFragment;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewVideo extends AppCompatActivity {
    private RecyclerView videopostlist;
    private DatabaseReference VideoPostRef;
    private FirebaseAuth mAuth;
    private String current_UserID, postKey;
    private CircleImageView backtomenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        postKey = getIntent().getExtras().get("1").toString();
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        VideoPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Video_Posts");
        videopostlist = (RecyclerView)findViewById(R.id.MyVideoView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        videopostlist.setLayoutManager(linearLayoutManager);
        backtomenu = (CircleImageView)findViewById(R.id.BackToMenu);
        backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        Query Videopost = VideoPostRef.orderByChild("UserID").startAt(current_UserID).endAt(current_UserID + "\uf8ff");
        FirebaseRecyclerAdapter<Video,VideoFragment.VideoPostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, VideoFragment.VideoPostHolder>(
                Video.class,
                R.layout.allvideopost,
                VideoFragment.VideoPostHolder.class,
                Videopost
        ) {
            @Override
            protected void populateViewHolder(VideoFragment.VideoPostHolder viewHolder, Video model, int position) {
                final String postkey = getRef(position).getKey();
                Uri uri = Uri.parse(model.getVideoUrl());
                viewHolder.setpostvideo(String.valueOf(uri));

                if(postKey.equals("1")){
                    viewHolder.deletevideo.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(ViewVideo.this, VideoDetail.class);
                            i.putExtra("PostKey", postkey);
                            startActivity(i);

                        }
                    });

                }

                viewHolder.deletevideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(ViewVideo.this,android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ViewVideo.this);
                        }

                        builder.setTitle("Delete Video")
                                .setMessage("Do you want delete this video ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        VideoPostRef.child(postkey).removeValue();
                                        FancyToast.makeText(ViewVideo.this,"Video Has Been Delete",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                    }
                                })
                                .show();


                    }
                });

            }
        };
        videopostlist.setAdapter(firebaseRecyclerAdapter);
    }
}
