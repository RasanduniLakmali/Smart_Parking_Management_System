package lk.ijse.parkingspaceservice.service;


import lk.ijse.parkingspaceservice.dto.AllowParkingDTO;
import lk.ijse.parkingspaceservice.dto.AvailableParkingDTO;
import lk.ijse.parkingspaceservice.dto.ParkingDTO;
import lk.ijse.parkingspaceservice.entity.AllowParking;
import lk.ijse.parkingspaceservice.entity.Parking;
import lk.ijse.parkingspaceservice.repo.AllowParkingRepo;
import lk.ijse.parkingspaceservice.repo.ParkingRepo;
import lk.ijse.vehicleservice.dto.VehicleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

            assert vehicleResponse != null;

            if (vehicleResponse.getLicense_plate() .equals(licensePlate)) {
                AllowParking allowParking = new AllowParking();
                allowParking.setSpace_code(dto.getSpaceCode());
                allowParking.setLicense_plate(dto.getLicensePlate());
                allowParking.setUser_id(dto.getUserId());
                allowParking.setAllowed_date(dto.getAllowedDate());
                allowParking.setEntry_time(dto.getEntryTime());
                allowParking.setExit_time(dto.getExitTime());
                allowParkingRepo.save(allowParking);
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
}


//    @Override
//    public boolean deleteSpace(int parkingId) {
//         parkingRepo.deleteByParking_id(parkingId);
//         return true;
//    }


