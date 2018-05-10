package production.rts.attendancemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeTableActivity extends AppCompatActivity {

    TouchImageView timetableImage;
    public static final int PICK_IMAGE = 1;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        timetableImage = findViewById(R.id.timeTableImage);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser muser = mAuth.getCurrentUser();
        uid = muser.getUid();
        final DatabaseReference idRefernce = database.getReference("Users").child(uid);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        timetableImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        idRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    //Toast.makeText(TimeTableActivity.this, "No Children Found", Toast.LENGTH_SHORT).show();
                }else {
                    Picasso.get().load(dataSnapshot.child("TimeTable").getValue().toString()).placeholder(R.drawable.classroom).error(R.drawable.classroom).into(timetableImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser muser = mAuth.getCurrentUser();
        String getUID = muser.getUid();

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            Uri imageURL = data.getData();
            StorageReference filePath = mStorageRef.child("TimeTable").child(getUID+".jpg");
            filePath.putFile(imageURL).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        String photoURI = task.getResult().getDownloadUrl().toString();
                        final DatabaseReference myRef = database.getReference("Users").child(uid);
                        myRef.child("TimeTable").setValue(photoURI);
                        Toast.makeText(TimeTableActivity.this,"Photo Updated",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TimeTableActivity.this,"Photo Upload Failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "PhotoPicker Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
