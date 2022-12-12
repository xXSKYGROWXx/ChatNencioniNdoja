package it.fi.itismeucci;

import java.io.*;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    int portaServer = 8088;
    
    BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    String nome = null;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;

    public void comunica() throws IOException {
        Socket mioSocket = new Socket("127.0.0.1", portaServer);
        outVersoServer = new DataOutputStream(mioSocket.getOutputStream());
        inDalServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
        

        //Inserimento del nome

        while(true){
            System.out.println("Inserisci il nome: ");
            nome = tastiera.readLine();
            Messaggio msg0 = new Messaggio(nome,null,null,0);
            send(msg0);
            Messaggio msgRicevuto = ricevi();
            if(msgRicevuto.getContenuto().equals("sisenatore")){
                System.out.println("Il nome è disponibile");
                break;
            }
            else{
                System.out.println("Il nome non è disponibile, inserirne un altro");
            }
        }
        Comunica C1 = new Comunica(nome);
        ThreadInput ti = new ThreadInput(C1);
        ThreadOutput to = new ThreadOutput(C1);

        to.start();
        ti.start();
    }
    //Serializzazione in un oggetto JSon e invio del messaggio
    private void send(Messaggio msg) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        String msgStringa = objectMapper.writeValueAsString(msg);
        outVersoServer.writeBytes(msgStringa + "\n");
    }

    //Deserializzazione dell'oggetto JSon ricevuto
    private Messaggio ricevi() throws IOException{
        String ricevuto = inDalServer.readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio msg = objectMapper.readValue(ricevuto, Messaggio.class);
        return msg;
    }
}