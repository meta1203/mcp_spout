// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet9Respawn extends Packet
{

    public Packet9Respawn()
    {
    }

    public Packet9Respawn(byte byte0)
    {
        respawnDimension = byte0;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleRespawn(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        respawnDimension = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(respawnDimension);
    }

    public int getPacketSize()
    {
        return 1;
    }

    public byte respawnDimension;
}
