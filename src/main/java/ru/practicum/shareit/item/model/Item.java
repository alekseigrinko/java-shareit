package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "NAME")
    private String name;
    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;
    @NotNull
    @Column(name = "IS_AVAILABLE")
    private Boolean available;
    @Column(name = "OWNER_ID")
    private Long owner;
    @Column(name = "REQUEST_ID")
    private Long requestId;
}
