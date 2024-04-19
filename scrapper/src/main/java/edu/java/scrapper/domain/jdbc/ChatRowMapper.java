package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.dto.Chat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Component
public class ChatRowMapper implements RowMapper<Chat> {
    public static OffsetDateTime parseDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss.")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, false)
            .appendPattern("x")
            .toFormatter();

        return OffsetDateTime.parse(date, formatter);
    }

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chat(
            rs.getLong("id"),
            rs.getLong("chat_id"),
            parseDate(rs.getString("addition_time"))
        );
    }
}
