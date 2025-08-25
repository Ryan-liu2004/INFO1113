/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

/**
 * Provides basic input/output handling, logging, and state saving/loading capabilities.
 * Supports input caching, logging user commands to a file, and restoring program state from a file.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Super {
    private static Scanner scan;
    private final static String LOG_PATH="log";
    private static PrintWriter logwriter;
    private static List<String> save_cache, load_cache;
    private final static int cache_MAXN=100;
    private static boolean end_load, initialized=false;
    public String mode="guest";

    /**
     * Constructor. Initializes input scanner and logging system.
     * Ensures initialization occurs only once.
     */
    public Super(){
        if(initialized) return;
        initialized=true;
        scan=new Scanner(System.in);
        try{
            logwriter = new PrintWriter(new FileWriter(LOG_PATH));
            logwriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        save_cache=new ArrayList<>();
        end_load=true;
    }

    /**
     * Resets the scanner to standard input. For testing purposes.
     */
    public static void testing_resetinput() {scan=new Scanner(System.in);}
    //public void print(Object str) {System.out.print(str);}
    //public void println(Object str) {System.out.println(str);}

    /**
     * Prints a message without a newline, unless in loading mode.
     * @param str the object to print
     */
    public void print(Object str) {if(end_load) System.out.print(str);}

    /**
     * Prints a message with a newline, unless in loading mode.
     * @param str the object to print
     */
    public void println(Object str) {if(end_load) System.out.println(str);}

    /**
     * Throws a runtime exception with a custom message.
     * @param message the error message
     */
    public void throwRE(String message) {throw new RuntimeException(message);}

    /**
     * Reads the next line from user input or load cache, trimming whitespace.
     * Automatically caches the input if not in loading mode.
     * @return the trimmed line of input
     */
    public String nextLine(){
        if(!end_load){
            if(load_cache.size()!=0) return load_cache.remove(0);
            end_load=true;
            print("Data loaded successfully\nPlease enter the 'help' to see the available commands\n> ");
        }
        String line=scan.nextLine().trim();
        cache_write(line);
        return line;
    }

    /**
     * Writes the current save cache to the log file.
     * @param mode true to append, false to overwrite
     */
    private void log_write(boolean mode){
        try{
            logwriter = new PrintWriter(new FileWriter(LOG_PATH, mode));
            for(String str: save_cache) logwriter.println(str);
            save_cache.clear();
            logwriter.close();
        }catch(IOException  e){
            e.printStackTrace();
        }
    }
    /**
     * Writes the current save cache to the log file in append mode.
     */
    private void log_write() {log_write(true);}

    /**
     * Caches a line of user input for later saving. Ignores commands starting with 'save' or 'load'.
     * @param line the user input line
     */
    private void cache_write(String line){
        if(line.startsWith("save") || line.startsWith("load")) return; 
        //ignore the save and load command
        save_cache.add(line);
        if(save_cache.size()<=cache_MAXN) return;
        log_write();
    }

    /**
     * Saves the current state (including input cache and random seed) to a specified file.
     * @param path the file path to save to
     * @param seed the random seed to save
     */
    public void com_save(String path, long seed){
        //read log and write into specified file
        try{
            PrintWriter writer = new PrintWriter(path);
            log_write();
            writer.println(seed);
            try{
                Scanner f_scan=new Scanner(new File(LOG_PATH));
                while(f_scan.hasNextLine()) writer.println(f_scan.nextLine());
                f_scan.close();
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            writer.close();
            println("Save Successfully");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads a previously saved state (including random seed and cached input) from a specified file.
     * @param path the file path to load from
     * @return the loaded random seed, or null if loading failed
     */
    public Long com_load(String path){
        Scanner f_scan;
        try{
            if(mode.equals("guest")) f_scan=new Scanner(new File(path));
            else f_scan=new Scanner(new File(System.getProperty("user.dir")+"/src/test/resources/citadels/"+path));
        }catch(FileNotFoundException e) {
            //e.printStackTrace();
            return null;
        }
        long seed=Long.parseLong(f_scan.nextLine());  //load random seed
        load_cache=new ArrayList<>();
        while(f_scan.hasNextLine()) load_cache.add(f_scan.nextLine());
        f_scan.close();
        save_cache.clear();
        save_cache = new ArrayList<>(load_cache);
        log_write(false);
        end_load=false;
        return seed;
    }
}
