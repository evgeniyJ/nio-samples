package org.evgeniy.ua.file;

import org.evgeniy.ua.util.ProgramArgumentsUtil;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class DirectoryWatchService {

    private static final String DEFAULT_DIRECTORY = "D:\\test";

    private final String path;

    public DirectoryWatchService(String path) {
        Objects.requireNonNull(path);
        this.path = path;
    }

    public void monitor() {
        Path path = Paths.get(this.path);
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
            );
            for (; ; ) {
                final WatchKey key = watchService.take();
                if (Objects.nonNull(key)) {
                    key.pollEvents().forEach(this::processEvent);
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processEvent(WatchEvent<?> watchEvent) {
        WatchEvent.Kind<?> kind = watchEvent.kind();
        if (kind == StandardWatchEventKinds.OVERFLOW) {
            return;
        }
        final Path filename = (Path) watchEvent.context();
        System.out.println(kind + " -> " + filename);
    }

    public static void monitor(String[] args) {
        String path = ProgramArgumentsUtil.extract(args, 1, DEFAULT_DIRECTORY);
        System.out.println("Starting monitor directory : " + path);
        new DirectoryWatchService(path).monitor();
    }
}
