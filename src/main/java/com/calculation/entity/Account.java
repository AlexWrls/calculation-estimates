package com.calculation.entity;

import com.calculation.entity.calculation.Estimated;
import com.calculation.entity.calculation.Product;
import com.calculation.entity.scheme.Building;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity implements UserDetails {


    @Email(message = "Введите вашу почту")
    @NotBlank(message = "Поле не может быть пустым!")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Поле не может быть пустым!")
    @Size(min = 6, message = "Проль длжен быть минимум 6 символов")
    @Column(name = "password")
    private String password;
    @Column(name = "status")
    private boolean status;
    @Column(name = "company")
    private String company;
    @Column(name = "department")
    private String department;
    @Column(name = "address")
    private String address;
    @Column(name = "site")
    private String site;
    @Column(name = "phone")
    private String phone;
    @Column(name = "position")
    private String position;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @Transient
    private String passwordConfirm;

    @Column(name = "activation_code")
    private String activationCode;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "role",joinColumns = @JoinColumn(name = "id_account"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",insertable = false,updatable = false)
    private List<Product> products;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",insertable = false,updatable = false)
    private List<Estimated> estimateds;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",insertable = false,updatable = false)
    private List<Building> buildings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
