package domain;

public class Reservation extends Entity<Long>{

    private String agency;
    private Excursion excursion;
    private String clientName;
    private String clientPhone;
    private Integer numberTickets;

    public Reservation(String agency, Excursion excursion, String clientName, String clientPhone, Integer numberTickets) {
        this.agency = agency;
        this.excursion = excursion;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.numberTickets = numberTickets;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Excursion getExcursion() {
        return excursion;
    }

    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public Integer getNumberTickets() {
        return numberTickets;
    }

    public void setNumberTickets(Integer numberTickets) {
        this.numberTickets = numberTickets;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "agency='" + agency + '\'' +
                ", excursion=" + excursion +
                ", clientName='" + clientName + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", numberTickets=" + numberTickets +
                '}';
    }
}
