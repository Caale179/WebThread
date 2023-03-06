package sujet;

import java.util.ArrayList;
import java.util.List;

import static sujet.Tools.*;


public class WebGrep {
    static final List<String> dejaFait = new ArrayList<>();
    static int nbThreads = 0;

    public static void main(String[] args) {
        // Initialize the program using the options given in argument
        if (args.length == 0)
            initialize("-cT --threads=30000 Nantes https://fr.wikipedia.org/wiki/Nantes");
        else initialize(args);

        //Generate the given number of Threads or less if needed
        for (int i = 0; i < numberThreads(); i++) {
            if (StartingURLEmpty()) {
                break;
            } else {
                nbThreads ++;
                Thread thread = new WebThread();
                thread.start();
            }
        }
    }

    //synchronized because can be used by all the threads.
    //returns the last URL in the list
    public static synchronized String lastAddress() {
        if (!startingURL().isEmpty()) {
            dejaFait.add(startingURL().get(startingURL().size() - 1));
            return startingURL().remove(startingURL().size() - 1);
        }else return "";
    }

    //returns the list of the added Strings.
    public static synchronized List<String> addAddress(List<String> refs) {
        List<String> ajoutes = new ArrayList<>();
        for (String href : refs) {
            //must check if it ain't already done.
            // and if it's not already in the TO-DO list
            if (!dejaFait.contains(href) && !startingURL().contains(href)) {
                startingURL().add(href);
                ajoutes.add(href);
            }
        }
        return ajoutes;
    }
    //explicit....
    public static synchronized int getNbThreads(){
        return nbThreads;
    }

    public static synchronized boolean StartingURLEmpty(){
        return startingURL().isEmpty();
    }


}