package com.vdurmont.emoji;

public final class TestTools {

    /**
     * No need for a constructor, all the methods are static.
     */
    private TestTools() {
    }

    public static boolean containsEmojis(
            final Iterable<Emoji> emojis,
            final String... aliases
    ) {
        for (final String alias : aliases) {
            final boolean contains = containsEmoji(emojis, alias);
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsEmoji(final Iterable<Emoji> emojis, final String alias) {
        for (final Emoji emoji : emojis) {
            for (final String al : emoji.getAliases()) {
                if (alias.equals(al)) {
                    return true;
                }
            }
        }
        return false;
    }
}
