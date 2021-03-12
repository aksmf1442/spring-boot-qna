package com.codessquad.qna.handlebars;

import com.github.jknack.handlebars.Options;
import java.io.IOException;

@pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper
public class HandlebarsHelper {

    public Object equals(Object s1, Object s2, Options options) throws IOException {
        Options.Buffer buffer = options.buffer();
        if (s1 == null || s2 == null || !s1.equals(s2)) {
            buffer.append(options.inverse());
        } else {
            buffer.append(options.fn());
        }
        return buffer;
    }
}
