package org.evgeniy.ua.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgramArgumentsUtilTest {

    @Test
    public void extractTest() {
        String[] args = {"netty", "1234"};

        String firstArgument = ProgramArgumentsUtil.extract(args, 0, "nio");
        Integer secondArgument = ProgramArgumentsUtil.extract(args, 1, Integer::valueOf, 8080);
        String thirdArgument = ProgramArgumentsUtil.extract(args, 2, "test");

        assertEquals(firstArgument, "netty");
        assertEquals((int) secondArgument, 1234);
        assertEquals(thirdArgument, "test");
    }
}
