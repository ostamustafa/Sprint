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

import android.os.AsyncTask;

public class ConnectionTask extends AsyncTask<Void, Integer, ResponseBuilder> {

    private SprintBuilder builder;

    ConnectionTask(SprintBuilder builder) {
        this.builder = builder;
    }

    @Override
    protected ResponseBuilder doInBackground(Void... Void) {
        if (builder.getUrl() == null || builder.getUrl().isEmpty()) {
            builder.getCallback().onException(new Exception("URL must not be empty"));
            return null;
        }
        ResponseBuilder result;
        try {
            SprintUtil util = new SprintUtil(builder);
            result = util.request();
            return result;
        } catch (Exception e) {
            builder.getCallback().onException(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseBuilder result) {
        if (result != null) {
            if (result.getResult().size() == 0)
                builder.getCallback().onException(new Exception("No results from server"));
            else if (result.getResult().size() == 1) {
                int key = result.getResult().keyAt(0);
                Response response = new Response();
                response.setStatusCode(key);
                response.setBody(result.getResult().get(key));
                response.setHeaders(result.getHeaders());
                builder.getCallback().onResponse(response);
            } else {
                builder.getCallback().onException(new Exception("More than one argument was passed from server"));
            }
        }
    }
}
