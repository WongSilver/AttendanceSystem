package edu.wong.adapter;

public interface ItemTouchHelperInterface {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);

    //删除
    void onItemDissmiss(int position);
}
