package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static HashMap<String,ThreadServer> utenti;

    public void connetti() throws IOException {

        System.out.println("server partito");
        ServerSocket server = new ServerSocket(8088);

        for (;;) {
            Socket client = server.accept();
            new ThreadServer(client).start();
        }

    }

    
}

