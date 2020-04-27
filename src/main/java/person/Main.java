package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

public class Main {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Faker faker = new Faker();

    public static Person randomPerson() {
        Person person = new Person();

        String name = faker.name().fullName();
        person.setName(name);

        java.util.Date date = faker.date().birthday(1,99);
        java.time.LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        Person.Gender gender = faker.options().option(Person.Gender.values());
        person.setGender(gender);

        Address address = new Address(
                faker.address().country(),
                faker.address().state(),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().zipCode()
        );
        person.setAddress(address);

        String email = faker.internet().emailAddress();
        person.setEmail(email);

        String profession = faker.company().profession();
        person.setProfession(profession);

        return person;
    }

    private static void createRandomPerson() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < 1000; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        createRandomPerson();
    }
}