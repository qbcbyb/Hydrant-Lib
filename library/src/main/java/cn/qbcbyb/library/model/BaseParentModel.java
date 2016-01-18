package cn.qbcbyb.library.model;

import java.util.List;

public abstract class BaseParentModel<T extends BaseModel> extends BaseModel {

    private List<T> children;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
