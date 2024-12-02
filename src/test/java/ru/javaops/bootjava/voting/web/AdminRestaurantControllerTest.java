package ru.javaops.bootjava.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.bootjava.AbstractControllerTest;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;

import static ru.javaops.bootjava.voting.web.AdminRestaurantController.REST_URL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;
}
