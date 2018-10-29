package net.leon.myfypproject2.UserInterface;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.leon.myfypproject2.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserBios extends AppCompatActivity {
    private TextView genderview;
    private EditText AgeET,DisplayNameET,AboutET;
    private FirebaseAuth mAuth;
    private CircleImageView Profile_photo,backbtn;
    private DatabaseReference UserRef, UserProfileRef;
    private StorageReference UserProfileImageRef;
    private String This_User,UserAge,UserGender,AboutUser,UserDisplayName;
    private int User_Follower=0,User_Following = 0;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_bios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        This_User = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserProfileRef = FirebaseDatabase.getInstance().getReference().child("User Profile");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        genderview = (TextView)findViewById(R.id.GenderView);
        AgeET = (EditText)findViewById(R.id.UserAge);
        DisplayNameET = (EditText)findViewById(R.id.DisplayName);
        AboutET = (EditText)findViewById(R.id.About_User);
        Profile_photo = (CircleImageView)findViewById(R.id.profile_photo);
        Profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,Gallery_Pick);
            }
        });
        backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("ProfilePicture")) {
                        String image = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.userprofile).into(Profile_photo);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        UserRef.child(This_User).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("ProfilePicture")){
                        String profileimg = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(profileimg).placeholder(R.drawable.userprofile).into(Profile_photo);
                    }else {
                        FancyToast.makeText(EditUserBios.this,"Profile name do not exists",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        UserProfileRef.child(This_User).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("DisplayName")){
                        String disname = dataSnapshot.child("DisplayName").getValue().toString();
                        DisplayNameET.setText(disname);
                    }if(dataSnapshot.hasChild("Age")){
                        String age = dataSnapshot.child("Age").getValue().toString();
                        AgeET.setText(age);
                    }if(dataSnapshot.hasChild("Gender")){
                        String gender = dataSnapshot.child("Gender").getValue().toString();
                        genderview.setText(gender);
                    }if(dataSnapshot.hasChild("About")){
                        String about = dataSnapshot.child("About").getValue().toString();
                        AboutET.setText(about);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            Uri ImageUrl = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                StorageReference filepath = UserProfileImageRef.child(This_User + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditUserBios.this, "Your Profile Picture Has Been Created", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            UserRef.child(This_User).child("ProfilePicture").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(EditUserBios.this,"Profile Image is Saved",Toast.LENGTH_SHORT).show();
                                            }else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(EditUserBios.this,"Failed To Save : "+message,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else{
                Toast.makeText(this,"Fail to Edit Picture",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radioButton_Male:
                if(checked)
                    genderview.setText("Male");
                break;
            case R.id.radioButton_Female:
                if(checked)
                    genderview.setText("Female");
                break;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editpofile, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Done_Editbios) {
            ValidateInput();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ValidateInput() {
        UserDisplayName = DisplayNameET.getText().toString();
        UserAge = AgeET.getText().toString();
        UserGender = genderview.getText().toString();
        AboutUser = AboutET.getText().toString();


        if(TextUtils.isEmpty(UserDisplayName)){
            FancyToast.makeText(this,"Please Enter Display Name ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        } else if (TextUtils.isEmpty(UserAge) ) {
            FancyToast.makeText(this, "Please Enter Your Age ", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        }else if (TextUtils.isEmpty(UserGender) ){
            FancyToast.makeText(this,"Please Select Your Gender ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else if (TextUtils.isEmpty(AboutUser) ){
            FancyToast.makeText(this,"Please Enter Your Details ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else {
            UpdateUserProfile();

        }
    }

    private void UpdateUserProfile() {
        UserRef.child(This_User).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String usercountry = dataSnapshot.child("Country").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    HashMap ImagepostsMap = new HashMap();
                    ImagepostsMap.put("DisplayName", UserDisplayName);
                    ImagepostsMap.put("Age", UserAge);
                    ImagepostsMap.put("Gender", UserGender);
                    ImagepostsMap.put("About", AboutUser);
                    ImagepostsMap.put("Country", usercountry);
                    ImagepostsMap.put("Username", username);
                    ImagepostsMap.put("Follower", User_Follower);
                    ImagepostsMap.put("Following", User_Following);
                    UserProfileRef.child(This_User).updateChildren(ImagepostsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        FancyToast.makeText(EditUserBios.this,"Edit User Profile has Been Done ",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                        BackUserInterface();
                                    }else {
                                        String message = task.getException().getMessage();
                                        FancyToast.makeText(EditUserBios.this,"Failed To Edit Profile " + message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
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

    private void BackUserInterface() {
        Intent i = new Intent(EditUserBios.this, UserInterface.class);
        startActivity(i);
        finish();
    }
}
