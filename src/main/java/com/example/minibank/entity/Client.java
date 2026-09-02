package com.example.minibank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    // @PrePersist первый раз использую,
    // метод будет автоматически вызван перед сохранением объекта в базу (INSERT)
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
