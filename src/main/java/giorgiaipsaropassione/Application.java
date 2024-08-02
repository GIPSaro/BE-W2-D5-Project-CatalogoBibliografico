package giorgiaipsaropassione;

import com.github.javafaker.Faker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Application {

    public static void main(String[] args) throws IOException {
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

        // CHIAMATA PER CERCARE LIBRO TRAMITE AUTORE
        cercaPubblicazionePerAutore(catalogo);

        //CHIAMATA PER LEGGERE E SALVARE SU DISCO
        salvaSuDisco(catalogo);
        System.out.println(leggereFile());

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

    //METODO PER CARCARE UN LIBRO O UNA RIVISTRA TRAMITE ANNO DI PUBBLICAZIONE

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

    private static void cercaPubblicazionePerAutore(Set<Pubblicazione> catalogo) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome dell'autore del libro o rivista da cercare: ");
        String autoreDaCercare = scanner.nextLine();

        boolean trovata = false;
        for (Pubblicazione pubblicazione : catalogo) {
            if (pubblicazione instanceof Libro && ((Libro) pubblicazione).getAutore().equalsIgnoreCase(autoreDaCercare)) {
                System.out.println("Libro trovato: " + pubblicazione);
                trovata = true;
            }
        }

        if (!trovata) {
            System.out.println("Nessun libro o rivista trovato con autore " + autoreDaCercare);
        }
    }

    // METODO PER SCRIVERE SU DISCO
    public static void salvaSuDisco(Set<Pubblicazione> catalogo) {
        File file = new File("Catalogo.txt");
        try (FileWriter writer = new FileWriter(file)) {
            for (Pubblicazione pubblicazione : catalogo) {
                writer.write(pubblicazione.toString() + "\n");
            }
            System.out.println("Catalogo salvato su disco con successo.");
            System.out.println("Il catalogo è di lunghezza: " + catalogo.size());
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del catalogo: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // METODO PER LEGGERE DA DISCO
    public static Set<Pubblicazione> leggereFile() {
        Set<Pubblicazione> catalogo = new HashSet<>();
        File file = new File("Catalogo.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    if (line.startsWith("ISBN: ")) {
                        String[] parts = line.split(" - ");
                        if (parts.length > 1) {
                            String type = parts[0].split(": ")[0];
                            if (type.equals("ISBN")) {
                                if (parts[4].contains("Periodico")) {
                                    catalogo.add(Rivista.fromString(line));
                                } else {
                                    catalogo.add(Libro.fromString(line));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della linea: " + line + " - " + e.getMessage());
                }
            }
            System.out.println("Catalogo letto dal disco con successo.");
            System.out.println("Il catalogo è di lunghezza: " + catalogo.size());
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del catalogo: " + e.getMessage());
        }
        return catalogo;
    }

}