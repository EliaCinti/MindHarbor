package it.uniroma2.mindharbor.utilities;

import com.opencsv.exceptions.CsvException;
import it.uniroma2.mindharbor.exception.DAOException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Utility class for reading and writing CSV files.
 */
public class CsvUtilities {
    private CsvUtilities() {
        /* no instance */
    }

    public static final String ERR_ACCESS = "Errore di I/O accedendo al file: %s";
    public static final String ERR_PARSER = "Errore durante il parsing del file CSV: %s";
    public static final String ERR_MOVE_FILE = "Errore durante il trasferimento del file da %s a %s";

    /**
     * Reads all rows from a specified CSV file and returns them as a list of string arrays.
     * <p>
     * This method reads the content of a CSV file using a {@link CSVReader} and returns
     * all rows as a list of string arrays. Each array represents a row, where each element
     * corresponds to a column in the CSV file.
     * </p>
     *
     * @param fd The CSV file to read.
     * @return A list of string arrays, where each array represents a row in the CSV file.
     * @throws DAOException If an error occurs while accessing or parsing the CSV file.
     */
    public static List<String[]> readAll(File fd) throws DAOException {
        try (CSVReader reader = new CSVReader(new FileReader(fd))) {
            return reader.readAll();
        } catch (IOException e) {
            throw new DAOException(String.format(ERR_ACCESS, fd), e);
        } catch (CsvException e) {
            throw new DAOException(String.format(ERR_PARSER, fd), e);
        }
    }

    /**
     * Updates a CSV file by writing a new table of data, including a specified header.
     * <p>
     * This method creates a temporary file where it writes the provided data along with the header.
     * Once the writing process is completed successfully, the temporary file replaces the original file.
     * The method is synchronized to ensure thread safety.
     * </p>
     *
     * @param fd     The CSV file to update.
     * @param header An array representing the header row to be added at the beginning of the file.
     * @param table  A list of string arrays, where each array represents a row in the updated CSV file.
     * @throws DAOException If an error occurs while writing or replacing the CSV file.
     */
    public static synchronized void updateFile(File fd, String[] header, List<String[]> table) throws DAOException {
        File fdTmp = new File(fd.getAbsolutePath() + ".tmp");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fdTmp))) {
            table.addFirst(header);  // Adding header as the first row
            writer.writeAll(table);
        } catch (IOException e) {
            throw new DAOException(String.format(ERR_ACCESS, fdTmp), e);
        }
        try {
            Files.move(fdTmp.toPath(), fd.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DAOException(String.format(ERR_MOVE_FILE, fdTmp, fd), e);
        }
    }

    public static synchronized void writeFile(File fd, String[] tableRecord) throws DAOException{
        try (CSVWriter writer = new CSVWriter(new FileWriter(fd, true))) {
            writer.writeNext(tableRecord);
        } catch (IOException e) {
            throw new DAOException(e.getMessage());
        }
    }
}
