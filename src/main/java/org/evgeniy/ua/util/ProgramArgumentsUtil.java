package org.evgeniy.ua.util;

import java.util.Optional;
import java.util.function.Function;

public final class ProgramArgumentsUtil {

    private ProgramArgumentsUtil() {
        throw new UnsupportedOperationException();
    }

    public static String extract(String[] args, int position, String other) {
        return extract(args, position, Function.identity(), other);
    }

    public static <T> T extract(String[] args, int position, Function<String, T> mapper, T other) {
        return extract(args, position)
                .map(mapper)
                .orElse(other);
    }

    public static Optional<String> extract(String[] args, int position) {
        if (args.length - 1 < position) {
            return Optional.empty();
        }
        return Optional.of(args[position]);
    }
}
