package com.xabe.broker.kafka.payload;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

public class MessagePayload {
    private final String message;
    private final int number;
    private final LocalDateTime dateTime;

    public MessagePayload(String message, int number, LocalDateTime dateTime) {
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

            MessagePayload messagePayload1 = (MessagePayload) o;

            return new EqualsBuilder()
                    .append(number, messagePayload1.number)
                    .append(message, messagePayload1.message)
                    .append(dateTime, messagePayload1.dateTime)
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
