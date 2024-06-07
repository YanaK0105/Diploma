package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min=2, max=30, message = "Name should be between 2 and 30 characters")
    @NotEmpty(message = "Name should not be empty")
    @Column(unique = true)
    private String name;

    @NonNull
    @Size(max = 255, min = 4, message = "Password must be no more than 255 characters long and at least 4 characters long")
    private String password;


    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    //РОЛЬ ЭТО объект
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
