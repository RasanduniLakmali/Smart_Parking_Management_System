package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {

    public boolean saveVehicle(VehicleDTO vehicleDTO);

    boolean updateVehicle(VehicleDTO vehicleDTO);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO findByLicensePlate(String licensePlate);
}
