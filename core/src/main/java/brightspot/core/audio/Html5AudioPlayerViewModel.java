package brightspot.core.audio;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.audio.Html5audioPlayerView;

/**
 * Viewmodel for constructing a Html5 Audio Player. Provides the source urls for the various formats provided to the
 * html5 player on the frontend.
 */
public class Html5AudioPlayerViewModel extends ViewModel<Audio> implements Html5audioPlayerView {

    @Override
    public Iterable<? extends Map> getSources() {
        return Optional.ofNullable(model)
            .map(Audio::getAudioProvider)
            .map(AudioProvider::getItems)
            .map(items -> items.stream()
                .map(item -> ImmutableMap.of("src", item.getFile().getPublicUrl(), "type", item.getMimeType()))
                .collect(Collectors.toList()))
            .orElse(null);
    }
}
