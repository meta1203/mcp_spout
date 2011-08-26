package net.minecraft.src;

import com.pclewis.mcpatcher.mod.TextureUtils;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
//Spout HD Start
/*
import javax.imageio.ImageIO;
import net.minecraft.src.PanelCrashReport;
*/
import com.pclewis.mcpatcher.mod.TextureUtils;
//Spout HD End
class CanvasMojangLogo extends Canvas {

	private BufferedImage logo;


	public CanvasMojangLogo() {
		try {
//Spout HD Start
			this.logo = TextureUtils.getResourceAsBufferedImage("/gui/logo.png");
//Spout HD End
		} catch (IOException var2) {
			;
		}

		byte var1 = 100;
		this.setPreferredSize(new Dimension(var1, var1));
		this.setMinimumSize(new Dimension(var1, var1));
	}

	public void paint(Graphics var1) {
		super.paint(var1);
		var1.drawImage(this.logo, this.getWidth() / 2 - this.logo.getWidth() / 2, 32, (ImageObserver)null);
	}
}
