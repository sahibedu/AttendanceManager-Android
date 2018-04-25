package production.rts.attendancemanager;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {


    Button registerbtn;
    private FirebaseAuth mAuth;
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    ProgressBar progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerbtn = findViewById(R.id.signupbtn);
        nameEditText = findViewById(R.id.nameedittext);
        emailEditText = findViewById(R.id.emailedittext);
        passwordEditText = findViewById(R.id.passwordedittext);
        confirmPasswordEditText = findViewById(R.id.confirmpassedittext);
        progressIndicator = findViewById(R.id.progressIndicator);

        progressIndicator.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().equalsIgnoreCase("") || emailEditText.getText().toString().equalsIgnoreCase("") || passwordEditText.getText().toString().equalsIgnoreCase("") || confirmPasswordEditText.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(RegisterActivity.this, "Enter Your Crenditals", Toast.LENGTH_SHORT).show();
                } else {
                    if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                        progressIndicator.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in Successful
                                    UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.updateProfile(nameUpdate);
                                    progressIndicator.setVisibility(View.GONE);
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "VerificationMailSend", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent gotoLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(gotoLoginActivity);
                                } else {
                                    //Sign in Unsuccessful
                                    progressIndicator.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });   
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password Don't Match", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.signOut();
    }


}
