public class TUI {

    private TUI() {}
    public static void main(String[] args) {

        Dictionary dict;

        if (args[0].equals("--h")) {
            System.out.println("create 'Implementierung'\tLegt ein Dictionary an.");
            System.out.println("r[n] 'Dateiname'\t\tLiest n Eintraege einer Datei in das Dictionary ein.");
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
        else if (args[0].matches("r\\[\\d\\]") && args[1].matches(".*\\.txt")) {
            // TODO: entweder über JFileChooser (ohne Dateiname) oder Zeile für Zeile einlesen und als Eintrag abspeichern
            System.out.println("funktioniert");
        }
        else if (args[0].equals("p")) {
            // TODO: Lösung finden für das evtl. leere Dictionary (vllt mit try-catch)
            if (dict == null) {
                System.out.println("Es muss erst ein neues Dictionary angelegt werden.");
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
        }

    }
}
