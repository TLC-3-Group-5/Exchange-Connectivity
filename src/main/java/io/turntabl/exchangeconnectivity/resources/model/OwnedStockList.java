package io.turntabl.exchangeconnectivity.resources.model;

import java.util.List;

public class OwnedStockList {
    private List<OwnedStock> ownedStockList;

    public OwnedStockList(List<OwnedStock> ownedStockList) {
        this.ownedStockList = ownedStockList;
    }

    public OwnedStockList() {
    }

    public List<OwnedStock> getOwnedStockList() {
        return ownedStockList;
    }

    public void setOwnedStockList(List<OwnedStock> ownedStockList) {
        this.ownedStockList = ownedStockList;
    }
}
