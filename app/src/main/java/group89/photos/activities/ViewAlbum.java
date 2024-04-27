package group89.photos.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.Photos;
import group89.photos.R;
import group89.photos.photoview.PhotoAdapter;

public class ViewAlbum extends AppCompatActivity {

    private String albumName = "Album Name";
    private RecyclerView photoList;
    private ActivityResultLauncher<Intent> startForResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar viewAlbumToolbar = findViewById(R.id.viewAlbumToolbar);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            albumName = bundle.getString("albumName");
        }

        viewAlbumToolbar.setTitle(this.albumName);
        setSupportActionBar(viewAlbumToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Photo> photos = AlbumManager.getInstance().getAlbumByName(albumName).getPhotos();
        photoList = findViewById(R.id.photoList);
        photoList.setLayoutManager(new LinearLayoutManager(this));
        photoList.setAdapter(new PhotoAdapter(getApplicationContext(), photos));

        this.startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        this.updateAlbumPhotos(result);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater newCarPrices = getMenuInflater();
        newCarPrices.inflate(R.menu.view_menu, menu);
        return true;
    }
    public void renameAlbum(MenuItem item) {

    }

    public void deleteAlbum(MenuItem item) {
        // TODO: Add confirmation popup
        AlbumManager.getInstance().deleteAlbum(albumName);
        AlbumManager.getInstance().saveAlbums();

        finish();
    }

    public void addPhoto(MenuItem item) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startForResult.launch(pickPhotoIntent);
    }

    private void updateAlbumPhotos(ActivityResult res) {
        if (res.getData() == null) return;
        Uri imageUri = res.getData().getData();
        // Need persistant permissions for the URI or the app will crash when you try to enter tha
        // album after the first time
        getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Photo newPhoto = new Photo(imageUri);
        AlbumManager.getInstance().getAlbumByName(albumName).addPhoto(newPhoto);
        AlbumManager.getInstance().saveAlbums();

        List<Photo> photos = AlbumManager.getInstance().getAlbumByName(albumName).getPhotos();
        photoList.setAdapter(new PhotoAdapter(getApplicationContext(), photos));
    }
}
