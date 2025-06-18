package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.service.VehicleService;

import lk.ijse.vehicleservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("register")
    public ResponseUtil saveVehicle(@RequestBody VehicleDTO vehicleDTO) {

        System.out.println("vehicle passed " + vehicleDTO);

        boolean isSaved = vehicleService.saveVehicle(vehicleDTO);

        if (isSaved) {
            return new ResponseUtil(201, "Vehicle registered successfully!", null);
        } else {
            return new ResponseUtil(200, "Vehicle not registered!", null);
        }

    }

    @PutMapping("update")
    public ResponseUtil updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        System.out.println("vehicle passed " + vehicleDTO);

        boolean isUpdated = vehicleService.updateVehicle(vehicleDTO);
        System.out.println("Vehicle updated " + isUpdated);

        if (isUpdated) {
            return new ResponseUtil(201, "Vehicle updated successfully!", null);
        } else {
            return new ResponseUtil(200, "Vehicle not updated!", null);
        }
    }

    @GetMapping("getAll")
    public List<VehicleDTO> getAllVehicles (){
        List<VehicleDTO> vehicleDTOS = vehicleService.getAllVehicles();
        return vehicleDTOS;
    }

    @GetMapping("/{licensePlate}")
    public VehicleDTO getVehicleByLicensePlate(@PathVariable String licensePlate) {
        VehicleDTO vehicle = vehicleService.findByLicensePlate(licensePlate);
        return vehicle;
    }
}

