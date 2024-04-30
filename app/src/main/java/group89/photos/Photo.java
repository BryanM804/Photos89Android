package group89.photos;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<String> personTags;
    private List<String> locationTags;
    // I would think this has the same restriction of only one location per photo
    // But it never says that anywhere in the assignment so no need to overcomplicate
    private String imageURI; // Uri is not serializable

    public Photo(Uri imageURI, String name) {
        this.personTags = new ArrayList<>();
        this.locationTags = new ArrayList<>();

        this.imageURI = imageURI.toString();
        this.name = name;
    }

    public Uri getImage() {
        return Uri.parse(this.imageURI);
    }

    public String getName() {
        return this.name;
    }

    public void addTag(String type, String tag) {
        if (type.equalsIgnoreCase("person")) {
            personTags.add(tag);
        } else if (type.equalsIgnoreCase("location")) {
            locationTags.add(tag);
        }
    }

    public void removeTag(String type, int index) {
        if (type.equals("person")) {
            personTags.remove(index);
        } else {
            locationTags.remove(index);
        }
    }

    public List<String> getPersonTags() {
        return this.personTags;
    }
    public List<String> getLocationTags() {
        return this.locationTags;
    }

    public List<String> getLowercasePersonTags() {
        List<String> lcTags = new ArrayList<>();
        for (String s : personTags) {
            lcTags.add(s.toLowerCase());
        }
        return lcTags;
    }
    public List<String> getLowercaseLocationTags() {
        List<String> lcTags = new ArrayList<>();
        for (String s : locationTags) {
            lcTags.add(s.toLowerCase());
        }
        return lcTags;
    }

    public boolean equals(Object other) {
        Photo oPhoto = (Photo) other;

        return (this.personTags.equals(oPhoto.getPersonTags()) &&
                this.locationTags.equals(oPhoto.getLocationTags()) &&
                this.imageURI.equals(oPhoto.getImage().toString()));
    }
}
