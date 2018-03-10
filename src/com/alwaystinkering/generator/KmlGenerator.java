package com.alwaystinkering.generator;

import com.alwaystinkering.parser.GeoPoint;
import com.alwaystinkering.parser.LogData;
import com.alwaystinkering.util.Log;

import java.io.*;

public class KmlGenerator {

    private static final String TAG = "KmlGenerator";

    private final static String KML_TEMPLATE =
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
        "  <Document>\n" +
        "    <name>Flight Path</name>\n" +
        "    <description>This path is generated from data collected by OpenTX. Data points are only as accurate as the source</description>\n" +
        "    <Style id=\"orangeLineBluePoly\">\n" +
        "      <LineStyle>\n" +
        "        <color>af4192f4</color>\n" +
        "        <width>2</width>\n" +
        "      </LineStyle>\n" +
        "      <PolyStyle>\n" +
        "        <color>7ff4a041</color>\n" +
        "      </PolyStyle>\n" +
        "    </Style>\n" +
        "    <Placemark>\n" +
        "      <name>%s</name>\n" +
        "      <description>Flight Path recorded from Taranis Telemetry</description>\n" +
        "      <styleUrl>#orangeLineBluePoly</styleUrl>\n" +
        "        <LookAt>\n" +
        "          <longitude>%f</longitude>\n" +
        "          <latitude>%f</latitude>\n" +
        "          <altitude>0</altitude>\n" +
        "          <heading>0</heading>\n" +
        "          <tilt>44.61038665812578</tilt>\n" +
        "          <range>500</range>\n" +
        "        </LookAt>\n" +
        "      <LineString>\n" +
        "        <extrude>1</extrude>\n" +
        "        <tessellate>1</tessellate>\n" +
        "        <altitudeMode>absolute</altitudeMode>\n" +
        "        <coordinates> %s" +
        "        </coordinates>\n" +
        "      </LineString>\n" +
        "    </Placemark>\n" +
        "  </Document>\n" +
        "</kml>";

    public void generateKmlFile(LogData logData, String outputPath) {

        if (logData.getPath().size() > 0) {
            StringBuilder coordinateString = new StringBuilder();
            for (GeoPoint point : logData.getPath()) {
                coordinateString.append(point.toString());
            }

            String kmlOut = String.format(KML_TEMPLATE,
                    // Name postscript
                    " " + logData.getSessionName(),
                    // Look At Coord
                    logData.getPath().get(0).getLon(),
                    logData.getPath().get(0).getLat(),
                    // Path Coords
                    coordinateString.toString());

            File kmlOutputDir = new File(outputPath);
            kmlOutputDir.mkdirs();
            File kmlFile = new File(kmlOutputDir.getAbsolutePath() + File.separator + logData.getSessionName() + ".kml");

            try {
                Log.d(TAG, "Attempting to create: " + kmlFile.getAbsolutePath());
                if (kmlFile.exists()) {
                    kmlFile.delete();
                }
                kmlFile.createNewFile();
                BufferedWriter br = new BufferedWriter(new FileWriter(kmlFile));
                br.write(kmlOut);
                br.flush();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Trying to create kml with no points: " + logData.getSessionName());
        }
    }
}
