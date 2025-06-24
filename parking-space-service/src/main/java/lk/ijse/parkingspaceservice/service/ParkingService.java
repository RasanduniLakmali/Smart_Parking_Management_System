package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSessionDTO;
import lk.ijse.parkingspaceservice.entity.ParkingSession;

import java.time.LocalTime;
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

    boolean updateExitTime(int parkingId, LocalTime exitTime);

    boolean checkStatus(String spaceCode);

    boolean startParkingSession(int parkingId, int userId);

    ParkingSessionDTO finishParkingSession(int sessionId);

    ParkingSessionDTO getSessionById(int sessionId);

    ParkingSessionDTO getSessionByUser(int userId);

//    boolean deleteSpace(int parkingId);
}
