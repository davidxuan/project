package acdc;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewPattern extends Pattern {
    String path;
    public NewPattern(DefaultMutableTreeNode _root, String p) {
        super(_root);
        name = "New Pattern";
        path = p;
    }

    public void execute() throws IOException {
//        System.out.println(path);
        processInput(path);
    }

    public void processInput(String path) throws IOException {
        List<File> fileList = getSourceCode(path);
        for (File f : fileList) {
            String fn = f.getName().substring(f.getName().lastIndexOf("/") + 1);
            String cn = fn.substring(0, fn.lastIndexOf("."));
            HashMap<String, List<String>> parsed = parseFile(f);
            System.out.println(parsed.get("imports"));
        }
    }

    public List<File> getSourceCode(String path) {
        List<File> fileList = new ArrayList<>();
        File[] fs = new File(path).listFiles();
        if (fs == null) {
            return null;
        }
        for (File f : fs) {
            if (f.isFile() && f.toString().endsWith("java")) {
                fileList.add(f);
            }
            else if (f.isDirectory()) {
                fileList.addAll(getSourceCode(f.getAbsolutePath()));
            }
        }
        return fileList;
    }


    public HashMap<String, List<String>> parseFile(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f.toString()));
        String content = new String(Files.readAllBytes(Paths.get(f.toString())));
        String[] content_list = content.split("\n");
        HashMap<String, List<String>> ret = new HashMap<>();
        ret.put("package", new ArrayList<>());
        ret.put("imports", new ArrayList<>());

        for (String line : content_list) {
            if (line.startsWith("import")) {
                String[] l = line.split(" ");
                ret.get("imports").add(l[1]);
            } else if (line.startsWith("package")) {
                String[] l = line.split(" ");
                ret.get("package").add(l[1]);
            }
        }
        return ret;
    }
}
