package cc.mallet.nitin;

import cc.mallet.fst.CRF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: nitin
 * Date: 08/05/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class CRFModelReader {
    private CRFModelReader() {

    }

    public static CRF read(final String fileName) throws IOException, ClassNotFoundException {
        CRF crf;
        ObjectInputStream s =
                new ObjectInputStream(new FileInputStream(new File(fileName)));
        try {
            crf = (CRF) s.readObject();
        }
        finally {
            s.close();
        }

        return crf;
    }

    public static void main(final String [] args) throws Exception {
        CRF crf = read(args[0]);
        crf.print();
    }
}
