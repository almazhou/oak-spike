import com.mongodb.DB;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.util.MongoConnection;
import sun.net.www.MimeTable;

import javax.jcr.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;

public class OakDemo {
    public static void main(String[] args) throws Exception {
        save();
        saveFile();
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

    public static void saveFile() throws RepositoryException, FileNotFoundException {
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

        contentNode.getProperty("jcr:mimeType");
    }


}
