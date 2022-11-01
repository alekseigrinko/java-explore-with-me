package ru.practicum.server.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError extends RuntimeException {
    String status; // Код статуса HTTP-ответа (example: FORBIDDEN)
    String reason; // Общее описание причины ошибки
    String message; // Сообщение об ошибке
    String timestamp; // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
