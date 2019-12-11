package acdc;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/*
 * This pattern "Attach" clusters files A and B together in such circumstance：
 * Ignore (anonymous) inner classes, file A is depended and is only depended by file B.
 */
public class Attach extends Pattern {
    public Attach (DefaultMutableTreeNode _root) {
        super(_root);
        name = "Attach";
    }

    public void execute() {
        Vector vModified = new Vector();//will contain nodes which were moved
        Vector vTree = allNodes(root);

        // Traverse to find file that is only depended by one other file, attach it to its source
        for (int i=0; i<vTree.size(); i++) {
            Node ncurr = (Node) vTree.elementAt(i);
            DefaultMutableTreeNode curr = (DefaultMutableTreeNode) ncurr.getTreeNode();

            HashSet nSources = ncurr.getSources();
            int sourceCnt = nSources.size(); // count for the sources num exclude those point from (annoymous) inner classes
            Iterator inSources = nSources.iterator();

            int dollarPos = ncurr.getName().indexOf("$");
            if (dollarPos == -1) dollarPos = ncurr.getName().length();
            String keyname = ncurr.getName().substring(ncurr.getName().lastIndexOf(".") + 1, dollarPos);
            // Ignore cases that depended by (annoymous) inner classes
            while (inSources.hasNext()) {
                Node nSncurr = (Node) inSources.next();
                if (nSncurr.getName().contains(keyname)) {
                    inSources.remove();
                    sourceCnt--;
                }
            }

            // This file A is only depended by one other file
            if (sourceCnt == 1) {
                Iterator iS = nSources.iterator();
                Node sourceNode = null;
                if (iS.hasNext()) {
                    sourceNode = (Node) iS.next(); // The file B which depend on file A
                }

                // Create a new cluster node which contains fila A and file B
                Node cluster_node = new Node(sourceNode.getName() + " " + ncurr.getName(), "Unknown");
                DefaultMutableTreeNode tcluster = new DefaultMutableTreeNode(cluster_node);
                cluster_node.setTreeNode(tcluster);

                // Add the new cluster node under the parent of the current node in the traversal
                DefaultMutableTreeNode source = (DefaultMutableTreeNode) sourceNode.getTreeNode();
                DefaultMutableTreeNode source_parent = (DefaultMutableTreeNode) source.getParent();
                source_parent.add(tcluster);

                // Make the files A and B children of the new cluster node
                tcluster.add(source);
                tcluster.add(curr);

                Enumeration evt = source.breadthFirstEnumeration();
                while (evt.hasMoreElements()) {
                    DefaultMutableTreeNode ec = (DefaultMutableTreeNode) evt.nextElement();
                    if (!vModified.contains((Node) ec.getUserObject()))
                        vModified.add((Node) ec.getUserObject());
                }
                Enumeration ecurr = curr.breadthFirstEnumeration();
                while (ecurr.hasMoreElements()) {
                    DefaultMutableTreeNode em = (DefaultMutableTreeNode) ecurr.nextElement();
                    if (!vModified.contains((Node) em.getUserObject()))
                        vModified.add((Node) em.getUserObject());
                }
            }
        }
        induceEdges(vModified,root);
    }
}