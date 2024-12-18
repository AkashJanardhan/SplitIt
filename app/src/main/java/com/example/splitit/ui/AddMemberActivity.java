package com.example.splitit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.splitit.R;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;

public class AddMemberActivity extends AppCompatActivity {

    private AppDatabaseHelper dbHelper;
    private long groupId;
    private static final int PICK_IMAGE_REQUEST = 100;

    private EditText etMemberName;
    private Button btnSaveMember, btnPickImage;
    private ImageView ivMemberPreview;
    private String selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        etMemberName = findViewById(R.id.etMemberName);
        btnSaveMember = findViewById(R.id.btnSaveMember);
        btnPickImage = findViewById(R.id.btnPickImage);
        ivMemberPreview = findViewById(R.id.ivMemberPreview);

        dbHelper = new AppDatabaseHelper(this);
        groupId = getIntent().getLongExtra("group_id", -1);

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSaveMember.setOnClickListener(v -> {
            String memberName = etMemberName.getText().toString().trim();
            if(!memberName.isEmpty()) {
                addMember(memberName, selectedImageUri);
            } else {
                Toast.makeText(this, "Enter member name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if(data != null && data.getData() != null) {
                selectedImageUri = data.getData().toString();
                ivMemberPreview.setImageURI(data.getData());
                ivMemberPreview.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addMember(String name, String imageUri){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME, name);
        values.put(DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID, groupId);
        values.put(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_IMAGE, imageUri);

        long newRowId = db.insert(DatabaseContract.MemberEntry.TABLE_NAME, null, values);
        if(newRowId != -1) {
            Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding member", Toast.LENGTH_SHORT).show();
        }
    }

}

