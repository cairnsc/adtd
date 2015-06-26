package com.thoughtworks.adtd.testutil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static String consumeResourceFile(String path) throws IOException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(path);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
