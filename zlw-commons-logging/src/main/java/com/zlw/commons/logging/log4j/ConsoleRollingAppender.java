package com.zlw.commons.logging.log4j;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConsoleRollingAppender extends ConsoleAppender {
    /**
     * The default maximum file size is 10MB.
     */
    protected long maxFileSize = 10 * 1024 * 1024;

    /**
     * There is one backup file by default.
     */
    protected int maxBackupIndex = 1;

    private long nextRollover = 0;
    /**
     * Controls file truncatation. The default value for this variable is
     * <code>true</code>, meaning that by default a <code>FileAppender</code>
     * will append to an existing file and not truncate it.
     * <p>
     * <p>
     * This option is meaningful only if the FileAppender opens the file.
     */
    protected boolean fileAppend = true;
    SimpleDateFormat sdf;
    Date now = new Date();
    /**
     * The name of the log file.
     */
    protected String fileName = null;
    /**
     * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd"
     * meaning daily rollover.
     */
    private String datePattern = "'.'yyyy-MM-dd";
    /**
     * Do we do bufferedIO?
     */
    protected boolean bufferedIO = false;

    /**
     * Determines the size of IO buffer be. Default is 8K.
     */
    protected int bufferSize = 8 * 1024;

    public ConsoleRollingAppender() {
        super();
    }

    public ConsoleRollingAppender(Layout layout, String filename, boolean append)
            throws IOException {
        this.layout = layout;
        this.setFile(filename, append, false, bufferSize);
    }

    /**
     * If the value of <b>File</b> is not <code>null</code>, then
     * {@link #setFile} is called with the values of <b>File</b> and
     * <b>Append</b> properties.
     *
     * @since 0.8.1
     */
    public void activateOptions() {
        if (fileName != null) {
            try {
                setFile(fileName, fileAppend, bufferedIO, bufferSize);
            } catch (IOException e) {
                errorHandler.error("setFile(" + fileName + "," + fileAppend
                        + ") call failed.", e, ErrorCode.FILE_OPEN_FAILURE);
            }
            sdf = new SimpleDateFormat(datePattern);
        } else {
            // LogLog.error("File option not set for appender ["+name+"].");
            LogLog.warn("File option not set for appender [" + name + "].");
            LogLog.warn("Are you using FileAppender instead of ConsoleAppender?");
        }
    }

    /**
     * <p>
     * Sets and <i>opens</i> the file where the log output will go. The
     * specified file must be writable.
     * <p>
     * <p>
     * If there was already an opened file, then the previous file is closed
     * first.
     * <p>
     * <p>
     * <b>Do not use this method directly. To configure a FileAppender or one of
     * its subclasses, set its properties one by one and then call
     * activateOptions.</b>
     *
     * @param fileName The path to the log file.
     * @param append   If true will append to fileName. Otherwise will truncate
     *                 fileName.
     */
    public synchronized void setFile(String fileName, boolean append,
                                     boolean bufferedIO, int bufferSize) throws IOException {
        LogLog.debug("setFile called: " + fileName + ", " + append);

        // It does not make sense to have immediate flush and bufferedIO.
        if (bufferedIO) {
            setImmediateFlush(false);
        }

        reset();
        FileOutputStream ostream = null;
        try {
            //
            // attempt to create file
            //
            ostream = new FileOutputStream(fileName, append);
        } catch (FileNotFoundException ex) {
            //
            // if parent directory does not exist then
            // attempt to create it and try to create file
            // see bug 9150
            //
            String parentName = new File(fileName).getParent();
            if (parentName != null) {
                File parentDir = new File(parentName);
                if (!parentDir.exists() && parentDir.mkdirs()) {
                    ostream = new FileOutputStream(fileName, append);
                } else {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
        PrintStream ps = new PrintStream(ostream, true);
        System.setOut(ps);
        Writer fw = new OutputStreamWriter(new BufferedOutputStream(ps,
                bufferSize));
        if (bufferedIO) {
            fw = new BufferedWriter(fw);
        }
        this.setQWForFiles(fw);
        this.fileName = fileName;
        this.fileAppend = append;
        this.bufferedIO = bufferedIO;
        this.bufferSize = bufferSize;
        writeHeader();
        LogLog.debug("setFile ended");

    }

    /**
     * Sets the quiet writer being used.
     * <p>
     * This method is overriden by {@link RollingFileAppender}.
     */
    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

    /**
     * This method differentiates RollingFileAppender from its super class.
     *
     * @since 0.9.0
     */
    protected void subAppend(LoggingEvent event) {
        super.subAppend(event);
        if (fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            if (size >= maxFileSize && size >= nextRollover) {
                rollOver();
            }
        }
    }

    /**
     * Implements the usual roll over behaviour.
     * <p>
     * <p>
     * If <code>MaxBackupIndex</code> is positive, then files {
     * <code>File.1</code>, ..., <code>File.MaxBackupIndex -1</code> are renamed
     * to {<code>File.2</code>, ..., <code>File.MaxBackupIndex</code> .
     * Moreover, <code>File</code> is renamed <code>File.1</code> and closed. A
     * new <code>File</code> is created to receive further log output.
     * <p>
     * <p>
     * If <code>MaxBackupIndex</code> is equal to zero, then the
     * <code>File</code> is truncated with no backup files created.
     */
    public// synchronization not necessary since doAppend is alreasy synched
    void rollOver() {
        File target;
        File file;

        if (qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            LogLog.debug("rolling over count=" + size);
            // if operation fails, do not roll again until
            // maxFileSize more bytes are written
            nextRollover = size + maxFileSize;
        }
        LogLog.debug("maxBackupIndex=" + maxBackupIndex);

        boolean renameSucceeded = true;
        String datedFilename = fileName + sdf.format(new Date());
        // If maxBackups <= 0, then there is no file renaming to be done.
        if (maxBackupIndex > 0) {
            // Delete the oldest file, to keep Windows happy.
            file = new File(datedFilename + '.' + maxBackupIndex);
            if (file.exists())
                renameSucceeded = file.delete();

            // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3,
            // 2}
            for (int i = maxBackupIndex - 1; i >= 1 && renameSucceeded; i--) {
                file = new File(datedFilename + "." + i);
                if (file.exists()) {
                    target = new File(datedFilename + '.' + (i + 1));
                    LogLog.debug("Renaming file " + file + " to " + target);
                    renameSucceeded = file.renameTo(target);
                }
            }

            if (renameSucceeded) {
                // Rename fileName to fileName.1
                target = new File(datedFilename + "." + 1);

                this.closeFile(); // keep windows happy.

                file = new File(fileName);
                LogLog.debug("Renaming file " + file + " to " + target);
                renameSucceeded = file.renameTo(target);
                //
                // if file rename failed, reopen file with append = true
                //
                if (!renameSucceeded) {
                    try {
                        this.setFile(fileName, true, bufferedIO, bufferSize);
                    } catch (IOException e) {
                        if (e instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        LogLog.error("setFile(" + fileName
                                + ", true) call failed.", e);
                    }
                }
            }
        }

        //
        // if all renames were successful, then
        //
        if (renameSucceeded) {
            try {
                // This will also close the file. This is OK since multiple
                // close operations are safe.
                this.setFile(fileName, false, bufferedIO, bufferSize);
                nextRollover = 0;
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("setFile(" + fileName + ", false) call failed.", e);
            }
        }
    }

    /**
     * Closes the previously opened file.
     */
    protected void closeFile() {
        if (this.qw != null) {
            try {
                this.qw.close();
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                // Exceptionally, it does not make sense to delegate to an
                // ErrorHandler. Since a closed appender is basically dead.
                LogLog.error("Could not close " + qw, e);
            }
        }
    }

    public long getMaximumFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = OptionConverter.toFileSize(maxFileSize,
                this.maxFileSize + 1);
    }

    public void setMaximumFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }

    public void setMaxBackupIndex(int maxBackupIndex) {
        this.maxBackupIndex = maxBackupIndex;
    }

    public long getNextRollover() {
        return nextRollover;
    }

    public void setNextRollover(long nextRollover) {
        this.nextRollover = nextRollover;
    }

    public boolean isFileAppend() {
        return fileAppend;
    }

    public void setFileAppend(boolean fileAppend) {
        this.fileAppend = fileAppend;
    }

    public void setFile(String file) {
        // Trim spaces from both ends. The users probably does not want
        // trailing spaces in file names.
        String val = file.trim();
        fileName = val;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isBufferedIO() {
        return bufferedIO;
    }

    public void setBufferedIO(boolean bufferedIO) {
        this.bufferedIO = bufferedIO;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
