package usecase;

import domain.entity.SortOption;
import domain.entity.Transaction;
import domain.entity.TransactionType;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.ITransactionRepository;
import java.util.List;

/**
 * Use case yang menangani logika bisnis pencatatan keuangan.
 * Tidak melakukan I/O — hanya memproses data dan melempar checked exception domain.
 */
public class FinanceUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final ITransactionRepository transactionRepository;

    public FinanceUseCase(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /** Mengambil semua transaksi. */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /** Menambahkan pemasukan baru setelah validasi. */
    public Transaction addIncome(String description, long amount) throws ValidationException {
        return addTransaction(description, amount, TransactionType.INCOME);
    }

    /** Menambahkan pengeluaran baru setelah validasi. */
    public Transaction addExpense(String description, long amount) throws ValidationException {
        return addTransaction(description, amount, TransactionType.EXPENSE);
    }

    private Transaction addTransaction(String description, long amount, TransactionType type)
            throws ValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Keterangan tidak boleh kosong");
        }
        if (amount <= 0) {
            throw new ValidationException("Jumlah tidak valid");
        }
        return transactionRepository.save(description.trim(), amount, type);
    }

    /** Menghitung saldo: total pemasukan dikurangi total pengeluaran. */
    public long getBalance() {
        return transactionRepository.findAll().stream()
                .mapToLong(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : -t.getAmount())
                .sum();
    }

    /** Menghapus transaksi berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeTransaction(int id) throws EntityNotFoundException {
        boolean deleted = transactionRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException("Transaksi", id);
        }
    }

    /** Mencari transaksi yang keterangannya mengandung kata kunci (case-insensitive). */
    public List<Transaction> searchTransactions(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return transactionRepository.findBy(t -> t.getDescription().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan transaksi sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Transaction> sortTransactions(SortOption option) {
        return transactionRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }
}
