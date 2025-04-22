package com.remitly.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "swift_codes")
public class SwiftCode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "swift_code", unique = true, nullable = false)
    private String swiftCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "address")
    private String address;

    @Column(name = "country_iso2")
    private String countryISO2;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "is_headquarter")
    private boolean isHq;

    // Self-referencing relationship (HQ → branches)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private SwiftCode headquarter;

    @OneToMany(mappedBy = "headquarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwiftCode> branches = new ArrayList<>();

}

