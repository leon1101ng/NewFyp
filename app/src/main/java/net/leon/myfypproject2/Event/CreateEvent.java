package net.leon.myfypproject2.Event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.leon.myfypproject2.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEvent extends AppCompatActivity {
    private  static final String TAG = "CreateEvent";
    private int PLACE_PICKER_REQUEST = 1;
    private EditText eventtitle,eventdesc,eventnoofppl;
    private TextView datestart,dateend,timestart,timeend,eventlocated;
    private DatePickerDialog.OnDateSetListener datestartSetListener,dateendSetListener;
    private TimePickerDialog timestartpicker,timeendpicker;
    private CircleImageView Backbtn;

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
