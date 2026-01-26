package com.system.vetcare.domain;

import javax.persistence.*;
import com.system.vetcare.domain.enums.EAuthority;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EAuthority title;

    public String getAuthority() {
        return title.name();
    }

}