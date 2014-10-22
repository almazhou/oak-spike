import java.io.*;

public class Serializer {
    public static OutputStream serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return o;
    }

    public static Object deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(inputStream);
        return o.readObject();
    }
}