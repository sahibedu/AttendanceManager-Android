package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView nameText,greetingText;
    CardView attendanceBtn, profileBtn, notesBtn, timetableBtn,attendanceViewBtn,discussionBtn;
    TextView verificationMsg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attendanceBtn = findViewById(R.id.cardView1);
        timetableBtn = findViewById(R.id.cardView2);
        notesBtn = findViewById(R.id.cardView3);
        discussionBtn = findViewById(R.id.cardView4);
        attendanceViewBtn = findViewById(R.id.cardView5);
        profileBtn = findViewById(R.id.cardView6);
        verificationMsg = findViewById(R.id.verificationMsg);
        nameText = findViewById(R.id.tv_name_header_main);
        greetingText = findViewById(R.id.tv_greeting_main);

        mAuth = FirebaseAuth.getInstance();

        nameText.setText(mAuth.getCurrentUser().getDisplayName());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH", Locale.US);
        String greetingMsg;
        if(Integer.parseInt(dateFormat.format(date)) < 12) greetingMsg = "Good Morning";
        else if(Integer.parseInt(dateFormat.format(date)) < 16) greetingMsg = "Good Afternoon";
        else if(Integer.parseInt(dateFormat.format(date)) < 20) greetingMsg = "Good Evening";
        else greetingMsg = "Good Night";
        greetingText.setText(greetingMsg);

        if(mAuth.getCurrentUser().isEmailVerified()){
            verificationMsg.setVisibility(View.INVISIBLE);
        }

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

        attendanceViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAttendanceViewer = new Intent(MainActivity.this,ViewClass.class);
                startActivity(gotoAttendanceViewer);
            }
        });

        discussionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Create Notification Activity
                Toast.makeText(MainActivity.this, "To Be Implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
