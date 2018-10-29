package net.leon.myfypproject2.UserInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.ProfileFragment.BioFragment;
import net.leon.myfypproject2.ProfileFragment.ImagePostFragment;
import net.leon.myfypproject2.ProfileFragment.PostFragment;
import net.leon.myfypproject2.ProfileFragment.VideoFragment;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserAccount.Login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Imagepost extends AppCompatActivity {
    private CircleImageView HomeBtn;
    private ImageView selectimage;
    private EditText imagecaption;
    final static int Gallery_Pick = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private Uri uriImage;
    private StorageReference ImagesPost;
    private String CurrentDate,CurrentTime,Postrandomname,blank;
    private String Downloadlink;
    private DatabaseReference UsersRef, ImagePostRef;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private String imagecap;
    private int PLACE_PICKER_REQUEST = 2;
    private String location;
    private ImageView takepicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImagesPost = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ImagePostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts");


        mTitle.setText("Add Image Post");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        selectimage = (ImageView)findViewById(R.id.Select_image);
        imagecaption = (EditText)findViewById(R.id.image_caption);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoGallery();
            }
        });
        HomeBtn = (CircleImageView)findViewById(R.id.ImagePOSTHome);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.Imagepost_nav);
        bnv.setOnNavigationItemSelectedListener(navlistener);
        takepicture = (ImageView)findViewById(R.id.takepicture);
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
    }

    private void PhotoGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,Gallery_Pick);
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
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
                case R.id.SubmitImagePost:
                    ValidationImagePost();
                    break;


            }

            return true;
        }

    };

    private void PickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(Imagepost.this),PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e ){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }


    private void ValidationImagePost() {
        imagecap = imagecaption.getText().toString();

        if(uriImage == null){
            FancyToast.makeText(this,"Please Select Your Image.",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }else if(TextUtils.isEmpty(imagecap)){
            FancyToast.makeText(this,"Please Enter Your Image Caption",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }else{
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            StorageReference filepath = ImagesPost.child("Image Post").child(uriImage.getLastPathSegment() + Postrandomname + ".jpg");

            filepath.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Downloadlink = task.getResult().getDownloadUrl().toString();
                        FancyToast.makeText(Imagepost.this,"Image Post Has Been Uploaded...", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        SavingImagePostInfo();
                    }else {
                        String message = task.getException().getMessage();
                        FancyToast.makeText(Imagepost.this,"Failed To Upload Image Post"+ message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                }
            });


        }
    }

    private void SavingImagePostInfo() {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userfullname = dataSnapshot.child("Fullname").getValue().toString();
                    String userprofilepicture = dataSnapshot.child("ProfilePicture").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    HashMap ImagepostsMap = new HashMap();
                    ImagepostsMap.put("UserID", current_user_id);
                    ImagepostsMap.put("Date", CurrentDate);
                    ImagepostsMap.put("Time", CurrentTime);
                    ImagepostsMap.put("ImageCaption", imagecap);
                    ImagepostsMap.put("ImageUrl", Downloadlink);
                    ImagepostsMap.put("PostImage", userprofilepicture);
                    ImagepostsMap.put("Fullname", userfullname);
                    ImagepostsMap.put("Username", username);
                    ImagepostsMap.put("ImagePostLocation", location);
                    ImagePostRef.child("I" + Postrandomname).updateChildren(ImagepostsMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(Imagepost.this,"Image Post Has Been Uploaded...", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                BackToHome();
                            }else {
                                String message = task.getException().getMessage();
                                FancyToast.makeText(Imagepost.this,"Failed To Upload Image Post"+ message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
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
        Intent backtohome = new Intent(Imagepost.this, UserInterface.class);
        backtohome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backtohome);
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode ==RESULT_OK){
                Place place = PlacePicker.getPlace(Imagepost.this, data);
                location = "" + (place.getAddress());

            }
        }

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            uriImage = data.getData();
            selectimage.setImageURI(uriImage);
        }


        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                selectimage.setImageBitmap(imageBitmap);
            }
        }


    }
}
