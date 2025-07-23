package com.joboffer.user_service.domain;

import com.joboffer.common_lib.events.UserRegisteredEvent;
import com.joboffer.user_service.domain.dto.CreateUserRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@Transactional
public class UserService {
    public static final String USER_REGISTERED = "user-registered";
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserService(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        log.info("UserService initialized with KafkaTemplate: {}", kafkaTemplate);
        log.info("KafkaTemplate default topic: {}", kafkaTemplate.getDefaultTopic());
    }
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("User with email" + request.email() + "already exists");
        }
        User newUser = new User(request.email(), request.firstName(), request.lastName());
        User saved = userRepository.save(newUser);

        UserRegisteredEvent event = UserRegisteredEvent.createEvent(
                saved.getId(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
        log.info("=== KAFKA DEBUG START ===");
        log.info("Próba wysłania eventu: {}", event);
        log.info("KafkaTemplate instance: {}", kafkaTemplate);
        log.info("KafkaTemplate class: {}", kafkaTemplate.getClass());

        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("user-registered", event);

            log.info("Future created: {}", future);

            SendResult<String, Object> result = future.get(10, TimeUnit.SECONDS);

            log.info("✅ SUCCESS! Message sent to Kafka:");
            log.info("   Topic: {}", result.getRecordMetadata().topic());
            log.info("   Partition: {}", result.getRecordMetadata().partition());
            log.info("   Offset: {}", result.getRecordMetadata().offset());
            log.info("   Timestamp: {}", result.getRecordMetadata().timestamp());

        } catch (InterruptedException e) {
            log.error("❌ INTERRUPTED: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("❌ EXECUTION ERROR: {}", e.getMessage(), e);
            log.error("   Cause: {}", e.getCause().getMessage());
        } catch (TimeoutException e) {
            log.error("❌ TIMEOUT: Message not sent within 10 seconds");
        } catch (Exception e) {
            log.error("❌ UNEXPECTED ERROR: {}", e.getMessage(), e);
        }

        log.info("=== KAFKA DEBUG END ===");

        return saved;
    }



    @Transactional
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
