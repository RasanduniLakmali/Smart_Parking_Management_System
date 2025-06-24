package lk.ijse.parkingspaceservice.service;


import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSessionDTO;
import lk.ijse.parkingspaceservice.entity.AllowParking;
import lk.ijse.parkingspaceservice.entity.Parking;
import lk.ijse.parkingspaceservice.entity.ParkingSession;
import lk.ijse.parkingspaceservice.repo.AllowParkingRepo;
import lk.ijse.parkingspaceservice.repo.ParkingRepo;
import lk.ijse.parkingspaceservice.repo.ParkingSessionRepo;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.vehicleservice.dto.VehicleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final WebClient webClient;

    @Autowired
    ParkingRepo parkingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AllowParkingRepo allowParkingRepo;

    @Autowired
    private ParkingSessionRepo parkingSessionRepo;

    public ParkingServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public boolean saveParking(ParkingDTO parkingDTO) {
        Parking parking = modelMapper.map(parkingDTO, Parking.class);
        parkingRepo.save(parking);
        return true;
    }

    @Override
    public boolean updateParking(ParkingDTO parkingDTO) {
        Optional<Parking> optionalParking = parkingRepo.findByCode(parkingDTO.getSpace_code());

        if (optionalParking.isPresent()) {
            Parking parking = optionalParking.get();
            parking.setSpace_code(parkingDTO.getSpace_code());
            parking.setLocation(parkingDTO.getLocation());
            parking.setCity(parkingDTO.getCity());
            parking.setZone(parkingDTO.getZone());
            parking.setDescription(parkingDTO.getDescription());
            parking.setIs_available(parkingDTO.getIs_available());
            parking.setOwner_id(parkingDTO.getOwner_id());
            parkingRepo.save(parking);
            return true;
        }
        return false;
    }

    @Override
    public List<ParkingDTO> getAllParkings() {
        List<Parking> parkingList = parkingRepo.findAll();
        return parkingList.stream()
                .map(parking -> modelMapper.map(parking, ParkingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AvailableParkingDTO> getAvailableSpaces(boolean isAvailable) {
        List<Parking> availableList = parkingRepo.findAvailableSpaces(isAvailable);
        System.out.println(availableList);
        return availableList.stream()
                .map(parking -> modelMapper.map(parking, AvailableParkingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AvailableParkingDTO> getSpacesByCity(String city) {
        List<Parking> spacesByCity = parkingRepo.filterSpacesByCity(city);
        return spacesByCity.stream()
                .map(parking -> modelMapper.map(parking, AvailableParkingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean markOccupied(String spaceCode) {
        parkingRepo.setOccupied(spaceCode);
        return true;
    }

    @Override
    public boolean markAvailable(String spaceCode) {
        parkingRepo.setAvailable(spaceCode);
        return true;
    }

    @Override
    public AllowParkingDTO allowParking(AllowParkingDTO dto) {

        System.out.println("get Allowed dto :" + dto);

        String licensePlate = dto.getLicensePlate();
        System.out.println(licensePlate);

        int userId = dto.getUserId();

        try {
            VehicleDTO vehicleResponse = webClient.get()
                    .uri("http://localhost:8082/vehicle-service/api/v1/vehicle/{licensePlate}",licensePlate)
                    .retrieve()
                    .bodyToMono(VehicleDTO.class)
                    .block();

            System.out.println(vehicleResponse);

            UserDTO userResponse = webClient.get()
                    .uri("http://localhost:8081/user-service/api/v1/user/getById/{userId}",userId)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();

            System.out.println(userResponse);

            assert vehicleResponse != null;

            if ((vehicleResponse.getLicense_plate() .equals(licensePlate))&& (userResponse != null)) {
                AllowParking allowParking = new AllowParking();
                allowParking.setSpace_code(dto.getSpaceCode());
                allowParking.setLicense_plate(dto.getLicensePlate());
                allowParking.setUser_id(dto.getUserId());
                allowParking.setAllowed_date(dto.getAllowedDate());
                allowParking.setEntry_time(dto.getEntryTime());
                allowParking.setExit_time(dto.getExitTime());
                allowParkingRepo.save(allowParking);

                Parking parking = parkingRepo.findBySpaceCode(dto.getSpaceCode());

                if (parking != null){
                    parking.setIs_available(false);
                    parkingRepo.save(parking);
                }
                return dto;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateExitTime(int parkingId, LocalTime exitTime) {
        Optional<AllowParking> allowParking = allowParkingRepo.findById(parkingId);

        if (allowParking.isPresent()) {
            AllowParking allowParkingModel = allowParking.get();
            allowParkingModel.setExit_time(exitTime);
            allowParkingRepo.save(allowParkingModel);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStatus(String spaceCode) {
        Parking parking = parkingRepo.findBySpaceCode(spaceCode);
        if(parking.getIs_available() == true){
            return true;
        }
        return false;
    }

    @Override
    public boolean startParkingSession(int parkingId, int userId) {

        try{
            ParkingSession parkingSession = new ParkingSession();
            parkingSession.setParkingId(parkingId);
            parkingSession.setUserId(userId);
            parkingSession.setEntryTime(LocalTime.now());
            parkingSession.setIsPaid(false);
            parkingSessionRepo.save(parkingSession);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public ParkingSessionDTO finishParkingSession(int sessionId) {
        Optional<ParkingSession> optionalParkingSession = parkingSessionRepo.findById(sessionId);
        ParkingSession parkingSession = optionalParkingSession.get();
        parkingSession.setExitTime(LocalTime.now());

        double ratePerHour = 200.00;
        double totalFee = calculateFee(parkingSession.getEntryTime(),parkingSession.getExitTime(),ratePerHour);
        parkingSession.setTotalFee(totalFee);
        ParkingSessionDTO parkingSessionDTO = modelMapper.map(parkingSessionRepo.save(parkingSession), ParkingSessionDTO.class);

        if(parkingSessionDTO != null){
            return parkingSessionDTO;
        }

        return null;
    }

    @Override
    public ParkingSessionDTO getSessionById(int sessionId) {
        Optional<ParkingSession> session = parkingSessionRepo.findById(sessionId);

        if(session.isPresent()){
            ParkingSession parkingSession = session.get();
            return modelMapper.map(parkingSession, ParkingSessionDTO.class);
        }
        throw new  RuntimeException("Invalid session ID");
    }

    @Override
    public ParkingSessionDTO getSessionByUser(int userId) {
        ParkingSession parkingSession = parkingSessionRepo.getByUser(userId);

        if(parkingSession != null){
            return modelMapper.map(parkingSession, ParkingSessionDTO.class);
        }
        throw new  RuntimeException("Invalid driver ID");
    }


    private double calculateFee(LocalTime entryTime, LocalTime exitTime, double ratePerHour) {
        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("Entry time and exit time must not be null");
        }

        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();

        // Convert minutes to decimal hours
        double hours = minutes / 60.0;

        // Charge by rounding up to the next full hour (if you want)
        double fee = Math.ceil(hours) * ratePerHour;

        return fee;
    }
}


//    @Override
//    public boolean deleteSpace(int parkingId) {
//         parkingRepo.deleteByParking_id(parkingId);
//         return true;
//    }


