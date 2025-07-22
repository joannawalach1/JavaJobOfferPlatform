package com.joboffer.user_service.domain;

import com.joboffer.common_lib.events.UserRegisteredEvent;
import com.joboffer.user_service.domain.dto.CreateUserRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserService(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
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
                saved.getLastName()
                );
        kafkaTemplate.send("user-registered", event);
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
