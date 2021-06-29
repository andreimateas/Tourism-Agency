package repository;



import domain.Excursion;

import java.time.LocalTime;

public interface ExcursionRepository extends Repository<Long, Excursion>{

    Iterable<Excursion> filterAttractionTime(String attraction, LocalTime start, LocalTime end);

    void reserveSeats(Excursion excursion,int noSeats);

}
