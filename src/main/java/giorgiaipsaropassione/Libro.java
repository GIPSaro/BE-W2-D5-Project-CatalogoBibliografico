package giorgiaipsaropassione;

import java.time.LocalDate;

public class Libro extends Pubblicazione {
    private String autore;
    private Genere genere;

    public Libro(long codiceIsbn, String titolo, LocalDate annoPubblicazione, int numPagine, String autore, Genere genere) {
        super(codiceIsbn, titolo, annoPubblicazione, numPagine);
        this.setAutore(autore);
        this.setGenere(genere);
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    @Override
    public String toString() {
        return "ISBN: " + getCodiceIsbn() +
                " - Title: " + getTitolo() +
                " - Anno Pubblicazione: " + getAnnoPubblicazione() +
                " - Pagine: " + getNumPagine() +
                " - Autore: " + getAutore() +
                " - Genere: " + getGenere();
    }
}
