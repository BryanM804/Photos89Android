package group89.photos.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;

public class ViewPhoto extends AppCompatActivity {

    private Photo viewingPhoto;
    private ActivityResultLauncher<Intent> startForResult;
    private ActivityResultLauncher<Intent> startForMove;
    private ListView personTagsView;
    private ListView locationTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo);

        Toolbar photoToolbar = findViewById(R.id.photoToolbar);
        setSupportActionBar(photoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ImageView imageView = findViewById(R.id.largeImageView);
            personTagsView = findViewById(R.id.personTagsList);
            locationTagsView = findViewById(R.id.locationTagsList);
            personTagsView.setOnItemClickListener((list, view, pos, id) -> removeTag(pos, "person"));
            locationTagsView.setOnItemClickListener((list, view, pos, id) -> removeTag(pos, "location"));

            Photo photoCopy = bundle.getSerializable("photo", Photo.class);
            viewingPhoto = AlbumManager.getInstance().getMatchingPhoto(photoCopy);

            imageView.setImageURI(viewingPhoto.getImage());
            updateTags();
        }

        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        updateTags();
                    }
                });
        startForMove = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater foodPrices = getMenuInflater();
        foodPrices.inflate(R.menu.photo_menu, menu);
        return true;
    }

    public void addTag(MenuItem menuItem) {
        Intent addTagIntent = new Intent(this, AddTag.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("photo", viewingPhoto);
        addTagIntent.putExtras(bundle);

        startForResult.launch(addTagIntent);
    }

    public void movePhoto(MenuItem menuItem) {
        Intent movePhotoIntent = new Intent(this, MovePhoto.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("photo", viewingPhoto);

        movePhotoIntent.putExtras(bundle);
        startForMove.launch(movePhotoIntent);
    }

    public void removePhoto(MenuItem menuItem) {
        Album album = AlbumManager.getInstance().getAlbumByName(AlbumManager.getInstance().getSelectedAlbum());
        if (album != null) {
            album.removePhoto(viewingPhoto);
        }
        AlbumManager.getInstance().saveAlbums();
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void removeTag(int position, String type) {
        // TODO: make confirmation popup
        viewingPhoto.removeTag(type, position);
        AlbumManager.getInstance().saveAlbums();
        updateTags();
    }

    public void updateTags() {
        personTagsView.setAdapter(new ArrayAdapter<>(this, R.layout.tag, viewingPhoto.getPersonTags()));
        locationTagsView.setAdapter(new ArrayAdapter<>(this, R.layout.tag, viewingPhoto.getLocationTags()));
    }
}
