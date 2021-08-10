package com.example.cf_chatapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cf_chatapp.ChatActivity;
import com.example.cf_chatapp.Login;
import com.example.cf_chatapp.R;
import com.example.cf_chatapp.Model.UserModel;
import com.example.cf_chatapp.ShowImageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private static final int REQUEST_CHOOSE_PICTURE = 11;
    private static final int REQUEST_CAPTURE_PICTURE = 12;
    CircleImageView imageView;
    TextView tvUsername, signOut, changeUsername, changeProfileImg;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;
    private Context context;
    private String profileImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = view.findViewById(R.id.profile_img_frag);
        tvUsername = view.findViewById(R.id.tvUsername_frag);
        signOut = view.findViewById(R.id.btnsignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        changeUsername = view.findViewById(R.id.btnChangeUsername);
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openpopupChangeName(Gravity.CENTER);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                profileImage = userModel.getAvatar();
                tvUsername.setText(userModel.getUsername());
                if (userModel.getAvatar().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(profileImage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openImage();
                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                intent.putExtra("imageUrl", profileImage);
                startActivity(intent);
            }
        });
        changeProfileImg = view.findViewById(R.id.btnChangeProfileImage);
        changeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissionsIfNeeded();
//                openpopupChangepicture(Gravity.CENTER);
                startChoosePicture(REQUEST_CHOOSE_PICTURE);


            }
        });
        return view;
    }

//    private void openpopupChangepicture(int v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = this.getLayoutInflater();
//        View view = inflater.inflate(R.layout.popup_change_profile_image, null);
//        builder.setView(view);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//        TextView tvChoosePicture = view.findViewById(R.id.tvChoosePicture);
//        TextView tvCarpturePicture = view.findViewById(R.id.tvCapturePicture);
//        tvChoosePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startChoosePicture(REQUEST_CHOOSE_PICTURE);
//            }
//        });
//        tvCarpturePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startCapturePicture(REQUEST_CAPTURE_PICTURE);
//            }
//        });
//    }

    private void startCapturePicture(int requestCapturePicture) {


    }

    private void startChoosePicture(int requestChoosePicture) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, requestChoosePicture);

    }

    private void openpopupChangeName(int v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_change_username, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView tvOldUsername = view.findViewById(R.id.tvOldUsername);
        EditText edtNewUsername = view.findViewById(R.id.edtNewUsername);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                tvOldUsername.setText("Old Username: " + userModel.getUsername());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        Button btnOke = view.findViewById(R.id.btnOke);
        Button btnCancle = view.findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = edtNewUsername.getText().toString();

                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                Map<String, Object> map = new HashMap<>();
                map.put("username", newUsername);
                reference.updateChildren(map);
                Toast.makeText(getContext(), "Change username is successfully!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });


    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading.....");
        pd.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("avatar", mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_PICTURE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void checkAndRequestPermissionsIfNeeded() {
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String cameraPermission = Manifest.permission.CAMERA;

        // int hasWriteExternalStoragePermission;

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            hasWriteExternalStoragePermission = PackageManager.PERMISSION_GRANTED;
//        } else {
//            hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
//        }
        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(getContext(), writeExternalStorage);

        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(getContext(), readExternalStorage);
        int hasCameraPermission = ActivityCompat.checkSelfPermission(getContext(), cameraPermission);

        List<String> permissions = new ArrayList<>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(cameraPermission);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {
            ActivityCompat.requestPermissions(getActivity(), params, 123);
        } else {
        }
    }
}
