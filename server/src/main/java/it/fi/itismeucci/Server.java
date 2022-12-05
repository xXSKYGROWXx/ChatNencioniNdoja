package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    HashMap<String,Socket> utenti = new HashMap<>();

    public void connetti() throws IOException {

        System.out.println("server partito");
        ServerSocket server = new ServerSocket(8080);

        for (;;) {
            Socket client = server.accept();
            (new ThreadServer(client,utenti)).start();
        }

    }

    
}

