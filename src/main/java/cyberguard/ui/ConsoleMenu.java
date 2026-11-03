package cyberguard.ui;

import cyberguard.domain.*;
import cyberguard.service.*;
import cyberguard.persistence.IncidentStorage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);
    private IncidentManager manager = new IncidentManager();
    private IncidentStorage storage = new IncidentStorage(Paths.get("data", "incidents.txt"));
    private boolean unsavedChanges = false;

    public void run() {
        System.out.println("CyberGuard - Cybersecurity Incident Manager");
        try {
            for (Incident incident : this.storage.load()) this.manager.addIncident(incident);
            System.out.println("Loaded " + this.manager.getIncidents().size() + " incidents.");
        } catch (IOException e) {
            System.out.println("Cannot load data: " + e.getMessage());
            System.out.println("Repair or back up the data file before restarting. Exiting to protect it.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            if (!this.scanner.hasNextLine()) {
                if (this.unsavedChanges && !save()) {
                    System.out.println("Input ended. Unsaved changes could not be written.");
                }
                return;
            }
            try {
                int choice = readNumber();
                switch (choice) {
                    case 1: addIncident(); break;
                    case 2: printIncidents(this.manager.getSortedIncidents()); break;
                    case 3: updateSeverity(); break;
                    case 4: updateStatus(); break;
                    case 5:
                        printIncidents(this.manager.findByStatus(readStatus()));
                        break;
                    case 6: save(); break;
                    case 0:
                        if (!this.unsavedChanges || save()) running = false;
                        break;
                    default: System.out.println("Choose a listed option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            } catch (IncidentException e) {
                System.out.println("Cannot complete action: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n1. Add incident");
        System.out.println("2. View incidents by severity");
        System.out.println("3. Change severity");
        System.out.println("4. Change status");
        System.out.println("5. Filter by status");
        System.out.println("6. Save records");
        System.out.println("0. Save and exit");
        System.out.print("Choice: ");
    }

    private String readLine() {
        if (!this.scanner.hasNextLine()) throw new IncidentException("Input ended. Action cancelled.");
        return this.scanner.nextLine().trim();
    }

    private int readNumber() {
        return Integer.parseInt(readLine());
    }

    private Severity readSeverity() {
        System.out.print("Severity (1 LOW, 2 MEDIUM, 3 HIGH, 4 CRITICAL): ");
        int choice = readNumber();
        if (choice < 1 || choice > 4) throw new IncidentException("Severity must be 1 to 4.");
        return Severity.values()[choice - 1];
    }

    private Status readStatus() {
        System.out.print("Status (1 OPEN, 2 INVESTIGATING, 3 RESOLVED): ");
        int choice = readNumber();
        if (choice < 1 || choice > 3) throw new IncidentException("Status must be 1 to 3.");
        return Status.values()[choice - 1];
    }

    private void addIncident() {
        System.out.print("Type (1 Phishing, 2 Malware): ");
        int type = readNumber();
        IncidentFactory factory;
        if (type == 1) factory = new PhishingFactory();
        else if (type == 2) factory = new MalwareFactory();
        else throw new IncidentException("Type must be 1 or 2.");
        System.out.print("Title: ");
        String title = readLine();
        System.out.print("Description: ");
        String description = readLine();
        System.out.print("Affected asset (for example, Office PC): ");
        Asset asset = new Asset(readLine());
        Severity severity = readSeverity();
        Incident incident = factory.createIncident(this.manager.getNextId(), title, description, asset, severity);
        this.manager.addIncident(incident);
        this.unsavedChanges = true;
        System.out.println("Created incident #" + incident.getId());
    }

    private void updateSeverity() {
        System.out.print("Incident ID: ");
        Incident incident = this.manager.findIncident(readNumber());
        incident.setSeverity(readSeverity());
        this.unsavedChanges = true;
        System.out.println("Severity updated.");
    }

    private void updateStatus() {
        System.out.print("Incident ID: ");
        Incident incident = this.manager.findIncident(readNumber());
        incident.changeStatus(readStatus());
        this.unsavedChanges = true;
        System.out.println("Status updated.");
    }

    private void printIncidents(ArrayList<Incident> incidents) {
        if (incidents.isEmpty()) System.out.println("No incidents found.");
        for (Incident incident : incidents) {
            System.out.println("\n#" + incident.getId() + " " + incident.getTitle());
            System.out.println("Type: " + incident.getType() + " | Severity: " + incident.getSeverity() +
                    " | Status: " + incident.getStatus());
            System.out.println("Description: " + incident.getDescription());
            System.out.println("Asset: " + incident.getAsset().getName());
            // The concrete incident type decides which advice is returned.
            System.out.println("Response advice: " + incident.getResponseAdvice());
        }
    }

    private boolean save() {
        try {
            this.storage.save(this.manager.getIncidents());
            this.unsavedChanges = false;
            System.out.println("Records saved to data/incidents.txt.");
            return true;
        } catch (IOException e) {
            System.out.println("Cannot save data: " + e.getMessage());
            System.out.println("Your changes are still in memory. Fix the problem and try again.");
            return false;
        }
    }
}
