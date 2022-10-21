import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class TUI {

    private static Dictionary<String, String> dic;
    private static Scanner scanner = new Scanner(System.in);

    private TUI() {}

    public static void main(String[] args) throws Exception {


        System.out.println("Welcome to Dictionary TUI");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            commands(input);
        }
    }

    private static void commands(String command) throws Exception {

        String args[] = command.split(" ");

        switch (args[0]) {
            case "create":
                    create(args);
                break;
            case "r":
                read(args);
                break;
            case "p":
                if (dic == null)
                    System.out.println("Use 'create' to create your first Dictionary!");
                else
                    print();
                break;
            case "s":
                if (dic == null)
                    System.out.println("Use 'create' to create your first Dictionary!");
                else
                    search(args);
                break;
            case "i":
                if (dic == null)
                    System.out.println("Use 'create' to create your first Dictionary!");
                else
                    insert(args);
                break;
            case "d":
                if (dic == null)
                    System.out.println("Use 'create' to create your first Dictionary!");
                else
                    remove(args);
                break;
            case "exit":
                scanner.close();
                System.exit(0);
        }
    }

    private static void create(String[] args) {
        System.out.println("Creating new Dictionary");
        if (args[0].equals("HashDictionary")) {
            dic = new HashDictionary(3);
        }
        else if (args[0].equals("BinaryTreeDictionary")) {
            dic = new BinaryTreeDictionary<>();
        }
        else {
            dic = new SortedArrayDictionary();
        }
    }

    private static void print() {
        for (Dictionary.Entry<String, String> v : dic)
            System.out.println(v.getKey() + ": " + v.getValue());
    }

    private static void read(String args[]) throws IOException {

        long start = 0;
        long stop = 0;
        BufferedReader rd;
        if (args.length < 3) {
            try {
                rd = new BufferedReader(new FileReader(args[1]));
                start = System.nanoTime();
                String line = rd.readLine();
                while (line != null) {
                    String entry[] = line.split(" ");
                    dic.insert(entry[0], entry[1]);
                    line = rd.readLine();
                }
                stop = System.nanoTime();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                rd = new BufferedReader(new FileReader(args[2]));
                start = System.nanoTime();
                for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                    String line = rd.readLine();
                    String entry[] = line.split(" ");
                    dic.insert(entry[0], entry[1]);
                }
                stop = System.nanoTime();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        long diff = stop - start;
        System.out.println("Read took " + (diff / 1000000) + "ms");
    }

    private static void search(String[] args) {

        long start = 0;
        long stop = 0;
        try {
            start = System.nanoTime();
            System.out.println(dic.search(args[1]));
            stop = System.nanoTime();
        } catch (NullPointerException e) {
            System.err.println("Wort wurde nicht gefunden!");
        }

        long diff = stop - start;
        System.out.println("Search took " + (diff / 1000) + "µs");
    }

    private static void insert(String[] args) {
        System.out.printf("Adding %s: %s to the Dictionary\n", args[1], args[2]);
        dic.insert(args[1], args[2]);
    }

    private static void remove(String[] args) {
        System.out.printf("Removing %s from Dictionary\n", args[1]);
        dic.remove(args[1]);
    }
}

/**if (args[0].equals("--h")) {
    System.out.println("create 'Implementierung'\tLegt ein Dictionary an.");
    System.out.println("r [n] 'Dateiname'\t\tLiest n Eintraege einer Datei in das Dictionary ein.");
    System.out.println("p\t\t\t\tGibt alle Einträge des Dictionary in der Konsole aus.");
    System.out.println("s 'deutsch'\t\t\tGibt das entsprechende englische Wort aus.");
    System.out.println("i 'deutsch' 'englisch'\t\tFuegt ein neues Wortpaar in das Dictionary ein.");
    System.out.println("d 'deutsch'\t\t\tLoescht einen Eintrag");
    System.out.println("exit\t\t\t\tBeendet das Programm.");
}
else if (args[0].equals("create")) {
    // default: SortedArrayDictionary
    dict = new SortedArrayDictionary<>();
}
else if (args[0].equals("create") && args[1].matches("SortedArrayDictionary|HashDictionary|BinaryTreeDictonary")) {
    if (args[1].equals("SortedArrayDictionary")) {
        dict = new SortedArrayDictionary<>();
    } else if (args[1].equals("HashDictionary")) {
        dict = new HashDictionary<>(3);
    } else if (args[1].equals("BinaryTreeDictionary")) {
        dict = new BinaryTreeDictionary<>();
    }
}
else if (args[0].equals("r") && args[1].matches("\\d") && args[2].matches(".*\\.txt")) {
    BufferedReader rd;
    try {
        rd = new BufferedReader(new FileReader(args[2]));
        String line = rd.readLine();
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            // read next line
            line = rd.readLine();
            String entry[] = line.split(" ");
            if (dict == null) {
                System.out.println("Dictionary wurde noch nicht initialisiert!");
                System.exit(1);
            } else {
                dict.insert(entry[0], entry[1]);
            }
            rd.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
else if (args[0].equals("p")) {
    if (dict == null) {
        System.out.println("Dictionary wurde noch nicht initialisiert!");
        System.exit(1);
    } else {
        for (var e : dict) {
            System.out.println(e);
        }
    }
}
else if (args[0].equals("s") && args[1].matches(".*")) {
    System.out.println(dict.search(args[1]));
}
else if (args[0].equals("i") && args[1].matches(".*") && args[2].matches(".*")) {
    dict.insert(args[1], args[2]);
}
else if (args[0].equals("d") && args[1].matches(".*")) {
    dict.remove(args[1]);
}
else if (args[0].equals("exit")) {
    System.out.println("Hasta la vista!\n");
    System.exit(0);
}
else {
    System.out.println("type 'java TUI --h' for help\n");
    System.exit(0);
}*/