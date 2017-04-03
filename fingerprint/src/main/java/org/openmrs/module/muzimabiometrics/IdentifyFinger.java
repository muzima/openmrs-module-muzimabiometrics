import jlibfprint.*;
import java.io.*;
public class IdentifyFinger {
    public static void main(String[] args) {
        JlibFprint jlibfprint = new JlibFprint();
        JlibFprint.fp_print_data pd1, pd2;
        int matchValue;
        try
        {
            System.out.println("Place left hand ring finger for identification...");
            pd1 = jlibfprint.enroll_finger();
              InputStream file = new FileInputStream("templates/elly.ser");
      				InputStream buffer = new BufferedInputStream(file);
      				ObjectInput input = new ObjectInputStream(buffer);
      				pd2 = (JlibFprint.fp_print_data)input.readObject();
              matchValue = JlibFprint.img_compare_print_data(pd1, pd2);

              System.out.println(matchValue);
              if (matchValue > JlibFprint.BOZORTH_THRESHOLD)
              {
                  System.out.println("[OK] The two fingerprints are compatible!");
              }
              else
              {
                  System.out.println("[FAIL] The two fingerprints are not compatible!");
              }

        }
        catch(ClassNotFoundException e) {
  				System.err.println("Failed to deserialize as class wasn't found");
  			}
  			catch(IOException e) {
  			}
        catch (JlibFprint.EnrollException e)
        {
            System.err.format("Enroll Exception [%d]\n", e.enroll_exception);
            e.printStackTrace();
        }
    }
}
