package production.rts.attendancemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView nameText, emailText, phoneText, positionText;
    CircleImageView displayImage;
    public static final int PICK_IMAGE = 1;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.profileText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.numberText);
        positionText = findViewById(R.id.positionText);
        displayImage = findViewById(R.id.displayPhoto);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        nameText.setText(mUser.getDisplayName());
        emailText.setText(mUser.getEmail());
        phoneText.setText(mUser.getUid());
        Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.mipmap.ic_account_circle_black_48dp).error(R.mipmap.ic_account_circle_black_48dp).into(displayImage);

        displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuth = FirebaseAuth.getInstance();
        String getUID = mAuth.getUid();

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            Uri imageURL = data.getData();
            StorageReference filePath = mStorageRef.child("DisplayPics").child(getUID+".jpg");
            filePath.putFile(imageURL).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                   if(task.isSuccessful()){
                       String photoURI = task.getResult().getDownloadUrl().toString();
                       UserProfileChangeRequest imageUpdate = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(photoURI)).build();
                       FirebaseUser user = mAuth.getCurrentUser();
                       user.updateProfile(imageUpdate);
                       Toast.makeText(ProfileActivity.this,"Photo Updated",Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(ProfileActivity.this,"Photo Upload Failed",Toast.LENGTH_LONG).show();
                   }
                }
            });
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "PhotoPicker Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}

