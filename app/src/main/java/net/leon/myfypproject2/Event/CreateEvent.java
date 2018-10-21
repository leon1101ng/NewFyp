package net.leon.myfypproject2.Event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
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

import net.leon.myfypproject2.FanClub.CreateClub;
import net.leon.myfypproject2.Model.Event;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEvent extends AppCompatActivity {
    private  static final String TAG = "CreateEvent";
    private int PLACE_PICKER_REQUEST = 1;
    private TextInputLayout eventpricely;
    private EditText eventtitle,eventdesc,eventnoofppl,eventprice;
    private TextView datestart,dateend,timestart,timeend,eventlocated;
    private DatePickerDialog.OnDateSetListener datestartSetListener,dateendSetListener;
    private TimePickerDialog timestartpicker,timeendpicker;
    private CircleImageView Backbtn;
    private DatabaseReference EventRef,UserRef;
    private FirebaseAuth mAuth;
    private String CurrentUser,title,desc,noofppl,startd,startt,endd,endt,location,price = "",CurrentDate,CurrentTime,eventposname,paymentV;
    private Button createevent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        datestart = (TextView) findViewById(R.id.DateStart);
        dateend = (TextView)findViewById(R.id.DateEnd);
        timestart = (TextView)findViewById(R.id.TimeStart);
        timeend = (TextView) findViewById(R.id.TimeEnd);
        eventlocated = (TextView)findViewById(R.id.EventLocation);
        Backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        eventtitle = (EditText)findViewById(R.id.EventTitle);
        eventdesc = (EditText)findViewById(R.id.EventDesp);
        eventnoofppl = (EditText)findViewById(R.id.EventNumberOfPeople);
        eventprice = (EditText)findViewById(R.id.EventPrice);
        eventpricely = (TextInputLayout)findViewById(R.id.EventPricely);
        eventprice.setVisibility(View.INVISIBLE);
        eventpricely.setVisibility(View.INVISIBLE);
        CurrentUser = mAuth.getCurrentUser().getUid();
        EventRef = FirebaseDatabase.getInstance().getReference().child("Event");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        createevent = (Button)findViewById(R.id.Create_Event);
        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInput();
            }
        });

        Backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        datestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEvent.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        datestartSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        datestartSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + day);
                String date = day +"/" + month + "/" + year;
                datestart.setText(date);
            }
        };

        dateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEvent.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateendSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

            dateendSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month +1;
                    Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + day);
                    String date = day +"/" + month + "/" + year;
                    dateend.setText(date);
                }
            };

        timestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestartpicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        timestart.setText(hourOfDay + ":" + minutes);
                    }
                },0,0,false);
                timestartpicker.show();
            }
        });

        timeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeendpicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        timeend.setText(hourOfDay + ":" + minutes);
                    }
                },0,0,false);
                timeendpicker.show();
            }
        });
    }

    private void CheckInput() {
        title = eventtitle.getText().toString();
        desc = eventdesc.getText().toString();
        noofppl = eventnoofppl.getText().toString();
        startd = datestart.getText().toString();
        startt = timestart.getText().toString();
        endd = dateend.getText().toString();
        endt = timeend.getText().toString();
        location = eventlocated.getText().toString();
        price = eventprice.getText().toString();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Please Enter Event Title",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(desc)){
            Toast.makeText(this, "Please Enter Event Description",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(noofppl)){
            Toast.makeText(this, "Please Enter Amount of People in the Event ",Toast.LENGTH_SHORT).show();
        }else if(startd.equals("Select Date")){
            Toast.makeText(this, "Please Select Event Start Date",Toast.LENGTH_SHORT).show();
        }else if(startt.equals("Select Time")){
            Toast.makeText(this, "Please Select Event Start Time",Toast.LENGTH_SHORT).show();
        }else if(endd.equals("Select Date")){
            Toast.makeText(this, "Please Select Event End Date",Toast.LENGTH_SHORT).show();
        }else if(endt.equals("Select Time")){
            Toast.makeText(this, "Please Select Event End Time",Toast.LENGTH_SHORT).show();
        }else if(location.equals("Select Location")){
            Toast.makeText(this, "Please Select Event Location",Toast.LENGTH_SHORT).show();
        }else {

            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());

            eventposname = CurrentDate + CurrentTime;
            StoreDataToDB();


        }
    }
    public void onRadioButtonClicked1(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.freeevent:
                if(checked)
                    eventprice.setVisibility(View.INVISIBLE);
                eventpricely.setVisibility(View.INVISIBLE);
                paymentV = "Free";
                break;
            case R.id.paidevent:
                if(checked)
                    eventprice.setVisibility(View.VISIBLE);
                eventpricely.setVisibility(View.VISIBLE);
                paymentV = "Paid";
                break;

        }
    }

    private void StoreDataToDB() {
        UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Username = dataSnapshot.child("username").getValue().toString();
                    HashMap Eventmap = new HashMap();
                    Eventmap.put("Username", Username);
                    Eventmap.put("Event_Title",title);
                    Eventmap.put("Event_Description", desc);
                    Eventmap.put("Event_People", noofppl);
                    Eventmap.put("Event_StartDate",startd);
                    Eventmap.put("Event_StartTime", startt);
                    Eventmap.put("Event_EndDate", endd);
                    Eventmap.put("Event_EndTime",endt);
                    Eventmap.put("Event_Location", location);
                    Eventmap.put("Event_Payment",paymentV);
                    Eventmap.put("Event_Price",price);
                    EventRef.child(CurrentUser + eventposname).updateChildren(Eventmap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CreateEvent.this, "Event Has been Created ", Toast.LENGTH_SHORT).show();
                                BackToeventMenu();
                            } else {
                                String errormessage = task.getException().getMessage();
                                Toast.makeText(CreateEvent.this, "Fail to create the Event" + errormessage, Toast.LENGTH_SHORT).show();

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

    private void BackToeventMenu() {
        Intent I = new Intent(CreateEvent.this, EventMenu.class);
        finish();
        startActivity(I);
    }

    public void goPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(CreateEvent.this),PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e ){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode ==RESULT_OK){
                Place place = PlacePicker.getPlace(CreateEvent.this, data);
                eventlocated.setText(place.getAddress());

            }
        }
    }
}
