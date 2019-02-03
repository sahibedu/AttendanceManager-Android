package production.rts.attendancemanager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class AttendanceMarker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ListView listview;
    Toolbar toolbar;
    FirebaseDatabase database;
    boolean dateSelected = false;
    String currentDate;
    final ArrayList<String> rollNoList = new ArrayList<>();

    private FirebaseAuth mAuth;
    String uid, keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marker);
        listview = findViewById(R.id.listView_attendance_marker);
        toolbar = findViewById(R.id.toolbar_attendance_marker);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        keyValue = getIntent().getStringExtra("key");
        uid = user.getUid();

        final ArrayList<String> studentList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, R.id.textview1, studentList);

        //Populating Data
        DatabaseReference myRef = database.getReference("ClassList").child(uid).child(keyValue);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    studentList.add(String.valueOf(dsp.getKey()) + " " + String.valueOf(dsp.getValue()));
                    rollNoList.add(String.valueOf(dsp.getKey()));
                }
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final DatabaseReference attendanceRef = database.getReference("Attendance").child(uid).child(keyValue);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
                if (dateSelected) {
                    String rollNo = rollNoList.get(position);
                    DatabaseReference dateAttendance = attendanceRef.child(currentDate);
                    dateAttendance.child(rollNo).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().toString().equalsIgnoreCase("P")) {
                                DatabaseReference dsp = dataSnapshot.getRef();
                                dsp.setValue("A");
                                Toast.makeText(AttendanceMarker.this, "Marked Absent", Toast.LENGTH_SHORT).show();
                            } else {
                                DatabaseReference dsp = dataSnapshot.getRef();
                                dsp.setValue("P");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });//AddEvent Listener Ends Here   
                } else {
                    Toast.makeText(AttendanceMarker.this, "Select Date First", Toast.LENGTH_SHORT).show();
                }
            }//OnItemClick Ends Here
        });
    }//On Create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attendance_viewer_menu, menu);
        return true;
    }//On Create Options Menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calendar: {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
                return true;
            }
            case R.id.menu_present: {
                markAllPresent();
                return true;
            }
            case R.id.menu_absent: {
                markAllAbsent();
                return true;
            }
        }
        return false;
    }//On Options Item Selected

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateSelected = true;
        markAllPresent();
    }

    private void markAllPresent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAF","Hello Wolrd");
                final DatabaseReference attendanceRef = database.getReference("Attendance").child(uid).child(keyValue);
                final Iterator<String> iterator = rollNoList.iterator();
                final DatabaseReference dateAttendance = attendanceRef.child(currentDate);
                dateAttendance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                        } else {
                            while (iterator.hasNext()) {
                                dateAttendance.child(iterator.next()).setValue("P");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }).run();
    }

    private void markAllAbsent() {
        //Doing This on Another Thread
                final DatabaseReference attendanceRef = database.getReference("Attendance").child(uid).child(keyValue);
                final Iterator<String> iterator = rollNoList.iterator();
                final DatabaseReference dateAttendance = attendanceRef.child(currentDate);
                dateAttendance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                        } else {
                            while (iterator.hasNext()) {
                                dateAttendance.child(iterator.next()).setValue("F");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
}
