package com.devsling.fr.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "candidate")
public class Candidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;


    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column("skills")
    private List<String> skills;

    @Column("resume_url")
    private String resumeUrl;

    @ElementCollection
    @CollectionTable(name = "professional_experiences", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column("experience")
    private List<String> professionalExperiences;

    @OneToOne
    @JoinColumn(name = "image_data_id", referencedColumnName = "id")
    private String imageName;

}
