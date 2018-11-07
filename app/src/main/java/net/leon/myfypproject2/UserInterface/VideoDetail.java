package net.leon.myfypproject2.UserInterface;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.R;

public class VideoDetail extends AppCompatActivity {
    private String postKey;
    private DatabaseReference VideoPostRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private VideoView VideoDetail;
    private MediaController mediac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detail);


        postKey = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        VideoPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Video_Posts");
        VideoDetail = (VideoView)findViewById(R.id.VideoDetail);
        mediac = new MediaController(this);

        VideoPostRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String video = dataSnapshot.child("VideoUrl").getValue().toString();
                    Uri uri = Uri.parse(video);
                    VideoDetail.setVideoURI(uri);
                    VideoDetail.setMediaController(mediac);
                    VideoDetail.start();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
