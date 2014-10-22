import org.apache.jackrabbit.mongomk.util.MongoConnection;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.kernel.KernelNodeStore;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.query.QueryEngineSettings;

import javax.jcr.Repository;

public class MkRepository {
    private Repository repository;

    public MkRepository() throws Exception {
        String host = System.getProperty("mongo.host", "127.0.0.1");
        int port = Integer.getInteger("mongo.port", 27017);
        String dbName = System.getProperty("mongo.db", "MongoMKDB");
        MongoConnection connection = new MongoConnection(host, port, dbName);
        DocumentMK m = new DocumentMK.Builder()
                .setClusterId(1)
                .memoryCacheSize(64 * 1024 * 1024)
                .setMongoDB(connection.getDB())
                .open();
        QueryEngineSettings qs = new QueryEngineSettings();
        qs.setFullTextComparisonWithoutIndex(true);
        this.repository = new Jcr(new KernelNodeStore(m)).with(qs).createRepository();
    }

    public Repository getRepository() {
        return repository;
    }
}
