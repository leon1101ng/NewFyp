package net.leon.myfypproject2.UserInterface;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import net.leon.myfypproject2.Event.CreateEvent;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusPost extends AppCompatActivity {
    private CircleImageView HomeBtn,poststatus;
    private EditText Status;
    private TextView TagView,LocationView;
    private TextView ViewStatusLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private DatabaseReference UserStatusPostRef, UserRef;
    private FirebaseAuth mAuth;
    private String CurrentUser,CurrentDate, CurrentTime, Postrandomname,Statustext,Tagview,Locationview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add New Status");
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        UserStatusPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Status_Posts");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        HomeBtn = (CircleImageView)findViewById(R.id.ImagePOSTHome);
        poststatus = (CircleImageView)findViewById(R.id.PostStatus);
        Status = (EditText)findViewById(R.id.Status_Text);
        TagView = (TextView)findViewById(R.id.StatusTagView) ;
        LocationView = (TextView)findViewById(R.id.StatusLocationView);
        poststatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ViewStatusLocation = (TextView)findViewById(R.id.StatusLocationView);
    }

    private void CheckValidation() {
        Statustext = Status.getText().toString();
        Tagview = TagView.getText().toString();
        Locationview = LocationView.getText().toString();
        if(TextUtils.isEmpty(Statustext)){
            Toast.makeText(this,"Please Text To proceed Status Post", Toast.LENGTH_SHORT).show();
        }else
        {
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            StoreDataToDB();
        }
    }

    private void StoreDataToDB() {
        UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userfullname = dataSnapshot.child("Fullname").getValue().toString();
                String userprofilepicture = dataSnapshot.child("ProfilePicture").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                HashMap Statuspost = new HashMap();
                Statuspost.put("Status", Statustext);
                Statuspost.put("TagStatusView", Tagview);
                Statuspost.put("LocationStatusView", Locationview);
                Statuspost.put("Fullname", userfullname);
                Statuspost.put("ProfilePicture", userprofilepicture);
                Statuspost.put("UserID", CurrentUser);
                Statuspost.put("Username", username);
                UserStatusPostRef.child("S"  + Postrandomname).updateChildren(Statuspost)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(StatusPost.this,"Image Post Has Been Uploaded... ", Toast.LENGTH_SHORT).show();
                                    BackToHome();
                                }else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(StatusPost.this,"Failed To Upload Image Post " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void BackToHome() {
        Intent backtohome = new Intent(StatusPost.this, UserInterface.class);
        backtohome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backtohome);
        finish();
    }

    public void PlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(StatusPost.this),PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e ){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode ==RESULT_OK){
                Place place = PlacePicker.getPlace(StatusPost.this, data);
                ViewStatusLocation.setText(place.getAddress());

            }
        }
    }


}
