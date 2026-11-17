package org.library.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

/**
 * Application metrics registered in a Prometheus registry.
 * They are exposed by {@link MetricsServer} at http://localhost:8080/metrics.
 */
public final class LibraryMetrics {

    private static final PrometheusMeterRegistry REGISTRY =
            new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    private static final Counter OPERATIONS = Counter.builder("application_operations_total")
            .description("Total number of book operations (borrow and return)")
            .register(REGISTRY);

    private static final Counter SUCCESS = Counter.builder("application_operations_success_total")
            .description("Number of book operations completed successfully")
            .register(REGISTRY);

    private static final Counter ERRORS = Counter.builder("application_operations_error_total")
            .description("Number of failed book operations")
            .register(REGISTRY);

    private static final Counter BORROWED = Counter.builder("library_books_borrowed_total")
            .description("Number of successful book issues")
            .register(REGISTRY);

    private static final Counter RETURNED = Counter.builder("library_books_returned_total")
            .description("Number of successful book returns")
            .register(REGISTRY);

    private static final Counter UNAVAILABLE = Counter.builder("library_unavailable_book_attempts_total")
            .description("Number of attempts to work with a book that is not available")
            .register(REGISTRY);

    private static final Timer DURATION = Timer.builder("library_operation_duration")
            .description("Duration of a book operation")
            .register(REGISTRY);

    private LibraryMetrics() {
    }

    public static PrometheusMeterRegistry registry() {
        return REGISTRY;
    }

    /** Counts every borrow/return request, successful or not. */
    public static void operationStarted() {
        OPERATIONS.increment();
    }

    public static void operationSucceeded() {
        SUCCESS.increment();
    }

    public static void operationFailed() {
        ERRORS.increment();
    }

    public static void bookBorrowed() {
        BORROWED.increment();
    }

    public static void bookReturned() {
        RETURNED.increment();
    }

    public static void unavailableBookAttempt() {
        UNAVAILABLE.increment();
    }

    public static Timer.Sample startTimer() {
        return Timer.start(REGISTRY);
    }

    public static void stopTimer(Timer.Sample sample) {
        sample.stop(DURATION);
    }
}
