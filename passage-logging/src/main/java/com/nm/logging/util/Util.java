package com.nm.logging.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by kre5335 on 5/24/2017.
 */
public final class Util {

    /**
     * Finds provied attribute in json.
     * @param json
     * @param fieldName
     * @return
     * @throws IOException
     */
    public static String findValue(@NotNull String json, String fieldName) throws IOException {
        return findValue(json.getBytes(), fieldName);
    }

    /**
     * @see #findValue(String, String)
     *
     * @param json
     * @param fieldName
     * @return
     * @throws IOException
     */
    public static String findValue(@NotNull byte[] json, String fieldName) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);
        while(!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if(JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fn = parser.getCurrentName();
                parser.nextToken();
                if(fn.equals(fieldName)) {
                    return parser.getValueAsString();
                }
            }
        }
        return null;
    }

}
