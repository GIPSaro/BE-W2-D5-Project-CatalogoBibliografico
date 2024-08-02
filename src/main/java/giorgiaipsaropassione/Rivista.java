package giorgiaipsaropassione;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Rivista extends Pubblicazione {
    private Periodicita periodicita;

    public Rivista(long codiceIsbn, String titolo, LocalDate annoPubblicazione, int numPagine, Periodicita periodicita) {
        super(codiceIsbn, titolo, annoPubblicazione, numPagine);
        this.setPeriodicita(periodicita);
    }

    public static Rivista fromString(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Parsing Rivista: " + data);

        String[] parts = data.split(" - ");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Formato dati non valido per Rivista: " + data);
        }

        try {
            long codiceIsbn = Long.parseLong(parts[0].split(": ")[1]);
            String titolo = parts[1].split(": ")[1];
            LocalDate annoPubblicazione = LocalDate.parse(parts[2].split(": ")[1], formatter);
            int numPagine = Integer.parseInt(parts[3].split(": ")[1]);
            Periodicita periodicita = Periodicita.valueOf(parts[4].split(": ")[1]);

            return new Rivista(codiceIsbn, titolo, annoPubblicazione, numPagine, periodicita);
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nel parsing dei dati per Rivista: " + e.getMessage() + " - " + data);
        }
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
