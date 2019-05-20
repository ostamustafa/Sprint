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

import ostamustafa.sprint.header.Header;
import ostamustafa.sprint.multipart.MultiPartFile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

class SprintBuilder {

    private HashSet<Header> headers;
    private int timeout = 15000;
    private Method method;
    private ArrayList<MultiPartFile> files;
    private JSONObject jsonArguments;
    private String stringArguments;
    private HashMap<String, String> arguments;
    private String url;
    private SprintCallback callback;
    private boolean shouldValidateSSL;

    boolean shouldValidateSSL() {
        return shouldValidateSSL;
    }

    SprintBuilder setShouldValidateSSL(boolean shouldValidateSSL) {
        this.shouldValidateSSL = shouldValidateSSL;
        return this;
    }

    HashSet<Header> getHeaders() {
        return headers;
    }

    SprintBuilder setHeaders(Header... headers) {
        this.headers = new HashSet<>();
        this.headers.addAll(Arrays.asList(headers));
        return this;
    }

    int getTimeout() {
        return timeout;
    }

    void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    Method getMethod() {
        return method;
    }

    SprintBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    ArrayList<MultiPartFile> getFiles() {
        return files;
    }

    SprintBuilder setFiles(ArrayList<MultiPartFile> files) {
        this.files = files;
        return this;
    }

    JSONObject getJsonArguments() {
        return jsonArguments;
    }

    SprintBuilder setJsonArguments(JSONObject jsonArguments) {
        this.jsonArguments = jsonArguments;
        return this;
    }

    String getStringArguments() {
        return stringArguments;
    }

    SprintBuilder setStringArguments(String stringArguments) {
        this.stringArguments = stringArguments;
        return this;
    }

    HashMap<String, String> getArguments() {
        return arguments;
    }

    SprintBuilder setArguments(HashMap<String, String> arguments) {
        this.arguments = arguments;
        return this;
    }

    String getUrl() {
        return url;
    }

    SprintBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    SprintCallback getCallback() {
        return callback;
    }

    SprintBuilder setCallback(SprintCallback callback) {
        this.callback = callback;
        return this;
    }
}
