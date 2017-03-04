package scienceWork.ObjectClasses;

import java.io.File;

public class Directory {
    private String dir;
    private int countFiles;
    private File dirFile;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public void setCountFiles(int countFiles) {
        this.countFiles = countFiles;
    }

    public File getDirFile() {
        return dirFile;
    }

    public void setDirFile(File dirFile) {
        this.dirFile = dirFile;
    }
}
