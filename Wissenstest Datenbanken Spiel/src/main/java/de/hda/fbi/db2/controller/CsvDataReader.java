package de.hda.fbi.db2.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by a.hofmann on 03.10.2016.
 */
public class CsvDataReader {

  private static final String SPLIT_CHARACTER = ";";
  private static final Logger log = Logger.getLogger(CsvDataReader.class.getName());

  private CsvDataReader() {
  }

  /**
   * Lists all available csv files in the resource folder.
   * <strong>WARNING:</strong> May not work on your machine
   *
   * @return list of csv filenames
   * @throws IOException        ioException
   * @throws URISyntaxException uriSyntaxException
   */
  public static List<String> getAvailableFiles() throws IOException, URISyntaxException {
    final Enumeration<URL> en = CsvDataReader.class.getClassLoader().getResources("");
    en.nextElement();
    File hackyFile = new File(en.nextElement().toURI());
    File[] filenames = hackyFile.listFiles();

    List<String> fileList;

    if (filenames == null) {
      fileList = new ArrayList<>();
    } else {

      fileList = Arrays.stream(filenames)
          .filter(File::isFile)
          .map(File::getName)
          .filter(name -> name.endsWith(".csv"))
          .collect(Collectors.toList());
    }

    return fileList;
  }

  /**
   * Reads the given embedded file and returns its content in an accessible form.
   *
   * @param resourceName filename of CSV file in resource folder
   * @return content of the related file as a list of split strings including the CSV-header at
   *     first position.
   * @throws URISyntaxException uriSyntaxException
   * @throws IOException        iOException
   */
  public static List<String[]> read(String resourceName) throws IOException, URISyntaxException {
    try {
      if (!getAvailableFiles().contains(resourceName)) {
        throw new IOException("File not found in Resources.");
      }
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      log.warning("CsvDataReader.getAvailableFiles() threw an exception."
          + " Skipping file verification.");
    }
    return readCsvResource("/" + resourceName);
  }

  /**
   * Reads the embedded {@code Wissenstest_sample200.csv} file and returns its content in an
   * accessible form.
   *
   * @return content of the related file as a list of split strings including the CSV-header at
   *     first position.
   * @throws URISyntaxException uriSyntaxException
   * @throws IOException        iOException
   */
  public static List<String[]> read() throws URISyntaxException, IOException {
    return readCsvResource("/Wissenstest_sample200.csv");
  }

  private static List<String[]> readCsvResource(String resourcePath) throws IOException {
    InputStream inputStream = CsvDataReader.class.getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IllegalArgumentException("Resource '" + resourcePath + "' does not exist");
    }
    try (inputStream; BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

      return reader.lines()
          .map(line -> line.split(SPLIT_CHARACTER))
          .collect(Collectors.toList());
    }
  }
}
