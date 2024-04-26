package group89.photos;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> personTags;
    private List<String> locationTags;
    // I would think this has the same restriction of only one location per photo
    // But it never says that anywhere in the assignment so no need to overcomplicate
    private Uri imageURI;

    public Photo(Uri imageURI) {
        this.personTags = new ArrayList<>();
        this.locationTags = new ArrayList<>();

        this.imageURI = imageURI;
    }

    public Uri getImage() {
        return this.imageURI;
    }

    public void addTag(String type, String tag) {
        if (type.equalsIgnoreCase("person")) {
            personTags.add(tag);
        } else if (type.equalsIgnoreCase("location")) {
            locationTags.add(tag);
        }
    }

    public List<String> getPersonTags() {
        return this.personTags;
    }
    public List<String> getLocationTags() {
        return this.locationTags;
    }
}
