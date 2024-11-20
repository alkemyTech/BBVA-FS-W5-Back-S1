package com.BBVA.DiMo_S1.D_models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "el nombre no debe estar nulo")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date updateDate;

    @PrePersist
    protected void onCreate(){
        this.creationDate = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updateDate = new Date();
    }
}
