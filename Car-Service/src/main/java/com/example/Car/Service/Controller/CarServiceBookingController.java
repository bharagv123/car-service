package com.example.Car.Service.Controller;

import com.example.Car.Service.Entity.BookingServiceEntity;
import com.example.Car.Service.Entity.RescheduleBookingEntity;
import com.example.Car.Service.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarServiceBookingController {

    @Autowired
    BookingService bookingService;
    @PostMapping("/api/carservice/booking")
    public ResponseEntity<String> addBookingDetails(@RequestBody BookingServiceEntity bookingServiceEntity){
        return bookingService.addBookingDetails(bookingServiceEntity);
    }

    @PostMapping("/api/reschedule/carservice/booking")
    public  ResponseEntity<String> rescheduleBookingDetails(@RequestBody RescheduleBookingEntity rescheduleBookingEntity){
         return bookingService.rescheduleBookingDetails(rescheduleBookingEntity);
    }

    @GetMapping("/api/all/booking/slots")
    public ResponseEntity<String> allBookingDetails(){
        String totalBookings = bookingService.getAllBookingServices();
        return new ResponseEntity<String>(totalBookings,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/api/delete/booking/{aid}")
    public ResponseEntity<String> cancelBooking(@PathVariable("aid") int appointmentId){
        return bookingService.deleteBookingDetails(appointmentId);

    }

    @PostMapping("/api/all/open/slots")
    public ResponseEntity<String> emptyBookingLots(){
        String result = bookingService.getEmptyBookingslots();
        return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
    }

}
