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

import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {

    private SparseArray<String> result;
    private Map<String, List<String>> headers;

    protected ResponseBuilder() {
        result = new SparseArray<>();
        headers = new HashMap<>();
    }

    public SparseArray<String> getResult() {
        return result;
    }

    public ResponseBuilder setResult(SparseArray<String> result) {
        this.result = result;
        return this;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public ResponseBuilder setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }
}
