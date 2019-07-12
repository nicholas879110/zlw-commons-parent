/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zlw.commons.logging.log4j;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DailyRollingFileAppender extends {@link FileAppender} so that the underlying
 * file is rolled over at a user chosen frequency.
 * <p>
 * DailyRollingFileAppender has been observed to exhibit synchronization issues
 * and data loss. The log4j extras companion includes alternatives which should
 * be considered for new deployments and which are discussed in the
 * documentation for org.apache.log4j.rolling.RollingFileAppender.
 * <p>
 * <p>
 * The rolling schedule is specified by the <b>DatePattern</b> option. This
 * pattern should follow the {@link SimpleDateFormat} conventions. In
 * particular, you <em>must</em> escape literal text within a pair of single
 * quotes. A formatted version of the date pattern is used as the suffix for the
 * rolled file name.
 * <p>
 * <p>
 * For example, if the <b>File</b> option is set to <code>/foo/bar.log</code>
 * and the <b>DatePattern</b> set to <code>'.'yyyy-MM-dd</code>, on 2001-02-16
 * at midnight, the logging file <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2001-02-16</code> and logging for 2001-02-17 will continue
 * in <code>/foo/bar.log</code> until it rolls over the next day.
 * <p>
 * <p>
 * Is is possible to specify monthly, weekly, half-daily, daily, hourly, or
 * minutely rollover schedules.
 * <p>
 * <p>
 * <table border="1" cellpadding="2">
 * <tr>
 * <th>DatePattern</th>
 * <th>Rollover schedule</th>
 * <th>Example</th>
 * <p>
 * <tr>
 * <td><code>'.'yyyy-MM</code>
 * <td>Rollover at the beginning of each month</td>
 * <p>
 * <td>At midnight of May 31st, 2002 <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-05</code>. Logging for the month of June will be
 * output to <code>/foo/bar.log</code> until it is also rolled over the next
 * month.
 * <p>
 * <tr>
 * <td><code>'.'yyyy-ww</code>
 * <p>
 * <td>Rollover at the first day of each week. The first day of the week depends
 * on the locale.</td>
 * <p>
 * <td>Assuming the first day of the week is Sunday, on Saturday midnight, June
 * 9th 2002, the file <i>/foo/bar.log</i> will be copied to
 * <i>/foo/bar.log.2002-23</i>. Logging for the 24th week of 2002 will be output
 * to <code>/foo/bar.log</code> until it is rolled over the next week.
 * <p>
 * <tr>
 * <td><code>'.'yyyy-MM-dd</code>
 * <p>
 * <td>Rollover at midnight each day.</td>
 * <p>
 * <td>At midnight, on March 8th, 2002, <code>/foo/bar.log</code> will be copied
 * to <code>/foo/bar.log.2002-03-08</code>. Logging for the 9th day of March
 * will be output to <code>/foo/bar.log</code> until it is rolled over the next
 * day.
 * <p>
 * <tr>
 * <td><code>'.'yyyy-MM-dd-a</code>
 * <p>
 * <td>Rollover at midnight and midday of each day.</td>
 * <p>
 * <td>At noon, on March 9th, 2002, <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-03-09-AM</code>. Logging for the afternoon of the 9th
 * will be output to <code>/foo/bar.log</code> until it is rolled over at
 * midnight.
 * <p>
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH</code>
 * <p>
 * <td>Rollover at the top of every hour.</td>
 * <p>
 * <td>At approximately 11:00.000 o'clock on March 9th, 2002,
 * <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-03-09-10</code>. Logging for the 11th hour of the 9th
 * of March will be output to <code>/foo/bar.log</code> until it is rolled over
 * at the beginning of the next hour.
 * <p>
 * <p>
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH-mm</code>
 * <p>
 * <td>Rollover at the beginning of every minute.</td>
 * <p>
 * <td>At approximately 11:23,000, on March 9th, 2001, <code>/foo/bar.log</code>
 * will be copied to <code>/foo/bar.log.2001-03-09-10-22</code>. Logging for the
 * minute of 11:23 (9th of March) will be output to <code>/foo/bar.log</code>
 * until it is rolled over the next minute.
 * <p>
 * </table>
 * <p>
 * <p>
 * Do not use the colon ":" character in anywhere in the <b>DatePattern</b>
 * option. The text before the colon is interpeted as the protocol specificaion
 * of a URL which is probably not what you want.
 *
 * @author Eirik Lygre
 * @author Ceki G&uuml;lc&uuml;
 */
