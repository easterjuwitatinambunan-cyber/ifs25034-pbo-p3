import adapter.presenter.FinancePresenter;
import adapter.repository.TransactionRepository;
import domain.repository.ITransactionRepository;
import framework.view.FinanceView;
import usecase.FinanceUseCase;

/**
 * Titik masuk aplikasi (Composition Root).
 * Semua dependency antar layer disusun di sini — satu-satunya tempat
 * yang mengetahui implementasi konkret dari setiap interface.
 */
public class App {
    public static void main(String[] args) {
        // Layer adapter: implementasi konkret repository (penyimpanan in-memory)
        ITransactionRepository transactionRepository = new TransactionRepository();

        // Layer usecase: logika bisnis, hanya bergantung pada interface repository
        FinanceUseCase financeUseCase = new FinanceUseCase(transactionRepository);

        // Layer adapter: presenter untuk memformat output ke layar
        FinancePresenter financePresenter = new FinancePresenter();

        // Layer framework: UI konsol yang menerima input user
        FinanceView financeView = new FinanceView(financeUseCase, financePresenter);

        // Menjalankan loop menu utama aplikasi
        financeView.show();
    }
}
