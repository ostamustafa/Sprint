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

package ostamustafa.sprint.multipart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class MultiPartFile {

    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "----SprintRequest";

    private File file;
    private String name;

    public MultiPartFile(String name, File file) {
        this.file = file;
        this.name = name;
    }

    public void writeData(DataOutputStream stream) throws IOException {
        String builder = TWO_HYPHENS + BOUNDARY +
                CRLF +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + '"' +
                CRLF +
                "Content-Type: image/" + getExtension().toLowerCase() +
                CRLF + CRLF;
        stream.writeBytes(builder);
        stream.write(getBytes());
        stream.writeBytes(CRLF);
        stream.flush();
    }

    private byte[] getBytes() {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(getFormat(), 80, stream);
        return stream.toByteArray();
    }

    private String getExtension() {
        int length = file.getName().length();
        int dotIndex = file.getName().indexOf('.');
        return file.getName().substring(dotIndex + 1, length).toUpperCase();
    }

    private Bitmap.CompressFormat getFormat() {
        return Bitmap.CompressFormat.valueOf(getExtension());
    }
}
