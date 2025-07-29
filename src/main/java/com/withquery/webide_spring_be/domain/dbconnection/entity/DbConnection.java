package com.withquery.webide_spring_be.domain.dbconnection.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "db_connection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DbConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}