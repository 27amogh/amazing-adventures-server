package student.adventure;

import org.glassfish.grizzly.http.server.HttpServer;

import student.server.AdventureResource;
import student.server.CS126Server;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Conditionally use console or server using command-line arguments.
        HttpServer server = CS126Server.createServer(AdventureResource.class);
        server.start();
        Adventure.main(args);
    }
}