package org.evgeniy.ua;

import com.google.common.collect.ImmutableMap;
import org.evgeniy.ua.client.LoadTestingClient;
import org.evgeniy.ua.file.DirectoryWatchService;
import org.evgeniy.ua.server.Server;

import java.util.Arrays;
import java.util.Map;

public class Main {

    private static final Map<String, Executable> APPLICATIONS = ImmutableMap.of(
            "server", Server::run,
            "client", LoadTestingClient::start,
            "directory-watch", DirectoryWatchService::monitor
    );

    private static final String DEFAULT_APP = "server";

    public static void main(String[] args) throws Exception {
        String applicationKey = DEFAULT_APP;
        if (args.length > 0) {
            applicationKey = args[0];
            args = Arrays.copyOfRange(args, 1, args.length);
        }
        APPLICATIONS.get(applicationKey).run(args);
    }
}
