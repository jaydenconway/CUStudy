import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.net.http.HttpClient.newHttpClient;

class CUStudy {

    static final DateTimeFormatter HOUR_12H = DateTimeFormatter.ofPattern("ha", Locale.ENGLISH);
    static final LocalTime DAY_START = LocalTime.of(9, 0);
    static final LocalTime DAY_END = LocalTime.of(19, 0);

    static final String FREE = "·  ";
    static final String BOOKED = "█  ";

    static final String BANNER = """

             ▄▄▄▄▄▄▄ ▄▄▄  ▄▄▄      ▄▄▄▄▄▄▄ ▄▄▄▄▄▄▄▄▄ ▄▄▄  ▄▄▄ ▄▄▄▄▄▄   ▄▄▄   ▄▄▄\s
            ███▀▀▀▀▀ ███  ███     █████▀▀▀ ▀▀▀███▀▀▀ ███  ███ ███▀▀██▄ ███   ███\s
            ███      ███  ███      ▀████▄     ███    ███  ███ ███  ███ ▀███▄███▀\s
            ███      ███▄▄███        ▀████    ███    ███▄▄███ ███  ███   ▀███▀  \s
            ▀███████ ▀██████▀     ███████▀    ███    ▀██████▀ ██████▀     ███   \s
            """;

    public static void main(String[] args) throws IOException, InterruptedException {

        LocalDate today = LocalDate.of(2026, 8, 17);
        LocalDate tomorrow = today.plusDays(1);

        String body = "lid=2986&gid=0&eid=-1&seat=0&seatId=0&zone=0"
                + "&start=" + today
                + "&end=" + tomorrow
                + "&pageIndex=0&pageSize=18";

        HttpClient client = newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://carletonu.libcal.com/spaces/availability/grid"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "CUSTUDY/0.1")
                .header("Referer", "https://carletonu.libcal.com/spaces?lid=2986&gid=0&c=0")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        GridResponse grid = gson.fromJson(response.body(), GridResponse.class);

        HashMap<Integer, ArrayList<String>> map = new HashMap<>();

        for (Slot slot : grid.slots) {
            // className is set on slots that are already taken.
            // The value is "s-lc-eq-checkout" - it means unavailable.
            if (slot.className != null) {
                continue;
            }

            int itemId = slot.itemId;

            if (!map.containsKey(itemId)) {
                map.put(itemId, new ArrayList<>());
            }

            map.get(itemId).add(slot.start.substring(11, 16));
        }

        HashMap<Integer, Integer> capacities = new HashMap<>();
        HashMap<Integer, String> rooms = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of("rooms.txt"));

        for (String line : lines) {
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",");

            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();

            int cap = Integer.parseInt(parts[2].trim());
            capacities.put(id, cap);

            rooms.put(id, name);
        }

        if (grid.slots.isEmpty()) {
            System.out.println("No availability found - the library may be closed.");
            return;
        }

        ArrayList<Integer> ids = new ArrayList<>(map.keySet());
        ids.sort(Comparator.comparing(rooms::get));

        System.out.println(BANNER);
        System.out.println("  MacOdrum Library study rooms - " + today);
        System.out.println();
        System.out.println("        " + buildHeader());
        System.out.println();

        int count = 0;

        for (int id : ids) {
            String roomName = rooms.get(id);
            String label = roomName + " (" + capacities.get(id) + ")";

            ArrayList<String> timeSlots = map.get(id);

            System.out.printf("  %-10s %s%n", label, buildBar(timeSlots));

            count++;
            if (count % 5 == 0) {
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("  " + FREE.trim() + " free    " + BOOKED.trim() + " booked");
        System.out.println();
    }

    static String buildBar(ArrayList<String> timeSlots) {
        StringBuilder bar = new StringBuilder();
        LocalTime t = DAY_START;

        while (t.isBefore(DAY_END)) {
            if (timeSlots.contains(t.toString())) {
                bar.append(FREE);
            } else {
                bar.append(BOOKED);
            }
            t = t.plusMinutes(30);
        }

        return bar.toString();
    }

    static String buildHeader() {
        StringBuilder header = new StringBuilder();
        LocalTime t = DAY_START;

        while (t.isBefore(DAY_END)) {
            if (t.getMinute() == 0) {
                header.append(String.format("%-6s", t.format(HOUR_12H)));
            }
            t = t.plusMinutes(30);
        }

        return header.toString();
    }
}