package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {

    Button saveBtn;
    EditText textBox;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_notes);
        final String uid = mAuth.getUid();
        final DatabaseReference myRef = database.getReference("Notes").child(uid);

        textBox = findViewById(R.id.notesEditText);
        saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HashMap<String,String> userMap = new HashMap<>();
                //userMap.put("Notes",textBox.getText().toString());
                myRef.child("Notes").setValue(textBox.getText().toString());
                Intent gotoNotesViewer = new Intent(NotesActivity.this,NotesViewerActivity.class);
                startActivity(gotoNotesViewer);
            }
        });

    }
}
