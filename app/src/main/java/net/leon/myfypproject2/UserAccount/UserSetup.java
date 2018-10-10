package net.leon.myfypproject2.UserAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSetup extends AppCompatActivity {

    private EditText setupusernama,setupname,setupcountry;
    private Button setupsubmit;
    private CircleImageView userprofile;
    private ProgressDialog loadingprofile;

    private FirebaseAuth mAuth;
    private DatabaseReference userref;
    private StorageReference UserImage;
    String currentUserID;
    final static int Gallery_Pick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);
        loadingprofile = new ProgressDialog(this);
        setupusernama = (EditText)findViewById(R.id.Setupusername);
        setupname = (EditText)findViewById(R.id.Setupname);
        setupcountry = (EditText)findViewById(R.id.Setupcountry);
        userprofile = (CircleImageView)findViewById(R.id.Userprofile);
        mAuth = FirebaseAuth.getInstance();
        UserImage = FirebaseStorage.getInstance().getReference().child("Profile Images");
        currentUserID = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        setupsubmit = (Button)findViewById(R.id.Setupsubmit);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image = dataSnapshot.child("ProfilePicture").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.userprofile).into(userprofile);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setupsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserDetail();
            }
        });

        userprofile = (CircleImageView)findViewById(R.id.Userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,Gallery_Pick);
            }
        });
        userref.addValueEventListener(new ValueEventListener() {
        @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("ProfilePicture")) {
                        String image = dataSnapshot.child("ProfilePicture").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.userprofile).into(userprofile);
                    }else {
                        Toast.makeText(UserSetup.this,"Please select profile picture first",Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
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
                StorageReference filepath = UserImage.child(currentUserID + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserSetup.this, "Your Profile Picture Has Been Created", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            userref.child("ProfilePicture").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(UserSetup.this,"Profile Image is Saved",Toast.LENGTH_SHORT).show();
                                            }else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(UserSetup.this,"Failed To Save : "+message,Toast.LENGTH_SHORT).show();
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

    private void SaveUserDetail() {
        int vip = 0;
        String username = setupusernama.getText().toString();
        String name = setupname.getText().toString();
        String country = setupcountry.getText().toString();
        int Balance = 0;

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please Enter Your Username",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(country)){
            Toast.makeText(this,"Please Enter Your Country",Toast.LENGTH_SHORT).show();
        }else {
            loadingprofile.setTitle("Creating User Profile");
            loadingprofile.setMessage("Please Wait!");
            loadingprofile.show();
            loadingprofile.setCanceledOnTouchOutside(true);
            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("Fullname", name);
            userMap.put("Country", country);
            userMap.put("InAppCredit", Balance);
            userMap.put("Vip", vip);
            userref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        toMainMenus();
                        Toast.makeText(UserSetup.this, "Your Account is created Successfully", Toast.LENGTH_SHORT).show();
                        loadingprofile.dismiss();

                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(UserSetup.this,"Failed to Create"+message,Toast.LENGTH_SHORT).show();
                        loadingprofile.dismiss();
                    }
                }
            });

        }
    }

    private void toMainMenus() {
        Intent SetupUser = new Intent(UserSetup.this, MainActivity.class);
        SetupUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupUser);
        finish();

    }

}
