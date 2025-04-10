package m1.info.reza.customer;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findOrCreate(String phone) {
        Optional<Customer> customerOptional = customerRepository.findByPhone(phone);

        if(customerOptional.isPresent()) {
            return customerOptional.get();
        }

        Customer customer = new Customer(phone);
        customerRepository.save(customer);

        return customer;
    }

}
