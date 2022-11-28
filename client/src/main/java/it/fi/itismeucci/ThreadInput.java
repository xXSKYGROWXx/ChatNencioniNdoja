package it.fi.itismeucci;

public class ThreadInput extends Thread {
    Comunica comunica;
    public ThreadInput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
            comunica.input();
        } catch (Exception e) {
        }
    }
}
