package com.nerius.taurus.db.repository;

import com.nerius.taurus.db.entities.Ad;
import org.springframework.data.repository.CrudRepository;

public interface AdRepository extends CrudRepository<Ad,Long> {
}
