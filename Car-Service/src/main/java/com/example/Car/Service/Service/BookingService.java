package com.example.Car.Service.Service;

import com.example.Car.Service.Entity.BookingServiceEntity;
import com.example.Car.Service.Entity.RescheduleBookingEntity;
import com.example.Car.Service.Repository.BookRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookRepositoryInterface bookRepositoryInterface;


    private final int TOT_SERVICES = 3;
    private final int TOT_HOURS = 24;
    int[][] slotsArray = new int[TOT_SERVICES][TOT_HOURS];

    public ResponseEntity<String> addBookingDetails(BookingServiceEntity bookingServiceEntity) {
        BookingServiceEntity bse = bookRepositoryInterface.findByServiceTypeAndStartTime(bookingServiceEntity.getServiceType(),bookingServiceEntity.getStartTime());
        if(bse==null){
            bookRepositoryInterface.save(bookingServiceEntity);
            return new ResponseEntity<>("Booked successfully",HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<String>("cannot be Booked at that time",HttpStatus.ACCEPTED);
        }
    }

    public ResponseEntity<String> rescheduleBookingDetails(RescheduleBookingEntity rescheduleBookingEntity) {
        int aId = rescheduleBookingEntity.getAppointmentId();
        int startTime = rescheduleBookingEntity.getStartTime();
        BookingServiceEntity bookingServiceEntity = bookRepositoryInterface.findById(aId).get();
        if (bookingServiceEntity==null) {
            return new ResponseEntity<String>("Appointment id incorrect", HttpStatus.ACCEPTED);
        } else {
            String serviceType = bookingServiceEntity.getServiceType();
            BookingServiceEntity bookingServiceEntity1 = bookRepositoryInterface.findByServiceTypeAndStartTime(serviceType, startTime);
            if (bookingServiceEntity1 == null) {
                BookingServiceEntity bookingServiceEntity2 = new BookingServiceEntity();
                bookingServiceEntity2.setCustomer("Bhargav");
                bookingServiceEntity2.setStartTime(rescheduleBookingEntity.getStartTime());
                bookingServiceEntity2.setEndTime(rescheduleBookingEntity.getStartTime() + 1);
                bookingServiceEntity2.setServiceType(bookingServiceEntity.getServiceType());
                bookRepositoryInterface.save(bookingServiceEntity2);
                bookRepositoryInterface.deleteById(bookingServiceEntity.getAppointmentId());
                return new ResponseEntity<>("Successfully Rescheduled", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<String>("Appointment not possible at that time", HttpStatus.ACCEPTED);
            }
        }
    }

    public String getTypeString(List<BookingServiceEntity>list,String type,int serviceType){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<list.size();i++){
            int sT = list.get(i).getStartTime();
            int eT = list.get(i).getEndTime();
            String str =  Integer.toString(sT) + "-" + Integer.toString(eT);
            sb.append(str);
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        String sPart = sb.toString();
        return  type + sPart;
    }

    public String getAllBookingServices() {
        List<BookingServiceEntity> bookingServiceEntitiesType0 = bookRepositoryInterface.findByServiceType("Type0");
        List<BookingServiceEntity> bookingServiceEntitiesType1 = bookRepositoryInterface.findByServiceType("Type1");
        List<BookingServiceEntity> bookingServiceEntitiesType2 = bookRepositoryInterface.findByServiceType("Type2");
        String Type0 = getTypeString(bookingServiceEntitiesType0,"Type0 : ",0);
        String Type1 = getTypeString(bookingServiceEntitiesType1,"Type1 : ",1);
        String Type2 = getTypeString(bookingServiceEntitiesType2,"Type2 : ",2);
        String totString = Type0 + "\n" + Type1 + "\n" + Type2;
        return totString;
    }

    public ResponseEntity<String> deleteBookingDetails(int appointmentId) {
        BookingServiceEntity bookingServiceEntity = bookRepositoryInterface.findById(appointmentId).get();
        if(bookingServiceEntity == null) {
            return new ResponseEntity<String>("No booking with appointmentId",HttpStatus.ACCEPTED);
        } else {
            bookRepositoryInterface.deleteById(appointmentId);
            return new ResponseEntity<String>("Deleted booking sucessfully",HttpStatus.ACCEPTED);
        }
    }

    public String getOpenSlotsByServiceType(int serviceType){
        List<String> openSlots = new ArrayList<>();
        int start = -1;
        for (int i = 0; i < TOT_HOURS; i++) {
            if (slotsArray[serviceType][i] == 0) {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    openSlots.add(start + "-" + i);
                    start = -1;
                }
            }
        }
        if (start != -1) {
            openSlots.add(start + "-" + TOT_HOURS);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < openSlots.size(); i++) {
            sb.append(openSlots.get(i));
            if (i < openSlots.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public void fillbookedSlots(List<BookingServiceEntity> list,int serviceType){
        for (int i=0;i< list.size();i++){
            slotsArray[serviceType][list.get(i).getStartTime()] = 1;
        }
    }

    public String getEmptyBookingslots() {
        String result = "";
        List<BookingServiceEntity> bookingServiceEntitiesType0 = bookRepositoryInterface.findByServiceType("Type0");
        List<BookingServiceEntity> bookingServiceEntitiesType1 = bookRepositoryInterface.findByServiceType("Type1");
        List<BookingServiceEntity> bookingServiceEntitiesType2 = bookRepositoryInterface.findByServiceType("Type2");
        fillbookedSlots(bookingServiceEntitiesType0,0);
        fillbookedSlots(bookingServiceEntitiesType1,1);
        fillbookedSlots(bookingServiceEntitiesType2,2);
        for (int i=0;i<TOT_SERVICES;i++){
            result += "Type" + Integer.toString(i) + " : ";
            result += getOpenSlotsByServiceType(i);
            result += "\n";
        }
        return result;
    }
}
