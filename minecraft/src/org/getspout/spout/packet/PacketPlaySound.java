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
import net.minecraft.src.*;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.sound.Music;
import org.getspout.spout.sound.SoundEffect;

public class PacketPlaySound implements SpoutPacket{
	short soundId;
	boolean location = false;
	int x, y, z;
	int volume, distance;
	
	public PacketPlaySound() {
		
	}

	@Override
	public int getNumBytes() {
		return 23;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		soundId = input.readShort();
		location = input.readBoolean();
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		distance = input.readInt();
		volume = input.readInt();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeShort(soundId);
		output.writeBoolean(location);
		if (!location) {
			output.writeInt(-1);
			output.writeInt(-1);
			output.writeInt(-1);
			output.writeInt(-1);
		}
		else {
			output.writeInt(x);
			output.writeInt(y);
			output.writeInt(z);
			output.writeInt(distance);
		}
		output.writeInt(volume);
	}

	@Override
	public void run(int entityId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (e != null) {
				SoundManager sndManager = SpoutClient.getHandle().sndManager;
				if (soundId > -1 && soundId <= SoundEffect.getMaxId()) {
					SoundEffect effect = SoundEffect.getSoundEffectFromId(soundId);
					if (!location) {
						sndManager.playSoundFX(effect.getName(), 0.5F, 0.7F, effect.getSoundId(), volume / 100F);
					}
					else {
						sndManager.playSound(effect.getName(), x, y, z, 0.5F, (distance / 16F), effect.getSoundId(), volume / 100F);
					}
				}
				soundId -= (1 + SoundEffect.getMaxId());
				if (soundId > -1 && soundId <= Music.getMaxId()) {
					Music music = Music.getMusicFromId(soundId);
					sndManager.playMusic(music.getName(), music.getSoundId(), volume / 100F);
				}
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPlaySound;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
