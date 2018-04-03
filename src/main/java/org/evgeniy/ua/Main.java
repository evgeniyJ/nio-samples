package org.evgeniy.ua;

import com.google.common.collect.ImmutableMap;
import org.evgeniy.ua.client.LoadTestingClient;
import org.evgeniy.ua.file.DirectoryWatchService;
import org.evgeniy.ua.server.Server;
import org.evgeniy.ua.util.ProgramArgumentsUtil;

import java.util.Map;
import java.util.function.Consumer;

public class Main {

    private static final Map<String, Consumer<String[]>> APPLICATIONS = ImmutableMap.of(
            "server", Server::run,
            "client", LoadTestingClient::start,
            "directory-watch", DirectoryWatchService::monitor
    );

    private static final String DEFAULT_APP = "server";

    public static void main(String[] args) {
        String applicationKey = ProgramArgumentsUtil.extract(args, 0, DEFAULT_APP);
        APPLICATIONS.get(applicationKey).accept(args);
    }
}
