package com.oilpipelinemodel.app.Utils;

import java.util.Objects;

public final class PairPosition {
    private final int branch;
    private final int branchPosition;


    public static PairPosition newInstance(int branch, int branchPosition){
        return new PairPosition(branch, branchPosition);
    }

    private PairPosition(int branch, int branchPosition){
        this.branch=branch;
        this.branchPosition=branchPosition;
    }

    public int getBranch() {
        return branch;
    }

    public int getBranchPosition() {
        return branchPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairPosition that = (PairPosition) o;
        return branch == that.branch &&
                branchPosition == that.branchPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(branch, branchPosition);
    }

    @Override
    public String toString() {
        return "PairPosition{" +
                "branch=" + branch +
                ", branchPosition=" + branchPosition +
                '}';
    }
}
