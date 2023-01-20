package brightspot.blog;

import java.util.List;

import brightspot.author.Author;
import com.psddev.cms.db.BoardSearchResultField;
import com.psddev.dari.db.Alteration;
import com.psddev.dari.db.Recordable;

public class BlogPostPageAlteration extends Alteration<BlogPostPage> {

    @BoardSearchResultField
    @Recordable.InternalName("hasAuthors.getAuthors")
    private List<Author> getAuthors;

    @BoardSearchResultField
    @Recordable.InternalName("hasBlog.getBlog")
    private Blog getBlog;
}
