package production.rts.attendancemanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NotesViewerActivity extends AppCompatActivity {

    FloatingActionButton floatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);

        floatBtn = findViewById(R.id.floatingActionButton);

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNotesEditor = new Intent(NotesViewerActivity.this, NotesActivity.class);
                startActivity(gotoNotesEditor);
            }
        });
    }
}
