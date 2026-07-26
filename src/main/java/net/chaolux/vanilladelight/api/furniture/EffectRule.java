package net.chaolux.vanilladelight.api.furniture;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record EffectRule(int tint, float shade, float alpha, boolean emissive, @Nullable ResourceLocation overlay,float metallic,float pulseSpeed,float pulseMin,float pulseMax,float scrollU,float scrollV) {
    public static final EffectRule NONE=new EffectRule(0xFFFFFFFF,1.0f,1.0f,false,null,0.0f,0.0f,1.0f,1.0f,0.0f,0.0f);

    public EffectRule {
        shade=Math.max(0.0f,shade);
        alpha=clamp(alpha);
        metallic=clamp(metallic);
        pulseSpeed=Math.max(0.0f,pulseSpeed);
        pulseMin=clamp(pulseMin);
        pulseMax=clamp(pulseMax);
        if(pulseMax < pulseMin) {
            float value=pulseMin;
            pulseMin=pulseMax;
            pulseMax=value;
        }
    }

    public boolean animated() {
        return this.pulseSpeed > 0.0f || this.scrollU != 0.0f || this.scrollV != 0.0f;
    }

    private static float clamp(float value) {
        return Math.max(0.0f,Math.min(1.0f,value));
    }

    public float pulse(long tick,float partialTick) {
        if(this.pulseSpeed <= 0.0f) return 1.0f;
        double phase=(tick+partialTick) * this.pulseSpeed;
        float normalized=(float) ((Math.sin(phase)+1.0)*0.5);
        return this.pulseMin + (this.pulseMax - this.pulseMin) * normalized;
    }

}
