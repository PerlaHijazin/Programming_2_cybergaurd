package cyberguard.ui;

import cyberguard.domain.*;
import cyberguard.persistence.IncidentStorage;
import cyberguard.service.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final IncidentManager manager = new IncidentManager();
    private final IncidentStorage storage = new IncidentStorage(Path.of("data", "incidents.json"));

    public void run() {
        System.out.println("CyberGuard - Cybersecurity Incident Manager");
        if (!load()) return;

        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) break;
            try {
                switch (readNumber()) {
                    case 1 -> addIncident();
                    case 2 -> print(manager.getSortedIncidents());
                    case 3 -> { findById().setSeverity(readSeverity()); System.out.println("Severity updated."); }
                    case 4 -> { findById().changeStatus(readStatus()); System.out.println("Status updated."); }
                    case 5 -> print(manager.findByStatus(readStatus()));
                    case 6 -> save();
                    case 0 -> { if (save()) { System.out.println("Goodbye!"); return; } }
                    default -> System.out.println("Choose a listed option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            } catch (IncidentException e) {
                System.out.println("Cannot complete action: " + e.getMessage());
            }
        }
        save(); // input ended (e.g. Ctrl+D): keep the user's changes
    }

    private void printMenu() {
        System.out.println("\n1. Add incident\n2. View incidents by severity\n3. Change severity");
        System.out.println("4. Change status\n5. Filter by status\n6. Save\n0. Save and exit");
        System.out.print("Choice: ");
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) throw new IncidentException("Input ended.");
        return scanner.nextLine().trim();
    }

    private int readNumber() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private int askChoice(String prompt, int max) {
        int choice = Integer.parseInt(ask(prompt));
        if (choice < 1 || choice > max) throw new IncidentException("Enter a number from 1 to " + max + ".");
        return choice;
    }

    private Severity readSeverity() {
        return Severity.values()[askChoice("Severity (1 LOW, 2 MEDIUM, 3 HIGH, 4 CRITICAL): ", 4) - 1];
    }

    private Status readStatus() {
        return Status.values()[askChoice("Status (1 OPEN, 2 INVESTIGATING, 3 RESOLVED): ", 3) - 1];
    }

    private Incident findById() {
        return manager.findIncident(Integer.parseInt(ask("Incident ID: ")));
    }

    private void addIncident() {
        String type = askChoice("Type (1 Phishing, 2 Malware): ", 2) == 1 ? "PHISHING" : "MALWARE";
        IncidentFactory factory = IncidentFactory.forType(type);
        String title = ask("Title: ");
        String description = ask("Description: ");
        Asset asset = new Asset(ask("Affected asset (for example, Office PC): "));
        Incident incident = factory.createIncident(manager.getNextId(), title, description,
                asset, readSeverity(), Status.OPEN);
        manager.addIncident(incident);
        System.out.println("Created incident #" + incident.getId());
    }

    private void print(List<Incident> incidents) {
        if (incidents.isEmpty()) System.out.println("No incidents found.");
        for (Incident incident : incidents) System.out.println(incident); // toString uses overridden methods
    }

    private boolean load() {
        try {
            for (Incident incident : storage.load()) manager.addIncident(incident);
            System.out.println("Loaded " + manager.getIncidents().size() + " incidents.");
            return true;
        } catch (IOException | IncidentException e) {
            System.out.println("Cannot load data: " + e.getMessage());
            System.out.println("Fix or remove data/incidents.json and restart. Exiting to protect it.");
            return false;
        }
    }

    private boolean save() {
        try {
            storage.save(manager.getIncidents());
            System.out.println("Saved to data/incidents.json.");
            return true;
        } catch (IOException e) {
            System.out.println("Cannot save data: " + e.getMessage());
            return false;
        }
    }
}
