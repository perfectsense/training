package bex.training.album;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.psddev.cms.db.Content;
import com.psddev.dari.util.ObjectUtils;

import bex.training.character.Character;
import bex.training.movie.Movie;

public class Album extends Content {

    private String title;

    private Set<Song> songs;

    @Indexed
    private Movie movie;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Song> getSongs() {
        if (songs == null) {
            songs = new HashSet<>();
        }
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Indexed
    public Set<Character> getAllCharacters() {
        Set<Character> characters = new HashSet<>();

        for (Song song : getSongs()) {
            Character songCharacter = song.getCharacter();

            if (songCharacter != null) {
                characters.add(songCharacter);
            }
        }

        Optional.ofNullable(getMovie())
                .map(Movie::getFeaturedCharacters)
                .filter(set -> !ObjectUtils.isBlank(set))
                .ifPresent(characters::addAll);

        return characters;
    }
}
