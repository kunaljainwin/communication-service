// package com.my.comms.service.comms_service.service;

// import com.my.service.comms_service.model.NotificationAction;
// import com.my.service.comms_service.util.enums.ResultType;
// import com.my.service.comms_service.repository.INotificationActionRepository;
// import com.my.service.comms_service.service.NotificationActionService;

// import jakarta.persistence.EntityNotFoundException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.Instant;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class NotificationActionServiceTest {

//     @Mock
//     private INotificationActionRepository repository;

//     @InjectMocks
//     private NotificationActionService service;

//     private UUID id;
//     private NotificationAction existing;

//     @BeforeEach
//     void setup() {
//         id = UUID.randomUUID();

//         existing = new NotificationAction();
//         existing.setId(id);
//         existing.setResultType(null);
//         existing.setActedAt(null);
//     }

//     // ---------- CREATE ----------

//     @Test
//     void create_shouldSaveEntity() {
//         NotificationAction input = new NotificationAction();
//         when(repository.save(input)).thenReturn(input);

//         NotificationAction result = service.create(input);

//         assertNotNull(result);
//         verify(repository).save(input);
//     }

//     // ---------- UPDATE ----------

//     @Test
//     void update_shouldThrow_whenNotFound() {
//         when(repository.findById(id)).thenReturn(Optional.empty());

//         assertThrows(EntityNotFoundException.class,
//                 () -> service.update(id, new NotificationAction()));

//         verify(repository, never()).save(any());
//     }

//     @Test
//     void update_shouldRejectPartialResultTypeOnly() {
//         when(repository.findById(id)).thenReturn(Optional.of(existing));

//         NotificationAction update = new NotificationAction();
//         update.setResultType(ResultType.SUCCESS);

//         IllegalStateException ex = assertThrows(
//                 IllegalStateException.class,
//                 () -> service.update(id, update)
//         );

//         assertEquals("result and actedAt must be updated together.", ex.getMessage());
//         verify(repository, never()).save(any());
//     }

//     @Test
//     void update_shouldRejectPartialActedAtOnly() {
//         when(repository.findById(id)).thenReturn(Optional.of(existing));

//         NotificationAction update = new NotificationAction();
//         update.setActedAt(Instant.now());

//         IllegalStateException ex = assertThrows(
//                 IllegalStateException.class,
//                 () -> service.update(id, update)
//         );

//         assertEquals("result and actedAt must be updated together.", ex.getMessage());
//         verify(repository, never()).save(any());
//     }

//     @Test
//     void update_shouldAllowFirstTimeResultAndActedAt() {
//         when(repository.findById(id)).thenReturn(Optional.of(existing));
//         when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         Instant now = Instant.now();

//         NotificationAction update = new NotificationAction();
//         update.setResultType(ResultType.APPROVED);
//         update.setActedAt(now);

//         NotificationAction result = service.update(id, update);

//         assertEquals(ResultType.APPROVED, result.getResultType());
//         assertEquals(now, result.getActedAt());
//         verify(repository).save(existing);
//     }

//     @Test
//     void update_shouldRejectSecondTimeResultAndActedAt() {
//         existing.setResultType(ResultType.APPROVED);
//         existing.setActedAt(Instant.now());

//         when(repository.findById(id)).thenReturn(Optional.of(existing));

//         NotificationAction update = new NotificationAction();
//         update.setResultType(ResultType.REJECTED);
//         update.setActedAt(Instant.now());

//         IllegalStateException ex = assertThrows(
//                 IllegalStateException.class,
//                 () -> service.update(id, update)
//         );

//         assertEquals("result and actedAt can only be set once.", ex.getMessage());
//         verify(repository, never()).save(any());
//     }

//     @Test
//     void update_shouldAllowNoOpUpdate() {
//         when(repository.findById(id)).thenReturn(Optional.of(existing));
//         when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         NotificationAction result = service.update(id, new NotificationAction());

//         assertNull(result.getResultType());
//         assertNull(result.getActedAt());
//         verify(repository).save(existing);
//     }

//     // ---------- REPLACE ----------

//     @Test
//     void replace_shouldReplaceWhenExists() {
//         NotificationAction input = new NotificationAction();

//         when(repository.existsById(id)).thenReturn(true);
//         when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

//         NotificationAction result = service.replace(id, input);

//         assertEquals(id, result.getId());
//         verify(repository).save(input);
//     }

//     @Test
//     void replace_shouldThrow_whenNotExists() {
//         when(repository.existsById(id)).thenReturn(false);

//         assertThrows(EntityNotFoundException.class,
//                 () -> service.replace(id, new NotificationAction()));

//         verify(repository, never()).save(any());
//     }

//     // ---------- DELETE ----------

//     @Test
//     void delete_shouldInvokeRepository() {
//         service.delete(id);
//         verify(repository).deleteById(id);
//     }

//     // ---------- FIND ----------

//     @Test
//     void findById_shouldReturnOptional() {
//         when(repository.findById(id)).thenReturn(Optional.of(existing));

//         Optional<NotificationAction> result = service.findById(id);

//         assertTrue(result.isPresent());
//         verify(repository).findById(id);
//     }
// }
