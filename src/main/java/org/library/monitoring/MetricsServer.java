package org.library.monitoring;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Serves the metrics of the application: http://localhost:8081/metrics */
public final class MetricsServer {

    /** Port 8080 is usually taken by Jenkins, so the default is 8081. Override with -Dmetrics.port=... */
    public static final int PORT = Integer.getInteger("metrics.port", 8081);

    private static HttpServer server;

    private MetricsServer() {
    }

    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/metrics", MetricsServer::handleMetrics);
        server.start();
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static void handleMetrics(HttpExchange exchange) throws IOException {
        byte[] body = LibraryMetrics.registry().scrape().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4; charset=utf-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}
