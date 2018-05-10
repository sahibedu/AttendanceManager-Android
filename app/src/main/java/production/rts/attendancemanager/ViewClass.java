package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewClass extends AppCompatActivity {

    ListView listview;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

            listview = findViewById(R.id.listView1);
            mAuth = FirebaseAuth.getInstance();
            muser = mAuth.getCurrentUser();
            final String uid = muser.getUid();
            final ArrayList<String> classList = new ArrayList<>();
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classList);

            DatabaseReference myRef = database.getReference("ClassList");
            DatabaseReference userReference = myRef.child(uid);

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        classList.add(String.valueOf(dsp.getKey())); //add result into array list
                    }
                    listview.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent gotoAttendanceViewer = new Intent(ViewClass.this,ViewAttendance.class);
                    gotoAttendanceViewer.putExtra("key",classList.get(position));
                    startActivity(gotoAttendanceViewer);
                }
            });
    }
}
