package net.leon.myfypproject2.CelebritySchedule;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivitySchedule extends AppCompatActivity {
    private String datekey,CurrentDate,CurrentTime, CurrentUser,pname,ptime,pdate, username, userimage;
    private TextView plandate,plantime;
    private Button createplan;
    private EditText planname;
    private TimePickerDialog timestartpicker;
    private int countPosts = 0;
    private DatabaseReference CelebrityScheduleRef, UserRef;
    private FirebaseAuth mAuth;
    private CircleImageView backtoMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        datekey = getIntent().getExtras().get("date").toString();
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        CelebrityScheduleRef = FirebaseDatabase.getInstance().getReference().child("CelebritySchedule");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUser);
        backtoMenu = (CircleImageView)findViewById(R.id.BackToMenu);
        backtoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        plandate = (TextView)findViewById(R.id.PlanDate);
        plandate.setText(datekey);
        plantime = (TextView)findViewById(R.id.PlanTime);
        planname = (EditText)findViewById(R.id.PlanName);
        createplan = (Button)findViewById(R.id.CreatePlan);

        Calendar caldate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-EEEE");
        CurrentDate = currentdate.format(caldate.getTime());

        Calendar caltime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = currenttime.format(caltime.getTime());

        plantime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestartpicker = new TimePickerDialog(AddActivitySchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay >= 0 && hourOfDay < 12){
                            String picktime = hourOfDay + ":" + minutes + " Morning";
                            plantime.setText(picktime);
                        }else if(hourOfDay >= 12 && hourOfDay < 16){
                            String picktime = hourOfDay + ":" + minutes + " Afternoon";
                            plantime.setText(picktime);
                        }else if(hourOfDay >= 16 && hourOfDay < 21){
                            String picktime = hourOfDay + ":" + minutes + " Evening";
                            plantime.setText(picktime);
                        }else if(hourOfDay >= 21 && hourOfDay < 24){
                            String picktime = hourOfDay + ":" + minutes + " Night";
                            plantime.setText(picktime);
                        }
                    }
                },0,0,false);
                timestartpicker.show();
            }
        });

        createplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPlan();
            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username = dataSnapshot.child("username").getValue().toString();
                    userimage = dataSnapshot.child("ProfilePicture").getValue().toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void CheckPlan() {
        ptime = plantime.getText().toString();
        pname = planname.getText().toString();
        pdate = plandate.getText().toString();


        if(TextUtils.isEmpty(pname)){
            FancyToast.makeText(AddActivitySchedule.this,"Please Enter Plan Name ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else if(TextUtils.isEmpty(ptime)){
            FancyToast.makeText(AddActivitySchedule.this,"Please Choose The Plan Time ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else if(ptime.equals(CurrentTime)){
            FancyToast.makeText(AddActivitySchedule.this,"The Current Time Cannot Be Plan Time... Please Reenter Plan Time ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else if(ptime.equals("SELECT TIME")){
            FancyToast.makeText(AddActivitySchedule.this,"Please Choose The Plan Time ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }else {
            StoreData();

        }


    }

    private void StoreData() {
        CelebrityScheduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    countPosts = (int) dataSnapshot.getChildrenCount();
                }else {
                    countPosts = 0;
                }

                final String RandomKey =   CurrentTime + CurrentDate ;

                HashMap scheduleMap = new HashMap();
                scheduleMap.put("Uid", CurrentUser);
                scheduleMap.put("PlanName", pname);
                scheduleMap.put("Date", pdate);
                scheduleMap.put("Time", ptime);
                scheduleMap.put("counter", countPosts);
                scheduleMap.put("Username",username);
                scheduleMap.put("UserImage",userimage);
                CelebrityScheduleRef.child(RandomKey).updateChildren(scheduleMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    FancyToast.makeText(AddActivitySchedule.this,"Plan Has Been Created !", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                    finish();

                                } else {

                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
