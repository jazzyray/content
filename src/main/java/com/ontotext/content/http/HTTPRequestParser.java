package com.ontotext.content.http;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Scanner;

public class HTTPRequestParser {

    private Scanner reader;
    private String method, url;
    private Hashtable headers, params;
    private int[] ver;
    private String body;

    public HTTPRequestParser(InputStream is) {

        reader = new Scanner(is);
        method = "";
        url = "";
        body = "";
        headers = new Hashtable();
        params = new Hashtable();
        ver = new int[2];
    }

    public int parseRequest() throws IOException {
        String initial, prms[], cmd[], temp[];
        int ret, idx, i;

        ret =  HttpURLConnection.HTTP_OK;

        initial = reader.nextLine();
        if (initial == null || initial.length() == 0) {
            return 0;
        }
        if (Character.isWhitespace(initial.charAt(0))) {
            // starting whitespace, return bad request
            return  HttpURLConnection.HTTP_BAD_REQUEST;
        }

        cmd = initial.split("\\s");
        if (cmd.length != 3) {
            return  HttpURLConnection.HTTP_BAD_REQUEST;
        }

        if (cmd[2].indexOf("HTTP/") == 0 && cmd[2].indexOf('.') > 5) {
            temp = cmd[2].substring(5).split("\\.");
            try {
                ver[0] = Integer.parseInt(temp[0]);
                ver[1] = Integer.parseInt(temp[1]);
            } catch (NumberFormatException nfe) {
                ret =  HttpURLConnection.HTTP_BAD_REQUEST;
            }
        } else {
            ret =  HttpURLConnection.HTTP_BAD_REQUEST;
        }

        if (cmd[0].equals(HttpMethod.POST) || cmd[0].equals(HttpMethod.PUT) || cmd[0].equals(HttpMethod.DELETE)) {
            method = cmd[0];

            idx = cmd[1].indexOf('?');
            if (idx < 0) {
                url = cmd[1];
            }
            else {
                url = URLDecoder.decode(cmd[1].substring(0, idx), "ISO-8859-1");
                prms = cmd[1].substring(idx+1).split("&");

                params = new Hashtable();
                for (i=0; i<prms.length; i++) {
                    temp = prms[i].split("=");
                    if (temp.length == 2) {

                        params.put(URLDecoder.decode(temp[0], "ISO-8859-1"),
                                URLDecoder.decode(temp[1], "ISO-8859-1"));
                    }
                    else if(temp.length == 1 && prms[i].indexOf('=') == prms[i].length()-1) {
                        params.put(URLDecoder.decode(temp[0], "ISO-8859-1"), "");
                    }
                }
            }
            parseHeaders();
            if (headers == null) {
                ret = HttpURLConnection.HTTP_BAD_REQUEST;
            }

            parseBody();
        } else if (ver[0] == 1 && ver[1] >= 1) {
            if (cmd[0].equals(HttpMethod.OPTIONS) ||
                    cmd[0].equals("TRACE") ||
                    cmd[0].equals("CONNECT") ||
                    cmd[0].equals(HttpMethod.GET) ||
                    cmd[0].equals(HttpMethod.HEAD)) {
                ret = HttpURLConnection.HTTP_NOT_IMPLEMENTED;
            }
        } else {

            ret = HttpURLConnection.HTTP_BAD_REQUEST;
        }

        if (ver[0] == 1 && ver[1] >= 1 && getHeader(HttpHeaders.HOST) == null) {
            ret = HttpURLConnection.HTTP_BAD_REQUEST;
        }

        return ret;
    }

    private void parseHeaders() throws IOException {
        int idx;

        String line = reader.nextLine();
        while ((line != null) && (!line.equals(""))) {
            idx = line.indexOf(':');
            if (idx < 0) {
                headers = null;
                break;
            } else {
                headers.put(line.substring(0, idx).toLowerCase(), line.substring(idx+1).trim());
            }
            if (reader.hasNext()) {
                line = reader.nextLine();
            } else {
                line = null;
            }
        }
    }

    private void parseBody() {
        if (reader.hasNext()) {
            String line = reader.nextLine();
            if (line != null) {
                body = line;
            }
        }
    }

    // https://www.w3.org/Protocols/rfc1341/4_Content-Type.html
    public String getCleanContentType() {
        return removeOptionaMimeTypeParam(this.getHeader(HttpHeaders.CONTENT_TYPE));
    }

    public String removeOptionaMimeTypeParam(String mimeType) {
        return mimeType.substring(0, mimeType.indexOf(";"));
    }


    public String getMethod() {
        return method;
    }

    public String getHeader(String key) {
        if (headers != null)
            return (String) headers.get(key.toLowerCase());
        else return null;
    }

    public Hashtable getHeaders() {
        return headers;
    }

    public String getRequestURL() {
        return url;
    }

    public String getParam(String key) {
        return (String) params.get(key);
    }

    public Hashtable getParams() {
        return params;
    }

    public String getVersion() {
        return ver[0] + "." + ver[1];
    }

    public String getBody() {
        return body;
    }
}