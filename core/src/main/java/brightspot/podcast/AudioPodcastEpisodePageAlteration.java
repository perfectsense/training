package brightspot.podcast;

import com.psddev.cms.db.BoardSearchResultField;
import com.psddev.dari.db.Alteration;

public class AudioPodcastEpisodePageAlteration extends Alteration<AudioPodcastEpisodePage> {

    @BoardSearchResultField
    @InternalName("hasPodcast.getPodcast")
    private Podcast getPodcast;
}
