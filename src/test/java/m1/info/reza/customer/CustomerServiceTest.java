package m1.info.reza.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findOrCreate_shouldReturnExistingCustomer() {
        String email = "existing@example.com";
        Customer existing = new Customer(email);

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(existing));

        Customer result = customerService.findOrCreate(email);

        assertEquals(existing, result);
        verify(customerRepository, never()).save(any()); // Ne pas sauvegarder si déjà existant
    }

    @Test
    void findOrCreate_shouldCreateAndReturnNewCustomer() {
        String email = "new@example.com";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.findOrCreate(email);

        assertEquals(email, result.getEmail());
        verify(customerRepository).save(any(Customer.class)); // On vérifie que save a bien été appelé
    }
}
