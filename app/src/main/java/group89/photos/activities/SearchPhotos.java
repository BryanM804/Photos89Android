package group89.photos.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;
import group89.photos.photoview.PhotoAdapter;

public class SearchPhotos extends AppCompatActivity
{
    private RecyclerView resultsView;
    private List<Photo> results;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_photos);

        Toolbar searchToolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search");

        // No need to set the title on the action bar as SearchView will be always expanded
        // getSupportActionBar().setTitle("Search");

        TagSuggestionAdapter suggestionAdapter = new TagSuggestionAdapter(this);
        searchView.setSuggestionsAdapter(suggestionAdapter);

        resultsView = findViewById(R.id.resultsList);
        resultsView.setLayoutManager(new LinearLayoutManager(this));
        results = new ArrayList<>();
        context = this;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                List<Photo> matches = searchPhotos(query);

                // here is where the matches are returned. the photos can then be displayed

                // May or may not work, not sure yet
                results = matches;
                resultsView.setAdapter(new PhotoAdapter(context, results));
                Log.d("INFO", "Results updated, got " + matches.size() + " results\n" + matches);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (!newText.isEmpty())
                {
                    Set<String> suggestions = new HashSet<>();

                    suggestions.addAll(suggestionAdapter.getAllPersonTags());
                    suggestions.addAll(suggestionAdapter.getAllLocationTags());

                    suggestionAdapter.updateCursor(newText, suggestions);
                }

                return true;
            }
        });

        // Ensure the SearchView is always expanded and does not collapse into an icon
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();  // Optionally request focus to the SearchView
    }

    private List<Photo> searchPhotos(String query)
    {
        List<Photo> photos = new ArrayList<>();

        query = query.toLowerCase(); // simplify

        Pattern pattern = Pattern.compile("(\\w+)=(\\w[\\w\\s]*)");
        String[] components = query.split("\\s+(and|or)\\s+");

        if (components.length != 1 && components.length != 2)
        {
            Toast.makeText(this, "Invalid search. Use tag=value format.", Toast.LENGTH_LONG).show();
            return photos;
        }

        Matcher matcher = pattern.matcher(components[0]);

        if (!matcher.find())
        {
            Toast.makeText(this, "Invalid search. Use tag=value format.", Toast.LENGTH_LONG).show();
            return photos;
        }

        String tagName = matcher.group(1);
        String tagValue = matcher.group(2).trim();

        List<Photo> firstMatches = findPhotosByTag(tagName, tagValue);

        if (components.length == 2)
        {
            matcher = pattern.matcher(components[1]);

            if (!matcher.find())
            {
                Toast.makeText(this, "Invalid search. Use tag=value format.", Toast.LENGTH_LONG).show();
                return photos;
            }

            tagName = matcher.group(1);
            tagValue = matcher.group(2).trim();

            List<Photo> secondMatches = findPhotosByTag(tagName, tagValue);

            if (query.contains(" and "))
            {
                Set<Photo> firstSet = new HashSet<>(firstMatches);
                firstSet.retainAll(secondMatches);
                photos = new ArrayList<>(firstSet);
            } else if (query.contains(" or "))
            {
                Set<Photo> unionSet = new HashSet<>(firstMatches);
                unionSet.addAll(secondMatches);
                photos = new ArrayList<>(unionSet);
            } else
            {
                Toast.makeText(this, "Invalid search. Use tag=value format.", Toast.LENGTH_LONG).show();
                return photos;
            }
        } else
        {
            photos = firstMatches;
        }

        return photos;
    }

    private List<Photo> findPhotosByTag(String tagName, String tagValue)
    {
        List<Photo> matchingPhotos = new ArrayList<>();
        for (Album album : AlbumManager.getInstance().getAlbums())
        {
            for (Photo photo : album.getPhotos())
            {

                if (tagName.equals("person"))
                {
                    for (String t : photo.getLowercasePersonTags()) {
                        if (t.startsWith(tagValue) && !matchingPhotos.contains(photo)) {
                            matchingPhotos.add(photo);
                        }
                    }
                } else if (tagName.equals("location"))
                {
                    for (String t : photo.getLowercaseLocationTags()) {
                        if (t.startsWith(tagValue) && !matchingPhotos.contains(photo)) {
                            matchingPhotos.add(photo);
                        }
                    }
                }
                Log.d("INFO", "Searched photo: " + photo.toString() +" for tag: " + tagValue);
            }
        }
        return matchingPhotos;
    }
    private static final class TagSuggestionAdapter extends CursorAdapter
    {
        private LayoutInflater inflater;

        public TagSuggestionAdapter(Context context)
        {
            super(context, null, false);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            int columnIndex = cursor.getColumnIndex("tag");
            if (columnIndex != -1)
            {
                String tag = cursor.getString(columnIndex);
                if (tag != null)
                {
                    ((TextView) view.findViewById(android.R.id.text1)).setText(tag);
                }
            }
        }

        public void updateCursor(String prefix, Set<String> tags)
        {
            MatrixCursor cursor = new MatrixCursor(new String[] {"_id", "tag"});
            int id = 0;
            for (String tag : tags)
            {
                if (tag.toLowerCase().startsWith(prefix.toLowerCase()))
                {
                    cursor.addRow(new Object[] {id++, tag});
                }
            }
            changeCursor(cursor);
        }

        private Set<String> getAllPersonTags()
        {
            List<String> personTags = new ArrayList<>();

            for (Album album : AlbumManager.getInstance().getAlbums())
            {
                for (Photo photo : album.getPhotos())
                {
                    personTags.addAll(photo.getPersonTags());
                }
            }

            return new HashSet<>(personTags);
        }

        private Set<String> getAllLocationTags()
        {
            List<String> locationTags = new ArrayList<>();

            for (Album album : AlbumManager.getInstance().getAlbums())
            {
                for (Photo photo : album.getPhotos())
                {
                    locationTags.addAll(photo.getLocationTags());
                }
            }

            return new HashSet<>(locationTags);
        }
    }
}