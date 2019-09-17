package bex.training.album;

import com.psddev.dari.db.Record;
import bex.training.character.Character;

public class Song extends Record {

    private String title;

    private Character character;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public String getLabel() {
        return getTitle();
    }
}
