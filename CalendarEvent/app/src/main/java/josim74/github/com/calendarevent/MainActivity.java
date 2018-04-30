package josim74.github.com.calendarevent;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    CompactCalendarView compactCalendarView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);


        String str_date="13-09-2018";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //set an event for teacher professional day
        //final Event event1 = new Event(Color.GREEN, 1540252800000L, "Teacher's Professional Day");
        final Event event1 = new Event(Color.BLUE, date.getTime(), "Teacher's Professional Day");

        compactCalendarView.addEvent(event1);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                if (dateClicked.toString().compareTo("Sun Oct 21 09:00:00 GMT+06:00 2018") == 0) {
                    Toast.makeText(context, "Today is "+event1.getData(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No Event Planned for that day. "+dateClicked.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                actionBar.setTitle(simpleDateFormat.format(firstDayOfNewMonth));
            }
        });


    }
}
