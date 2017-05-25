package com.example.vyas.assignment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CalendarPickerController {
    private com.github.tibolte.agendacalendarview.AgendaCalendarView mAgendaCalendarView;
    private List<CalendarEvent> eventList;
    private Calendar minDate;
    private Calendar maxDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAgendaCalendarView=(com.github.tibolte.agendacalendarview.AgendaCalendarView) findViewById(R.id.agenda_calendar_view);

        Initialise(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mockList(List<CalendarEvent> eventList,long l,String eventString) {

        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.add(Calendar.DAY_OF_YEAR, (int) l+1);
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.add(Calendar.DAY_OF_YEAR, (int) l+1);
        endTime3.set(Calendar.MINUTE, 0);
        BaseCalendarEvent event3 = new BaseCalendarEvent(eventString, "A beautiful small town", "Dalvík",
                ContextCompat.getColor(this, R.color.yellow), startTime3, endTime3, true);

       eventList.add(event3);

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);

    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        //ParseException check
        try {
            //Date format
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

            //Fetch current date with given format
            Date mCurrentDate = (Date)formatter.parse(formatter.format(Calendar.getInstance().getTime()));

            //Check today date is less or grater then selected date
            if(mCurrentDate.before(dayItem.getDate()))
            {
                //difference between two dates
                long diff =  dayItem.getDate().getTime()-mCurrentDate.getTime();
                long mDiffernceDays=printDifference(diff);

                //call to create event
                DisplayDialog(eventList,mDiffernceDays);



            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onEventSelected(CalendarEvent event) {

    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }


    public long printDifference(long different){

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;

        return elapsedDays;

    }

    public void Initialise(long l)
    {
         minDate = Calendar.getInstance();
         maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

         eventList = new ArrayList<>();
         mockList(eventList,l,"Example");

    }


    public void DisplayDialog(final List<CalendarEvent> eventList_dio, final long l)
    {
                // Create custom dialog object
                final Dialog dialog = new Dialog(MainActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.event_layout);
                // Set dialog title
                dialog.setTitle(getString(R.string.create_event));

                // set values for custom dialog components - text button
                final EditText text = (EditText) dialog.findViewById(R.id.EdtDialog);

                dialog.show();

                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if(text.getText().toString().length()!=0) {
                            mockList(eventList_dio, l, text.getText().toString());
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, R.string.please_insert_event_title,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Initialise(0);
    }
}
