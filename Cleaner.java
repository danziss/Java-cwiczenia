
import java.util.*;
import java.io.*;


public class Cleaner implements CleanerInterface {
    @Override
    public void removeMain(String filename) {

        try {

            File inFile = new File(filename);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(filename));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            Stack stos = new Stack();
            String line;
            boolean flag = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("public")&&line.contains("static")&&line.contains("void")&&line.contains("main(String[] ")){
                  stos.push("{");
              }

              if (!stos.empty()){
                  for (char c: line.toCharArray()){
                      if (c == '{'){
                          stos.push("{");
                      }
                      if (c == '}'){
                          stos.pop();
                      }
                      if (stos.empty() && c == ';'){
                          flag = true;
                      }
                  }

              }
              if (flag){
                  flag = false;
                  continue;
              }

              if (stos.empty()){
                  pw.println(line);
                  pw.flush();
              }

            }

            pw.close();
            br.close();

            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");



        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
