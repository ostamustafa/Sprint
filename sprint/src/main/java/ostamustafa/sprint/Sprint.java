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

import ostamustafa.sprint.header.Accept;
import ostamustafa.sprint.header.ContentType;
import ostamustafa.sprint.header.Header;
import ostamustafa.sprint.multipart.MultiPartFile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Sprint {

    private static volatile Sprint sprint;
    private SprintBuilder builder;

    private Sprint() {
        if (sprint != null)
            throw new RuntimeException("Use instance() method to get the single instance of this class.");
    }

    public static Sprint instance() {
        return instance(Method.GET);
    }

    public static Sprint instance(Method method) {
        if (sprint == null) {
            synchronized (Sprint.class) {
                if (sprint == null)
                    sprint = new Sprint();
            }
        }
        sprint.builder = new SprintBuilder();
        sprint.builder.setMethod(method);
        sprint.builder.setHeaders(ContentType.URL_ENCODED, Accept.JSON);
        return sprint;
    }

    public Sprint headers(Header... headers) {
        builder.setHeaders(headers);
        return this;
    }

    public Sprint timeout(int timeout) {
        builder.setTimeout(timeout);
        return this;
    }

    public Sprint shouldValidateSSL(boolean shouldValidate) {
        builder.setShouldValidateSSL(shouldValidate);
        return this;
    }

    public SprintResult request(String url) {
        builder.setUrl(url);
        return new SprintResult(builder);
    }

    public SprintResult request(String url, HashMap<String, String> arguments) {
        builder.setUrl(url).setArguments(arguments);
        return new SprintResult(builder);
    }

    public SprintResult request(String url, ArrayList<MultiPartFile> files) {
        builder.setUrl(url).setFiles(files);
        return new SprintResult(builder);
    }

    public SprintResult request(String url, HashMap<String, String> arguments, ArrayList<MultiPartFile> files) {
        builder.setUrl(url).setArguments(arguments).setFiles(files);
        return new SprintResult(builder);
    }

    public SprintResult request(String url, String arguments) {
        builder.setUrl(url).setStringArguments(arguments);
        return new SprintResult(builder);
    }

    public SprintResult request(String url, JSONObject jsonParameters) {
        builder.setUrl(url).setJsonArguments(jsonParameters);
        return new SprintResult(builder);
    }
}
