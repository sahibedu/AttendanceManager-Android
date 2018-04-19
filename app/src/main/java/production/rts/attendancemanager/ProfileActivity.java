package production.rts.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    TextView nameText, emailText, phoneText, positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.profileText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.numberText);
        positionText = findViewById(R.id.positionText);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        nameText.setText(mUser.getDisplayName());
        emailText.setText(mUser.getEmail());
        phoneText.setText(mUser.getUid());


    }
}

