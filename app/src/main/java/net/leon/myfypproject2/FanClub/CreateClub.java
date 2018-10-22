package net.leon.myfypproject2.FanClub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateClub extends AppCompatActivity {
    private ImageButton clubpicture;
    private EditText clubname,clubtopic;
    private CircleImageView backbtn;
    private Button createclub;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, ClubRef;
    private StorageReference ClubPictureRef;
    private String Current_user,club_picture,club_name,club_topic,CurrentDate,CurrentTime,Postrandomname,Downloadlink;
    private ProgressDialog Create_Fan_Club;
    final static int Gallery_Pick = 1;
    private Uri UriClubImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        ClubPictureRef = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ClubRef = FirebaseDatabase.getInstance().getReference().child("Fan Club");

        backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        clubpicture = (ImageButton)findViewById(R.id.ClubPicture);
        clubpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        clubname = (EditText)findViewById(R.id.Club_Name);
        clubtopic = (EditText)findViewById(R.id.ClubTopic);
        Create_Fan_Club = new ProgressDialog(this);

        createclub = (Button)findViewById(R.id.Create_Club);
        createclub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateCreationInformation();
            }
        });


    }

    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode ==RESULT_OK && data!=null){
            UriClubImage = data.getData();
            clubpicture.setImageURI(UriClubImage);
        }
    }

    private void ValidateCreationInformation() {
        club_name = clubname.getText().toString();
        club_topic = clubtopic.getText().toString();

        if(UriClubImage == null){
            Toast.makeText(this , "Please Select Your Club Pictures !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(club_name)){
            Toast.makeText(this, "Please Enter the Club Name !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(club_topic)){
            Toast.makeText(this, "Please Enter the Club topic!", Toast.LENGTH_SHORT).show();

        } else {

            Create_Fan_Club.setTitle("Creating Fan Club");
            Create_Fan_Club.setMessage("Please Wait!");
            Create_Fan_Club.show();
            Create_Fan_Club.setCanceledOnTouchOutside(true);
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            StorageReference filepath = ClubPictureRef.child("Club Picture").child(UriClubImage.getLastPathSegment() + Postrandomname + ".jpg");

            filepath.putFile(UriClubImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Downloadlink = task.getResult().getDownloadUrl().toString();
                        Toast.makeText(CreateClub.this,"Products Has Been Uploaded...", Toast.LENGTH_SHORT).show();
                        StoreFanClubData();
                        Create_Fan_Club.dismiss();

                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(CreateClub.this,"Failed To Upload Products"+ message, Toast.LENGTH_SHORT).show();
                        Create_Fan_Club.dismiss();

                    }
                }
            });
        }
    }

    private void StoreFanClubData() {
        UserRef.child(Current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Username = dataSnapshot.child("username").getValue().toString();
                HashMap fanclubdb = new HashMap();
                fanclubdb.put("ClubPicture", Downloadlink);
                fanclubdb.put("ClubName", club_name);
                fanclubdb.put("ClubTopic", club_topic);
                fanclubdb.put("ClubCreator", Username);
                fanclubdb.put("UserID", Current_user);
                ClubRef.child( "C"+Postrandomname).updateChildren(fanclubdb)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(CreateClub.this, "Fan Club Has been Created ", Toast.LENGTH_SHORT).show();
                                    BackToClubMenu();
                                } else {
                                    String errormessage = task.getException().getMessage();
                                    Toast.makeText(CreateClub.this, "Fail to create the Fan club" + errormessage, Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void BackToClubMenu() {
        Intent backtohome = new Intent(CreateClub.this, FanClubMainMenu.class);
        backtohome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backtohome);
        finish();
    }
}
