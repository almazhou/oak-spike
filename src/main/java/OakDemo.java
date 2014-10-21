import com.mongodb.DB;
import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.util.MongoConnection;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import sun.net.www.MimeTable;

import javax.jcr.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class OakDemo {
    public static void main(String[] args) throws Exception {
        save();
        saveFile();
        Session session = getSession();

        List<Class> classes = new ArrayList<Class>();
        classes.add(PressRelease.class); // Call this method for each persistent class

        Mapper mapper = new AnnotationMapperImpl(classes);
        ObjectContentManager ocm =  new ObjectContentManagerImpl(session, mapper);

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


}
