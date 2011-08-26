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
import org.getspout.spout.entity.EntityManager;

public class PacketEntitySkin implements SpoutPacket {
	protected String texture = "";
	protected int entityId;
	protected byte textureId = 0;
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(texture) + 4 + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		textureId = input.readByte();
		texture = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeByte(textureId);
		PacketUtil.writeString(output, texture);
	}

	@Override
	public void run(int PlayerId) {
		if(texture.equals("[reset]")){
			texture = null;
		}
		EntityManager manager = SpoutClient.getInstance().getEntityManager();
		manager.setTexture(entityId, texture, textureId);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketEntitySkin;
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
