package com.devsling.fr.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(unique = true)
    @NotNull
    private String username;

    private Boolean enabled;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String gender;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "userRole", joinColumns = @JoinColumn(name = "idUser"), inverseJoinColumns = @JoinColumn(name = "idRole"))
    private List<Role> roles = new ArrayList<>();

}
