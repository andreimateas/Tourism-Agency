package domain;

import java.time.LocalTime;

public class Excursion extends Entity<Long>{

    private String touristAttraction;
    private String transportationCompany;
    private LocalTime departureTime;
    private Float price;
    private Integer availableSeats;

    public Excursion(String touristAttraction, String transportationCompany, LocalTime departureTime, Float price, Integer availableSeats) {
        this.touristAttraction = touristAttraction;
        this.transportationCompany = transportationCompany;
        this.departureTime = departureTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public String getTouristAttraction() {
        return touristAttraction;
    }

    public void setTouristAttraction(String touristAttraction) {
        this.touristAttraction = touristAttraction;
    }

    public String getTransportationCompany() {
        return transportationCompany;
    }

    public void setTransportationCompany(String transportationCompany) {
        this.transportationCompany = transportationCompany;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "touristAttraction='" + touristAttraction + '\'' +
                ", transportationCompany='" + transportationCompany + '\'' +
                ", departureTime=" + departureTime +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
