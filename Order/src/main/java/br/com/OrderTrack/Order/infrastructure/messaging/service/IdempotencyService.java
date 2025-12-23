package br.com.OrderTrack.Order.infrastructure.messaging.service;

import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProcessedEventEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProcessedEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {

    private final JPAProcessedEventsRepository repository;

    @Transactional
    public <T> void process(String messageId, T event, Consumer<T> action) {
        try {
            repository.saveAndFlush(new ProcessedEventEntity(messageId, LocalDateTime.now()));
        } catch (DataIntegrityViolationException e) {
            log.info("Duplicated event ignored (Idempotency Check): {}", messageId);
            return;
        }

        try {
            action.accept(event);
        } catch (Exception e) {
            log.error("Error processing event {}. Transaction will rollback.", messageId, e);
            throw e;
        }
    }
}