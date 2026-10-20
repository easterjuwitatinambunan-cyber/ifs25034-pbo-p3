package usecase;

import domain.entity.Contact;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IContactRepository;
import java.util.List;

/**
 * Use case yang menangani logika bisnis kontak teman.
 * Tidak melakukan I/O, hanya memproses data dan melempar checked exception domain.
 */
public class ContactUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IContactRepository contactRepository;

    public ContactUseCase(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /** Mengambil semua kontak. */
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    /** Menambahkan kontak baru setelah validasi. */
    public Contact addContact(String name, String phone, String email) throws ValidationException {
        validateName(name);
        validatePhone(phone);
        validateEmail(email);
        return contactRepository.save(name.trim(), phone.trim(), email.trim());
    }

    /**
     * Mengubah nama, telepon, dan/atau email kontak.
     * Parameter {@code null} berarti field tersebut tidak diubah.
     */
    public void updateContact(int id, String name, String phone, String email)
            throws EntityNotFoundException, ValidationException {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kontak", id));

        // Validasi semua field lebih dulu agar perubahan bersifat atomik.
        if (name != null) {
            validateName(name);
        }
        if (phone != null) {
            validatePhone(phone);
        }
        if (email != null) {
            validateEmail(email);
        }

        if (name != null) {
            contact.changeName(name.trim());
        }
        if (phone != null) {
            contact.changePhone(phone.trim());
        }
        if (email != null) {
            contact.changeEmail(email.trim());
        }
        contactRepository.update(contact);
    }

    /** Menghapus kontak berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeContact(int id) throws EntityNotFoundException {
        if (!contactRepository.deleteById(id)) {
            throw new EntityNotFoundException("Kontak", id);
        }
    }

    /** Mencari kontak yang namanya mengandung kata kunci (case-insensitive). */
    public List<Contact> searchContacts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return contactRepository.findBy(c -> c.getName().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan kontak sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Contact> sortContacts(SortOption option) {
        return contactRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }

    private void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Nama kontak tidak boleh kosong!");
        }
    }

    private void validatePhone(String phone) throws ValidationException {
        if (phone == null) {
            throw new ValidationException("Nomor telepon tidak valid!");
        }
    }

    private void validateEmail(String email) throws ValidationException {
        if (email == null) {
            throw new ValidationException("Email tidak valid!");
        }
    }
}