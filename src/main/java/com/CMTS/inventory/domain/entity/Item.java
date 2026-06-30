package com.CMTS.inventory.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name="items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private int quantity;

    private String name;

    public enum Categories {
        LIGHTS, SOUND, COSTUMES, PROPS, HAIR_MAKEUP
    }

    @Enumerated(EnumType.STRING)
    private Categories category;

    public enum Status {
        AVAILABLE, CHECKED_OUT
    }
    @Enumerated(EnumType.STRING)
    private Status status;

    private String location;

    private String photoURL;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "production_id")
    private Production production;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Production getProduction() {
        return production;
    }
    public void setProduction(Production production) {
        this.production = production;
    }
}
