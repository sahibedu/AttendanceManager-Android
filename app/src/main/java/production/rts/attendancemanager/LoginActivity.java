package production.rts.attendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView emailTextView;
    TextView passwordTextView;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextView = findViewById(R.id.idTextField);
        passwordTextView = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginbtn);
        registerBtn = findViewById(R.id.registerbtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTextView.getText().toString() == "admin") {
                    Toast.makeText(LoginActivity.this, emailTextView.getText(), Toast.LENGTH_SHORT).show();
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
