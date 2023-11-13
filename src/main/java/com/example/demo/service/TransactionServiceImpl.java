package com.example.demo.service;

import com.example.demo.entities.AuthUser;
import com.example.demo.entities.Card;
import com.example.demo.entities.Transaction;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.transaction.TransactionDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.demo.mappers.TransactionMapper.TRANSACTION_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ServiceRepository serviceRepository;
    private final CardRepository cardRepository;
    private final AuthUserRepository authUserRepository;
    private final TransactionRepository transactionRepository;
    private final JavaMailSenderService javaMailSenderService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public String getFullName(String number){
        return cardRepository.findByNumberAndActiveTrue(number)
                .orElseThrow(() -> new NotFoundException("Card not found")).getOwner();
    }

    @Override
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
    public Short create2(UUID id, HttpServletRequest request, Integer confirmCode){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        String authorization = request.getHeader("Authorization");
        String email = jwtTokenUtils.getEmail(authorization);
        AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!transaction.getConfirmCode().equals(confirmCode)) {
            throw new BadRequestException("Confirmation code is not correct");
        }
        if (transaction.getExpireTimeConfirmation().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Time out");
        }
        if (!transaction.getFromUser().getId().equals(authUser.getId())) {
            throw new ForbiddenAccessException("User was not confirmed");
        }
        if (transaction.getFromCard().getExpireDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Card %s expired".formatted(transaction.getFromCard()));
        }
        Card fromCard = transaction.getFromCard();
        Double value = transaction.getSumma() + (transaction.getSumma() / 100) * transaction.getCommission();
        if (fromCard.getBalance()<value) {
            throw new BadRequestException("Balance is not enough");
        }
        fromCard.setBalance(fromCard.getBalance()-value);
        if (transaction.getToUser().getId() != null) {
            transaction.setToUser(authUserRepository.findByIdAndActiveTrue(transaction.getToUser().getId())
                    .orElseThrow(() -> new NotFoundException("User %s not found"
                            .formatted(transaction.getToUser().getId()))));
            Card main = transaction.getToUser().getMainCard();
            if (main.getActive().equals(false)) {
                throw new BadRequestException("Card %s is not active".formatted(main));
            }
            main.setBalance(main.getBalance()+value);
            transaction.setToCard(main);
        } else if (transaction.getToCard().getNumber() != null) {
            transaction.setToCard(cardRepository.findByNumberAndActiveTrue(transaction.getToCard().getNumber())
                    .orElseThrow(() -> new NotFoundException("Card %s not found"
                            .formatted(transaction.getToCard().getNumber()))));
            if (transaction.getToCard().getExpireDate().isAfter(LocalDate.now())) {
                throw new BadRequestException("Card %s expired".formatted(transaction.getFromCard()));
            }
            Card toCard = transaction.getToCard();
            if (toCard.getActive().equals(false)) {
                throw new BadRequestException("Card %s is not active".formatted(toCard));
            }
            toCard.setBalance(toCard.getBalance()+value);
        } else if (transaction.getService().getId() != null) {
            transaction.setService(serviceRepository.findByIdAndEnableTrue(transaction.getService().getId())
                    .orElseThrow(() -> new NotFoundException("Service %s not found"
                            .formatted(transaction.getService().getId()))));
            com.example.demo.entities.Service service = transaction.getService();
            String restApi = service.getRestApi();
            RestTemplate restTemplate = new RestTemplate();
            /*
             * Here we need to warn created transaction service with its restApi
             * */
        } else if(transaction.getBankAccount()!=null) {
            RestTemplate restTemplate = new RestTemplate();
            /*
             * Here we need to check bank number with General bank system !!!
             * If bank account confirm, we need to warn that bank ! ! !
             * */
            transaction.setBankAccount(transaction.getBankAccount());
        }
        /*
         * If this transaction face to any exception, that transaction save failure.
         * Else this transaction save successfully!
         * */
        transaction.setSuccess(true);
        transactionRepository.save(transaction);
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
