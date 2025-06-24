package lk.ijse.parkingspaceservice.controller;

import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSessionDTO;
import lk.ijse.parkingspaceservice.service.ParkingService;
import lk.ijse.parkingspaceservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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

        boolean isAvailable = parkingService.checkStatus(dto.getSpaceCode());

        if (isAvailable == true){
            parkingService.allowParking(dto);
            return ResponseEntity.ok(new ResponseUtil(200, "Parking allowed successfully!", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseUtil(401, "Parking not allowed!Space is not available!", null));
    }


    @PutMapping("update-exit-time/{parkingId}")
    public ResponseEntity<ResponseUtil> updateExitTime(@PathVariable int parkingId, @RequestBody LocalTime exitTime){
        System.out.println(parkingId);
        try{
            boolean isUpdated = parkingService.updateExitTime(parkingId,exitTime);
            if(isUpdated){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseUtil(200, "Exit time updated successfully!",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(401, "Exit time not updated!", null));
        }
        return null;
    }


    @PostMapping("start")
    public ResponseEntity<ResponseUtil> startParkingSession(@RequestBody ParkingSessionDTO parkingSessionDTO){
        System.out.println(parkingSessionDTO.getParkingId());
        System.out.println(parkingSessionDTO.getUserId());

        try{
            boolean isSaved = parkingService.startParkingSession(parkingSessionDTO.getParkingId(),parkingSessionDTO.getUserId());
            if(isSaved){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseUtil(200, "Parking session starting detail saved!", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(401, "Parking session not starting!", null));
        }

        return null;
    }


    @PutMapping("finish/{sessionId}")
    public ResponseEntity<ResponseUtil> finishParkingSession(@PathVariable int sessionId){
        System.out.println(sessionId);

        try{
            ParkingSessionDTO parkingSessionDTO = parkingService.finishParkingSession(sessionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseUtil(200, "Parking session finished successfully!", parkingSessionDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(401, "Parking session not finished!", null));
        }


    }

    @GetMapping("/{sessionId}")
    public ParkingSessionDTO getSessionById(@PathVariable int sessionId){
        ParkingSessionDTO parkingSessionDTO = parkingService.getSessionById(sessionId);

        if (parkingSessionDTO != null){
            return parkingSessionDTO;
        }
        return null;
    }

    @GetMapping("getByDriver/{userId}")
    public ParkingSessionDTO getSessionByUserId(@PathVariable int userId){

        System.out.println("userId : " + userId);

        ParkingSessionDTO parkingSessionDTO = parkingService.getSessionByUser(userId);

        System.out.println("session found : " + parkingSessionDTO);

        if (parkingSessionDTO != null){
            return parkingSessionDTO;
        }
        return null;
    }

}
