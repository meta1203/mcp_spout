package net.minecraft.src;

import org.getspout.spout.gui.server.GuiFavorites;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

//Spout Start
import net.minecraft.src.ReconnectManager;
//Spout End

public class GuiConnectFailed extends GuiScreen {

	private String errorMessage;
	private String errorDetail;


	public GuiConnectFailed(String var1, String var2, Object ... var3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.errorMessage = var4.translateKey(var1);
		if(var3 != null) {
			this.errorDetail = var4.translateKeyFormat(var2, var3);
		} else {
			this.errorDetail = var4.translateKey(var2);
		}

		//Spout Start
		ReconnectManager.detectKick(var1, var2, var3);
		//Spout End
	}

	public void updateScreen() {}

	protected void keyTyped(char var1, int var2) {}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("Back to server list"))); //Spout
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiFavorites(new GuiMainMenu())); //Spout
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 16777215);
		this.drawCenteredString(this.fontRenderer, this.errorDetail, this.width / 2, this.height / 2 - 10, 16777215);
		super.drawScreen(var1, var2, var3);
		//Spout Start
		ReconnectManager.teleport(this.mc);
		//Spout End
	}
}
