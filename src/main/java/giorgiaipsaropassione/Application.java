package giorgiaipsaropassione;

import com.github.javafaker.Faker;

import java.time.LocalDate;
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

        // Mostra il catalogo dopo la possibile eliminazione
        catalogo.forEach(element -> System.out.println(element.toString()));
    }

    private static Libro genRandomBook() {
        Faker faker = new Faker();
        Random rnd = new Random();

        long codiceIsbn = faker.number().randomNumber(11, true);
        String title = faker.book().title();
        LocalDate annoPubblicazione = LocalDate.of(rnd.nextInt(2024 - 1980) + 1980, rnd.nextInt(12) + 1, rnd.nextInt(28) + 1);
        int numPagine = rnd.nextInt((500) + 100);
        String autore = faker.book().author();

        Genere[] generi = Genere.values();
        Genere genere = generi[rnd.nextInt(generi.length)];

        return new Libro(codiceIsbn, title, annoPubblicazione, numPagine, autore, genere);
    }

    private static Rivista genRandomRivista() {
        Faker faker = new Faker();
        Random rnd = new Random();

        long codiceIsbn = faker.number().randomNumber(11, true);
        String title = faker.book().title();
        LocalDate annoPubblicazione = LocalDate.of(rnd.nextInt(2024 - 1980) + 1980, rnd.nextInt(12) + 1, rnd.nextInt(28) + 1);
        int numPagine = rnd.nextInt((500) + 100);

        Periodicita[] periodi = Periodicita.values();
        Periodicita periodicita = periodi[rnd.nextInt(periodi.length)];

        return new Rivista(codiceIsbn, title, annoPubblicazione, numPagine, periodicita);
    }
    // Eliminazione libro tramite scanner e ISBN

    private static void eliminaLibro(Set<Pubblicazione> catalogo) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci l'ISBN del libro da eliminare: ");
        long isbnDaEliminare = scanner.nextLong();

        Pubblicazione libroDaRimuovere = null;
        for (Pubblicazione pubblicazione : catalogo) {
            if (pubblicazione instanceof Libro && ((Libro) pubblicazione).getCodiceIsbn() == isbnDaEliminare) {
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


}
