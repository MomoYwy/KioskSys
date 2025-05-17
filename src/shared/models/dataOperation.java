package shared.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dataOperation {
        public static <T> List<T> ReadFile(String fileName, Class<T> classs) {
        List<T> list = new ArrayList<>();
        File file = new File("src\\database\\" + fileName + ".txt");

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            br.readLine();  // Skip the header line (first line)
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip lines that are empty or contain invalid characters like "<init>"
                if (line.isEmpty() || line.contains("<init>")) {
                    continue;
                }

                //System.out.println("Processing line: " + line);  // Debugging: print the current line being processed
                String[] data = line.split(",");
                //System.out.println("Data length: " + data.length);  // Debugging: print the number of fields in the line

                try {
                    // Adjusted reflection to match the full constructor of PurchaseOrder
                    T obj = classs.getDeclaredConstructor(
                            String.class, String.class, String.class, String.class, int.class,
                            double.class, double.class, Date.class, Date.class,
                            String.class, String.class, String.class, String.class
                    )
                    .newInstance(
                            data[0], data[1], data[2], data[3], Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]), Double.parseDouble(data[6]),
                            new SimpleDateFormat("dd/MM/yyyy").parse(data[7]),
                            new SimpleDateFormat("dd/MM/yyyy").parse(data[8]),
                            data[9], data[10], data[11], data[12]
                    );
                    list.add(obj);
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ".txt: " + e.getMessage());
        }
        return list;
    
    
    }
    
            public static void WriteFile(Object Obj){
        Class<?> classObj = Obj.getClass();
        String FileName = classObj.getSimpleName();
        System.out.println("FileName:" + FileName + ".txt");
        try (FileWriter Fw = new FileWriter("File_Serialization" + FileName + ".txt", true);
             BufferedWriter Bw = new BufferedWriter(Fw);
             PrintWriter pw = new PrintWriter(Bw)) {
            for (Field field : classObj.getDeclaredFields()) {
                String fieldName = field.getName();
                String methodName = String.format("get%s", Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
                try {
                    Method method = classObj.getDeclaredMethod(methodName);
                    Object value = method.invoke(Obj);
                    pw.write(fieldName + ":" + value);
                    pw.write("\n");
                } catch (NoSuchMethodException e) {
                    System.err.println("Getter method not found for field: " + fieldName);
                } catch (Exception e) {
                    System.err.println("Error invoking getter for field: " + fieldName + " - " + e.getMessage());
                }
            }
            pw.write("---------------------");
            pw.write("\n");
        } catch (IOException Ex) {
            System.err.println("Error writing to file: " + FileName + ".txt - " + Ex.getMessage());
        }
    
    }
}
