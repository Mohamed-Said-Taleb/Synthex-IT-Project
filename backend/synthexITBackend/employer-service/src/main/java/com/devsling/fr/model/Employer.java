package com.devsling.fr.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "employer")
public class Employer  {

    @Id
    @Column("id")
    private Long id;

    @Column("company_name")
    private String companyName;

    @Column("email")
    private String email;

    @Column("sector")
    private String sector;

    @Column("address")
    private String address;

    @Column("phone_number")
    private String phoneNumber;
}
