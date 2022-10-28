package ru.practicum.server.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "requester_id", nullable = false)
    private long requester;
    @Column(name = "event_id", nullable = false)
    private long event;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
