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
