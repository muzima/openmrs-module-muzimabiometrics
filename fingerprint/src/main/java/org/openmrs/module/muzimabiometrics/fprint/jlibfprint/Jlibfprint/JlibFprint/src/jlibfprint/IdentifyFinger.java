/*
 * Jlibfprint
 * Copyright (C) 2012 Fabio Scippacercola <nonplay.programmer@gmail.com>
 *                    Agostino Savignano <savag88@gmail.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package jlibfprint;
import java.io.*;

/**
 *  A simple execution can be used to test the library.
 * @author agostino
 */
public class IdentifyFinger {
    /**
     * @param args the command line arguments
     */
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
