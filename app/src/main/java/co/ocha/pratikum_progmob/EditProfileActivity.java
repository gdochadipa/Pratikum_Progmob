package co.ocha.pratikum_progmob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import co.ocha.pratikum_progmob.SharedPrefed.SharedPrefed;
import co.ocha.pratikum_progmob.model.AddCartResponse;
import co.ocha.pratikum_progmob.remote.ApiInterface;
import co.ocha.pratikum_progmob.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPrefed sharedPrefManager;
    Button  btnPilihFoto,btnEditProfile;
    ImageView imageProfile;
    ProgressBar progressBar;
    StorageReference reference;
    ApiInterface apiInterface;

    EditText etEmail,etName, etphotoProfile;
    String tokenUser,urlProfile;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    String TAG="Test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        sharedPrefManager = new SharedPrefed(this);
        progressBar = findViewById(R.id.progressBar);

        btnPilihFoto = findViewById(R.id.btnPilihFoto);
        imageProfile = findViewById(R.id.imageProfile);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        etphotoProfile = findViewById(R.id.etphotoProfile);

        tokenUser = sharedPrefManager.getSPToken();
        etName.setText(sharedPrefManager.getSPNama());
        etEmail.setText(sharedPrefManager.getSPEmail());
        etphotoProfile.setText(sharedPrefManager.getSPPhoto());
        apiInterface = RetrofitClient.getClient(getApplicationContext()).create(ApiInterface.class);

        Glide.with(getApplicationContext())
                .load(urlProfile)
                .placeholder(R.drawable.ic_menu_camera)
                .override(200, 200) // resizing
                .centerCrop()
                .into(imageProfile);

        btnEditProfile.setOnClickListener(this);
        btnPilihFoto.setOnClickListener(this);
        reference = FirebaseStorage.getInstance().getReference();



    }

    private void uploadImage(){
        Toast.makeText(EditProfileActivity.this, "Sedang upload", Toast.LENGTH_SHORT).show();
        //Method ini digunakan untuk mengupload gambar pada Storage
        imageProfile.setDrawingCacheEnabled(true);
        imageProfile.buildLayer();
        Bitmap bitmap = ((BitmapDrawable) imageProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        String namaFile = UUID.randomUUID()+".jpg";
        String pathFile = "foto/"+namaFile;
        final StorageReference ref = reference.child(pathFile);

        UploadTask uploadTask = ref.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, "Uploading Berhasil", Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadURl = uri;
                        etphotoProfile.setText(downloadURl.toString());
                        updateProfile();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, "Uploading Gagal", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            }
        });


    }

    private void getImage(){
        //Method ini digunakan untuk mengambil gambar dari Kamera
        CharSequence[] menu = {"Kamera", "Galeri"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                Intent intentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intentGalery,REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if(resultCode == RESULT_OK){
                    imageProfile.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageProfile.setImageBitmap(bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK){
                    imageProfile.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    imageProfile.setImageURI(uri);
                }
                break;
        }
    }

    private void updateProfile(){
        Call<AddCartResponse> postProfile = apiInterface.editUser(tokenUser,etName.getText().toString(), etEmail.getText().toString(),etphotoProfile.getText().toString());
        postProfile.enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(),"Berhasil mengupdate Profile",Toast.LENGTH_LONG).show();
                    Log.d("RESPONSE", "onResponse: "+response.message().toString());
                    startActivity(new Intent(getApplicationContext(), BaseActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext()," Gagal mengupdate Profile",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPilihFoto:
                getImage();
                break;

            case R.id.btnEditProfile:
                uploadImage();

                break;
        }
    }
}