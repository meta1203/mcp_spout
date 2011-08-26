package net.minecraft.src;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.EnumOptions;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;

public class GuiDetailSettingsOF extends GuiScreen {

	private GuiScreen prevScreen;
	protected String title = "Detail Settings";
	private GameSettings settings;
	private static EnumOptions[] enumOptions = new EnumOptions[]{EnumOptions.CLOUDS, EnumOptions.CLOUD_HEIGHT, EnumOptions.TREES, EnumOptions.GRASS, EnumOptions.WATER, EnumOptions.RAIN, EnumOptions.SKY, EnumOptions.STARS, EnumOptions.BETTER_GRASS, EnumOptions.WEATHER, EnumOptions.AUTOSAVE_TICKS, EnumOptions.FAST_DEBUG_INFO, EnumOptions.CHUNK_UPDATES, EnumOptions.CHUNK_UPDATES_DYNAMIC, EnumOptions.FAR_VIEW, EnumOptions.TIME, EnumOptions.CLEAR_WATER};
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;


	public GuiDetailSettingsOF(GuiScreen var1, GameSettings var2) {
		this.prevScreen = var1;
		this.settings = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		int var2 = 0;
		EnumOptions[] var3 = enumOptions;
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			EnumOptions option = var3[var5];
			int var7 = this.width / 2 - 155 + var2 % 2 * 160;
			int var8 = this.height / 6 + 21 * (var2 / 2) - 10;
			if(!option.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(option.returnEnumOrdinal(), var7, var8, option, this.settings.getKeyBinding(option)));
			} else {
				this.controlList.add(new GuiSlider(option.returnEnumOrdinal(), var7, var8, option, this.settings.getKeyBinding(option), this.settings.getOptionFloatValue(option)));
			}
			((GuiButton)controlList.get(controlList.size() - 1)).enabled = SpoutClient.getInstance().isCheatMode() || !option.isVisualCheating();

			++var2;
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, var1.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100 && var1 instanceof GuiSmallButton) {
				this.settings.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
				var1.displayString = this.settings.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
			}

			if(var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.prevScreen);
			}

			if(var1.id != EnumOptions.CLOUD_HEIGHT.ordinal()) {
				ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				this.setWorldAndResolution(this.mc, var3, var4);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
		if(var1 == this.lastMouseX && var2 == this.lastMouseY) {
			short var4 = 700;
			if(System.currentTimeMillis() >= this.mouseStillTime + (long)var4) {
				int var5 = this.width / 2 - 150;
				int var6 = this.height / 6 - 5;
				if(var2 <= var6 + 98) {
					var6 += 105;
				}

				int var7 = var5 + 150 + 150;
				int var8 = var6 + 84 + 10;
				GuiButton var9 = this.getSelectedButton(var1, var2);
				if(var9 != null) {
					String var10 = this.getButtonName(var9.displayString);
					String[] var11 = this.getTooltipLines(var10);
					if(var11 == null) {
						return;
					}

					this.drawGradientRect(var5, var6, var7, var8, -536870912, -536870912);

					for(int var12 = 0; var12 < var11.length; ++var12) {
						String var13 = var11[var12];
						this.fontRenderer.drawStringWithShadow(var13, var5 + 5, var6 + 5 + var12 * 11, 14540253);
					}
				}

			}
		} else {
			this.lastMouseX = var1;
			this.lastMouseY = var2;
			this.mouseStillTime = System.currentTimeMillis();
		}
	}

	private String[] getTooltipLines(String var1) {
		return var1.equals("Clouds")?new String[]{"Clouds", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "  OFF - no clouds, fastest", "Fast clouds are rendered 2D.", "Fancy clouds are rendered 3D."}:(var1.equals("Cloud Height")?new String[]{"Cloud Height", "  OFF - default height", "  100% - above world height limit"}:(var1.equals("Trees")?new String[]{"Trees", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast trees have opaque leaves.", "Fancy trees have transparent leaves."}:(var1.equals("Grass")?new String[]{"Grass", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast grass uses default side texture.", "Fancy grass uses biome side texture."}:(var1.equals("Water")?new String[]{"Water", "  Default - as set by setting Graphics", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Fast water (1 pass) has some visual artifacts", "Fancy water (2 pass) has no visual artifacts"}:(var1.equals("Rain & Snow")?new String[]{"Rain & Snow", "  Default - as set by setting Graphics", "  Fast  - light rain/snow, faster", "  Fancy - heavy rain/snow, slower", "  OFF - no rain/snow, fastest", "When rain is OFF the splashes and rain sounds", "are still active."}:(var1.equals("Sky")?new String[]{"Sky", "  ON - sky is visible, slower", "  OFF  - sky is not visible, faster", "When sky is OFF the moon and sun are still visible."}:(var1.equals("Stars")?new String[]{"Stars", "  ON - stars are visible, slower", "  OFF  - stars are not visible, faster"}:(var1.equals("Better Grass")?new String[]{"Better Grass", "  OFF - default side grass texture, fastest", "  Fast - full side grass texture, slower", "  Fancy - dynamic side grass texture, slowest"}:(var1.equals("Weather")?new String[]{"Weather", "  ON - weather is active, slower", "  OFF  - weather is not active, faster", "The weather controls rain, snow and thunderstorms."}:(var1.equals("Autosave")?new String[]{"Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death."}:(var1.equals("Fast Debug Info")?new String[]{"Fast Debug Info", " OFF - default debug info screen, slower", " ON - debug info screen without lagometer, faster", "Removes the lagometer from the debug screen (F3)."}:(var1.equals("Chunk Updates")?new String[]{"Chunk updates per frame", " 1 - (default) slower world loading, higher FPS", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS"}:(var1.equals("Dynamic Updates")?new String[]{"Chunk updates per frame", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster."}:(var1.equals("Far View")?new String[]{"Far View", " OFF - (default) standard view distance", " ON - 3x view distance", "Far View is very resource demanding!", "3x view distance => 9x chunks to be loaded => FPS / 9", "Standard view distances: 32, 64, 128, 256", "Far view distances: 96, 192, 384, 512"}:(var1.equals("Time")?new String[]{"Time", " Default - normal day/night cycles", " Day Only - day only", " Night Only - night only"}:null)))))))))))))));
	}

	private String getButtonName(String var1) {
		int var2 = var1.indexOf(58);
		return var2 < 0?var1:var1.substring(0, var2);
	}

	private GuiButton getSelectedButton(int var1, int var2) {
		for(int var3 = 0; var3 < this.controlList.size(); ++var3) {
			GuiButton var4 = (GuiButton)this.controlList.get(var3);
			boolean var5 = var1 >= var4.xPosition && var2 >= var4.yPosition && var1 < var4.xPosition + var4.width && var2 < var4.yPosition + var4.height;
			if(var5) {
				return var4;
			}
		}

		return null;
	}

}
