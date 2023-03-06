package sujet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static sujet.Tools.*;
import static sujet.WebGrep.*;

public class WebThread extends Thread {

    private final ReentrantLock lock = new ReentrantLock(true);
    private String firstAddress;

    public WebThread(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public WebThread() {
        this.firstAddress = "";
    }

    @Override
    public void run() {
        //check if there is still a URL
        //Thanks to that, the pgm thread will kill itself when there is no more URLs (suicide XD)
        while (!StartingURLEmpty()) {
            String address;
            if (!this.firstAddress.equals("")) {
                address = firstAddress;
                lock.lock();
                dejaFait.add(firstAddress);
                lock.unlock();

                firstAddress = "";
            } else {
                address = lastAddress();
            }

            ParsedPage p = null;
            try {
                p = parsePage(address);
            } catch (IOException e) {
                //Instead of printing on the console
                //we'll write the errors in errors.txt
                File file = new File("errors.txt");
                try {
                    lock.lock();
                    if (!file.exists()) file.createNewFile();
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    fw.write(address + ": "+e +"\n\n");
                    fw.close();
                    lock.unlock();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (p != null) {
                // Check if matches were found
                if (!p.matches().isEmpty()) {
                    // Print the output
                    print(p);
                    //aFaire is the list of addresses in p but that aren't yet searched.
                    List<String> aFaire = addAddress(p.hrefs());
                    for (String addresse : aFaire) {
                        //for all the new addresses, a new thread is created if possible.
                        // -> without exceeding the limit
                        if (getNbThreads() < numberThreads()) {
                            lock.lock();
                            nbThreads++;
                            lock.unlock();
                            Thread threadRecursif = new WebThread(addresse);
                            threadRecursif.start();
                        }
                    }
                }
            }
        }
    }
}