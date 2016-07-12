package se.magmag.oauth.persistence.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by magnus.eriksson on 12/07/16.
 */

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private String googleId;

    private String name;
    private String email;
}
