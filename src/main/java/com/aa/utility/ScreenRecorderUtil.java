package com.aa.utility;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorderUtil extends ScreenRecorder {
	
public 	static boolean totallyRecordNotRequired=true;
	
	
    public static ScreenRecorder screenRecorder;
    public String name;
    private static File movieFile; // Field to store the full file path

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                               Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
        this.movieFile = new File(movieFolder,
                name + "-" + Library.DateTimeString() + "." + Registry.getInstance().getExtension(fileFormat));
       
        /*below code is old modified on date and time 16/01/2025 14:04
         *  this.movieFile = new File(movieFolder,
                name + "-" + dateFormat.format(new Date()) + "" + Registry.getInstance().getExtension(fileFormat));
       
         * 
         * 
         * 
         */
        
        return movieFile;
    }

    // Getter method for the full file path
    public static String getMovieFilePath() {
        return movieFile != null ? movieFile.getAbsolutePath() : null;
    }

    public static void startRecord(String methodName) throws Exception {
       
if(totallyRecordNotRequired) {
	
	return;
}
    	
    	File file = new File("./test-recordings/");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice()
                .getDefaultConfiguration();

        screenRecorder = new ScreenRecorderUtil(gc, captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, methodName);
        screenRecorder.start();
        
    }

    public static void stopRecord() throws Exception {
        
        
    	if(totallyRecordNotRequired) {
    		
    		return;
    	}
    	
    	screenRecorder.stop();
        // Log or print the file path of the recording
        if (screenRecorder instanceof ScreenRecorderUtil) {
            System.out.println("Recording saved at: " + ((ScreenRecorderUtil) screenRecorder).getMovieFilePath());
        }
    }
}
