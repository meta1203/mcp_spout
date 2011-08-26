/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.io;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileDownloadThread extends Thread{
	private static FileDownloadThread instance = null;
	private final ConcurrentLinkedQueue<Download> downloads = new ConcurrentLinkedQueue<Download>();
	private final ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();
	private final byte[] buffer = new byte[1024*1024];
	private volatile String activeDownload = null;
	
	protected FileDownloadThread() {
		super("File Download Thread");
	}
	
	public static FileDownloadThread getInstance() {
		if (instance == null) {
			instance = new FileDownloadThread();
			instance.start();
		}
		return instance;
	}
	
	public void addToDownloadQueue(Download download){
		downloads.add(download);
	}

	public boolean isDownloading(String url) {
		Iterator<Download> i = downloads.iterator();
		while(i.hasNext()) {
			Download download = i.next();
			if (download.getDownloadUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	public void onTick() {
		Iterator<Runnable> i = actions.iterator();
		while(i.hasNext()) {
			Runnable action = i.next();
			action.run();
			i.remove();
		}
	}
	
	public void abort() {
		this.interrupt();
		downloads.clear();
	}
	
	public String getActiveDownload() {
		return activeDownload;
	}
	
	public int getDownloadsRemaining() {
		return downloads.size();
	}
	
	public void run() {
		while(true) {
			Download next = downloads.poll();
			if (next != null) {
				try {
					if (!next.isDownloaded()) {
						System.out.println("Downloading File: " + next.getDownloadUrl());
						activeDownload = FileUtil.getFileName(next.getDownloadUrl());
						URL url = new URL(next.getDownloadUrl());
						URLConnection conn = url.openConnection();
						InputStream in = conn.getInputStream();
						
						FileOutputStream fos = new FileOutputStream(next.getTempFile());
						
						long length = conn.getContentLength();
						int bytes;
						long totalBytes = 0;
						long last = 0;
						
						long step = Math.max(1024*1024, length / 8);
						
						while ((bytes = in.read(buffer)) >= 0) {
							fos.write(buffer, 0, bytes);
							totalBytes += bytes;
							if (length > 0 && totalBytes > (last + step)) {
								last = totalBytes;
								long mb = totalBytes/(1024*1024);
								System.out.println("Downloading: " + next.getDownloadUrl() + " " + mb + "MB/" + (length/(1024*1024)));
							}
							try {
								Thread.sleep(25);
							} catch (InterruptedException e) {
								
							}
						}
						in.close();
						fos.close();
						next.move();
						System.out.println("File moved to: " + next.directory.getCanonicalPath());
						try {
							sleep(10); //cool off after heavy network useage
						} catch (InterruptedException e) {}
					}
					if (next.getCompletedAction() != null) {
						actions.add(next.getCompletedAction());
					}
				}
				catch (Exception e) {
					System.out.println("-----------------------");
					System.out.println("Download Failed!");
					e.printStackTrace();
					System.out.println("-----------------------");
				}
				activeDownload = null;
			}
			else {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
		}
	}
}
