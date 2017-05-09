import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static java.awt.Color.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class Script {


    static int progress;
    public String zip_file_path;
    public String list_selection;


    public static void main(String[] args) throws IOException {
        display app = new display();


    }

    public static void scriptCall() throws IOException {
        String dir = System.getProperty("user.dir");
        String st = new String(dir);
        st = new StringBuffer(st).insert(2, "\\").toString();
        String string=("powershell.exe"+ "\\"+" \""+st+"\\"+"scripts\\"+"test2.ps1\"");
        Process ps = Runtime.getRuntime().exec(string);
        ps.getOutputStream().close();
        String line;
        BufferedReader stdout = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        progress = 0;
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
            FileWriter file = new FileWriter("log.txt");
            file.write(line);
            file.close();
            String check = line;

            progress++;
            display.progressBar.setValue(progress);

            if(line.contains("Dest =")){
                String segments[] = line.split(" ");
                String document = segments[segments.length - 1];
                System.out.println(document);
                progressWindow(document,true);
            }
            else if(line.contains("Dest -") || line.contains("ERROR") || line.contains("ERROR") || line.contains("The parameter is incorrect")){
                String segments[] = line.split(" ");
                String document = segments[segments.length - 1];
                System.out.println(document);
                progressWindow(document, false);
                errorCheck();
            }
        }
        stdout.close();

    }


    public static void progressWindow(String input, Boolean check){
        Color color = gray;
        if(check){
            color = green;
        }
        else if(!check){
            color = red;
        }

        if(input.contains("Q")){
            display.drive1.setText("Q:");
            display.drive1.setBackground(color);
        }
        else if(input.contains("R")){
            display.drive2.setText("R:");
            display.drive2.setBackground(color);
        }
        else if(input.contains("S")){
            display.drive3.setText("S:");
            display.drive3.setBackground(color);
        }
        else if(input.contains("T")){
            display.drive4.setText("T:");
            display.drive4.setBackground(color);
        }
        else if(input.contains("U")){
            display.drive5.setText("U:");
            display.drive5.setBackground(color);
        }

    }

	private File tmp;

    public String[] parse_file() {
        List<String> list = null;
        try {
            list = Files.readAllLines(Paths.get("settings.ini"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] array = list.toArray(new String[list.size()]);

        return array;
    }

    public BiMap make_map(String[] array){
        BiMap<String,String> map = HashBiMap.create();
        for(int i=0; i < array.length; i++){String[] split = array[i].split(":");
            String
             key = split[0];
            String[] values = split[1].split(",");
            for(int d=0; d<values.length;d++){
            	String value = values[d];
            	map.put(key,value);
            }	
        }
        return map;
    }



    public Object[] generateDeviceList(BiMap map) {
        
    	map.values();
    	Object[] device_array = map.values().toArray(new Object[map.values().size()]);
    	Arrays.toString(device_array);
    	
        return device_array;
    }

    public String getKeyFromValue(BiMap map, String input) {
    	System.out.println(map.values());
    	String key = (String) map.inverse().get(input);
    	System.out.println("Key:"+key);
    	return key;
        
    }


    public static void  errorCheck(){
        JOptionPane.showMessageDialog(null, "There has been an issue with one of your drives \nThis error message may reoccur on the same drive \n Process will attempt to continue on other drives", "alert", JOptionPane.WARNING_MESSAGE);
    }
    
    


}