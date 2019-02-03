package production.rts.attendancemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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


    Button registerbtn;
    TextView loginText;
    FirebaseAuth mAuth;
    TextInputEditText nameEdit, emailEdit, passwordEdit, confirmPasswordEdit,phoneEdit;
    ProgressBar progressIndicator;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn = findViewById(R.id.btn_register_register);
        loginText = findViewById(R.id.tv_login_register);
        nameEdit = findViewById(R.id.et_name_register);
        emailEdit = findViewById(R.id.et_email_register);
        passwordEdit = findViewById(R.id.et_password_register);
        confirmPasswordEdit = findViewById(R.id.et_confirm_password_register);
        phoneEdit = findViewById(R.id.et_mobile_register);
        progressIndicator = findViewById(R.id.progressbar_register);

        progressIndicator.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdit.getText().toString().equalsIgnoreCase("") || emailEdit.getText().toString().equalsIgnoreCase("") || passwordEdit.getText().toString().equalsIgnoreCase("") || confirmPasswordEdit.getText().toString().equalsIgnoreCase("")||phoneEdit.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(RegisterActivity.this, "Enter Your Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    if(passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
                        progressIndicator.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in Successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nameEdit.getText().toString()).build();
                                    user.updateProfile(nameUpdate);
                                    DatabaseReference idRefernce = database.getReference("Users").child(user.getUid());
                                    idRefernce.child("PhoneNo").setValue(phoneEdit.getText().toString());
                                    progressIndicator.setVisibility(View.INVISIBLE);
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "Verification Mail Send", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent gotoLoginActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(gotoLoginActivity);
                                } else {
                                    //Sign in Unsuccessful
                                    progressIndicator.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });   
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
