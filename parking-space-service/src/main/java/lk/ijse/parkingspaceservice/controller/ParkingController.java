package lk.ijse.parkingspaceservice.controller;

import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;
import lk.ijse.parkingspaceservice.service.ParkingService;
import lk.ijse.parkingspaceservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/parking")
public class ParkingController {

    @Autowired
    ParkingService parkingService;

    @PostMapping("save")
    public ResponseUtil saveParkingSpace(@RequestBody ParkingDTO parkingDTO) {
        boolean isSaved = parkingService.saveParking(parkingDTO);

        if (isSaved) {
            return new ResponseUtil(201, "Parking saved successfully!", null);
        } else {
            return new ResponseUtil(200, "Parking not saved!", null);
        }

    }

    @PutMapping("update")
    public ResponseUtil updateParkingSpace(@RequestBody ParkingDTO parkingDTO) {
        boolean isUpdated = parkingService.updateParking(parkingDTO);

        if (isUpdated) {
            return new ResponseUtil(200, "Parking updated successfully!", null);
        }else {
            return new ResponseUtil(200, "Parking not updated!", null);
        }
    }

    @GetMapping("getAll")
    public List<ParkingDTO> getAllParkingSpaces() {
       List<ParkingDTO> parkingDTOList =  parkingService.getAllParkings();
       return parkingDTOList;
    }


    @GetMapping("available/{isAvailable}")
    public List<AvailableParkingDTO> getAvailableParkingSpaces(@PathVariable boolean isAvailable) {
        System.out.println(isAvailable);
        List<AvailableParkingDTO> availableSpaces = parkingService.getAvailableSpaces(isAvailable);
        System.out.println(availableSpaces);
        return availableSpaces;
    }


    @GetMapping("filter/{city}")
    public List<AvailableParkingDTO> getSpacesByCity(@PathVariable String city){
        List<AvailableParkingDTO> spacesByCity = parkingService.getSpacesByCity(city);
        return spacesByCity;
    }


    @PutMapping("occupy/{spaceCode}")
    public ResponseUtil markSpaceOccupied(@PathVariable String spaceCode){
        System.out.println(spaceCode);
        boolean isOccupied = parkingService.markOccupied(spaceCode);
        System.out.println("occupied " + isOccupied);
        if(isOccupied){
            return new ResponseUtil(201, "Space occupied successfully!", null);
        }
        else {
            return new ResponseUtil(200, "Space not occupied!", null);
        }
    }


    @PutMapping("available/{spaceCode}")
    public ResponseUtil markSpaceAvailable(@PathVariable String spaceCode){
        System.out.println(spaceCode);
        boolean isAvailable = parkingService.markAvailable(spaceCode);
        if(isAvailable){
            return new ResponseUtil(201, "Space set available successfully!!", null);
        }else {
            return new ResponseUtil(200, "Space not available!", null);
        }
    }


//    @DeleteMapping("delete/{parkingId}")
//    public ResponseUtil deleteParkingSpace(@PathVariable int parkingId){
//       boolean isDeleted = parkingService.deleteSpace(parkingId);
//
//       if (isDeleted){
//           return new ResponseUtil(201, "Space deleted successfully!!", null);
//       }else {
//           return new ResponseUtil(200, "Space not deleted!", null);
//       }
//    }


    @PostMapping("/allow")
    public ResponseEntity<ResponseUtil> allowParking(@RequestBody AllowParkingDTO dto) {
        System.out.println("allowParking : " + dto);
        parkingService.allowParking(dto);
        return ResponseEntity.ok(new ResponseUtil(200, "Parking allowed successfully!", null));
    }

}
