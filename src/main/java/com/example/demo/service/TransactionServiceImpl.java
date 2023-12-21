package com.example.demo.service;

import com.example.demo.entities.Transaction;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.transaction.TransactionDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.example.demo.mappers.TransactionMapper.TRANSACTION_MAPPER;
import static com.example.demo.utils.JwtTokenUtils.getEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CardRepository cardRepository;
    private final AuthUserRepository authUserRepository;
    private final TransactionRepository transactionRepository;
    private final JavaMailSenderService javaMailSenderService;

    @Override
    public String getFullName(String number){
        return cardRepository.findByNumberAndActiveTrue(number)
                .orElseThrow(() -> new NotFoundException("Card not found")).getOwner();
    }

    @Override
    @Transactional
    public UUID create(TransactionDto dto) {
        Transaction transaction = TRANSACTION_MAPPER.toEntity(dto);
        if (!authUserRepository.existsByIdAndActiveTrue(dto.fromUserId)) {
            throw new NotFoundException("User %s not found"
                    .formatted(dto.fromUserId));
        }
        transaction.setFromUser(authUserRepository.findByIdAndActiveTrue(dto.fromUserId)
                .orElseThrow(()->new NotFoundException("User %s not found"
                        .formatted(dto.fromUserId))));
        if (!cardRepository.existsByNumberAndActiveTrue(dto.fromCardNumber)) {
            throw new NotFoundException("Card not found %s"
                    .formatted(dto.fromCardNumber));
        }
        transaction.setFromCard(cardRepository.findByNumberAndActiveTrue(dto.fromCardNumber)
                .orElseThrow(()->new NotFoundException("Card %s not found"
                        .formatted(dto.fromCardNumber))));
        String message= """
                <h3>Hurmatli mijoz</h3>
                <h3>Sizning click hisob raqamingiz orqali, tranzaksiya amalga oshirilmoqda.</h3>
                Bu tranzaksiyani tasdiqlash kodi
                <h1>%s</h1>
                <h3>Agar siz ushbu tranzaksiyani amalga oshirmayotgan bo'lsangiz,</h3>
                <h3>iltimos ushbu xabarni o'chirib yuboring.</h3>
                <h3>Nima bo'lgan taqdirda ham ushbu kodni hech kimga aytmang</h3>
                """.formatted(transaction.getConfirmCode());
        javaMailSenderService.send(transaction.getFromUser().getEmail(),message);
        return transactionRepository.save(transaction).getId();
    }

    @Override
    @Transactional
    public Short create2(UUID id, HttpServletRequest request, Integer confirmCode){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        String authorization = request.getHeader("Authorization");
        String email = getEmail(authorization);
        authUserRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        RestTemplate restTemplate = new RestTemplate();
        if (transaction.getToUser().getId() != null) {
            transactionRepository.transactionCreate2WithToUser(id,confirmCode);
        }else if (transaction.getToCard().getNumber() != null) {
            transactionRepository.transactionCreate2WithToCard(id,confirmCode);
        } else if (transaction.getService().getId() != null) {
            transactionRepository.transactionCreate2WithToService(id,confirmCode);
            Transaction tr = transactionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Error"));
            restTemplate.put(tr.getService().getRestApi(),tr.getSumma());
            /*
             * Here we need to warn created transaction service with its restApi
             * */
        } else if(transaction.getBankAccount()!=null) {
            transactionRepository.transactionCreate2WithToBankAccount(id,confirmCode);
            String bankAccount = transactionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Error"))
                    .getBankAccount();
//            restTemplate.put("",bankAccount);
            /*
             * Here we need to check bank number with General bank system !!!
             * If bank account confirm, we need to warn that bank ! ! !
             * */
        }
        /*
         * If this transaction face to any exception, that transaction save failure.
         * Else this transaction save successfully!
         * */
        return 1;
    }
    @Override
    public Page<TransactionDto> transactions(Pageable pageable) {
        Page<Transaction> all = transactionRepository.findAll(pageable);
        Page<TransactionDto> dtoList = TRANSACTION_MAPPER.toDto(all);
        return correcting(all, dtoList);
    }

    @Override
    public Page<TransactionDto> myTransactions(Long id, Pageable pageable) {
        Page<Transaction> myTransactions = transactionRepository.findAllByUserId(id, pageable);
        Page<TransactionDto> dtoMyTransactions = TRANSACTION_MAPPER.toDto(myTransactions);
        return correcting(myTransactions, dtoMyTransactions);
    }
    public static Page<TransactionDto> correcting(Page<Transaction> all, Page<TransactionDto> dtoList){
        if (!dtoList.isEmpty()) {
            List<TransactionDto> dtoTransactions = dtoList.getContent();
            List<Transaction> transactions = all.getContent();
            for (int i = 0; i < dtoTransactions.size(); i++) {
                dtoTransactions.get(i).setServiceId(transactions.get(i).getService().getId());
                dtoTransactions.get(i).setFromCardNumber(transactions.get(i).getFromCard().getNumber());
                dtoTransactions.get(i).setToCardNumber(transactions.get(i).getToCard().getNumber());
                dtoTransactions.get(i).setToUserId(transactions.get(i).getToUser().getId());
            }
            return new PageImpl<>(dtoTransactions,dtoList.getPageable(),dtoTransactions.size());
        }
        return Page.empty();
    }
}
