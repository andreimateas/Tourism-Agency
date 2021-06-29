package repository;


import domain.AgencyEmployee;

public interface AgencyEmployeeRepository extends Repository<Long, AgencyEmployee>{

    AgencyEmployee findByUsername(String username);

}
