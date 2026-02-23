package com.my.service.comms_service.mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.my.service.comms_service.model.Notification;
import com.my.service.comms_service.dto.request.NotificationRequestDTO;
import com.my.service.comms_service.dto.response.NotificationResponseDTO;
import com.my.service.comms_service.contracts.comms.base.Action;
import com.my.service.comms_service.contracts.comms.request.NotificationMessageContext;
import com.my.service.comms_service.util.enums.NotificationStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    @Autowired
    private ObjectMapper objectMapper;

    public NotificationResponseDTO toDto(Notification entity) {
        if (entity == null)
            return null;

        NotificationResponseDTO dto = new NotificationResponseDTO();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setBody(entity.getBody());
        dto.setIcon(entity.getIcon());
        dto.setDeeplink(entity.getDeeplink());
        // dto.setMetadata(entity.getMetadata());
        dto.setActionMapping(extractKeysOnly(entity.getActionMapping()));
        dto.setStatus(entity.getStatus());
        dto.setAccountContext(entity.getAccountContext());
        dto.setActedAt(entity.getActedAt());

        return dto;
    }

    public Notification toEntity(NotificationRequestDTO dto) {
        if (dto == null)
            return null;

        Notification entity = new Notification();

        entity.setUserId(dto.getUserId());
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setBody(dto.getBody());
        entity.setIcon(dto.getIcon());
        entity.setDeeplink(dto.getDeeplink());
        entity.setMetadata(dto.getMetadata());
        entity.setActionMapping(dto.getActionMapping());
        entity.setIsPushEnabled(dto.getIsPushEnabled());
        entity.setAccountContext(dto.getAccountContext());
        return entity;
    }

    /**
     * Converts a list of MessageContext into a list of Notification entities.
     * Each MessageContext can contain multiple recipients → many Notification rows.
     */
    public List<Notification> toEntities(List<NotificationMessageContext> contexts) {

        List<Notification> result = new ArrayList<>();

        if (contexts == null || contexts.isEmpty()) {
            return result;
        }

        for (NotificationMessageContext ctx : contexts) {

            if (ctx.getRecipients() == null || ctx.getRecipients().isEmpty()) {
                throw new IllegalArgumentException("Recipients cannot be empty for notifications");
            }

            for (String userIdStr : ctx.getRecipients()) {

                UUID userId;
                try {
                    userId = UUID.fromString(userIdStr);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid userId in recipients: " + userIdStr);
                }

                Notification n = new Notification();
                n.setUserId(userId);
                n.setType(ctx.getNotificationType());
                n.setTitle(ctx.getSubject());
                n.setBody(ctx.getBody());
                n.setIcon(ctx.getIcon());
                n.setDeeplink(ctx.getDeeplink());

                // Actions
                JsonNode node = convertActionsToMap(ctx.getActions());
                n.setActionMapping(node);

                // Convert metaConfig → JsonNode (using objectMapper)
                if (ctx.getMetaConfig() != null) {
                    JsonNode metadata = objectMapper.valueToTree(ctx.getMetaConfig());
                    n.setMetadata(metadata);
                }

                n.setStatus(NotificationStatus.CREATED);

                n.setAccountContext(ctx.getAccountContext());
                n.setIsPushEnabled(ctx.getIsPushEnabled());

                result.add(n);
            }
        }

        return result;
    }

    public JsonNode convertActionsToMap(List<Action> actions) {

        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException("actions cannot be empty");
        }

        Map<String, URI> map = new LinkedHashMap<>();

        for (Action action : actions) {

            String key = action.getType().name(); // ENUM NAME = KEY
            URI uri = URI.create(action.getUri());

            if (map.containsKey(key)) {
                throw new IllegalArgumentException(
                        "Duplicate action type found: " + key);
            }

            map.put(key, uri);
        }

        return objectMapper.valueToTree(map);
    }
    public JsonNode extractKeysOnly(JsonNode actionMapping) {
        if (actionMapping == null || !actionMapping.isObject()) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode keysArray = mapper.createArrayNode();

        actionMapping.fieldNames().forEachRemaining(keysArray::add);

        return keysArray;
    }   


}