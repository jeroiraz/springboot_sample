package com.example.demo.repository;

import io.codenotary.immudb4j.exceptions.VerificationException;

public interface ImmutableRepository<K,V> {

    long save(K key, V value);
    long verifiedSave(K key, V value) throws VerificationException;
    V find(K key);
    V findAt(K key, long txId);
    long delete(K key);

}
