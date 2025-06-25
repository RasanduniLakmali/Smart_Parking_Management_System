package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.CardDetailsDTO;
import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.ReceiptDTO;

import java.util.List;

public interface PaymentService {
    boolean saveCardDetails(CardDetailsDTO cardDetailsDTO);

    boolean savePayment(PaymentRequestDTO requestDTO);

    ReceiptDTO setReceiptDetails(PaymentRequestDTO requestDTO);

    List<PaymentDTO> getAllPayments();
}
