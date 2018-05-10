package production.rts.attendancemanager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ViewAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser muser;
    final ArrayList<String> rollno = new ArrayList<>();
    final ArrayList<String> present = new ArrayList<>();
    final ArrayList<String> finalArray = new ArrayList<>();

    ListView listview;
    TextView datePickerText;
    String keyValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        listview = findViewById(R.id.listView1);
        datePickerText = findViewById(R.id.datePickerText);

        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        keyValue = getIntent().getStringExtra("key");

        datePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        datePickerText.setText(currentDate);
        getData();
    }

    void setText(){
        if ((finalArray.isEmpty())){
            Toast.makeText(this, "Array Empty", Toast.LENGTH_SHORT).show();
        }else{
            finalArray.clear();
            rollno.clear();
            present.clear();
            Toast.makeText(this, finalArray.size(), Toast.LENGTH_SHORT).show();
        }
        String value;
        final Iterator<String> iterator1 = rollno.iterator();
        final Iterator<String> iterator2 = present.iterator();
        while (iterator1.hasNext()){
            value = iterator1.next()+ "    "+iterator2.next();
            finalArray.add(value);
        }
    }

    void getData(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, finalArray);
        String uid = muser.getUid();
        String dateGet = datePickerText.getText().toString();

        final DatabaseReference myRef = database.getReference("ClassList").child(uid).child(keyValue);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    rollno.add(String.valueOf(dsp.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        DatabaseReference newRefernce = database.getReference("Attendance").child(uid).child("CSE-4K").child(dateGet);
        newRefernce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    present.add(String.valueOf(dsp.getValue().toString()));
                }
                setText();
                Toast.makeText(ViewAttendance.this, "Data Populated", Toast.LENGTH_SHORT).show();
                listview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(ViewAttendance.this, "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
