/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Configure
 */
package librecatalog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author van
 */
class Configure
{
    private static Properties config = new Properties();
    static boolean firstRun = true;
    private static int userLevel;
    
    public static String[] main (String[] args)
    {
        String filename = "config.properties";
        if (args.length>0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].startsWith("--config")) {
                    filename = args[idx].split("=")[1];
                }
        String path = getPath(filename);
        try
        {
            FileInputStream propFile = new FileInputStream( path );
            config.load(propFile);
            firstRun=false;
        }
        catch (FileNotFoundException fnfe)
        {
            if (args.length == 0) {
                args    = new String[1];
                args[0] = "--first-run";
            } else {
                boolean flagSet = false;
                for (int idx=0;idx < args.length; idx++)
                    if (args[idx].equals("--first-run")||args[idx].equals("-F")) {
                        flagSet=true;
                        break;
                    }
                if (!flagSet) {
                    String[] temp = new String[args.length+1];
                    System.arraycopy(args, 0, temp, 0, args.length);
                    temp[args.length]="--first-run";
                    args = temp;
                }
            }
            System.out.println("First run: or config file failure.");
            UserInterface.Error(101);
            if (UserInterface.productSetupKey())
                createConfig(filename);
            else
                System.exit(0);
        }
        catch (IOException ioe)
        {
            System.out.println("unexpected error:");
            ioe.printStackTrace(System.out);
            UserInterface.Error(102);
        }
        return args;
    }

    private static void writeConfig(String path)
    {
        
        try {
            FileOutputStream propFile = new FileOutputStream( path );
            config.store(propFile, "");
        }
        catch (FileNotFoundException fnfe)
        {
            UserInterface.Error(104);
        }
        catch (IOException ioe)
        {
            UserInterface.Error(104);
        }
    }
    
    static String getPath(String filename)
    {
        String path = "";
        if (!filename.startsWith("/")||!filename.startsWith(".")||!filename.startsWith(":\\",1)) {
            path = System.getProperty("java.class.path");
            if (path.endsWith(".jar"))
            {
                int lastSlash = path.lastIndexOf(System.getProperty("file.separator"));
                path = path.substring(0, lastSlash);
            }
            if (!path.endsWith(System.getProperty("file.separator")))
                path += System.getProperty("file.separator");
        }
        return path+filename;
    }

    private static void createConfig(String filename)
    {
        String path = getPath(filename);
        config.setProperty("PatronDB",Configure.getPath("Patrons.dbflat"));
        config.setProperty("ItemDB",Configure.getPath("Items.dbflat"));
        config.setProperty("FineDB",Configure.getPath("Fines.dbflat"));
        config.setProperty("AvailabilityDB",Configure.getPath("ItemAvailability.dbflat"));
        writeConfig(path);
    }
    
    public static String getProp(String key) {
        return config.getProperty(key);
    }

    static void setUserLevel(int value)
    {
        userLevel = value;
    }
    static int getUserLevel() {
        return userLevel;
    }
}
