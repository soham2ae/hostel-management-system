package com.hostel.service.impl;

import com.hostel.entity.Student;
import com.hostel.entity.Wallet;
import com.hostel.entity.WalletTransaction;
import com.hostel.enums.WalletTransactionType;
import com.hostel.exception.InsufficientWalletBalanceException;
import com.hostel.exception.InvalidAmountException;
import com.hostel.exception.ResourceNotFoundException;
import com.hostel.repository.StudentRepository;
import com.hostel.repository.WalletRepository;
import com.hostel.repository.WalletTransactionRepository;
import com.hostel.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Default implementation of {@link WalletService}.
 *
 * <p>Contains all business logic for creating wallets and recording
 * top-up and debit operations against the
 * {@link WalletTransaction} ledger. Repository details are not exposed
 * beyond this class.</p>
 *
 * <p>This implementation deliberately has no dependency on
 * {@code PaymentRepository} or {@code PaymentService} — wallet
 * bookkeeping is entirely self-contained within {@link Wallet} and
 * {@link WalletTransaction}.</p>
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code WalletServiceImpl}.
     *
     * @param walletRepository            the wallet repository
     * @param walletTransactionRepository the wallet transaction
     *                                    repository, used to record
     *                                    the ledger
     * @param studentRepository           the student repository, used
     *                                    to resolve the owning student
     *                                    when creating a wallet
     */
    public WalletServiceImpl(WalletRepository walletRepository,
                              WalletTransactionRepository walletTransactionRepository,
                              StudentRepository studentRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Wallet createWallet(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Wallet wallet = Wallet.builder()
                .student(student)
                .currentBalance(BigDecimal.ZERO)
                .build();

        return walletRepository.save(wallet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "id", walletId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Wallet findWalletByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return walletRepository.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "student id", studentId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Wallet topUp(Long studentId, BigDecimal actualAmountPaid, BigDecimal creditAmount) {
        requirePositive(actualAmountPaid, "actualAmountPaid");
        requirePositive(creditAmount, "creditAmount");

        Wallet wallet = findWalletByStudent(studentId);
        wallet.setCurrentBalance(wallet.getCurrentBalance().add(creditAmount));
        Wallet savedWallet = walletRepository.save(wallet);

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(savedWallet)
                .transactionType(WalletTransactionType.TOPUP)
                .actualAmountPaid(actualAmountPaid)
                .creditAmount(creditAmount)
                .transactionAmount(null)
                .transactionDate(LocalDateTime.now())
                .build();
        walletTransactionRepository.save(transaction);

        return savedWallet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Wallet debit(Long studentId, BigDecimal transactionAmount) {
        requirePositive(transactionAmount, "transactionAmount");

        Wallet wallet = findWalletByStudent(studentId);

        if (wallet.getCurrentBalance().compareTo(transactionAmount) < 0) {
            throw new InsufficientWalletBalanceException(wallet.getCurrentBalance(), transactionAmount);
        }

        wallet.setCurrentBalance(wallet.getCurrentBalance().subtract(transactionAmount));
        Wallet savedWallet = walletRepository.save(wallet);

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(savedWallet)
                .transactionType(WalletTransactionType.DEBIT)
                .actualAmountPaid(null)
                .creditAmount(null)
                .transactionAmount(transactionAmount)
                .transactionDate(LocalDateTime.now())
                .build();
        walletTransactionRepository.save(transaction);

        return savedWallet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<WalletTransaction> findWalletTransactions(Long studentId) {
        Wallet wallet = findWalletByStudent(studentId);
        return walletTransactionRepository.findByWallet(wallet);
    }

    /**
     * Validates that a monetary amount is strictly positive, rejecting
     * zero, negative, and {@code null} values.
     *
     * @param amount    the amount to validate
     * @param fieldName the name of the field being validated, used in
     *                  the error message
     */
    private void requirePositive(BigDecimal amount, String fieldName) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(fieldName, amount);
        }
    }
}
