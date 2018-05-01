package org.evgeniy.ua.util;

import java.util.Optional;
import java.util.function.Function;

public final class ProgramArgumentsUtil {

    private ProgramArgumentsUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param args     program arguments
     * @param position position of needed argument
     * @param other    default value in case of missed argument
     * @return argument from program arguments by specified position
     */
    public static String extract(String[] args, int position, String other) {
        return extract(args, position, Function.identity(), other);
    }

    /**
     * @param args     program arguments
     * @param position position of needed argument
     * @param mapper   function of mapping {@link String} to another value
     * @param other    default value in case of missed argument
     * @param <T>      type of return value
     * @return argument from program arguments by specified position
     */
    public static <T> T extract(String[] args, int position, Function<String, T> mapper, T other) {
        return extract(args, position)
                .map(mapper)
                .orElse(other);
    }

    private static Optional<String> extract(String[] args, int position) {
        if (args.length - 1 < position) {
            return Optional.empty();
        }
        return Optional.of(args[position]);
    }
}
