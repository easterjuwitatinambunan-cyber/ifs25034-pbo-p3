package domain.entity;

import java.util.Comparator;

/**
 * Kriteria pengurutan transaksi.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan terpusat di domain.
 */
public enum SortOption {
    /** Jumlah terbesar lebih dulu. */
    AMOUNT_DESC(Comparator.comparingLong(Transaction::getAmount).reversed()),

    /** Jumlah terkecil lebih dulu. */
    AMOUNT_ASC(Comparator.comparingLong(Transaction::getAmount)),

    /** Pemasukan ditampilkan lebih dulu. */
    INCOME_FIRST(Comparator.comparing((Transaction t) -> t.getType() != TransactionType.INCOME)),

    /** Pengeluaran ditampilkan lebih dulu. */
    EXPENSE_FIRST(Comparator.comparing((Transaction t) -> t.getType() != TransactionType.EXPENSE));

    private final Comparator<Transaction> comparator;

    SortOption(Comparator<Transaction> comparator) {
        this.comparator = comparator;
    }

    /** Mengembalikan comparator yang sesuai dengan opsi ini. */
    public Comparator<Transaction> comparator() {
        return comparator;
    }
}
