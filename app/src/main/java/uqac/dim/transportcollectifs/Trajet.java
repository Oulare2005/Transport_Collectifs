package uqac.dim.transportcollectifs;

public class Trajet {
    private String userId;
    private String tripId;
    private String departureDate;
    private int seats;
    private double price;

    // Constructeur vide requis par Firestore
    public Trajet() {}

    // Constructeur avec paramètres
    public Trajet(String userId, String tripId, String departureDate, int seats, double price) {
        this.userId = userId;
        this.tripId = tripId;
        this.departureDate = departureDate;
        this.seats = seats;
        this.price = price;
    }

    // Getters et setters
    public String getUserId() {
        return userId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public int getSeats() {
        return seats;
    }

    public double getPrice() {
        return price;
    }
}