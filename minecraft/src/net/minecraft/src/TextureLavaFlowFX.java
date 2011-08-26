package net.minecraft.src;
//Spout HD Start
import com.pclewis.mcpatcher.mod.TileSize;
//Spout HD End
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TextureFX;

public class TextureLavaFlowFX extends TextureFX {
//Spout HD Start
	protected float[] field_1143_g = new float[TileSize.int_numPixels];
	protected float[] field_1142_h = new float[TileSize.int_numPixels];
	protected float[] field_1141_i = new float[TileSize.int_numPixels];
	protected float[] field_1140_j = new float[TileSize.int_numPixels];
//Spout HD End
	int field_1139_k = 0;


	public TextureLavaFlowFX() {
		super(Block.lavaMoving.blockIndexInTexture + 1);
		this.tileSize = 2;
	}

	public void onTick() {
		++this.field_1139_k;

		int var2;
		float var3;
		int var5;
		int var6;
		int var7;
		int var8;
		int var9;
//Spout HD Start
		for(int var1 = 0; var1 < TileSize.int_size; ++var1) {
			for(var2 = 0; var2 < TileSize.int_size; ++var2) {
//Spout HD End
				var3 = 0.0F;
				int var4 = (int)(MathHelper.sin((float)var2 * 3.1415927F * 2.0F / 16.0F) * 1.2F);
				var5 = (int)(MathHelper.sin((float)var1 * 3.1415927F * 2.0F / 16.0F) * 1.2F);

				for(var6 = var1 - 1; var6 <= var1 + 1; ++var6) {
					for(var7 = var2 - 1; var7 <= var2 + 1; ++var7) {
//Spout HD Start
						var8 = var6 + var4 & TileSize.int_sizeMinus1;
						var9 = var7 + var5 & TileSize.int_sizeMinus1;
						var3 += this.field_1143_g[var8 + var9 * TileSize.int_size];
//Spout HD End
					}
				}
//Spout HD Start
				this.field_1142_h[var1 + var2 * TileSize.int_size] = var3 / 10.0F + (this.field_1141_i[(var1 + 0 & TileSize.int_sizeMinus1) + (var2 + 0 & TileSize.int_sizeMinus1) * TileSize.int_size] + this.field_1141_i[(var1 + 1 & TileSize.int_sizeMinus1) + (var2 + 0 & TileSize.int_sizeMinus1) * TileSize.int_size] + this.field_1141_i[(var1 + 1 & TileSize.int_sizeMinus1) + (var2 + 1 & TileSize.int_sizeMinus1) * TileSize.int_size] + this.field_1141_i[(var1 + 0 & TileSize.int_sizeMinus1) + (var2 + 1 & TileSize.int_sizeMinus1) * TileSize.int_size]) / 4.0F * 0.8F;
				this.field_1141_i[var1 + var2 * TileSize.int_size] += this.field_1140_j[var1 + var2 * TileSize.int_size] * 0.01F;
				if(this.field_1141_i[var1 + var2 * TileSize.int_size] < 0.0F) {
					this.field_1141_i[var1 + var2 * TileSize.int_size] = 0.0F;
//Spout HD End
				}
//Spout HD Start
				this.field_1140_j[var1 + var2 * TileSize.int_size] -= 0.06F;
				if(Math.random() < 0.0050D) {
					this.field_1140_j[var1 + var2 * TileSize.int_size] = 1.5F;
//Spout HD End
				}
			}
		}

		float[] var11 = this.field_1142_h;
		this.field_1142_h = this.field_1143_g;
		this.field_1143_g = var11;
//Spout HD Start
		for(var2 = 0; var2 < TileSize.int_numPixels; ++var2) {
			var3 = this.field_1143_g[var2 - this.field_1139_k / 3 * TileSize.int_size & TileSize.int_numPixelsMinus1] * 2.0F;
//Spout HD End
			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			var5 = (int)(var3 * 100.0F + 155.0F);
			var6 = (int)(var3 * var3 * 255.0F);
			var7 = (int)(var3 * var3 * var3 * var3 * 128.0F);
			if(this.anaglyphEnabled) {
				var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
				var9 = (var5 * 30 + var6 * 70) / 100;
				int var10 = (var5 * 30 + var7 * 70) / 100;
				var5 = var8;
				var6 = var9;
				var7 = var10;
			}

			this.imageData[var2 * 4 + 0] = (byte)var5;
			this.imageData[var2 * 4 + 1] = (byte)var6;
			this.imageData[var2 * 4 + 2] = (byte)var7;
			this.imageData[var2 * 4 + 3] = -1;
		}

	}
}
