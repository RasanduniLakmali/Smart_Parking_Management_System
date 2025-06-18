package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;

import java.util.List;

public interface ParkingService {

    boolean saveParking(ParkingDTO parkingDTO);

    boolean updateParking(ParkingDTO parkingDTO);

    List<ParkingDTO> getAllParkings();

    List<AvailableParkingDTO> getAvailableSpaces(boolean isAvailable);

    List<AvailableParkingDTO> getSpacesByCity(String city);

    boolean markOccupied(String spaceCode);

    boolean markAvailable(String spaceCode);

    AllowParkingDTO allowParking(AllowParkingDTO dto);

//    boolean deleteSpace(int parkingId);
}
