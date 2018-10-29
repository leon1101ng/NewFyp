package net.leon.myfypproject2.UserInterface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPost extends AppCompatActivity {
    private CircleImageView HomeBtn;
    final static int REQUEST_TAKE_GALLERY_VIDEO = 100;
    final static int REQUEST_VIDEO_CAPTURE = 1;
    String selectedVideoPath;
    private Uri VideoURI;
    private VideoView videoView;
    private StorageReference VideoPost;
    private DatabaseReference VideoPostRef, UserRef;
    private FirebaseAuth mAuth;
    private String Current_User,location, videocap, CurrentDate,CurrentTime,Postrandomname,Downloadlink;
    private EditText videocaption;
    private int PLACE_PICKER_REQUEST = 2;
    private ProgressDialog loadingupload;
    private ImageView opencamera,opencameragalley;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Video");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView vbnv = (BottomNavigationView) findViewById(R.id.Videopost_Nav);
        vbnv.setOnNavigationItemSelectedListener(navlistener);
        loadingupload = new ProgressDialog(this);
        opencamera = (ImageView)findViewById(R.id.opencamera);
        opencameragalley = (ImageView)findViewById(R.id.opencameragalley);
        opencameragalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoGallery();

            }
        });
        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
        videoView = (VideoView)findViewById(R.id.AddVideo);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        videocaption = (EditText) findViewById(R.id.video_caption);
        HomeBtn = (CircleImageView)findViewById(R.id.ImagePOSTHome);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        Current_User = mAuth.getCurrentUser().getUid();
        VideoPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Video_Posts");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        VideoPost = FirebaseStorage.getInstance().getReference();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.Tag_People:

                    break;
                case R.id.Add_location:
                    PickLocation();

                    break;
                case R.id.Submit_VideoPost:
                    ValidationVideoPost();
                    break;


            }

            return true;
        }

    };

    private void ValidationVideoPost() {
        videocap = videocaption.getText().toString();
        if(VideoURI == null){
            FancyToast.makeText(this,"Please Select Your Video To Upload.",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
        }else if(TextUtils.isEmpty(videocap)){
            FancyToast.makeText(this,"Please Enter Your Video Caption",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
        }else {
            loadingupload.setTitle("Posting Video");
            loadingupload.setMessage("Please Wait!");
            loadingupload.show();
            loadingupload.setCanceledOnTouchOutside(true);

            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            StorageReference filepath= VideoPost.child("Video Post").child(VideoURI.getLastPathSegment() + Postrandomname + ".mp4");
            UploadTask uploadTask =filepath.putFile(VideoURI);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Downloadlink = task.getResult().getDownloadUrl().toString();
                        FancyToast.makeText(VideoPost.this,"Video Has Been Uploaded...",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        SavingVideoPostInfo();
                    }else {
                        String message = task.getException().getMessage();
                        FancyToast.makeText(VideoPost.this,"Failed To Upload Video "+ message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                    }
                }
            });
        }
    }

    private void SavingVideoPostInfo() {

        UserRef.child(Current_User).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Username = dataSnapshot.child("username").getValue().toString();
                    String userprofilepicture = dataSnapshot.child("ProfilePicture").getValue().toString();
                    String username = dataSnapshot.child("Fullname").getValue().toString();
                    HashMap VideoPost = new HashMap();
                    VideoPost.put("UserID", Current_User);
                    VideoPost.put("Username", Username);
                    VideoPost.put("UserPicture", userprofilepicture);
                    VideoPost.put("UserFullname", username);
                    VideoPost.put("VideoUrl", Downloadlink);
                    VideoPost.put("VideoCaption", videocap);
                    VideoPost.put("VideoLocation", location);
                    VideoPostRef.child("V" + Postrandomname).updateChildren(VideoPost).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(VideoPost.this,"Video Has Been Uploaded... ",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                loadingupload.dismiss();
                                BackToHome();
                            }else {
                                String message = task.getException().getMessage();
                                FancyToast.makeText(VideoPost.this,"Failed To Upload Video "+ message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                loadingupload.dismiss();
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

    private void BackToHome() {
        Intent backtohome = new Intent(VideoPost.this, UserInterface.class);
        backtohome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backtohome);
        finish();
    }

    private void PickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(VideoPost.this),PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e ){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    public void VideoGallery(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO  && resultCode == RESULT_OK && data != null) {
            VideoURI= data.getData();
            videoView.setVideoURI(VideoURI);
            videoView.start();
        }

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(VideoPost.this, data);
                location = "" + (place.getAddress());

            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }


    }


}
