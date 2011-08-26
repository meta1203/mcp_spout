package net.minecraft.src;
//Spout HD Start
import com.pclewis.mcpatcher.mod.TileSize;
//Spout HD End
import net.minecraft.src.Block;
import net.minecraft.src.TextureFX;

public class TextureWaterFlowFX extends TextureFX {
//Spout HD Start
	protected float[] field_1138_g = new float[TileSize.int_numPixels];
	protected float[] field_1137_h = new float[TileSize.int_numPixels];
	protected float[] field_1136_i = new float[TileSize.int_numPixels];
	protected float[] field_1135_j = new float[TileSize.int_numPixels];
//Spout HD End
	private int field_1134_k = 0;


	public TextureWaterFlowFX() {
		super(Block.waterMoving.blockIndexInTexture + 1);
		this.tileSize = 2;
	}

	public void onTick() {
		++this.field_1134_k;

		int var1;
		int var2;
		float var3;
		int var5;
		int var6;
//BukkitContrib HD Start
		for(var1 = 0; var1 < TileSize.int_size; ++var1) {
			for(var2 = 0; var2 < TileSize.int_size; ++var2) {
//Spout HD End
				var3 = 0.0F;

				for(int var4 = var2 - 2; var4 <= var2; ++var4) {
//Spout HD Start
					var5 = var1 & TileSize.int_sizeMinus1;
					var6 = var4 & TileSize.int_sizeMinus1;
					var3 += this.field_1138_g[var5 + var6 * TileSize.int_size];
				}

				this.field_1137_h[var1 + var2 * TileSize.int_size] = var3 / 3.2F + this.field_1136_i[var1 + var2 * TileSize.int_size] * 0.8F;
//Spout HD End
			}
		}
//Spout HD Start
		for(var1 = 0; var1 < TileSize.int_size; ++var1) {
			for(var2 = 0; var2 < TileSize.int_size; ++var2) {
				this.field_1136_i[var1 + var2 * TileSize.int_size] += this.field_1135_j[var1 + var2 * TileSize.int_size] * 0.05F;
				if(this.field_1136_i[var1 + var2 * TileSize.int_size] < 0.0F) {
					this.field_1136_i[var1 + var2 * TileSize.int_size] = 0.0F;
				}

				this.field_1135_j[var1 + var2 * TileSize.int_size] -= 0.3F;
				if(Math.random() < 0.2D) {
					this.field_1135_j[var1 + var2 * TileSize.int_size] = 0.5F;
				}
			}
		}
//Spout HD End
		float[] var12 = this.field_1137_h;
		this.field_1137_h = this.field_1138_g;
		this.field_1138_g = var12;
//Spout HD Start
		for(var2 = 0; var2 < TileSize.int_numPixels; ++var2) {
			var3 = this.field_1138_g[var2 - this.field_1134_k * TileSize.int_size & TileSize.int_numPixelsMinus1];
//Spout HD End
			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			float var13 = var3 * var3;
			var5 = (int)(32.0F + var13 * 32.0F);
			var6 = (int)(50.0F + var13 * 64.0F);
			int var7 = 255;
			int var8 = (int)(146.0F + var13 * 50.0F);
			if(this.anaglyphEnabled) {
				int var9 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
				int var10 = (var5 * 30 + var6 * 70) / 100;
				int var11 = (var5 * 30 + var7 * 70) / 100;
				var5 = var9;
				var6 = var10;
				var7 = var11;
			}

			this.imageData[var2 * 4 + 0] = (byte)var5;
			this.imageData[var2 * 4 + 1] = (byte)var6;
			this.imageData[var2 * 4 + 2] = (byte)var7;
			this.imageData[var2 * 4 + 3] = (byte)var8;
		}

	}
}
