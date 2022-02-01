package com.chillaxmc.psychedelicraftv2.blocks;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockIronDryingTable extends BlockDryingTable {

    public BlockIronDryingTable() {
        super(Material.iron);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        if (par1 == 0) return bottomIcon;
        if (par1 == 1) return topIcon;
        return super.getIcon(par1, par2);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);

        bottomIcon = par1IconRegister.registerIcon(Psychedelicraftv2.modBase + "dryingTableBottomIron");
        topIcon = par1IconRegister.registerIcon(Psychedelicraftv2.modBase + "dryingTableTop");
    }

}
