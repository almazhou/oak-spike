import org.jcrom.annotations.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Page implements Serializable {
    @JcrName
    private String name;
    @JcrPath
    private String path;
    @JcrProperty
    private int id;

    @JcrProperty
    private Map<String,String> map;

    public Page(int id) {
        this.id = id;
        this.name = "jcrom";
        this.path = "/jcrom";
        map = new HashMap<String,String>();
        map.put("whatever","map");
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
