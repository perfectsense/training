package training;

import java.util.Optional;

import com.psddev.cms.db.Directory;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.cms.view.ViewModel;

@JsonView
@ViewInterface
public class PathViewModel extends ViewModel<Directory.Path> {

    @ViewKey("path")
    public String getPath() {
        return model.getPath();
    }

    @ViewKey("pathType")
    public String getPathType() {
        return Optional.ofNullable(model.getType())
            .map(Directory.PathType::name)
            .orElse(Directory.PathType.PERMALINK.name());
    }
}
