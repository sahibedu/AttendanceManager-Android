package production.rts.attendancemanager;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView emailTextView;
    TextView passwordTextView;
    Button loginBtn, registerBtn;
    ProgressBar progressIndicator;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextView = findViewById(R.id.idTextField);
        passwordTextView = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginbtn);
        registerBtn = findViewById(R.id.registerbtn);
        progressIndicator = findViewById(R.id.progressIndicator);
        mAuth = FirebaseAuth.getInstance();

        progressIndicator.setVisibility(View.INVISIBLE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTextView.getText().toString().equalsIgnoreCase("") || passwordTextView.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                } else {
                    progressIndicator.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailTextView.getText().toString(), passwordTextView.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sign in Completed
                                Toast.makeText(LoginActivity.this, "Welcome to Attendace Manager", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent gotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                progressIndicator.setVisibility(View.GONE);
                                gotoMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(gotoMainActivity);
                            } else {
                                progressIndicator.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoRegisterActivity);
            }
        });
    }
}
