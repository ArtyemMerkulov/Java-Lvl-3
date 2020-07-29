package homework3;

import java.io.*;
import java.util.ArrayList;

public class PageFileReader implements Serializable {

    private final ArrayList<Long> filePageIndexes;
    private final String filePath;
    private final int charsPerPage;

    public PageFileReader(final String filePath, final int charsPerPage) throws IOException {
        this.filePath = filePath;
        this.charsPerPage = charsPerPage;
        this.filePageIndexes = getPagesIndexes(filePath);
    }

    private ArrayList<Long> getPagesIndexes(final String filePath) throws IOException {
        byte fileByte;
        int counter = 0;

        RandomAccessFile fis = new RandomAccessFile(filePath, "r");

        ArrayList<Long> filePageIndexes = new ArrayList<>();
        filePageIndexes.add(0L);

        while ((fileByte = (byte) fis.read()) != -1) {
            if (fileByte < 0) fis.read();

            counter++;
            if (counter % charsPerPage == 0) filePageIndexes.add(fis.getFilePointer());
        }

        if (counter % charsPerPage > 0)  filePageIndexes.add(fis.getFilePointer());

        fis.close();

        return filePageIndexes;
    }

    public String getPage(final int pageNum) throws IOException {
        if (pageNum < 1 || pageNum > filePageIndexes.size() - 1) return null;

        int pageN = pageNum - 1;

        byte[] pageBytes = new byte[(int) (filePageIndexes.get(pageN + 1) - filePageIndexes.get(pageN))];

        RandomAccessFile raf = new RandomAccessFile(filePath, "r");

        raf.seek(filePageIndexes.get(pageN));
        raf.readFully(pageBytes);

        raf.close();

        return new String(pageBytes);
    }

    public int getTotalPages() {
        return filePageIndexes.size();
    }

    public static void save(String saveFilePath, PageFileReader obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFilePath));

        oos.writeObject(obj);
        oos.close();
    }

    public static PageFileReader load(String loadFilePath) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loadFilePath));

        PageFileReader obj = (PageFileReader) ois.readObject();

        ois.close();

        return obj;
    }
}
