import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;

public class Page {
    @JcrName
    private String name;
    @JcrPath
    private String path;
    @JcrProperty
    private int id;

    public Page(int id) {
        this.id = id;
        this.name = "jcrom";
        this.path = "/jcrom";
    }

    public Page() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
