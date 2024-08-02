package giorgiaipsaropassione;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Libro extends Pubblicazione {
    private String autore;
    private Genere genere;

    public Libro(long codiceIsbn, String titolo, LocalDate annoPubblicazione, int numPagine, String autore, Genere genere) {
        super(codiceIsbn, titolo, annoPubblicazione, numPagine);
        this.setAutore(autore);
        this.setGenere(genere);
    }

    public static Libro fromString(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Parsing Libro: " + data);

        String[] parts = data.split(" - ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Formato dati non valido per Libro: " + data);
        }

        try {
            long codiceIsbn = Long.parseLong(parts[0].split(": ")[1]);
            String titolo = parts[1].split(": ")[1];
            LocalDate annoPubblicazione = LocalDate.parse(parts[2].split(": ")[1], formatter);
            int numPagine = Integer.parseInt(parts[3].split(": ")[1]);
            String autore = parts[4].split(": ")[1];
            Genere genere = Genere.valueOf(parts[5].split(": ")[1]);

            return new Libro(codiceIsbn, titolo, annoPubblicazione, numPagine, autore, genere);
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nel parsing dei dati per Libro: " + e.getMessage() + " - " + data);
        }
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
