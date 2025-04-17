package m1.info.reza.customer;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findOrCreate(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);

        if(customerOptional.isPresent()) {
            return customerOptional.get();
        }

        Customer customer = new Customer(email);
        customerRepository.save(customer);

        return customer;
    }

}
