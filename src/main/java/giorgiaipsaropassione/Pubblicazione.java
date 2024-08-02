package giorgiaipsaropassione;

import java.time.LocalDate;

public class Pubblicazione {
    private long codiceIsbn;
    private String titolo;
    private LocalDate annoPubblicazione;
    private int numPagine;

    public Pubblicazione(long codiceIsbn, String titolo, LocalDate annoPubblicazione, int numPagine) {
        super();
        this.setCodiceIsbn(codiceIsbn);
        this.setTitolo(titolo);
        this.setAnnoPubblicazione(annoPubblicazione);
        this.setNumPagine(numPagine);


    }

    public long getCodiceIsbn() {
        return codiceIsbn;
    }

    public void setCodiceIsbn(long codiceIsbn) {
        this.codiceIsbn = codiceIsbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public LocalDate getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(LocalDate annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public int getNumPagine() {
        return numPagine;
    }

    public void setNumPagine(int numPagine) {
        this.numPagine = numPagine;
    }
}
