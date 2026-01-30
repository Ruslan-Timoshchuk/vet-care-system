package com.system.vetcare.domain;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(unique = true)
    private String email;

    private String password;
    
    private String phone;
    
    @Column(name = "legal_certificate_id")
    private String legalCertificateId;
    
    @ManyToMany
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;
   
    @Column(name = "account_non_expired")
    private boolean accountNonExpired;
    
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    private boolean enabled;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
}