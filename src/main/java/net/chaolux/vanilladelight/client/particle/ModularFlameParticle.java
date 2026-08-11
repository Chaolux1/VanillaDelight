package net.chaolux.vanilladelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ModularFlameParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float size;

    protected ModularFlameParticle(ClientLevel clientLevel, double x, double y, double z, double red, double green, double blue, SpriteSet spriteSet) {
        super(clientLevel,x,y,z,0.0,0.0,0.0);
        this.spriteSet=spriteSet;
        this.rCol= Mth.clamp((float) red,0.0f,1.0f);
        this.gCol= Mth.clamp((float) green,0.0f,1.0f);
        this.bCol= Mth.clamp((float) blue,0.0f,1.0f);
        this.alpha=0.95f;
        this.lifetime=8 + this.random.nextInt(4);
        this.size=0.14f + this.random.nextFloat() * 0.04f;
        this.quadSize=this.size;
        this.xd=(this.random.nextDouble() - 0.5) * 0.0007;
        this.yd=0.0007 + this.random.nextDouble() * 0.0011;
        this.zd=(this.random.nextDouble() - 0.5) * 0.0007;
        this.hasPhysics=false;
        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        this.xo=this.x;
        this.yo=this.y;
        this.zo=this.z;
        if(this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.move(this.xd,this.yd,this.zd);
        this.xd *= 0.8;
        this.yd *= 0.82;
        this.zd *= 0.8;
        float progress=(float) this.age / (float) this.lifetime;
        this.alpha=0.95f * (1.0f - progress * 0.72f);
        this.quadSize=this.size * (1.0f - progress * 0.1f);
    }

    @Override
    protected int getLightColor(float particle) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet=spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType,ClientLevel clientLevel,double x,double y,double z,double red,double green,double blue) {
            return new ModularFlameParticle(clientLevel,x,y,z,red,green,blue,this.spriteSet);
        }
    }
}
