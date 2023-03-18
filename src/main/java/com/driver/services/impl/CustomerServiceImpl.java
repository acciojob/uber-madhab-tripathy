package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		List<TripBooking> trip = customer.getTripBookingList();
		for(TripBooking tripBooking : trip){
			tripBooking.setCustomer(null);
		}
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		 List<Driver> driverList = driverRepository2.findAll();
		 Driver driver = new Driver();
		 Cab cab = null;
		 boolean isCab = false;
		 for(Driver d : driverList){
			 cab = d.getCab();
			 if(cab.getAvailable()){
				 driver = d;
				 isCab = true;
				 break;
			 }
		 }
		 if(!isCab){
			 throw new Exception("No cab available!");
		 }

		 Customer customer = customerRepository2.findById(customerId).get(); // get customer
		 TripBooking tripBooking = new TripBooking(); // make new trip
		  // make new Driver
		 driver.getCab().setAvailable(false);
		// set trip values
		 tripBooking.setFromLocation(fromLocation);
		 tripBooking.setToLocation(toLocation);
		 tripBooking.setDistanceInKm(distanceInKm);
		 tripBooking.setStatus(TripStatus.CONFIRMED);
		 tripBooking.setDriver(driver);

		 int totalBill = distanceInKm * 10;
		 tripBooking.setBill(totalBill);
		 // trip booked to the respective customer and set the customer
		 tripBooking.setCustomer(customer);
		 // add trip list in customer
		 List<TripBooking> customerList = customer.getTripBookingList();
		 customerList.add(tripBooking);
		 // add trip list in driver
		 List<TripBooking> findDriverList = driver.getTripBookingList();
		 findDriverList.add(tripBooking);
		 // save customer and driver in their respective table
		 customerRepository2.save(customer);
		 driverRepository2.save(driver);
		 return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		Driver driver = tripBooking.getDriver();
		Cab cab = driver.getCab();
		tripBooking.setBill(0);
		tripBooking.setDistanceInKm(0);
		tripBooking.setStatus(TripStatus.CANCELED);
		cab.setAvailable(true);
		driverRepository2.save(driver);
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		Driver driver = tripBooking.getDriver();
		driver.getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
		driverRepository2.save(driver);

	}
}
