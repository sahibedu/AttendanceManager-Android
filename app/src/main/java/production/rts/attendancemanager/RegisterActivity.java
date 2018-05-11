package production.rts.attendancemanager;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {


    Button registerbtn,backBtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText,phoneNumber;
    ProgressBar progressIndicator;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn = findViewById(R.id.registerbtn);
        backBtn = findViewById(R.id.backbtn);
        nameEditText = findViewById(R.id.nameedittext);
        emailEditText = findViewById(R.id.emailedittext);
        passwordEditText = findViewById(R.id.passwordedittext);
        phoneNumber = findViewById(R.id.phoneNumber);
        confirmPasswordEditText = findViewById(R.id.confirmpassedittext);
        progressIndicator = findViewById(R.id.progressIndicator);

        progressIndicator.setVisibility(View.INVISIBLE);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().equalsIgnoreCase("") || emailEditText.getText().toString().equalsIgnoreCase("") || passwordEditText.getText().toString().equalsIgnoreCase("") || confirmPasswordEditText.getText().toString().equalsIgnoreCase("")||phoneNumber.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(RegisterActivity.this, "Enter Your Crenditals", Toast.LENGTH_SHORT).show();
                } else {
                    if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                        progressIndicator.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in Successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                                    user.updateProfile(nameUpdate);
                                    DatabaseReference idRefernce = database.getReference("Users").child(user.getUid());
                                    idRefernce.child("PhoneNo").setValue(phoneNumber.getText().toString());
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                gotoLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoLoginActivity);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.signOut();
    }


}
