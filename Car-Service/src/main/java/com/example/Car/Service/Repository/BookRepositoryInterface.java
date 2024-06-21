package com.example.Car.Service.Repository;

import com.example.Car.Service.Entity.BookingServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepositoryInterface extends JpaRepository<BookingServiceEntity, Integer> {

    BookingServiceEntity findByServiceTypeAndStartTime(String serviceType, int startTime);

    List<BookingServiceEntity> findByServiceType(String type0);
}
