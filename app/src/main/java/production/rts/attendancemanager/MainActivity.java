package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    CardView attendanceBtn, profileBtn, notesBtn, timetableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attendanceBtn = findViewById(R.id.cardView1);
        timetableBtn = findViewById(R.id.cardView2);
        notesBtn = findViewById(R.id.cardView3);
        profileBtn = findViewById(R.id.cardView4);

        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAttendanceActivity = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(gotoAttendanceActivity);
            }
        });

        timetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTTActivity = new Intent(MainActivity.this, TimeTableActivity.class);
                startActivity(gotoTTActivity);
            }
        });

        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNotesActivity = new Intent(MainActivity.this, NotesViewerActivity.class);
                startActivity(gotoNotesActivity);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(gotoProfileActivity);
            }
        });
    }


}
