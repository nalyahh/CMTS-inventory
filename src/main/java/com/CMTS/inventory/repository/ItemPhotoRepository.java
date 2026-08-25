package com.CMTS.inventory.repository;

import com.CMTS.inventory.domain.entity.ItemPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPhotoRepository extends JpaRepository<ItemPhoto, Long> {
}