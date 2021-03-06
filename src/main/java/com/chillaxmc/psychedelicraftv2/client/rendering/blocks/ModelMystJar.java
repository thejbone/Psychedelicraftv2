/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

// Date: 9-3-2014 13:41:43
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package com.chillaxmc.psychedelicraftv2.client.rendering.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMystJar extends ModelBase
{
    //fields
    ModelRenderer glass1;
    ModelRenderer rope;
    ModelRenderer glass2;
    ModelRenderer knot;
    ModelRenderer cork;
    ModelRenderer glass3;

    public ModelMystJar()
    {
        textureWidth = 64;
        textureHeight = 32;

        glass1 = new ModelRenderer(this, 0, 0);
        glass1.addBox(-4F, 0F, -4F, 8, 5, 8);
        glass1.setRotationPoint(0F, 19F, 0F);
        glass1.setTextureSize(64, 32);
        glass1.mirror = true;
        setRotation(glass1, 0F, 0F, 0F);
        rope = new ModelRenderer(this, 33, 0);
        rope.addBox(-3.5F, 0F, -3.5F, 7, 2, 7);
        rope.setRotationPoint(0F, 17F, 0F);
        rope.setTextureSize(64, 32);
        rope.mirror = true;
        setRotation(rope, 0F, 0F, 0F);
        glass2 = new ModelRenderer(this, 0, 14);
        glass2.addBox(-4F, 0F, -4F, 8, 5, 8);
        glass2.setRotationPoint(0F, 12F, 0F);
        glass2.setTextureSize(64, 32);
        glass2.mirror = true;
        setRotation(glass2, 0F, 0F, 0F);
        knot = new ModelRenderer(this, 33, 2);
        knot.addBox(0F, 0F, -4F, 0, 5, 8);
        knot.setRotationPoint(3.5F, 17F, 0F);
        knot.setTextureSize(64, 32);
        knot.mirror = true;
        setRotation(knot, 0F, 0F, -0.2602503F);
        cork = new ModelRenderer(this, 33, 16);
        cork.addBox(-3F, 0F, -3F, 6, 2, 6);
        cork.setRotationPoint(0F, 10F, 0F);
        cork.setTextureSize(64, 32);
        cork.mirror = true;
        setRotation(cork, 0F, 0F, 0F);
        glass3 = new ModelRenderer(this, 33, 24);
        glass3.addBox(-3F, 0F, -3F, 6, 2, 6);
        glass3.setRotationPoint(0F, 17F, 0F);
        glass3.setTextureSize(64, 32);
        glass3.mirror = true;
        setRotation(glass3, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        glass1.render(f5);
        rope.render(f5);
        glass2.render(f5);
        knot.render(f5);
        cork.render(f5);
        glass3.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);

        cork.rotationPointX = par7Entity.rotationYaw * 2.0f;
        cork.rotateAngleY = par7Entity.rotationYaw * 0.1f;

        knot.rotateAngleZ = -0.2602503F - par7Entity.rotationPitch * 0.5f;
    }
}
