package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCache;
import net.minecraft.src.Entity;
import net.minecraft.src.ICamera;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class WorldRenderer {

	public World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	public int posX;
	public int posY;
	public int posZ;
	public int sizeWidth;
	public int sizeHeight;
	public int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean[] skipRenderPass = new boolean[2];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public float rendererRadius;
	public boolean needsUpdate;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	private boolean isInitialized = false;
	public List tileEntityRenderers = new ArrayList();
	private List tileEntities;
	//Spout Start
	public boolean isVisibleFromPosition = false;
	public double visibleFromX;
	public double visibleFromY;
	public double visibleFromZ;
	private boolean needsBoxUpdate = false;
	public boolean isInFrustrumFully = false;
	//Spout End

	public WorldRenderer(World var1, List var2, int var3, int var4, int var5, int var6, int var7) {
		this.worldObj = var1;
		this.tileEntities = var2;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = var6;
		this.rendererRadius = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
		this.glRenderList = var7;
		this.posX = -999;
		this.setPosition(var3, var4, var5);
		this.needsUpdate = false;
	}

	public void setPosition(int var1, int var2, int var3) {
		if(var1 != this.posX || var2 != this.posY || var3 != this.posZ) {
			this.setDontDraw();
			this.posX = var1;
			this.posY = var2;
			this.posZ = var3;
			this.posXPlus = var1 + this.sizeWidth / 2;
			this.posYPlus = var2 + this.sizeHeight / 2;
			this.posZPlus = var3 + this.sizeDepth / 2;
			this.posXClip = var1 & 1023;
			this.posYClip = var2;
			this.posZClip = var3 & 1023;
			this.posXMinus = var1 - this.posXClip;
			this.posYMinus = var2 - this.posYClip;
			this.posZMinus = var3 - this.posZClip;
			//Spout Start
			float var4 = 0.0F;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)var1 - var4), (double)((float)var2 - var4), (double)((float)var3 - var4), (double)((float)(var1 + this.sizeWidth) + var4), (double)((float)(var2 + this.sizeHeight) + var4), (double)((float)(var3 + this.sizeDepth) + var4));
			this.needsBoxUpdate = true;
			this.markDirty();
			this.isVisibleFromPosition = false;
			//Spout End
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
	}

	public void updateRenderer() {
		//Spout Start
		if(this.needsUpdate) {
			++chunksUpdated;
			if(this.needsBoxUpdate) {
				float var1 = 0.0F;
				GL11.glNewList(this.glRenderList + 2, 4864 /*GL_COMPILE*/);
				RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - var1), (double)((float)this.posYClip - var1), (double)((float)this.posZClip - var1), (double)((float)(this.posXClip + this.sizeWidth) + var1), (double)((float)(this.posYClip + this.sizeHeight) + var1), (double)((float)(this.posZClip + this.sizeDepth) + var1)));
				GL11.glEndList();
				this.needsBoxUpdate = false;
			}

			this.isVisible = true;
			this.isVisibleFromPosition = false;
			int var22 = this.posX;
			int var2 = this.posY;
			int var3 = this.posZ;
			int var4 = this.posX + this.sizeWidth;
			int var5 = this.posY + this.sizeHeight;
			int var6 = this.posZ + this.sizeDepth;

			for(int var7 = 0; var7 < 2; ++var7) {
				this.skipRenderPass[var7] = true;
			}

			Object var23 = Config.getFieldValue("LightCache", "cache");
			if(var23 != null) {
				Config.callVoid(var23, "clear", new Object[0]);
				Config.callVoid("BlockCoord", "resetPool", new Object[0]);
			}

			Chunk.isLit = false;
			HashSet var8 = new HashSet();
			var8.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			byte var9 = 1;
			ChunkCache var10 = new ChunkCache(this.worldObj, var22 - var9, var2 - var9, var3 - var9, var4 + var9, var5 + var9, var6 + var9);
			RenderBlocks var11 = new RenderBlocks(var10);

			for(int var12 = 0; var12 < 2; ++var12) {
				boolean var13 = false;
				boolean var14 = false;
				boolean var15 = false;

				for(int var16 = var2; var16 < var5; ++var16) {
					for(int var17 = var3; var17 < var6; ++var17) {
						for(int var18 = var22; var18 < var4; ++var18) {
							int var19 = var10.getBlockId(var18, var16, var17);
							if(var19 > 0) {
								if(!var15) {
									var15 = true;
									GL11.glNewList(this.glRenderList + var12, 4864 /*GL_COMPILE*/);
									tessellator.isLoadingChunk = true;
									tessellator.startDrawingQuads();
								}

								if(var12 == 0 && Block.isBlockContainer[var19]) {
									TileEntity var20 = var10.getBlockTileEntity(var18, var16, var17);
									if(TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
										this.tileEntityRenderers.add(var20);
									}
								}

								Block var25 = Block.blocksList[var19];
								int var21 = var25.getRenderBlockPass();
								if(var21 != var12) {
									var13 = true;
								} else if(var21 == var12) {
									var14 |= var11.renderBlockByRenderType(var25, var18, var16, var17);
								}
							}
						}
					}
				}

				if(var15) {
					tessellator.draw();
					GL11.glEndList();
					tessellator.isLoadingChunk = false;
				} else {
					var14 = false;
				}

				if(var14) {
					this.skipRenderPass[var12] = false;
				}

				if(!var13) {
					break;
				}
			}

			HashSet var24 = new HashSet();
			var24.addAll(this.tileEntityRenderers);
			var24.removeAll(var8);
			this.tileEntities.addAll(var24);
			var8.removeAll(this.tileEntityRenderers);
			//Spout End
			this.tileEntities.removeAll(var8);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
		}
	}

	public float distanceToEntitySquared(Entity var1) {
		float var2 = (float)(var1.posX - (double)this.posXPlus);
		float var3 = (float)(var1.posY - (double)this.posYPlus);
		float var4 = (float)(var1.posZ - (double)this.posZPlus);
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public void setDontDraw() {
		for(int var1 = 0; var1 < 2; ++var1) {
			this.skipRenderPass[var1] = true;
		}

		this.isInFrustum = false;
		this.isInitialized = false;
	}

	public void func_1204_c() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int var1) {
		return !this.isInFrustum?-1:(!this.skipRenderPass[var1]?this.glRenderList + var1:-1);
	}

	public void updateInFrustrum(ICamera var1) {
		this.isInFrustum = var1.isBoundingBoxInFrustum(this.rendererBoundingBox);
		//Spout Start
		if(this.isInFrustum && Config.isOcclusionEnabled() && Config.isOcclusionFancy()) {
			this.isInFrustrumFully = var1.isBoundingBoxInFrustumFully(this.rendererBoundingBox);
		} else {
			this.isInFrustrumFully = false;
		}
		//Spout End
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
		return !this.isInitialized?false:this.skipRenderPass[0] && this.skipRenderPass[1];
	}

	public void markDirty() {
		this.needsUpdate = true;
	}

}
