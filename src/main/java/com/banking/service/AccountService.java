package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.Person;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Account> findByPersonId(Long personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }
        return accountRepository.findByPerson_Id(personId);
    }

    public Account create(Account account, Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personId));
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account number '" + account.getAccountNumber() + "' already exists");
        }
        account.setPerson(person);
        return accountRepository.save(account);
    }

    public Account update(Long id, Account updated, Long personId) {
        Account existing = findById(id);
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personId));

        if (!existing.getAccountNumber().equals(updated.getAccountNumber())
                && accountRepository.existsByAccountNumber(updated.getAccountNumber())) {
            throw new IllegalArgumentException("Account number '" + updated.getAccountNumber() + "' already exists");
        }
        existing.setAccountNumber(updated.getAccountNumber());
        existing.setAccountType(updated.getAccountType());
        existing.setBalance(updated.getBalance());
        existing.setPerson(person);
        return accountRepository.save(existing);
    }

    public void delete(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
