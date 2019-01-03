package com.xabe.broker.rabbitmq.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

public class MessagePayload {
    private final String message;
    private final int number;
    private final LocalDateTime dateTime;

    @JsonCreator
    public MessagePayload(
            @JsonProperty("message") String message,
            @JsonProperty("number") int number,
            @JsonProperty("dateTime") LocalDateTime dateTime) {
        this.message = message;
        this.number = number;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public int getNumber() {
        return number;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessagePayload) {

            MessagePayload message1 = (MessagePayload) o;

            return new EqualsBuilder()
                    .append(number, message1.number)
                    .append(message, message1.message)
                    .append(dateTime, message1.dateTime)
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(message)
                .append(number)
                .append(dateTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
