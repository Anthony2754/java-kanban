import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
        new KVServer().start();
    }
}
