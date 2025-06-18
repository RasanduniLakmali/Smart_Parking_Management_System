package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repo.VehicleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class vehicleServiceImpl implements VehicleService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Override
    public boolean saveVehicle(VehicleDTO vehicleDTO) {
       Vehicle vehicle =  modelMapper.map(vehicleDTO, Vehicle.class);

       vehicleRepo.save(vehicle);
       return true;
    }

    @Override
    public boolean updateVehicle(VehicleDTO vehicleDTO) {

        System.out.println("update vehicle get : " + vehicleDTO );

        Optional<Vehicle> optionalVehicle = vehicleRepo.findByLicensePlate(vehicleDTO.getLicense_plate());

        System.out.println("vehicle found " + optionalVehicle);

        if(optionalVehicle.isPresent()){
           Vehicle vehicle = optionalVehicle.get();
           vehicle.setLicense_plate(vehicleDTO.getLicense_plate());
           vehicle.setModel(vehicleDTO.getModel());
           vehicle.setType(vehicleDTO.getType());
           vehicle.setColor(vehicleDTO.getColor());
           vehicle.setDriver_id(vehicleDTO.getDriver_id());
           vehicle.setRegistration_date(vehicleDTO.getRegistration_date());
           vehicleRepo.save(vehicle);
           return true;
        }
        return false;
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
          List<Vehicle> vehicleList = vehicleRepo.findAll();
        return vehicleList.stream()
                .map(vehicle -> modelMapper.map(vehicle,VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO findByLicensePlate(String licensePlate) {
        Optional<Vehicle> optionalVehicle = vehicleRepo.findByLicensePlate(licensePlate);
        if (optionalVehicle.isPresent()) {
            return modelMapper.map(optionalVehicle.get(),VehicleDTO.class);
        }
        return null;
    }
}
