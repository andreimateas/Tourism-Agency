package repository;


import domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {

    Iterable<E> findAll();

    E save(E entity);

    E delete(ID id);

}