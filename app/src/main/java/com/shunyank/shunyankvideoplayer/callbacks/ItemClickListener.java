package com.shunyank.shunyankvideoplayer.callbacks;

public interface ItemClickListener {

    void  onItemClick(Object item);
    void  onMarkAsComplete(Object item,int pos);
    default void  onRemoveAsComplete(Object item, int pos){

    }

}
