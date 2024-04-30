package group89.photos.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;

public class AddTag extends AppCompatActivity {

    private Photo taggingPhoto;
    private TextView tagValueInput;
    private RadioButton personRadioButton;
    private RadioButton locationRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) finish();

        Photo photoCopy = bundle.getSerializable("photo", Photo.class);
        taggingPhoto = AlbumManager.getInstance().getMatchingPhoto(photoCopy);

        Toolbar addTagToolbar = findViewById(R.id.addTagToolbar);
        setSupportActionBar(addTagToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagValueInput = findViewById(R.id.tagValueInput);
        personRadioButton = findViewById(R.id.personRadioButton);
        locationRadioButton = findViewById(R.id.locationRadioButton);
    }

    public void saveTag(View view) {
        String tag = tagValueInput.getText().toString();

        if (!tag.isEmpty()) {
            if (personRadioButton.isChecked()) {
                if (taggingPhoto.getLowercasePersonTags().contains(tag.toLowerCase())) {
                    Toast.makeText(this, "This photo already has this tag!", Toast.LENGTH_SHORT).show();
                    return;
                }

                taggingPhoto.addTag("person", tag);
                AlbumManager.getInstance().saveAlbums();
                Log.d("INFO", "Added person tag");

                setResult(RESULT_OK);
                finish();
            } else if (locationRadioButton.isChecked()) {
                if (taggingPhoto.getLowercaseLocationTags().contains(tag.toLowerCase())) {
                    Toast.makeText(this, "This photo already has this tag!", Toast.LENGTH_SHORT).show();
                    return;
                }

                taggingPhoto.addTag("location", tag);
                AlbumManager.getInstance().saveAlbums();
                Log.d("INFO", "Added location tag");

                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Please select a tag type!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter a tag!", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(View view) {
        finish();
    }
}
