package it.fi.itismeucci;

public class Messaggio {
    String contenuto;
    String destinatario;
    String mittente;
    int cod;

    public Messaggio() {
    }

    public Messaggio(String contenuto, String destinatario, String mittente, int cod) {
        this.contenuto = contenuto;
        this.destinatario = destinatario;
        this.mittente = mittente;
        this.cod = cod;
    }

    public String getContenuto() {
        return this.contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public String getDestinatario() {
        return this.destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMittente() {
        return this.mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public int getCod() {
        return this.cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
    
}
