package it.fi.itismeucci;

public class ThreadOutput extends Thread {
      Comunica comunica;
    public ThreadOutput(Comunica comunica){
        this.comunica = comunica;
    }

    public void run(){
        try {
            comunica.output();
        } catch (Exception e) {}
    }
}
