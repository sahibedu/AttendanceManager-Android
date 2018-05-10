package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    CardView attendanceBtn, profileBtn, notesBtn, timetableBtn,attendanceViewBtn;
    TextView verificationMsg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attendanceBtn = findViewById(R.id.cardView1);
        timetableBtn = findViewById(R.id.cardView2);
        notesBtn = findViewById(R.id.cardView3);
        profileBtn = findViewById(R.id.cardView4);
        attendanceViewBtn = findViewById(R.id.cardView5);
        verificationMsg = findViewById(R.id.verificationMsg);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser().isEmailVerified()){
            verificationMsg.setVisibility(View.GONE);
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
    }


}
