import com.mongodb.DB;
import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.util.MongoConnection;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.jcrom.Jcrom;
import sun.net.www.MimeTable;
import zhou.Asset;
import zhou.Entity;

import javax.jcr.*;
import java.io.*;
import java.util.*;

public class OakDemo {
    public static void main(String[] args) throws Exception {
        save();
        saveFile();
        useJcrom();
        useBinary();
        Session session = getSession();

        List<Class> classes = new ArrayList<Class>();
        classes.add(PressRelease.class); // Call this method for each persistent class
        classes.add(Asset.class); // Call this method for each persistent class
        classes.add(Entity.class); // Call this method for each persistent class

        String[] files = {
                "jcrMapping.xml"
        };

        Mapper mapper = new AnnotationMapperImpl(classes);
        ObjectContentManager ocm =  new ObjectContentManagerImpl(session, mapper);


        savePressRelease(ocm);

        saveAsset(ocm);
    }

    private static void saveAsset(ObjectContentManager ocm) {
        Asset asset = new Asset();
        asset.setPath("/asset");
        Entity entity1 = new Entity();
        Map<String, Entity> map = new HashMap<String, Entity>();
        map.put("hasmap", new Entity());
        entity1.setEntityMap(map);
        asset.setEntity(entity1);
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity());
        entity1.setSubEntities(entities);
        ocm.insert(asset);
        ocm.save();
        Asset ocmAsset = (Asset) ocm.getObject("/asset");

        Entity entity = ocmAsset.getEntity();
        List<Entity> subEntities = ocmAsset.getEntity().getSubEntities();
        System.out.println(entity.getPath());
    }

    private static void savePressRelease(ObjectContentManager ocm) {
        System.out.println("Insert a press release in the repository");
        PressRelease pressRelease = new PressRelease();
        pressRelease.setPath("/newtutorial");
        pressRelease.setTitle("This is the first tutorial on OCM");
        pressRelease.setPubDate(new Date());
        pressRelease.setContent("Many Jackrabbit users ask to the dev team to make a tutorial on OCM");

        ocm.insert(pressRelease);
        ocm.save();

// Retrieve
        System.out.println("Retrieve a press release from the repository");
        pressRelease = (PressRelease) ocm.getObject("/newtutorial");
        System.out.println("PressRelease title : " + pressRelease.getTitle());

// Delete
        System.out.println("Remove a press release from the repository");
        ocm.remove(pressRelease);
        ocm.save();
    }

    public static void save() throws Exception {
        MongoConnection connection = new MongoConnection("127.0.0.1", 27017, "Adobe-MongoDB");
        DB db = connection.getDB();
        Session session = getSession();
        Node rootNode =  session.getRootNode();
        Node hello = rootNode.addNode("hello");
        Node world = hello.addNode("world");
        HashMap<String, String> map = new HashMap<String, String>();
        world.setProperty("message","hello world whatever");
        session.save();

        Node node = rootNode.getNode("hello/world");
        System.out.println(node.getPath());
        System.out.println(node.getProperty("message").getString());

    }

    private static Session getSession() throws RepositoryException {
        Repository repo = new Jcr().createRepository();
        return repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
    }

    public static void saveFile() throws RepositoryException, IOException {
        Session session = getSession();
        Node rootNode = session.getRootNode();
        File file = new File("rose.txt");
        MimeTable mt = MimeTable.getDefaultTable();
        String mimeType = mt.getContentTypeFor(file.getName());
        if (mimeType == null) mimeType = "application/octet-stream";

        Node fileNode = rootNode.addNode(file.getName(), "nt:file");
        Node resNode = fileNode.addNode("jcr:content", "nt:resource");
        resNode.setProperty("jcr:mimeType", mimeType);
        resNode.setProperty("jcr:encoding", "");
        resNode.setProperty("jcr:data", new FileInputStream(file));
        Calendar lastModified = Calendar.getInstance();
        lastModified.setTimeInMillis(file.lastModified());
        resNode.setProperty("jcr:lastModified", lastModified);

        session.save();

        Node gotNode = rootNode.getNode(file.getName());

        Node contentNode = gotNode.getNode("jcr:content");

        InputStream stream = contentNode.getProperty("jcr:data").getBinary().getStream();

        File targetFile = new File("src/main/resources/targetFile.tmp");

        FileUtils.copyInputStreamToFile(stream, targetFile);


    }

    public static void useJcrom() throws RepositoryException {
        Jcrom jcrom = new Jcrom();
        jcrom.map(Page.class);
        Session session = getSession();
        Page page = new Page(2);
        Node rootNode = session.getRootNode();
        Node jcrom2 = rootNode.addNode("jcromParent");
        jcrom.addNode(jcrom2,page);
        session.save();

        Node jcrom1 = rootNode.getNode("jcromParent").getNode("jcrom");
        Page page1 = jcrom.fromNode(Page.class, jcrom1);

        System.out.println(page.getId());
        System.out.println(page1.getId());
    }

    public static void useBinary() throws RepositoryException, IOException, ClassNotFoundException {
        Session session = getSession();

        Node rootNode = session.getRootNode();

        Node binary = rootNode.addNode("binary");

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page",new Page(5));

        OutputStream serialize = Serializer.serialize(map);

//        binary.setProperty("map", (Binary) serialize);

        session.save();

//        InputStream stream = rootNode.getNode("binary").getProperty("map").getBinary().getStream();
//
//        Object deserialize = Serializer.deserialize(stream);
//
//        System.out.println(deserialize);

    }

}
