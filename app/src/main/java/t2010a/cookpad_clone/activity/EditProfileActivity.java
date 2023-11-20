package t2010a.cookpad_clone.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Category;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.Origin;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.repository.Repository;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etProfileFullName, etProfileAddress,
            etProfilePhone, etProfileEmail, etProfileDetail;
    private Button btnUpdateUser;
    private ImageView imageView;
    private RelativeLayout layoutUploadImg;
    private LinearLayout layoutGone;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private Repository repository;
    private User user;
    private String accessToken = "Bearer " + LocalDataManager.getAccessToken();
    private static int IMAGE_REQ = 1;
    private Uri imagePath;
    Map config = new HashMap();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        initData();
    }

    private void initView() {
        etProfileFullName = findViewById(R.id.etProfileFullName);
        etProfileAddress = findViewById(R.id.etProfileAddress);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        etProfileEmail = findViewById(R.id.etProfileEmail);
        etProfileDetail = findViewById(R.id.etProfileDetail);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        imageView = findViewById(R.id.imageView);
        layoutUploadImg = findViewById(R.id.layoutUploadImg);
        layoutGone = findViewById(R.id.layoutGone);

        btnUpdateUser.setOnClickListener(this);
        layoutUploadImg.setOnClickListener(this);

        setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initData() {
        user = LocalDataManager.getUserDetail();
        if (user == null) {
            etProfileFullName.setText("");
            etProfileAddress.setText("");
            etProfilePhone.setText("");
            etProfileEmail.setText("");
            etProfileDetail.setText("");
        } else {
            etProfileFullName.setText(user.getFullName());
            etProfileAddress.setText(user.getAddress());
            etProfilePhone.setText(user.getPhone());
            etProfileEmail.setText(user.getEmail());
            etProfileDetail.setText(user.getDetail());
            Glide.with(this).load(user.getAvatar()).into(imageView);
        }


    }

    private void setBtnUpdateUser() {
        repository = Repository.getInstance();
        String fullName = etProfileFullName.getText().toString().toLowerCase(Locale.ROOT).trim();
        String phone = etProfilePhone.getText().toString().trim();
        String email = etProfileEmail.getText().toString().trim();
        String address = etProfileAddress.getText().toString().trim();
        String detail = etProfileDetail.getText().toString().trim();

        if (imagePath != null) {
            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d("TAG", "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d("TAG", "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String strImage = (String) resultData.get("url");
                    Log.d("TAG", "onStart: " + "usuccess");

                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setAddress(address);
                    user.setAvatar(strImage);
                    user.setDetail(detail);

                    repository.getService().updateUser(user, user.getId(), accessToken).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                Log.d("TAG", "onResponse: change success");
                                Log.d("USER", "User: " + user);
                                Toast.makeText(EditProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                LocalDataManager.setUserDetail(response.body());
                                finish();
                            } else {
                                Log.d("TAG", "onResponse:change fail");
                                Log.d("USER", "User: " + user);
                                Toast.makeText(EditProfileActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d("TAG", "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d("TAG", "onStart: " + error);
                }
            }).dispatch();
        } else {
            Toast.makeText(this, "Thumbnail is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateUser:
                setBtnUpdateUser();
                break;
            case R.id.layoutUploadImg:
                requestPermission();
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMAGE_REQ);
        }
    }

    /*
     * sele the image from the gallery
     * */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath = data.getData();
                        Picasso.get().load(imagePath).into(imageView);
                        layoutGone.setVisibility(View.GONE);

                    }
                }
            });
}