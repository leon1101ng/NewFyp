package net.leon.myfypproject2.LiveStream;

import android.content.Intent;
import android.graphics.Camera;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Comment.LiveComment;
import net.leon.myfypproject2.Function.ViewImage;
import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayStreamVideo extends AppCompatActivity {

    private static final String APPLICATION_ID = "DWIdt8ySJaT8OSdKV3DUdA";
    private static final String API_KEY = "8cyhigsjrj0m6hl9tzxguib2r";
    private String LiveChannelID,liveID;
    private DatabaseReference LiveStreamref,LiveStreamCommentref;





    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            if (mPlayerStatusTextView != null)

                    mPlayerStatusTextView.setText("Status: " + playerState);



            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(PlayStreamVideo.this);
                    mMediaController.setAnchorView(mVideoSurface);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    mMediaController.show();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();



                }
                mMediaController = null;
            }
        }




        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_stream_video);
        postKey1 = getIntent().getExtras().get("PostKey").toString();
        LiveStreamref = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(postKey1);
        LiveComment = (CircleImageView)findViewById(R.id.LiveComment);
        LiveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveStreamref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("LiveID")) {
                                liveID = dataSnapshot.child("LiveID").getValue().toString();
                                OpenCommentFrag(liveID);
                            }}


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });








        mVideoSurface = (SurfaceView) findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView = (TextView) findViewById(R.id.PlayerStatusTextView);
        Closelive = (TextView) findViewById(R.id.CloseLiveTV);
        Closelive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveStreamCommentref = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(liveID).child("comment");
                Intent i = new Intent(PlayStreamVideo.this, MainActivity.class);
                startActivity(i);
                LiveStreamCommentref.removeValue();
                finish();
            }
        });








    }


    private void OpenCommentFrag(String liveID) {


        Fragment selectedfragment = new Fragment();
        selectedfragment = new LiveComment();
        Bundle bundle = new Bundle();
        bundle.putString("liveID", liveID);
        selectedfragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.RLStreamCm, selectedfragment).addToBackStack(null).commit();
    }

    SurfaceView mVideoSurface;
    TextView mPlayerStatusTextView;
    TextView Closelive;
    CircleImageView LiveComment;
    String postKey1;
    String post;



    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;

        if (mBroadcastPlayer != null && mBroadcastPlayer.isTypeLive())
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;

        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }


    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP && mBroadcastPlayer != null && mMediaController != null) {
            PlayerState state = mBroadcastPlayer.getState();
            if (state == PlayerState.PLAYING ||
                    state == PlayerState.BUFFERING ||
                    state == PlayerState.PAUSED ||
                    state == PlayerState.COMPLETED) {
                if (mMediaController.isShowing())
                    mMediaController.hide();
                else
                    mMediaController.show();
            } else {
                mMediaController.hide();
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = (SurfaceView) findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading Live Streaming");
        getLatestResourceUri();
    }

    void getLatestResourceUri() {

        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null) {
                        mPlayerStatusTextView.setText("Http exception: " + e);
                    }
                }});
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {}
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null && mBroadcastPlayer.isTypeLive())
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();


    }
    // ...
    final OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;
    MediaController mMediaController = null;

}
