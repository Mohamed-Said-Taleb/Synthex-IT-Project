package com.devsling.fr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "candidates")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;



    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "skills")
    private List<String> skills;

    @Column(name = "resume_url")
    private String resumeUrl;

    @ElementCollection
    @CollectionTable(name = "professional_experiences", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "experience")
    private List<String> professionalExperiences;
}
