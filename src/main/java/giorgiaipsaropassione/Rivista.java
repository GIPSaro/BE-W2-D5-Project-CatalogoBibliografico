package giorgiaipsaropassione;

import java.time.LocalDate;

public class Rivista extends Pubblicazione {
    private Periodicita periodicita;

    public Rivista(long codiceIsbn, String titolo, LocalDate annoPubblicazione, int numPagine, Periodicita periodicita) {
        super(codiceIsbn, titolo, annoPubblicazione, numPagine);
        this.setPeriodicita(periodicita);
    }

    public Periodicita getPeriodicita() {
        return periodicita;
    }

    public void setPeriodicita(Periodicita periodicita) {
        this.periodicita = periodicita;
    }

    @Override
    public String toString() {
        return "ISBN: " + getCodiceIsbn() +
                " - Title: " + getTitolo() +
                " - Anno Pubblicazione: " + getAnnoPubblicazione() +
                " - Pagine: " + getNumPagine() +
                " - Periodico: " + getPeriodicita();
    }
}
