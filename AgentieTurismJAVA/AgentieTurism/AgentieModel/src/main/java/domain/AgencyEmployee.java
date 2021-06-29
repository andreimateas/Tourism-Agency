package domain;

public class AgencyEmployee extends Entity<Long>{

    private String username;
    private String password;
    private String agencyName;

    public AgencyEmployee(String username, String password,String agencyName) {
        this.username=username;
        this.password = password;
        this.agencyName = agencyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "AgencyEmployee{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", agencyName='" + agencyName + '\'' +
                '}';
    }
}
