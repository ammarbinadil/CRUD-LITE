package com.banking.service;

import com.banking.entity.Person;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }

    public Person create(Person person) {
        if (personRepository.existsByEmail(person.getEmail())) {
            throw new IllegalArgumentException("A person with email '" + person.getEmail() + "' already exists");
        }
        return personRepository.save(person);
    }

    public Person update(Long id, Person updated) {
        Person existing = findById(id);
        // Check email uniqueness only if changed
        if (!existing.getEmail().equals(updated.getEmail()) && personRepository.existsByEmail(updated.getEmail())) {
            throw new IllegalArgumentException("A person with email '" + updated.getEmail() + "' already exists");
        }
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        return personRepository.save(existing);
    }

    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }
}
