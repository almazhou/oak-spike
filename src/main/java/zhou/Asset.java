package zhou;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.util.List;

@Node
public class Asset {

    @Field(path=true)
    private String path;

    @Bean
    private Entity entity;

    @Collection
    private List<Entity> subEntities;

    public Asset() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Entity> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(List<Entity> subEntities) {
        this.subEntities = subEntities;
    }
}
