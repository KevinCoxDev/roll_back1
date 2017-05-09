import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kcox on 09/05/2017.
 */
public class FilesThread implements Runnable {
    public String list_selection;
    public String zip_file_path;
    private File tmp;

    public static void main(String[] args) throws IOException {
        display app = new display();


    }

    @Override
    public void run() {
        Script files_object =  new Script();
        try {
            System.out.println("2ndSource: " + files_object.zip_file_path + "name: " + files_object.getKeyFromValue(files_object.make_map(files_object.parse_file()), files_object.list_selection));
            unzip(zip_file_path, getKeyFromValue(make_map(parse_file()),list_selection));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void error_check(int code){
        if(code == 1) {
            JOptionPane.showMessageDialog(null, "MD5 Check was invalid. Please check ZIP file", "Completed", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void unzip(String source,String name) throws IOException {
        System.out.println("Source: " + source + "name: " + name);
        String dest = "uncompressed_zip";

        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(dest);
            if(true) {
                pull_files(listDir(dest, name));
            }
            else{
                error_check(1);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private File listDir(String source, String name)
    {
        System.out.println("3Source: " + source + "name: " + name);
        File root = new File(source);
        File[] files = root.listFiles();
        File target = null;


        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                listDir(files[i].toString(),name);
                if(files[i].toString().contains(name)){
                    target= files[i];
                    System.out.println("target found: " + target);
                }

            }
        }


        if(target != null){

            tmp = target;
            System.out.print(tmp);
        }

        System.out.println("TEST: " +  target);
        return tmp;
    }

    private void pull_files(File source) throws IOException {
        File dest = new File("resources");
        FileUtils.cleanDirectory(dest);
        try {
            System.out.println("Source " + source);
            System.out.println("Dest " + dest);
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hash_check(String source) throws IOException {
        String path = "uncompressed_zip\\TPG-V8.80\\TPG-V8.80\\TPG-V8.80.tar-md5.txt";
        BufferedReader brTest = new BufferedReader(new FileReader(path));
        String text = brTest .readLine();
        String[] split = text.split(" ");
        String md5 = split[0].toUpperCase();
        System.out.print(md5);
        FileInputStream fis = new FileInputStream((source));
        String md5x = DigestUtils.md5Hex(fis);
        fis.close();
        return md5x.matches(md5);
    }

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

}
