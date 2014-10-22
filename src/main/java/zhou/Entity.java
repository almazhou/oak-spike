package zhou;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.util.List;
import java.util.Map;

@Node
public class Entity {
    @Field(path=true)
    private String path;

    @Collection
    private List<Entity> subEntities;

    @Collection
    private Map<String,Entity> entityMap;

    public Entity() {
    }

    public Entity(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Entity> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(List<Entity> subEntities) {
        this.subEntities = subEntities;
    }

    public Map<String, Entity> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Map<String, Entity> entityMap) {
        this.entityMap = entityMap;
    }
}
