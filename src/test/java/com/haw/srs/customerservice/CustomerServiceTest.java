package com.haw.srs.customerservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = "testing")
class CustomerServiceTest {



    @Autowired
    MovieService movieService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository realCustomerRepository;

    @Test
    void getAllCustomersSuccess() {

        realCustomerRepository.deleteAll();

        Customer customer = new Customer("Jane", "Doe", Gender.FEMALE, "jane.doe@mail.com", null);
        realCustomerRepository.save(customer);

        List<Customer> actual = customerService.getCustomers();
        assertThat(actual).size().isEqualTo(1);
        assertThat(actual.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void transferReservationsSuccess() throws CustomerNotFoundException {
        Movie movie1 = new Movie("The goadfaher 1", 250);
        Movie movie2 = new Movie("The goadfaher 2", 230);
        movieService.saveMovie(movie1);
        movieService.saveMovie(movie2);




        realCustomerRepository.deleteAll();

        Customer from = new Customer("John", "Smith", Gender.MALE);
        from.addReservation(new Reservation(movie1, 1, 5));
        from.addReservation(new Reservation(movie2, 1,8));
        realCustomerRepository.save(from);
        Customer to = new Customer("Eva", "Miller", Gender.FEMALE);
        realCustomerRepository.save(to);

        assertThat(from.getReservations()).size().isEqualTo(2);
        assertThat(to.getReservations()).size().isEqualTo(0);

        customerService.transferReservations(from.getLastName(), to.getLastName());

        // versuche es hier einmal ohne Neuladen...warum klappt es nicht?

        //  1. MovieService movieService; 2. save movie fehlen


        from = realCustomerRepository.findByLastName(from.getLastName()).get();
        to   = realCustomerRepository.findByLastName(to.getLastName()).get();
        assertThat(from.getReservations()).size().isEqualTo(0);
        assertThat(to.getReservations()).size().isEqualTo(2);



        /*this.realCustomerRepository.deleteAll();
        Movie jamesbond = new Movie("James Bond 007", 120);
        Movie rosamundepilcher = new Movie("Rosamunde Pilcher", 120);
        this.movieService.save(jamesbond);
        this.movieService.save(rosamundepilcher);
        Customer from = new Customer("John", "Smith", Gender.MALE);
        from.addReservation(new Reservation(jamesbond, 1, 2));
        from.addReservation(new Reservation(rosamundepilcher, 3, 4));
        this.realCustomerRepository.save(from);
        Customer to = new Customer("Eva", "Miller", Gender.FEMALE);
        this.realCustomerRepository.save(to);
        Assertions.assertThat(from.getReservations()).size().isEqualTo(2);
        Assertions.assertThat(to.getReservations()).size().isEqualTo(0);
        this.customerService.transferReservations(from.getLastName(), to.getLastName());
        from = (Customer)this.realCustomerRepository.findByLastName(from.getLastName()).get();
        to = (Customer)this.realCustomerRepository.findByLastName(to.getLastName()).get();
        Assertions.assertThat(from.getReservations()).size().isEqualTo(0);
        Assertions.assertThat(to.getReservations()).size().isEqualTo(2);*/
    }
}