package pl.palkaszymon.ecommercewebflux.messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitMQConfig {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Bean
    Mono<Connection> connectionMono(RabbitProperties rabbitProperties) throws NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.useNio();
        connectionFactory.useSslProtocol(); // Ensure SSL is used

        return Mono.fromCallable(() -> connectionFactory.newConnection("receiver-connection")).cache();
    }

    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }

    @Bean
    Receiver receiver(Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }

    @Bean
    public CommandLineRunner setupListeners(OrderConfirmationListener orderConfirmationListener) {
        return args -> orderConfirmationListener.setupListener();
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(new Queue("q.warehouse-notifications"));
        amqpAdmin.declareQueue(new Queue("q.customer-service-notifications"));
        amqpAdmin.declareQueue(new Queue("q.order-confirmations"));
    }
}
