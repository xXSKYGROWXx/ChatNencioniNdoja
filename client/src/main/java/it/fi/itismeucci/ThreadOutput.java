package it.fi.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadOutput extends Thread {
    BufferedReader tastiera;
    DataOutputStream out;
    String nome;
    public ThreadOutput(BufferedReader tastiera,DataOutputStream out,String nome){
        this.tastiera = tastiera;
        this.out = out;
        this.nome = nome;
    }

    public void run(){
        try {
            output();
        } catch (Exception e) {}
    }
    public void output() throws IOException {
        
        //Scelta di un'opzione tramite codice
       
            System.out.println("Cod = 1 messaggio");
            System.out.println("Cod = 2 richiedi lista");
            System.out.println("Cod = 3 disconessione");
            System.out.print("Inserire il codice: ");

            while(true){
            switch (Integer.parseInt(tastiera.readLine())) {
                //Invio di un messaggio ad un altro client
                case 1:
                    Messaggio msg1 = new Messaggio(null,null,nome,1);                  
                    System.out.println("Inserire il nome del destinatario(BROADCAST per mandalo a tutti)");
                    msg1.setDestinatario(tastiera.readLine());
                    System.out.println("Inserire il contenuto del messaggio");
                    msg1.setContenuto(tastiera.readLine());
                    send(msg1);
                    break;
                //Stampa della lista dei client connessi
                case 2:
                    Messaggio msg2 = new Messaggio(null,null,null,2);
                    send(msg2);
                    //while(controllo){}
                    //System.out.println(msgRicevuto.getContenuto()); //Nel server dobbiamo inserire la lista in contenuto
                    break;
                //Disconnessione del client dalla chat
                case 3:
                    Messaggio msg3 = new Messaggio(null,null,nome,3);
                    send(msg3);
                    break;
                //Messaggio di errore per codice errato
                default:
                System.out.println("Codice sbagliato");
                    break;
            }
        }
    }

    //Serializzazione in un oggetto JSon e invio del messaggio
    private void send(Messaggio msg) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        String msgStringa = objectMapper.writeValueAsString(msg);
        out.writeBytes(msgStringa + "\n");
    }

}
