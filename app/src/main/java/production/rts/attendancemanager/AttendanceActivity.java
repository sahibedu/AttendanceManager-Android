package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class AttendanceActivity extends AppCompatActivity {

    CardView obj1Btn, obj2Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        obj1Btn = findViewById(R.id.cardView1);
        obj2Btn = findViewById(R.id.cardView2);

        obj1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAttendaceViewer = new Intent(AttendanceActivity.this, AttendanceViewer.class);
                startActivity(gotoAttendaceViewer);
            }
        });

    }
}
