package com.dire.guard;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ResponseUtils {

    public static final String DEFAULT_APPLICATION_CONTENT_TYPE = "application/json;charset=utf=8";

    public static void writeObject(HttpServletResponse response, String text) throws IOException {
        response.setContentType(DEFAULT_APPLICATION_CONTENT_TYPE);
        response.getWriter().write(text);
    }
//    public static void writeObject(HttpServletResponse response, Object object, Map<String, String> headers) {
//
//    }
//
//    public static void writeText(HttpServletResponse response, String text) {
//
//    }
//
//    public static void writeText(HttpServletResponse response, String text, HttpHeaders httpHeaders) {
//
//    }
//
//    public static void writeWithBytes(HttpServletResponse response, byte[] bytes) {
//
//    }
//
//    public static void writeWithBytes(HttpServletResponse response, byte[] bytes, HttpHeaders httpHeaders) {
//
//    }
//
//    public static void writeFile(HttpServletResponse response, String filepath) {
//
//    }
//
//    public static void writeFile(HttpServletResponse response, String filepath, HttpHeaders httpHeaders) {
//
//    }
//
//    public static void writeFile(HttpServletResponse response, File file) {
//
//    }
//
//    public static void writeFile(HttpServletResponse response, File file, HttpHeaders httpHeaders) {
//
//    }
//
}
