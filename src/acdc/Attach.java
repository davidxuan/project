package acdc;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class Attach extends Pattern {
    public Attach (DefaultMutableTreeNode _root) {
        super(_root);
        name = "Attach";
    }

    public void execute() {
        Vector vModified = new Vector();//will contain nodes which were moved
        Vector vTree = allNodes(root);

        for (int i=0; i<vTree.size(); i++) {
            Node ncurr = (Node) vTree.elementAt(i);

            DefaultMutableTreeNode curr = (DefaultMutableTreeNode) ncurr.getTreeNode();
            String fileName = "/Users/shuqixiao/Desktop/res.txt";// TODO


            HashSet nSources = ncurr.getSources();
            int sourceCnt = nSources.size(); // count for the sources num exclude those point from themselves
            Iterator inSources = nSources.iterator();

            int dollarPos = ncurr.getName().indexOf("$");
            if (dollarPos == -1) dollarPos = ncurr.getName().length();
            String keyname = ncurr.getName().substring(ncurr.getName().lastIndexOf(".") + 1, dollarPos);

            while (inSources.hasNext()) {
                Node nSncurr = (Node) inSources.next();
                if (nSncurr.getName().contains(keyname)) {
                    inSources.remove();
                    sourceCnt--;
                }
            }

            if (sourceCnt == 1) {
                Iterator iS = nSources.iterator();
                Node sourceNode = null;
                if (iS.hasNext()) {
                    sourceNode = (Node) iS.next();
                }
                int fdollarPos = sourceNode.getName().indexOf("$");
                if (fdollarPos == -1) fdollarPos = sourceNode.getName().length();
                String sourcename = sourceNode.getName().substring(sourceNode.getName().lastIndexOf(".") + 1, fdollarPos);

                //create the new cluster node which will have extension .ch
                Node cluster_node = new Node(sourcename + "&" + keyname, "Unknown");
                DefaultMutableTreeNode tcluster = new DefaultMutableTreeNode(cluster_node);
                cluster_node.setTreeNode(tcluster);

                //add the new cluster node under the parent of the current node in the traversal
                DefaultMutableTreeNode source = (DefaultMutableTreeNode) sourceNode.getTreeNode();
                DefaultMutableTreeNode source_parent = (DefaultMutableTreeNode) source.getParent();
                source_parent.add(tcluster);
//                tcluster.setParent(source_parent);
//                source_parent.add(curr);

                //make the files with extension .c and .h children of the new cluster node
                tcluster.add(source);
//                source.setParent(tcluster);
                tcluster.add(curr);
//                curr.setParent(tcluster);

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
                induceEdges(vModified,root);
                vModified = new Vector();
            }
            /*
            if (ncurr.getName().contains("JrePlatform")) {
                try {
                    FileWriter writer=new FileWriter(fileName,true);
                    writer.write("\n\n===== Find !!!!!!!!!!!! =====\n");
                    writer.write("Name: " + ncurr.getName() + "\n");
                    writer.write("Type: " + ncurr.getType()+"\n");
                    writer.write("SourceSize: " + ncurr.getSources().size()+"\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashSet nSources = ncurr.getSources();
                int sourceCnt = nSources.size();
                Iterator inSources = nSources.iterator();
                int dollarPos = ncurr.getName().indexOf("$");
                if (dollarPos == -1) dollarPos = ncurr.getName().length();
                String keyname = ncurr.getName().substring(ncurr.getName().lastIndexOf(".") + 1, dollarPos);
                while(inSources.hasNext()) {
                    Node nSncurr = (Node) inSources.next();
                    if (nSncurr.getName().contains(keyname)) {
                        inSources.remove();
                        sourceCnt--;
                    }
                }
                Iterator it = nSources.iterator();
                while(it.hasNext()) {
                    Node nn = (Node) it.next();
                    try {
                            FileWriter writer=new FileWriter(fileName,true);
                            writer.write("== Here's are info of SOURCES == \n");
                            writer.write("Name: " + nn.getName() + "\n");
                            writer.write("Type: " + nn.getType()+"\n");
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
            */
        }
        induceEdges(vModified,root);
    }
}
