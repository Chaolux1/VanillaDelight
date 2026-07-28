package net.chaolux.vanilladelight.client.model.patternedcabinet;

public record PatternedCabinetPatternSet(PatternedCabinetControlMap close,PatternedCabinetControlMap open) {
    public PatternedCabinetControlMap cabinetControlMap(boolean controlMap) {
        return controlMap ? this.open : this.close;
    }
}
