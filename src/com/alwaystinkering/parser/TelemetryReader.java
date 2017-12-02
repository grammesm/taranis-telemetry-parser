package com.alwaystinkering.parser;

import com.alwaystinkering.util.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;

public class TelemetryReader {

    private static final String TAG = "TelemetryReader";

    public void parse(File telemetryFile) throws IOException {

        boolean first = true;
        String header;

        if (telemetryFile.exists()) {
            Log.debug(TAG, "Parsing: " + telemetryFile.getAbsolutePath());
            LineIterator it = FileUtils.lineIterator(telemetryFile, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();

                    if (first) {
                        header = line;
                        first = false;
                        Log.debug(TAG, "Header: " + header);
                        continue;
                    }

                    String[] parts = line.split(",");
                    String date = parts[0];
                    String time = parts[1];
                }
            } finally {
                it.close();
            }

        }
    }
}
