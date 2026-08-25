package com.CMTS.inventory.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_photos")
public class ItemPhoto {

    @Id
    private Long itemId;

    @Column(columnDefinition = "bytea")
    private byte[] data;

    private String contentType;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}