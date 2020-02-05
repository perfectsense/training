package brightspot.core.social;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

/**
 * Modification of {@link SocialEntity} to provide social network usernames.
 */
@Modification.Classes({ SocialEntity.class, SiteSettings.class })
@Recordable.FieldInternalNamePrefix("social.")
public class SocialEntityData extends Modification<Object> {

    private static final String SOCIAL_CLUSTER_NAME = "Social";

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"facebook\")}'></span>")
    @ToolUi.Tab("Front-End")
    private String facebookUsername;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"instagram\")}'></span>")
    @ToolUi.Tab("Front-End")
    private String instagramUsername;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"linkedin\")}'></span>")
    @ToolUi.Tab("Front-End")
    private LinkedInEntity linkedInEntity;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"pinterest\")}'></span>")
    @ToolUi.Tab("Front-End")
    private String pinterestUsername;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"tumblr\")}'></span>")
    @ToolUi.Tab("Front-End")
    private String tumblrUsername;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"twitter\")}'></span>")
    @ToolUi.Tab("Front-End")
    private String twitterUsername;

    @ToolUi.Cluster(SOCIAL_CLUSTER_NAME)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getUsernameNoteHtml(\"youtube\")}'></span>")
    @ToolUi.Tab("Front-End")
    private YouTubeEntity youTubeEntity;

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public void setInstagramUsername(String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public String getLinkedInUsername() {
        return linkedInEntity != null ? linkedInEntity.getLinkedInUsername() : "LinkedIn";
    }

    public String getPinterestUsername() {
        return pinterestUsername;
    }

    public void setPinterestUsername(String pinterestUsername) {
        this.pinterestUsername = pinterestUsername;
    }

    public String getTumblrUsername() {
        return tumblrUsername;
    }

    public void setTumblrUsername(String tumblrUsername) {
        this.tumblrUsername = tumblrUsername;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public YouTubeEntity getYouTubeEntity() {
        return youTubeEntity;
    }

    public void setYouTubeEntity(YouTubeEntity youTubeEntity) {
        this.youTubeEntity = youTubeEntity;
    }

    public String getYouTubeUsername() {
        return youTubeEntity != null ? youTubeEntity.getYouTubeId() : "Youtube";
    }

    /**
     * @return the Facebook URL.
     */
    protected String getFacebookUrl() {
        if (StringUtils.isBlank(facebookUsername)) {
            return null;
        }
        return "https://www.facebook.com/" + facebookUsername;
    }

    /**
     * @return the Instagram URL.
     */
    protected String getInstagramUrl() {
        if (StringUtils.isBlank(instagramUsername)) {
            return null;
        }
        return "https://www.instagram.com/" + instagramUsername + "/";
    }

    /**
     * @return the LinkedIn URL.
     */
    protected String getLinkedInUrl() {
        if (linkedInEntity == null) {
            return null;
        }
        return linkedInEntity.getUrl();
    }

    /**
     * @return the Pinterest URL.
     */
    protected String getPinterestUrl() {
        if (StringUtils.isBlank(pinterestUsername)) {
            return null;
        }
        return "https://www.pinterest.com/" + pinterestUsername;
    }

    /**
     * @return the Tumblr URL.
     */
    protected String getTumblrUrl() {
        if (StringUtils.isBlank(tumblrUsername)) {
            return null;
        }
        return "https://www.tumblr.com/blog/" + tumblrUsername;
    }

    /**
     * @return the Twitter URL.
     */
    protected String getTwitterUrl() {
        if (StringUtils.isBlank(twitterUsername)) {
            return null;
        }
        return "https://twitter.com/" + twitterUsername;
    }

    /**
     * @return the YouTube URL.
     */
    protected String getYouTubeUrl() {
        if (youTubeEntity == null) {
            return null;
        }
        return youTubeEntity.getUrl();
    }

    /**
     * This is a helper to render all the NoteHtml or the username fields. It uses the {@link SocialService} to delegate
     * to the appropriate getter for the given socialServiceName.
     *
     * @param socialServiceName the name of the social service.
     * @return the html note
     */
    public String getUsernameNoteHtml(String socialServiceName) {

        if (StringUtils.isBlank(socialServiceName)) {
            return null;
        }

        SocialService service = SocialService.getServiceByName(socialServiceName);

        if (service == null) {
            return null;
        }

        String url = service.getUrl(this);
        if (url != null) {
            return "Link: <a target=\"_blank\" href=\""
                + StringUtils.escapeHtml(url)
                + "\">"
                + StringUtils.escapeHtml(url)
                + "</a>";
        }

        return null;
    }
}
