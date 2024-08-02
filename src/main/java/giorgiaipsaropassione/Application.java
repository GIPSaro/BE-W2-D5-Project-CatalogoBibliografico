package giorgiaipsaropassione;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Application {

    public static void main(String[] args) {
        Set<Pubblicazione> catalogo = new HashSet<>();

        System.out.println("-------------Creazione Random di Libri e Riviste-----------------");
        System.out.println("con librearia Faker e LocalDate per data random di pubblicazione (tra 2024 e 1980)");
        Thread thread1 = new Thread(() -> {
            try {
                for (int i = 0; i < 25; i++) {
                    Libro libro = genRandomBook();
                    catalogo.add(libro);

                }

            } catch (Exception e) {
                System.err.println("Errore: " + e.getMessage());
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                for (int i = 0; i < 25; i++) {
                    Rivista rivista = genRandomRivista();
                    catalogo.add(rivista);

                }
            } catch (Exception e) {
                System.err.println("Errore: " + e.getMessage());
                e.printStackTrace();
            }

        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            System.err.println("Errore durante l'esecuzione dei thread: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Numero di pubblicazioni nel catalogo prima dell'eliminazione: " + catalogo.size());
        catalogo.forEach(element -> System.out.println(element.toString()));

        // CHIAMATA PER ELIMINARE LIBRO TRAMITE SCANNER CON RICHIESTA INSERIMENTO ISBN

        eliminaLibro(catalogo);

        System.out.println("Numero di pubblicazioni nel catalogo dopo l'eliminazione: " + catalogo.size());
        catalogo.forEach(element -> System.out.println(element.toString()));

        // CHIAMATA PER CARCARE LIBRO O RIVISTA TRAMITE SCANNER E RICHIESTA INSERIMENDO ISBN
        cercaPubblicazione(catalogo);

        //CHIAMATA PER CERCARE LIBRO O RIVISTRA TRAMITE ANNO DI PUBBLICAZIONE
        cercaPubblicazionePerAnno(catalogo);
    }
    //QUI APPLICO UN METODO RANDOM PER RANDOMIZZARE LA CREAZIONE DI LIBRI E RIVISTE (25 e 25)

    private static Libro genRandomBook() {
        Faker faker = new Faker();
        Random rnd = new Random();

        //RANDOM DI TITOLO E AUTORE E CODICE IBSN DI LUNGHEZZA 11 CIFRE
        long codiceIsbn = faker.number().randomNumber(11, true);
        String title = faker.book().title();
        String autore = faker.book().author();

        //RANDOM DI DATE (intervallo tra 2024-1980)
        LocalDate annoPubblicazione = LocalDate.of(rnd.nextInt(2024 - 1980) + 1980, rnd.nextInt(12) + 1, rnd.nextInt(28) + 1);

        //RANDOM NUM DI PAGINE
        int numPagine = rnd.nextInt((500) + 100);

        //RANDOM PER IL GENERE
        Genere[] generi = Genere.values();
        Genere genere = generi[rnd.nextInt(generi.length)];

        return new Libro(codiceIsbn, title, annoPubblicazione, numPagine, autore, genere);
    }

    private static Rivista genRandomRivista() {
        Faker faker = new Faker();
        Random rnd = new Random();

        //RANDOM DI CODICE IBSN E TITOlO
        long codiceIsbn = faker.number().randomNumber(11, true);
        String title = faker.book().title();

        //RANDOM DI DATE (intervallo tra 2024-1980)
        LocalDate annoPubblicazione = LocalDate.of(rnd.nextInt(2024 - 1980) + 1980, rnd.nextInt(12) + 1, rnd.nextInt(28) + 1);

        //RANDOM NUM DI PAGINE
        int numPagine = rnd.nextInt((500) + 100);

        //RANDOM DELL'ENUM PERIODICITA'
        Periodicita[] periodi = Periodicita.values();
        Periodicita periodicita = periodi[rnd.nextInt(periodi.length)];

        return new Rivista(codiceIsbn, title, annoPubblicazione, numPagine, periodicita);
    }


    // ELIMINAZIONE LIBRO O RIVISTA TRAMITE SCANNER CON RICHIESTA DI INSERIMENTO ISBN

    private static void eliminaLibro(Set<Pubblicazione> catalogo) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci l'ISBN del libro da eliminare: ");
        long isbnDaEliminare = scanner.nextLong();

        Pubblicazione libroDaRimuovere = null;
        for (Pubblicazione pubblicazione : catalogo) {
            if (pubblicazione instanceof Libro && pubblicazione.getCodiceIsbn() == isbnDaEliminare) {
                libroDaRimuovere = pubblicazione;
                break;
            }
        }

        if (libroDaRimuovere != null) {
            catalogo.remove(libroDaRimuovere);
            System.out.println("Libro con ISBN " + isbnDaEliminare + " eliminato.");
        } else {
            System.out.println("Libro con ISBN " + isbnDaEliminare + " non trovato.");
        }
    }

    //METODO PER LA RICERCA DI UN ELEMENTO LIBRO O RIVISTA TRAMITE LA RICHIESTA DI UN ISBN SPECIFICO

    private static void cercaPubblicazione(Set<Pubblicazione> catalogo) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci l'ISBN del libro o rivista da cercare: ");
        long isbnDaCercare = scanner.nextLong();

        for (Pubblicazione pubblicazione : catalogo) {
            if (pubblicazione instanceof Libro && pubblicazione.getCodiceIsbn() == isbnDaCercare) {
                System.out.println("Libro trovato: " + pubblicazione);
                return;
            } else if (pubblicazione instanceof Rivista && pubblicazione.getCodiceIsbn() == isbnDaCercare) {
                System.out.println("Rivista trovata: " + pubblicazione);
                return;
            }
        }

        System.out.println("Nessun libro o rivista trovato con ISBN " + isbnDaCercare);
    }

    //METODO PER CARCARE UN LIBRO UNA RIVISTRA TRAMITE ANNO DI PUBBLICAZIONE

    private static void cercaPubblicazionePerAnno(Set<Pubblicazione> catalogo) {
        Scanner scanner = new Scanner(System.in);
        LocalDate dataDaCercare = null;
        boolean inputValido = false;

        while (!inputValido) {
            System.out.print("Inserisci l'anno di pubblicazione del libro o rivista da cercare: ");
            String input = scanner.next();
            try {
                //ho dovuto usare LocalDate.parse e DateTimeFormatter.ISO_LOCAL_DATE per permettere l'inserimento
                // della data nel formato yyyy-mm-dd
                dataDaCercare = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
                inputValido = true;
            } catch (DateTimeParseException e) {
                System.out.println("Input non valido. Per favore, inserisci una data nel formato yyyy-MM-dd.");
            }
        }
        int annoDaCercare = dataDaCercare.getYear();
        boolean trovata = false;
        for (Pubblicazione pubblicazione : catalogo) {
            if (pubblicazione.getAnnoPubblicazione().getYear() == annoDaCercare) {
                System.out.println("Pubblicazione trovata: " + pubblicazione);
                trovata = true;
            }
        }

        if (!trovata) {
            System.out.println("Nessuna pubblicazione trovata per l'anno " + annoDaCercare);
        }
    }
}