package com.example.demo.repository;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.util.SerializationUtils;

import io.codenotary.immudb4j.*;
import io.codenotary.immudb4j.exceptions.VerificationException;

@Repository
public class ImmudbRepository<K,V> implements ImmutableRepository<K,V> {

    ImmuClient immudb = null;

    @PostConstruct
    void initialize() {
        try {
            FileImmuStateHolder stateHolder = FileImmuStateHolder.newBuilder()
                .withStatesFolder("./immudb_states")
                .build();

            immudb = ImmuClient.newBuilder()
                    .withServerUrl("localhost")
                    .withServerPort(3322)
                    .withStateHolder(stateHolder)
                    .build();

            immudb.login("immudb", "immudb");

            immudb.useDatabase("defaultdb");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long save(K key, V value) {
        try {
            TxMetadata hdr = immudb.set(serialize(key), serialize(value));
            return hdr.id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long verifiedSave(K key, V value) throws VerificationException {
        TxMetadata hdr = immudb.verifiedSet(serialize(key), serialize(value));
        return hdr.id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V find(K key) {
        try {
            return (V)deserialize(immudb.get(serialize(key)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V findAt(K key, long txId) {
        try {
            return (V)deserialize(immudb.getAt(serialize(key), txId).getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long delete(K key) {
        throw new RuntimeException("not supported");
    }

    private byte[] serialize(Object object) {
        return SerializationUtils.serialize((Serializable) object);
    }

    private Object deserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
       
}
