package lk.ijse.paymentservice.service;

import lk.ijse.parkingspaceservice.dto.ParkingSessionDTO;
import lk.ijse.paymentservice.client.ParkingSessionClient;
import lk.ijse.paymentservice.client.UserClient;
import lk.ijse.paymentservice.dto.CardDetailsDTO;
import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.ReceiptDTO;
import lk.ijse.paymentservice.entity.CardDetails;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.entity.Receipt;
import lk.ijse.paymentservice.repo.CardDetailsRepo;
import lk.ijse.paymentservice.repo.PaymentRepo;
import lk.ijse.paymentservice.repo.ReceiptDetailRepo;
import lk.ijse.userservice.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private CardDetailsRepo cardDetailsRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ParkingSessionClient parkingSessionClient;

    @Autowired
    private ReceiptDetailRepo receiptDetailRepo;

    @Override
    public boolean saveCardDetails(CardDetailsDTO cardDetailsDTO) {

        System.out.println("cardDetailsDTO get: " + cardDetailsDTO);

        try {
            CardDetails cardDetails = modelMapper.map(cardDetailsDTO, CardDetails.class);

            UserDTO userDTO = userClient.isUserExists(cardDetailsDTO.getDriverId());

            System.out.println(userDTO);

            if (userDTO != null) {
                cardDetailsRepo.save(cardDetails);
                return true;
            }

            throw new RuntimeException("User not found");

        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean savePayment(PaymentRequestDTO requestDTO) {

          System.out.println("requestDTO get: " + requestDTO);

         UserDTO userDTO =  userClient.isUserExists(requestDTO.getDriverId());

        System.out.println("user received : " + userDTO);

        ParkingSessionDTO parkingSessionDTO =parkingSessionClient.isSessionExists(requestDTO.getDriverId());

        System.out.println("session received : " + parkingSessionDTO);

         if(userDTO != null && parkingSessionDTO != null) {
             Payment payment = new Payment();
             payment.setParkingSessionId(parkingSessionDTO.getSessionId());
             payment.setDriverId(requestDTO.getDriverId());
             payment.setAmount(requestDTO.getAmount());
             payment.setMethod(requestDTO.getMethod());
             payment.setTransactionTime(LocalDateTime.now());
             paymentRepo.save(payment);
             return true;
         }
         return false;
    }


    @Override
    public ReceiptDTO setReceiptDetails(PaymentRequestDTO requestDTO) {
        System.out.println(requestDTO);
        Receipt receipt = new Receipt();
        receipt.setDriverId(String.valueOf(requestDTO.getDriverId()));
        receipt.setAmount(requestDTO.getAmount());
        receipt.setIssuedTime(LocalDateTime.now());

        // Find the latest receipt number
        Receipt lastReceipt = receiptDetailRepo.findTopByOrderByReceiptIdDesc();
        String nextReceiptNumber;

        if (lastReceipt == null) {
            nextReceiptNumber = "RE001";
        } else {
            // Extract numeric part, increment, and format
            String lastNumber = lastReceipt.getReceiptNumber().substring(2);
            int number = Integer.parseInt(lastNumber) + 1;
            nextReceiptNumber = String.format("RE%03d", number);
        }

        receipt.setReceiptNumber(nextReceiptNumber);

        // Save the new receipt
        receipt = receiptDetailRepo.save(receipt);

        // Build DTO
        ReceiptDTO dto = new ReceiptDTO();
        dto.setReceiptId(receipt.getReceiptId());
        dto.setReceiptNumber(receipt.getReceiptNumber());
        dto.setDriverId(receipt.getDriverId());
        dto.setAmount(receipt.getAmount());
        dto.setIssuedTime(receipt.getIssuedTime());

        return dto;
    }
}
