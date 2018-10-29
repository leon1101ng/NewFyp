package net.leon.myfypproject2.LiveStream;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.Comment.CameraComment;
import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;

import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity extends AppCompatActivity {
    private static final String APPLICATION_ID = "DWIdt8ySJaT8OSdKV3DUdA";
    private FirebaseAuth mAuth;
    private DatabaseReference LiveStreamRef,UserRef,LiveStreamRef1,LiveStreamCommentref;
    private String Current_User,postKey;
    private CircleImageView LiveComment;
    private TextView Closelive;
    private String postKey1,liveID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        postKey = getIntent().getExtras().get("LiveTitle").toString();
        mPreviewSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcastButton = (Button)findViewById(R.id.BroadcastButton);
        mAuth = FirebaseAuth.getInstance();
        Current_User = mAuth.getCurrentUser().getUid();
        LiveStreamRef = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(Current_User);
        LiveStreamRef1 = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(postKey);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        LiveComment = (CircleImageView)findViewById(R.id.LiveComment1);
        LiveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveStreamRef.addValueEventListener(new ValueEventListener() {
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
        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting())
                    mBroadcaster.startBroadcast();

                else
                    mBroadcaster.stopBroadcast();
            }
        });
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        Closelive = findViewById(R.id.CloseLiveTV);
        Closelive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveStreamCommentref = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(liveID).child("comment");
                Intent i = new Intent(CameraActivity.this, MainActivity.class);
                LiveStreamCommentref.removeValue();

                startActivity(i);
            }
        });
    }

    private void OpenCommentFrag(String liveID) {
        Fragment selectedfragment = new Fragment();
        selectedfragment = new CameraComment();
        Bundle bundle = new Bundle();
        bundle.putString("liveID", liveID);
        selectedfragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.RLCameraCm, selectedfragment).addToBackStack(null).commit();
    }

    private void storeData() {
        UserRef.child(Current_User).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userfullname = dataSnapshot.child("Fullname").getValue().toString();
                    String userprofilepicture = dataSnapshot.child("ProfilePicture").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    String Status = "Streaming";
                    Random rand = new Random();


                    HashMap livestream = new HashMap();
                    livestream.put("PostImage", userprofilepicture);
                    livestream.put("Fullname", userfullname);
                    livestream.put("Username", username);
                    livestream.put("Status", Status);
                    livestream.put("LiveTitle", postKey);
                    livestream.put("LiveID", Current_User);
                    LiveStreamRef.updateChildren(livestream)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){

                                        FancyToast.makeText(CameraActivity.this,"Start Streaming ", FancyToast.LENGTH_LONG,FancyToast.CONFUSING,R.drawable.ic_stream).show();
                                    }else {
                                        String message = task.getException().getMessage();
                                        FancyToast.makeText(CameraActivity.this,"Failed To Start Streaming " + message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                                    }
                                }
                            });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    SurfaceView mPreviewSurface;
    Button mBroadcastButton;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }

    Broadcaster mBroadcaster;

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            if (broadcastStatus == BroadcastStatus.STARTING){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                storeData();}
            if (broadcastStatus == BroadcastStatus.IDLE){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                ChangeStatus();
            }

            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Stop Live");
            Log.i("myfypproject2", "Received status change: " + broadcastStatus);
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("myfypproject2", "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {

        }
        @Override
        public void onChatMessage(String s) {
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
        }
        @Override
        public void onBroadcastIdAvailable(String s) {
        }
    };

    private void ChangeStatus() {

        HashMap livestream = new HashMap();
        String Status = "End Streaming";
        livestream.put("Status", Status);
        LiveStreamRef.updateChildren(livestream)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            FancyToast.makeText(CameraActivity.this,"End Streaming ", FancyToast.LENGTH_LONG,FancyToast.CONFUSING,R.drawable.ic_stream).show();

                        }else {
                            String message = task.getException().getMessage();
                            FancyToast.makeText(CameraActivity.this,"Failed To End Streaming " + message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });
    }

    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);

        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
