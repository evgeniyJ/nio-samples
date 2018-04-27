# Samples with Java NIO library

NioEchoServer - simple TCP/IP server, which consumes data from client and writes into socket corresponding messages.

NettyEchoServer - same echo server but using Netty framework.

Nio2EchoServer - same echo server but using NIO2 async channel.

LoadTestingClient - class, which creates 10000 client connections, you can test load on your nio server.

DirectoryWatchService - simple usage of Watch Service API. Class which monitors changes in predefined directory.

You can play with generated jar file. Follow next steps :
 - Run maven command : "mvn clean install" to build executable jar file;
 - Execute : "java -jar target/nio-samples.jar args", where args could be next keywords :
    - "server nio 8888" for NioEchoServer on port 8888;
    - "server netty 7777" for NettyEchoServer on port 7777;
    - "server nio2 3456" for Nio2EchoServer on port 3456;
    - "client 7777" for LoadTestingClient and server runs on local machine on port 7777;
    - "directory-watch D:\\watch" for DirectoryWatchService, where "D:\\watch" is directory to monitor.