public class SizeRollingFileAppender extends FileAppender {

	/**
	 * The default maximum file size is 10MB.
	 */
	protected long maxFileSize = 10 * 1024 * 1024;
	private String datePattern = "'.'yyyy-MM-dd";
	/**
	 * There is one backup file by default.
	 */
	protected int maxBackupIndex = 1;

	private long nextRollover = 0;
	SimpleDateFormat sdf;
	Date now = new Date();

	/**
	 * The default constructor simply calls its
	 * {@link FileAppender#FileAppender parents constructor}.
	 */
	public SizeRollingFileAppender() {
		super();
	}

	/**
	 * Instantiate a RollingFileAppender and open the file designated by
	 * <code>filename</code>. The opened filename will become the ouput
	 * destination for this appender.
	 * 
	 * <p>
	 * If the <code>append</code> parameter is true, the file will be appended
	 * to. Otherwise, the file desginated by <code>filename</code> will be
	 * truncated before being opened.
	 */
	public SizeRollingFileAppender(Layout layout, String filename,
			boolean append) throws IOException {
		super(layout, filename, append);
	}

	/**
	 * Instantiate a FileAppender and open the file designated by
	 * <code>filename</code>. The opened filename will become the output
	 * destination for this appender.
	 * 
	 * <p>
	 * The file will be appended to.
	 */
	public SizeRollingFileAppender(Layout layout, String filename)
			throws IOException {
		super(layout, filename);
	}

	/**
	 * Returns the value of the <b>MaxBackupIndex</b> option.
	 */
	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	public void activateOptions() {
		super.activateOptions();
		sdf = new SimpleDateFormat(datePattern);
	}

	/**
	 * Get the maximum size that the output file is allowed to reach before
	 * being rolled over to backup files.
	 * 
	 * @since 1.1
	 */
	public long getMaximumFileSize() {
		return maxFileSize;
	}

	/**
	 * Implements the usual roll over behaviour.
	 * 
	 * <p>
	 * If <code>MaxBackupIndex</code> is positive, then files {
	 * <code>File.1</code>, ..., <code>File.MaxBackupIndex -1</code> are renamed
	 * to {<code>File.2</code>, ..., <code>File.MaxBackupIndex</code> .
	 * Moreover, <code>File</code> is renamed <code>File.1</code> and closed. A
	 * new <code>File</code> is created to receive further log output.
	 * 
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
		String datedFilename = fileName + sdf.format(now);
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

	public synchronized void setFile(String fileName, boolean append,
			boolean bufferedIO, int bufferSize) throws IOException {
		super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
		if (append) {
			File f = new File(fileName);
			((CountingQuietWriter) qw).setCount(f.length());
		}
	}

	/**
	 * Set the maximum number of backup files to keep around.
	 * 
	 * <p>
	 * The <b>MaxBackupIndex</b> option determines how many backup files are
	 * kept before the oldest is erased. This option takes a positive integer
	 * value. If set to zero, then there will be no backup files and the log
	 * file will be truncated when it reaches <code>MaxFileSize</code>.
	 */
	public void setMaxBackupIndex(int maxBackups) {
		this.maxBackupIndex = maxBackups;
	}

	/**
	 * Set the maximum size that the output file is allowed to reach before
	 * being rolled over to backup files.
	 * 
	 * <p>
	 * This method is equivalent to {@link #setMaxFileSize} except that it is
	 * required for differentiating the setter taking a <code>long</code>
	 * argument from the setter taking a <code>String</code> argument by the
	 * JavaBeans {@link java.beans.Introspector Introspector}.
	 * 
	 * @see #setMaxFileSize(String)
	 */
	public void setMaximumFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * Set the maximum size that the output file is allowed to reach before
	 * being rolled over to backup files.
	 * 
	 * <p>
	 * In configuration files, the <b>MaxFileSize</b> option takes an long
	 * integer in the range 0 - 2^63. You can specify the value with the
	 * suffixes "KB", "MB" or "GB" so that the integer is interpreted being
	 * expressed respectively in kilobytes, megabytes or gigabytes. For example,
	 * the value "10KB" will be interpreted as 10240.
	 */
	public void setMaxFileSize(String value) {
		maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
	}

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
}
