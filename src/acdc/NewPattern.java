package acdc;

import javax.swing.tree.DefaultMutableTreeNode;

public class NewPattern extends Pattern {
    String path;
    public NewPattern(DefaultMutableTreeNode _root, String p) {
        super(_root);
        name = "New Pattern";
        path = p;
    }

    public void execute() {
        System.out.println(path);
    }
}
