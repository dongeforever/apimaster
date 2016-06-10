package com.dova.apimaster.common.domain;

import java.util.List;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class UnitInject {

    private int fromUnitId;
    private Expression injectExp;


    public int getFromUnitId() {
        return fromUnitId;
    }

    public void setFromUnitId(int fromUnitId) {
        this.fromUnitId = fromUnitId;
    }

    public Expression getInjectExp() {
        return injectExp;
    }

    public void setInjectExp(Expression injectExp) {
        this.injectExp = injectExp;
    }



    public UnitInject fromUnitId(int fromUnitId){
        this.fromUnitId = fromUnitId;
        return this;
    }
    public UnitInject injectExp(Expression injectExp){
        this.injectExp = injectExp;
        return this;
    }
}
