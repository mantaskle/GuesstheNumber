package mkle.ktu.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private final static String PREFS_NAME = "PlayerPrefs";

    private final static String KEY_NAME = "name";
    private final static String KEY_AGE = "age";
    private final static String KEY_PATH = "path";

    private final static String DEFAULT_NAME = "Player";
    private final static int DEFAULT_AGE = 20;

    //static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    private ExifInterface exifObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText nameField = findViewById(R.id.name_field);
        EditText ageField = findViewById(R.id.age_field);

        SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String name = sharedPrefs.getString(KEY_NAME, DEFAULT_NAME);
        int age = sharedPrefs.getInt(KEY_AGE, DEFAULT_AGE);

        nameField.setText(name);
        ageField.setText(age + "");

        ImageView profilePicture = findViewById(R.id.profile_pic);


        String profilePicturePath = sharedPrefs.getString(KEY_PATH, null);
        if (profilePicturePath != null) {
            Log.d("PATH", "onCreate: " + profilePicturePath);
            setPic(profilePicturePath, profilePicture);
        } else {
            profilePicture.setBackgroundColor(Color.BLACK);
        }
    }

    public void onSaveClick(View v) {
        finish();
        EditText nameField = findViewById(R.id.name_field);
        EditText ageField = findViewById(R.id.age_field);

        String name = DEFAULT_NAME;
        int age = DEFAULT_AGE;

        if (nameField.getText().toString().trim().length() != 0) {
            name = nameField.getText().toString();
        }

        if (ageField.getText().toString().trim().length() != 0) {
            age = Integer.parseInt(ageField.getText().toString());
        }

        SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();

        prefsEditor.putString(KEY_NAME, name);
        prefsEditor.putInt(KEY_AGE, age);
        prefsEditor.putString(KEY_PATH, currentPhotoPath);

        prefsEditor.apply();
    }

    public void onTakePictureClick(View v) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                findViewById(R.id.profile_pic).setBackgroundColor(Color.RED);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView profilePicture = findViewById(R.id.profile_pic);
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(imageBitmap);
        }*/
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic(currentPhotoPath, profilePicture);
        }
    }

    private void setPic(String path, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getMaxWidth();
        int targetH = imageView.getMaxHeight();
        Log.d("TARGET", "setPic: " + "w " + targetW + " h " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        try {
            exifObject = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedAndResized = scaleDown(rotateImageIfRequired(bitmap, orientation), 512, true);
        imageView.setImageBitmap(rotatedAndResized);
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, int orientation) {

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    // image resize

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
