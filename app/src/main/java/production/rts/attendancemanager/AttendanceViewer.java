package production.rts.attendancemanager;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class AttendanceViewer extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ListView listview;
    TextView datePickerText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final ArrayList<String> rollNoList = new ArrayList<>();
    private FirebaseAuth mAuth;
    String uid, keyValue;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        datePickerText.setText(currentDate);
        markAllPresent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);
        keyValue = getIntent().getStringExtra("key");
        listview = findViewById(R.id.listView1);
        datePickerText = findViewById(R.id.datePickerText);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        final ArrayList<String> studentList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);


        DatabaseReference myRef = database.getReference("ClassList").child(uid).child(keyValue);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //add result into array list
                    studentList.add(String.valueOf(dsp.getKey()) + " " + String.valueOf(dsp.getValue()));
                    rollNoList.add(String.valueOf(dsp.getKey()));
                }
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        datePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });


        final DatabaseReference attendanceRef = database.getReference("Attendance").child(uid).child(keyValue);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                String rollNo = rollNoList.get(position);
                String dateToBe = datePickerText.getText().toString();
                DatabaseReference dateAttendance = attendanceRef.child(dateToBe);
                dateAttendance.child(rollNo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equalsIgnoreCase("P")) {
                            DatabaseReference dsp = dataSnapshot.getRef();
                            dsp.setValue("A");
                            Toast.makeText(AttendanceViewer.this, "Marked Absent", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void markAllPresent() {
        final DatabaseReference attendanceRef = database.getReference("Attendance").child(uid).child(keyValue);
        Iterator<String> iterator = rollNoList.iterator();
        while (iterator.hasNext()) {
            String dateToBe = datePickerText.getText().toString();
            DatabaseReference dateAttendance = attendanceRef.child(dateToBe);
            dateAttendance.child(iterator.next()).setValue("P");
        }
    }
}
