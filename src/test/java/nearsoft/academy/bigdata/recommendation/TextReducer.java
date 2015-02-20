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
    static BufferedWriter writer = null;
    static MemoryIDMigrator memoryIDMigrator;
 
    //creates a file named temp.csv where everythins is parsed and processed as Mahout wants
    public static void textReducer(String path) throws IOException
    {
        //read the input of the file
        FileInputStream fin = new FileInputStream(path);
        GZIPInputStream gzis = new GZIPInputStream(fin);
        InputStreamReader xover = new InputStreamReader(gzis);
        BufferedReader is = new BufferedReader(xover);
        memoryIDMigrator = new MemoryIDMigrator();
     
        //write the output of the file
        String line,currentBlock="";
        File logFile = new File("temp.csv");
        writer = new BufferedWriter(new FileWriter(logFile));
        //int index_line = 0;

        //process and transorm it
        while ((line = is.readLine()) != null){
        //create the block we need 
            //System.out.println(++index_line);
            //++index_line;
            if(line.equals("")){
 
                //thread here
                //<thread>
                try{
                    processBlock(currentBlock);

                }
                catch(ArrayIndexOutOfBoundsException  e)
                {
                    //System.out.println(index_line+":"+currentBlock);
                }
                //</thread>
                currentBlock = "";
                continue;
            }
            else{
                currentBlock += line+'\n';  
            }
            
            
        }
        is.close();
        writer.close();
        
    }

    // process block by block
    private static void processBlock(String block) throws IOException
    {

        //instead of split. use indexOf & substring
        String[] arrayOfLines = block.split("\n");
        write(Long.toString(memoryIDMigrator.toLongID(substr(arrayOfLines[0], ':'))));
        write(",");

        write(Long.toString(memoryIDMigrator.toLongID(substr(arrayOfLines[1], ':'))));
        write(",");

        write(substr(arrayOfLines[4], ':'));
        write("\n");
    }
    //creates a substring using only indexOf and substring
    private static String substr(String line, char limit)
    {
        return line.substring(line.indexOf(limit)+2, line.length());
    } 
    //writes to the file
    private static void write(String line) throws IOException
    {
        writer.write(line);        
    }
    public static void main(String[] args) throws IOException {
        TextReducer.textReducer("movies.txt.gz");
    }
 
}