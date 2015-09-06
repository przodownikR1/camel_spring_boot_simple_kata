package pl.java.scalatech.error;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

public final class CamelJmsTestHelper {

    private static AtomicInteger counter = new AtomicInteger(0);

    private CamelJmsTestHelper() {
    }

    public static ConnectionFactory createConnectionFactory() {
        return createConnectionFactory(null);
    }

    public static ConnectionFactory createConnectionFactory(String options) {
        // using a unique broker name improves testing when running the entire test suite in the same JVM
        int id = counter.incrementAndGet();
        String url = "vm://test-broker-" + id + "?broker.persistent=false&broker.useJmx=false";
        if (options != null) {
            url = url + "&" + options;
        }
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory;
    }

    public static ConnectionFactory createPersistentConnectionFactory() {
        return createPersistentConnectionFactory(null);
    }

    public static ConnectionFactory createPersistentConnectionFactory(String options) {
        // using a unique broker name improves testing when running the entire test suite in the same JVM
        int id = counter.incrementAndGet();
        String url = "vm://test-broker-" + id + "?broker.persistent=true&broker.useJmx=false";
        if (options != null) {
            url = url + "&" + options;
        }
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory;
    }
}