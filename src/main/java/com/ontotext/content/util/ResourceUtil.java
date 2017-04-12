package com.ontotext.content.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {

    public static final String getResourceFileAsString(String resource) {
        try {
            InputStream is = ResourceUtil.class.getClassLoader().getResourceAsStream(resource);
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }


    public static final InputStream getResourceFileAsStream(String resource) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(resource);
    }
}
