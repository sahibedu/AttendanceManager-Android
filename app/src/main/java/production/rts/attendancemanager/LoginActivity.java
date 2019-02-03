package production.rts.attendancemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    TextInputEditText emailEdit, passwordEdit;
    Button loginBtn, registerBtn;
    ProgressBar progressIndicator;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = findViewById(R.id.et_email_login);
        passwordEdit = findViewById(R.id.et_password_login);
        loginBtn = findViewById(R.id.loginbtn);
        registerBtn = findViewById(R.id.registerbtn);
        progressIndicator = findViewById(R.id.progressIndicator);
        mAuth = FirebaseAuth.getInstance();

        progressIndicator.setVisibility(View.INVISIBLE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEdit.getText().toString().equalsIgnoreCase("") || passwordEdit.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Please fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    progressIndicator.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sign in Completed
                                Toast.makeText(LoginActivity.this, "Welcome to Attendance Manager", Toast.LENGTH_SHORT).show();
                                Intent gotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                progressIndicator.setVisibility(View.INVISIBLE);
                                gotoMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(gotoMainActivity);
                            } else {
                                progressIndicator.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
