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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketBiomeWeather implements SpoutPacket {
	public byte biome;
	public byte weather;

	@Override
	public int getNumBytes() {
		return 2;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		biome = input.readByte();
		weather = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeByte(biome);
		output.writeByte(weather);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketBiomeWeather;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void run(int PlayerId) {
		String biomeString = "";
		
		switch(biome) {
		case 0: biomeString = "Rainforest"; break;
		case 1: biomeString = "Swampland"; break;
		case 2: biomeString = "Seasonal Forest"; break;
		case 3: biomeString = "Forest"; break;
		case 4: biomeString = "Savanna"; break;
		case 5: biomeString = "Shrubland"; break;
		case 6: biomeString = "Taiga"; break;
		case 7: biomeString = "Desert"; break;
		case 8: biomeString = "Plains"; break;
		case 9: biomeString = "Ice Desert"; break;
		case 10: biomeString = "Tundra"; break;
		case 11: biomeString = "Hell"; break;
		case 12: biomeString = "Sky"; break;
		default: break;
		}
		
		SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);
		
		switch(weather) {
		case 0: 
			SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, false);
			SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, false);
			break;
		case 1:
			SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, false);
			SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, true);
			break;
		case 2:
			SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, true);
			SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, false);
			break;
		case 3:
			SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);
			break;
		default:
			SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);
			break;
		}
	}

	@Override
	public void failure(int playerId) {
		
	}
}
