package learn.wreck.data;

import learn.wreck.models.Host;
import learn.wreck.models.Guest;
import learn.wreck.models.Reservation;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER="id,start_date,end_date,guest_id,total";

    private final String directory;

    public ReservationFileRepository(String directory){this.directory=directory;}

    @Override
    public List<Reservation> findByDate(LocalDate date) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(date)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields, date));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public Reservation add(Reservation reservation) throws DataException {

        if (item == null) {
            return null;
        }

        List<Reservation> all = findAll();

        int nextId = all.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        item.setId(nextId);

        all.add(item);
        writeAll(all);

        return item;
    }

    @Override
    public boolean edit(Reservation reservation) throws DataException {
        List<Reservation> all = findByDate(reservation .getDate());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(reservation  .getId())) {
                all.set(i, reservation );
                writeAll(all, reservation  .getDate());
                return true;
            }
        }
        return false;
    }

    private String getFilePath(LocalDate date) {
        return Paths.get(directory, date + ".csv").toString();
    }

    private void writeAll(List<Reservation> forages, LocalDate date) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(date))) {

            writer.println(HEADER);

            for (Reservation item : forages) {
                writer.println(serialize(item));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation item) {
        return String.format("%s,%s,%s,%s",
                item.getId(),
                item.getForager().getId(),
                item.getItem().getId(),
                item.getKilograms());
    }

    private Reservation deserialize(String[] fields, LocalDate date) {
        Reservation result = new Reservation();
        result.setId(fields[0]);
        result.setDate(date);
        result.setKilograms(Double.parseDouble(fields[3]));

        Forager forager = new Forager();
        forager.setId(fields[1]);
        result.setForager(forager);

        Reservation item = new Reservation();
        item.setId(Integer.parseInt(fields[2]));
        result.setItem(item);
        return result;
    }
}
