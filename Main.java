import java.util.LinkedList;

class ParkingSpace {
    int spaceId;
    boolean isOccupied;
    int cost;

    public ParkingSpace(int spaceId, int cost) {
        this.spaceId = spaceId;
        this.isOccupied = false;
        this.cost = cost;
    }

    public void occupy() {
        this.isOccupied = true;
    }

    public void vacate() {
        this.isOccupied = false;
    }
}

class EVParkingSpace extends ParkingSpace {
    boolean hasCharger;

    public EVParkingSpace(int spaceId, int cost, boolean hasCharger) {
        super(spaceId, cost);
        this.hasCharger = hasCharger;
    }
}

class VIPParkingSpace extends ParkingSpace {
    public VIPParkingSpace(int spaceId, int cost, int extraFee) {
        super(spaceId, cost + extraFee);
    }
}

class Node {
    ParkingSpace parkingSpace;
    Node next;

    public Node(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
        this.next = null;
    }
}

class LinkedListParking {
    Node head;

    public void addSpace(ParkingSpace parkingSpace) {
        Node newNode = new Node(parkingSpace);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public ParkingSpace findSpace() {
        Node current = head;
        while (current != null) {
            if (!current.parkingSpace.isOccupied) {
                return current.parkingSpace;
            }
            current = current.next;
        }
        return null;
    }

    public void displaySpaces() {
        Node current = head;
        while (current != null) {
            ParkingSpace space = current.parkingSpace;
            System.out.println("Space ID: " + space.spaceId + ", Occupied: " + space.isOccupied + ", Cost: " + space.cost + " INR");
            current = current.next;
        }
    }
}

class ParkingLot {
    LinkedListParking parkingSpaces = new LinkedListParking();
    LinkedList<String> waitingQueue = new LinkedList<>();

    public void addSpace(int spaceId, int cost, String spaceType, Object extra) {
        ParkingSpace space;
        if ("ev".equals(spaceType)) {
            space = new EVParkingSpace(spaceId, cost, (Boolean) extra);
        } else if ("vip".equals(spaceType)) {
            space = new VIPParkingSpace(spaceId, cost, (Integer) extra);
        } else {
            space = new ParkingSpace(spaceId, cost);
        }
        parkingSpaces.addSpace(space);
    }

    public int allocateSpace() {
        ParkingSpace space = parkingSpaces.findSpace();
        if (space != null) {
            space.occupy();
            return space.spaceId;
        }
        System.out.println("No available spaces. Adding user to waiting queue.");
        waitingQueue.add("User");
        return -1;
    }

    public void releaseSpace(int spaceId) {
        Node current = parkingSpaces.head;
        while (current != null) {
            if (current.parkingSpace.spaceId == spaceId && current.parkingSpace.isOccupied) {
                current.parkingSpace.vacate();
                System.out.println("Space " + spaceId + " is now free.");
                return;
            }
            current = current.next;
        }
        System.out.println("Space not found or already free.");
    }

    public void updateDynamicPricing(int spaceId, int newPrice) {
        Node current = parkingSpaces.head;
        while (current != null) {
            if (current.parkingSpace.spaceId == spaceId) {
                current.parkingSpace.cost = newPrice;
                System.out.println("Price updated for space " + spaceId + ".");
                return;
            }
            current = current.next;
        }
        System.out.println("Space not found.");
    }

    public void displayParkingSlots() {
        parkingSpaces.displaySpaces();
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot();
        lot.addSpace(1, 50, "regular", null);
        lot.addSpace(2, 40, "regular", null);
        lot.addSpace(3, 60, "regular", null);
        lot.addSpace(4, 30, "regular", null);
        lot.addSpace(5, 70, "regular", null);
        lot.addSpace(6, 55, "ev", true);
        lot.addSpace(7, 35, "vip", 20);
        lot.addSpace(8, 65, "regular", null);
        lot.addSpace(9, 45, "ev", false);
        lot.addSpace(10, 75, "vip", 30);

        System.out.println("Available parking slots:");
        lot.displayParkingSlots();

        System.out.println("Allocating a space: " + lot.allocateSpace());
        System.out.println("Allocating another space: " + lot.allocateSpace());

        lot.displayParkingSlots();

        lot.releaseSpace(1);
        lot.updateDynamicPricing(2, 45);
        lot.displayParkingSlots();
    }
}
