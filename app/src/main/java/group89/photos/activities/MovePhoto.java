package group89.photos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;

public class MovePhoto extends AppCompatActivity {

    private AlbumManager albumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_photo);

        albumManager = AlbumManager.getInstance();

        Toolbar moveToolbar = findViewById(R.id.moveToolbar);
        setSupportActionBar(moveToolbar);
        getSupportActionBar().setTitle("Move Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView moveAlbumList = findViewById(R.id.moveAlbumList);
        moveAlbumList.setAdapter(new ArrayAdapter<>(this, R.layout.album, albumManager.getAlbums()));
        moveAlbumList.setOnItemClickListener((list, view, pos, id) -> movePhoto(pos));
    }

    public void movePhoto(int position) {
        Album moveTo = albumManager.getAlbums().get(position);
        Photo photoCopy = getIntent().getExtras().getSerializable("photo", Photo.class);
        Photo movingPhoto = albumManager.getMatchingPhoto(photoCopy);

        albumManager.movePhoto(movingPhoto, moveTo);
        albumManager.saveAlbums();

        setResult(Activity.RESULT_OK);
        finish();
    }
}
