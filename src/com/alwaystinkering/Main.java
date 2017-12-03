package com.alwaystinkering;

import com.alwaystinkering.parser.TelemetryReader;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        File[] files = new File("telemetry_logs").listFiles(pathname -> {
            if (pathname.getName().endsWith(".csv")) {
                return true;
            } else {
                return false;
            }
        });

        for (File file : files) {
            try {
                new TelemetryReader().parse(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
