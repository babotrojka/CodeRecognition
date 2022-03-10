package hr.fer.zemris.ocitavanje.koda.data.fileVisitors;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * FileVisitor which contains list of character directory names
 */
public class DirectoryFileVisitor extends SimpleFileVisitor<Path> {
    /**
     * List of character directory names
     */
    private List<File> directoryNames;

    public DirectoryFileVisitor(List<File> directoryNames) {
        this.directoryNames = directoryNames;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(dir.getFileName().toString().length() > 1) return FileVisitResult.CONTINUE;

        directoryNames.add(dir.toFile());
        return FileVisitResult.SKIP_SUBTREE;
    }
}
