package lk.ijse.paymentservice.controller;

import lk.ijse.paymentservice.dto.CardDetailsDTO;
import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.ReceiptDTO;
import lk.ijse.paymentservice.entity.Receipt;
import lk.ijse.paymentservice.service.PaymentService;
import lk.ijse.paymentservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("saveCard")
    public ResponseEntity<ResponseUtil> addCardDetails(@RequestBody CardDetailsDTO cardDetailsDTO){
        System.out.println(cardDetailsDTO);
        try{
            boolean isSaved = paymentService.saveCardDetails(cardDetailsDTO);
            System.out.println(isSaved);

            if(isSaved){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseUtil(200,"Card Details Saved Successfully!",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(401, "Card details not saved!", null));
        }
        return null;
    }


    @PostMapping("requestPay")
    public ResponseEntity<ResponseUtil> savePaymentDetails(@RequestBody PaymentRequestDTO requestDTO){

        System.out.println(requestDTO);

        try{
            boolean isSaved = paymentService.savePayment(requestDTO);

            System.out.println("payment status : " + isSaved);

            if(isSaved){
                ReceiptDTO  receiptDTO = createReceipt(requestDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseUtil(200,"Payment Successfull!",receiptDTO));
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUtil(401, "Payment failed!", null));
        }
        return null;
    }


    private ReceiptDTO createReceipt(PaymentRequestDTO requestDTO) {

        ReceiptDTO receiptDTO = paymentService.setReceiptDetails(requestDTO);

        if (receiptDTO != null){
            return receiptDTO;
        }
        return null;
    }
}
