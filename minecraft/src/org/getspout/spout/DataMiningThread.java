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
package org.getspout.spout;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;

import net.minecraft.src.World;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;

public class DataMiningThread extends Thread{
	private volatile boolean onLogin = false;
	private volatile boolean multiplayer = false;
	private boolean runOnce = false;
	private static DataMiningThread instance = null;
	
	public DataMiningThread() {
		instance = this;
	}
	
	public static DataMiningThread getInstance() {
		return instance;
	}
	
	public void onLogin() {
		onLogin = true;
	}
	
	private void doLogin() {
		onRunOnce();
		if (SpoutClient.getInstance().getServerVersion().getVersion() > 99) {
			onSpoutLogin();
		}
		else {
			onVanillaLogin();
		}
	}
	
	private void doSinglePlayer() {
		onRunOnce();
		pingLink("http://bit.ly/spoutsp");
	}
	
	private void onRunOnce() {
		File runOnce = new File(FileUtil.getCacheDirectory(), "spout");
		if (!runOnce.exists()) {
			try {
				runOnce.createNewFile();
				pingLink("http://bit.ly/spoutcraftinstall");
			}
			catch (Exception e) {}
		}
	}
	
	private void onSpoutLogin() {
		pingLink("http://bit.ly/spoutcraftlogin");
	}
	
	private void onVanillaLogin() {
		pingLink("http://bit.ly/vanillalogin");
	}
	
	@SuppressWarnings("unused")
	private void pingLink(String Url) {
		try {
			URL url = new URL(Url);
			HttpURLConnection con = (HttpURLConnection)(url.openConnection());
			System.setProperty("http.agent", ""); //Spoofing the user agent is required to track stats
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String str;
			while ((str = in.readLine()) != null);
			in.close();
		}
		catch (Exception e) {}
	}
	
	public void run() {
		while(true) {
			try {
				sleep(10000);
			}
			catch (InterruptedException e1) {}
			if (onLogin) {
				doLogin();
				onLogin = false;
			}
			World world = SpoutClient.getHandle().theWorld;
			if (world != null) {
				if (!runOnce) {
					multiplayer = world.multiplayerWorld;
					runOnce = true;
					onRunOnce();
					System.out.println("Ran Once, Multiplayer: " + multiplayer);
				}
				if (multiplayer != world.multiplayerWorld) {
					System.out.println("Switched to " + (world.multiplayerWorld ? "Multiplayer" : "Singleplayer"));
					if (world.multiplayerWorld) {
						doSinglePlayer();
					}
					multiplayer = world.multiplayerWorld;
				}
			}
		}
	}
}