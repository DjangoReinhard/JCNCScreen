package de.schwarzrot.nml;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;


public class CheckBufferDescriptor implements Runnable {
   public CheckBufferDescriptor(String arg) {
      bufDesc   = new BufferDescriptor();
      offsets   = new HashMap<String, Integer>();
      keys      = new SortedList<String>(GlazedLists.eventList(bufDesc.keySet()));
      errorKeys = new ArrayList<String>();
      this.arg  = new File(arg);
   }


   @Override
   public void run() {
      readNativeDescription();
      //      tellDescriptor();
      checkDescriptors();
      System.out.flush();

      if (bufDesc.keySet().contains(BufferDescriptor.ToolTable)) {
         System.err.println("\nNML-Status contains ToolTable!");
      } else {
         System.err.println("\nNML-Status has NO ToolTable!");
      }
      System.err.println("\tcheck >BufferDescriptor.nmlHasToolTable< is in sync"
            + " with linuxcnc compile-switch TOOL_NML\n");
      if (errorKeys.size() > 0) {
         System.err.println("BufferDescriptor failed on these keys:");
         for (String k : errorKeys) {
            System.err.println("\t" + k);
         }
      } else {
         System.err.println("BufferDescriptor is upToDate");
      }
   }


   protected void checkDescriptors() {
      BufferEntry e;

      for (String k : keys) {
         e = bufDesc.get(k);

         if (e == null) {
            errorKeys.add(k);
            System.err.println("missing BufferEntry for key [" + k + "]");
         } else {
            if (!offsets.containsKey(k)) {
               errorKeys.add(k);
               System.err.println("missing native offset for key [" + k + "]");
            } else {
               Integer o = offsets.get(k);

               if (o != e.offset) {
                  errorKeys.add(k);
                  System.err.println(k + " offset differs: src: " + o + " <> dst: " + e.offset);
               } else {
                  System.out.println(k + " OK");
                  System.out.flush();
               }
            }
         }
      }
   }


   protected void readNativeDescription() {
      File dumpBufDesc = new File("../native", "DumpBufDesc");

      if (!dumpBufDesc.exists() || !dumpBufDesc.canExecute()) {
         if (arg.exists()) {
            if (arg.isDirectory()) {
               dumpBufDesc = new File(arg, "DumpBufDesc");
            } else {
               if (arg.canExecute())
                  dumpBufDesc = arg;
            }
         }
      }
      if (!dumpBufDesc.exists() || !dumpBufDesc.isFile() || !dumpBufDesc.canExecute()) {
         System.err.println("could not find or execute: " + dumpBufDesc.getAbsolutePath());
         System.exit(-1);
      }
      ProcessBuilder pb = new ProcessBuilder(dumpBufDesc.getAbsolutePath(), "doIt");
      Process        p  = null;
      BufferedReader in = null;
      String         line;

      try {
         pb.directory(dumpBufDesc.getParentFile());
         pb.redirectErrorStream(true);

         p  = pb.start();
         in = new BufferedReader(new InputStreamReader(p.getInputStream()));
         Matcher m;

         while ((line = in.readLine()) != null) {
            m = bePat.matcher(line);
            if (m.find(0)) {
               offsets.put(m.group(2), Integer.parseInt(m.group(1)));
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (p != null) {
            try {
               p.waitFor();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         if (in != null) {
            try {
               in.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }


   protected void tellDescriptor() {
      BufferEntry e;

      for (String k : keys) {
         e = bufDesc.get(k);

         System.out.println(k + " => " + e.offset);
      }
   }


   public static void main(String[] args) {
      String cbDir = ".";

      if (args.length > 0)
         cbDir = args[0];
      SwingUtilities.invokeLater(new CheckBufferDescriptor(cbDir));
   }


   private List<String>         keys;
   private List<String>         errorKeys;
   private Map<String, Integer> offsets;
   private IBufferDescriptor    bufDesc;
   private File                 arg;
   private static Pattern       bePat = Pattern.compile("\\[\\s*(\\d+)\\]\\s+(\\S+)");
}
