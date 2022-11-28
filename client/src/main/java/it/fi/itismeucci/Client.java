package it.fi.itismeucci;

import java.io.*;

public class Client {

    public void comunica() throws IOException {

        Comunica C1 = new Comunica();
        ThreadInput ti = new ThreadInput(C1);
        ThreadOutput to = new ThreadOutput(C1);

        to.start();
        ti.start();
    }
}