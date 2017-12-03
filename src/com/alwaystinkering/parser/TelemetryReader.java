package com.alwaystinkering.parser;

import com.alwaystinkering.generator.KmlGenerator;
import com.alwaystinkering.util.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelemetryReader {

    private static final String TAG = "TelemetryReader";

    private DateFormat dateWriteFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
    private DateFormat dateReadFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
    private List<String> fileContents = new ArrayList<>();

    private KmlGenerator kmlGenerator;

    public TelemetryReader() {
        kmlGenerator = new KmlGenerator();
    }
    public void parse(File telemetryFile) throws IOException {

        fileContents.clear();
        boolean first = true;
        String header;
        int gpsIndex = 0;

        List<SessionData> sessionDataList = new ArrayList<>();

        SessionData sessionData = new SessionData();
        long period = 0;
        int currentIndex = 1;

        // Read the file
        if (telemetryFile.exists()) {
            Log.d(TAG, "Parsing: " + telemetryFile.getAbsolutePath());
            LineIterator it = FileUtils.lineIterator(telemetryFile, "UTF-8");
            try {
                Date previousDate = null;
                while (it.hasNext()) {
                    String line = it.nextLine();
                    fileContents.add(line);

                    if (first) {
                        header = line;
                        String[] headerParts = header.split(",");
                        for (int i = 0; i < headerParts.length; i++) {
                            if (headerParts[i].toLowerCase().contains("gps")) {
                                gpsIndex = i;
                                Log.d(TAG, "GPS index found: " + gpsIndex);
                                break;
                            }
                        }
                        first = false;
                        Log.d(TAG, "Header: " + header);
                        continue;
                    }

                    String[] parts = line.split(",");
                    String date = parts[0];
                    String time = parts[1];
                    Date logDate = dateReadFormat.parse(date + "-" + time);

                    if (sessionData.getStartIndex() == -1) {
                        sessionData.setStartIndex(currentIndex);
                        sessionData.setName(dateWriteFormat.format(logDate));
                    }

                    boolean newSession = false;
                    if (previousDate != null) {
                        long diff = logDate.getTime() - previousDate.getTime();
                        if (period != 0) {
                            // If the diff is more than 20 ms, assume new session
                            if (Math.abs(diff - period) > 50) {
                                Log.d(TAG, "New Session? currentIndex: " + currentIndex + " diff: " + diff + ", last diff: " + period);
                                sessionData.setEndIndex(currentIndex);
                                sessionDataList.add(new SessionData(sessionData));
                                sessionData = new SessionData();
                                sessionData.setName(dateWriteFormat.format(logDate));
                                sessionData.setStartIndex(currentIndex + 1);
                                newSession = true;
                            }
                        }
                        if (!newSession) {
                            period = diff;
                        }
                    }
                    previousDate = logDate;
                    currentIndex++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                it.close();
            }

            // Add last session
            if (sessionData.getEndIndex() == -1) {
                sessionData.setEndIndex(currentIndex - 1);
                sessionDataList.add(sessionData);
            }

            Log.d(TAG, "Found " + sessionDataList.size() + " sessions");
            // Dump session data:
            Log.d(TAG, "Session Data: " + sessionDataList);

            // Parse the data
            for (SessionData session : sessionDataList) {
                LogData logData = new LogData();
                logData.setSessionName(session.getName());

                // Set start and end dates
                logData.setStart(getDateFromLine(fileContents.get(session.getStartIndex())));
                logData.setEnd(getDateFromLine(fileContents.get(session.getEndIndex())));

                List<GeoPoint> coords = new ArrayList<>();
                // Populate coords
                for (int i = session.getStartIndex(); i <= session.getEndIndex(); i++) {
                    GeoPoint point = getPointFromLine(gpsIndex, fileContents.get(i));
                    if (point != null && point.isValid()) {
                        coords.add(point);
                    }
                }
                logData.setPath(coords);

                // Create kml file
                kmlGenerator.generateKmlFile(logData, "telemetry_kml_files");

            }

        }
    }

    private Date getDateFromLine(String line) {
        String[] parts = line.split(",");
        String date = parts[0];
        String time = parts[1];
        try {
            return dateReadFormat.parse(date + "-" + time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GeoPoint getPointFromLine(int index, String line) {
        String[] parts = line.split(",");
        String gps = parts[index];
        String altM = parts[index + 1];
        String[] gpsParts = gps.split(" ");
        try {
            double lon = Double.valueOf(gpsParts[1]);
            double lat = Double.valueOf(gpsParts[0]);
            double alt = Double.valueOf(altM);
            return new GeoPoint(lat, lon, alt);
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
            return null;
        }
    }
}
