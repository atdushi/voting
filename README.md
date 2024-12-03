# Voting

## Technical requirement:
Design and implement a REST API using Hibernate/Spring/SpringMVC (or SpringBoot) without frontend.
The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl
commands to get data for voting and vote.

P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

---

- Stack: JDK 21, Spring Boot 3.x, Lombok, [H2](http://localhost:8080/h2-console/), Caffeine Cache
- Run: `mvn spring-boot:run` in root directory.

---

## REST API

[REST API documentation](http://localhost:8080/)

Credentials:

```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Guest: guest@gmail.com / guest
```

---

## Expected UI

<details>
  <summary>Admin Users</summary>

  ![](/images/Untitled2.png)

</details>

<details>
  <summary>Admin Restaurants and Dishes</summary>

  ![](/images/Untitled3.png)

</details>

<details>
  <summary>Voting</summary>

  ![](/images/Untitled4.png)

</details>

