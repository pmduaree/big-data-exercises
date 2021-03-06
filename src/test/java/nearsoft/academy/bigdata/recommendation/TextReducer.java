package nearsoft.academy.bigdata.recommendation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
 
public class TextReducer {
    private BufferedWriter writer = null;
    private MemoryIDMigrator memoryIDMigrator;
    private String path;
    private int numberOfLines = 0;
    public TextReducer(String path , MemoryIDMigrator m)
    {
        this.path = path;
        memoryIDMigrator = m;
    }
    //creates a file named temp.csv where everythins is parsed and processed as Mahout wants
    public void clean() throws IOException
    {
        //read the input of the file
        FileInputStream fin = new FileInputStream(path);
        GZIPInputStream gzis = new GZIPInputStream(fin);
        InputStreamReader xover = new InputStreamReader(gzis);
        BufferedReader is = new BufferedReader(xover);
     
        //write the output of the file
        String line,currentBlock="";
        File logFile = new File("temp.csv");
        writer = new BufferedWriter(new FileWriter(logFile));
        //int index_line = 0;
        //process and transform it
        line = is.readLine();
        while (line != null ){
            //first line is product ID
            String productId = substr(line, ':');
            line = is.readLine();
            //second line is user ID
            String userId = substr(line, ':');
            line = is.readLine();

            //read until score's line
            while(line == null || line.equals("") || !line.matches("^review/score(.*)"))
                line = is.readLine();


            if(line == null || line.equals("") || !line.matches("^review/score(.*)")) //there was no score
            {
                while(line == null || !line.equals(""))
                    line = is.readLine();
                continue;
            }
            //the curret line is the score
            String score = substr(line, ':');
            //store to file
            //<thread> if possible
            cleanBlock(userId, productId, score);
            //</thread>
            //read until next paragraph
            while(line == null || !line.equals(""))
                line = is.readLine();

            //read the next line (should be the product id or null)
            line = is.readLine();
        }
        is.close();
        writer.close();
        
    }

    // clean the file
    private void cleanBlock(String userId, String productId, String score) throws IOException
    {
        //instead of split. use indexOf & substring

        //write the user id 
        write(Long.toString(memoryIDMigrator.toLongID(userId.trim())));
        write(",");

        //we need to store the product id as String and long, using a map:
        String productString = substr(productId.trim(), ':');
        long productLong = memoryIDMigrator.toLongID(productString);
        memoryIDMigrator.storeMapping(productLong, productString);

        //write to memory the product long id
        write(Long.toString(productLong));
        write(",");

        //wrie to a file the score of the review
        write(substr(score, ':'));
        write("\n");
        numberOfLines++;
    } 
    //number of lines = number of reviews
    public int getNumberOfLines()
    {
        return numberOfLines;
    }
    //creates a substring using only indexOf and substring
    private String substr(String line, char limit)
    {
        return line.substring(line.indexOf(limit)+1, line.length());
    } 
    //writes to the file
    private void write(String line) throws IOException
    {
        writer.write(line);        
    }
 
}