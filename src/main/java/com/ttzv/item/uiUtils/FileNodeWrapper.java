package com.ttzv.item.uiUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class FileNodeWrapper<T> {

    private Path path;
    private String name;
    private T node;

    public FileNodeWrapper(Path path, String name) {
        this.path = path;
        this.name = name;
    }

    public FileNodeWrapper() {

    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public T getNode() {
        return node;
    }

    @Override
    public String toString() {
        return name;
    }
}
