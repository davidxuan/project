package acdc;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class NewPattern extends acdc.Pattern {
    String path;
    HashMap<String, HashMap<String, List<String>>> map = new HashMap<>();

    public NewPattern(DefaultMutableTreeNode _root, String p) {
        super(_root);
        name = "New Pattern";
        path = p;
    }

    public void execute() throws IOException {
        processInput(path);
    }

    public void processInput(String path) throws IOException {
        List<File> fileList = getSourceCode(path);
        for (File f : fileList) {
            String fn = f.getName().substring(f.getName().lastIndexOf("/") + 1);
            String cn = fn.substring(0, fn.lastIndexOf("."));
            HashMap<String, List<String>> parsed = parseFile(f);
            map.put(cn, parsed);
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
        String content = new String(Files.readAllBytes(Paths.get(f.toString())));
        String[] content_list = content.split("\n");
        HashMap<String, List<String>> ret = new HashMap<>();
        ret.put("package", new ArrayList<>());
        ret.put("imports", new ArrayList<>());
        ret.put("extends", new ArrayList<>());
        ret.put("implements", new ArrayList<>());

        for (String line : content_list) {
            if (line.trim().matches("package ([\\w&&\\D]([\\w\\.]*[\\w])?);")) {
                String[] l = line.split(" ");
                ret.get("package").add(l[1]);
            }
            else if (line.trim().matches("import ([\\w&&\\D]([\\w\\.]*[\\w])?);")) {
                String[] l = line.split(" ");
                ret.get("imports").add(l[1]);
            }

            else if (Pattern.compile(" class ([\\w&&\\D]([\\w\\.]*[\\w])?) extends ([\\w&&\\D]([\\w\\.]*[\\w])?)").matcher(line).find()) {
                String[] l = line.trim().split(" ");
                int idx = Arrays.asList(l).indexOf("extends");
                ret.get("extends").add(l[idx+1]);
            }

            else if (Pattern.compile("class ([\\w&&\\D]([\\w\\.]*[\\w])?) implements ([\\w&&\\D]([\\w\\.]*[\\w])?)").matcher(line).find()) {
                String[] l = line.trim().split(" ");
                int idx = Arrays.asList(l).indexOf("implements");
                ret.get("implements").addAll(Arrays.asList(Arrays.copyOfRange(l, idx+1, l.length-1)));
            }
        }
        return ret;
    }
}
