package com.vdurmont.emoji;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the emojis from a JSON database.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public final class EmojiLoader {

    /**
     * No need for a constructor, all the methods are static.
     */
    private EmojiLoader() {
    }

    /**
     * Loads a JSONArray of emojis from an InputStream, parses it and returns the
     * associated list of {@link com.vdurmont.emoji.Emoji}s
     *
     * @param stream the stream of the JSONArray
     * @return the list of {@link com.vdurmont.emoji.Emoji}s
     * @throws IOException if an error occurs while reading the stream or parsing
     *                     the JSONArray
     */
    public static List<Emoji> loadEmojis(final InputStream stream) throws IOException {
        final JSONArray emojisJSON = new JSONArray(inputStreamToString(stream));
        final List<Emoji> emojis = new ArrayList<>(emojisJSON.length());
        for (int i = 0; i < emojisJSON.length(); i++) {
            final Emoji emoji = buildEmojiFromJSON(emojisJSON.getJSONObject(i));
            if (emoji != null) {
                emojis.add(emoji);
            }
        }
        return emojis;
    }

    private static String inputStreamToString(
            final InputStream stream
    ) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
        final BufferedReader br = new BufferedReader(isr);
        String read;
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        br.close();
        return sb.toString();
    }

    protected static Emoji buildEmojiFromJSON(
            final JSONObject json
    ) {
        if (!json.has("emoji")) {
            return null;
        }

        final byte[] bytes = json.getString("emoji").getBytes(StandardCharsets.UTF_8);
        String description = null;
        if (json.has("description")) {
            description = json.getString("description");
        }
        boolean supportsFitzpatrick = false;
        if (json.has("supports_fitzpatrick")) {
            supportsFitzpatrick = json.getBoolean("supports_fitzpatrick");
        }
        final List<String> aliases = jsonArrayToStringList(json.getJSONArray("aliases"));
        final List<String> tags = jsonArrayToStringList(json.getJSONArray("tags"));
        return new Emoji(description, supportsFitzpatrick, aliases, tags, bytes);
    }

    private static List<String> jsonArrayToStringList(final JSONArray array) {
        final List<String> strings = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            strings.add(array.getString(i));
        }
        return strings;
    }
}
