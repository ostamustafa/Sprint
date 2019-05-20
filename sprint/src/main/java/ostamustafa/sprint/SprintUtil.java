/*
 * MIT License
 *
 * Copyright (c) 2019 Mustafa Osta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ostamustafa.sprint;

import android.util.Log;
import android.webkit.URLUtil;

import ostamustafa.sprint.header.ContentType;
import ostamustafa.sprint.header.Header;
import ostamustafa.sprint.multipart.MultiPartFile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

class SprintUtil {

    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "----SprintRequest";
    private static final String UTF8 = "UTF-8";

    private HttpURLConnection connection;
    private SprintBuilder builder;

    SprintUtil(SprintBuilder builder) throws Exception {
        this.builder = builder;
        init();
    }

    private void init() throws Exception {
        URL url1 = new URL(builder.getUrl());
        if (URLUtil.isHttpUrl(builder.getUrl()))
            connection = (HttpURLConnection) url1.openConnection();
        else if (URLUtil.isHttpsUrl(builder.getUrl())) {
            checkSSL();
            connection = (HttpsURLConnection) url1.openConnection();
        } else
            throw new Exception("only HTTP and HTTPS urls are supported");
        connection.setRequestMethod(builder.getMethod().getMethod());
        connection.setDoOutput(builder.getMethod() != Method.GET);
        connection.setConnectTimeout(builder.getTimeout());
        connection.setUseCaches(false);
        setHeaders();
    }

    private void checkSSL() throws NoSuchAlgorithmException, KeyManagementException {
        if (!builder.shouldValidateSSL()) {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        }
    }

    private void setHeaders() {
        connection.setRequestProperty("Connection", "Keep-Alive");
        for (Header header : builder.getHeaders()) {
            if (!header.value().isEmpty())
                connection.setRequestProperty(header.key(), header.value());
        }
    }

    ResponseBuilder request() throws IOException {
        ContentType contentType = getContentType();
        switch (contentType) {
            case FORM_DATA:
                return formData();
            case JSON:
                return json();
            case URL_ENCODED:
                return urlEncoded();
            case RAW:
                return raw();
            default:
                closeConnection();
                return null;
        }

    }

    private ResponseBuilder formData() throws IOException {
        setupFormData();
        ResponseBuilder result = new ResponseBuilder();
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        if (builder.getArguments() != null) {
            outputStream.writeBytes(getFormDataString());
            outputStream.flush();
        }
        if (builder.getFiles() != null)
            for (MultiPartFile file : builder.getFiles())
                file.writeData(outputStream);
        outputStream.writeBytes(TWO_HYPHENS);
        outputStream.writeBytes(BOUNDARY);
        outputStream.writeBytes(TWO_HYPHENS);
        outputStream.writeBytes(CRLF);
        outputStream.flush();
        outputStream.close();
        checkStatus(result);
        return result;
    }

    private ResponseBuilder json() throws IOException {
        ResponseBuilder result = new ResponseBuilder();
        if (builder.getJsonArguments() != null) {
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(builder.getJsonArguments().toString());
            outputStream.flush();
            outputStream.close();
        }
        checkStatus(result);
        return result;
    }

    private ResponseBuilder raw() throws IOException {
        ResponseBuilder result = new ResponseBuilder();
        if (builder.getStringArguments() != null) {
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(builder.getStringArguments());
            outputStream.flush();
            outputStream.close();
        }
        checkStatus(result);
        return result;
    }

    private ResponseBuilder urlEncoded() throws IOException {
        ResponseBuilder result = new ResponseBuilder();
        if (builder.getArguments() != null) {
            connection.setRequestProperty("Content-Length", Integer.toString(getUrlEncodedString().getBytes().length));
            //Send request
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(getUrlEncodedString());
            outputStream.flush();
            outputStream.close();
        }

        checkStatus(result);
        return result;
    }

    private void checkStatus(ResponseBuilder result) throws IOException {
        int status = connection.getResponseCode();
        InputStream inputStream;
        //Get Response
        if (status == HttpURLConnection.HTTP_OK)
            inputStream = connection.getInputStream();
        else
            inputStream = connection.getErrorStream();
        result.setHeaders(connection.getHeaderFields());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
            response.append('\r');
            result.getResult().put(status, response.toString());
        }
        bufferedReader.close();
        inputStream.close();
        closeConnection();
    }

    private String getUrlEncodedString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Log.d("Request Arguments", builder.getArguments() + "");
        for (Map.Entry<String, String> entry : builder.getArguments().entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), UTF8));
            result.append("=");
            switch (entry.getValue()) {
                case "true":
                    result.append(URLEncoder.encode(String.valueOf(true), UTF8));
                    break;
                case "false":
                    result.append(URLEncoder.encode(String.valueOf(false), UTF8));
                    break;
                default:
                    result.append(URLEncoder.encode(entry.getValue(), UTF8));
                    break;
            }
        }

        return result.toString();

    }

    private void setupFormData() {
        ContentType contentType = getContentType();
        connection.setRequestProperty(contentType.key(), contentType.value() + "; boundary=" + BOUNDARY);
    }

    private String getFormDataString() throws IOException {
        StringBuilder result = new StringBuilder();
        if (builder.getArguments() != null) {
            for (Map.Entry<String, String> entry : builder.getArguments().entrySet()) {
                result.append(TWO_HYPHENS).append(BOUNDARY)
                        .append(CRLF)
                        .append("Content-Disposition: form-data; ")
                        .append("name=")
                        .append('"')
                        .append(URLEncoder.encode(entry.getKey(), UTF8))
                        .append('"')
                        .append(CRLF)
                        .append(CRLF);
                switch (entry.getValue()) {
                    case "true":
                        result.append(URLEncoder.encode(String.valueOf(true), UTF8));
                        break;
                    case "false":
                        result.append(URLEncoder.encode(String.valueOf(false), UTF8));
                        break;
                    default:
                        result.append(entry.getValue());
                        break;
                }
                result.append(CRLF);
            }
        }
        return result.toString();
    }

    private ContentType getContentType() {
        ContentType contentType = ContentType.RAW;
        for (Header header : builder.getHeaders())
            if (header instanceof ContentType)
                contentType = (ContentType) header;
        return contentType;
    }

    private void closeConnection() {
        if (connection != null)
            connection.disconnect();
    }
}
