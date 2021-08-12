package com.codereggs.superHeroeTeam.models;

import com.codereggs.superHeroeTeam.enums.Role;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;

@Data
@Document(collection= "users")
@Accessors(chain = true)
public class User {

    @Id
    @Indexed
    private String id;

    @Indexed
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    private String photo;

    private Long createdAt;

    private Long updatedAt;

    @Field(targetType = FieldType.STRING)
    private Role role;
}
