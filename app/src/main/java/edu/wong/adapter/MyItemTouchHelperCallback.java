package edu.wong.adapter;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperInterface adapter;
    RecyclerView.ViewHolder viewHolder;
    OnItemSwipeListener listener;
    //限制ImageView长度所能增加的最大值
    private double ICON_MAX_SIZE = 50;
    //ImageView的初始长宽
    private int fixedWidth = 150;

    public MyItemTouchHelperCallback(ItemTouchHelperInterface adapter, OnItemSwipeListener listener) {
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dargFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//允许上下拖动
        int sawpFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //允许左滑
        this.viewHolder = viewHolder;
        return makeMovementFlags(dargFlags, sawpFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        adapter.onItemDissmiss(viewHolder.getAdapterPosition());
    }

    //重写其他方法
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //重写方法实现侧滑出现提示
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //防止复用导致显示问题
        viewHolder.itemView.setScrollX(0);
        ((MyRecyclerViewAdapter.MyViewHolder) viewHolder).tvRemove.setText("不批准");
        ((MyRecyclerViewAdapter.MyViewHolder) viewHolder).tvAccept.setText("批准");

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //仅对侧滑状态下的效果做出改变
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
            if (Math.abs(dX) <= getSlideLimitation(viewHolder)) {
                viewHolder.itemView.scrollTo(-(int) dX, 0);
            }
        } else {
            //拖拽状态下不做改变，需要调用父类的方法
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    /**
     * 获取删除方块的宽度
     */
    public int getSlideLimitation(RecyclerView.ViewHolder viewHolder) {
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
        return viewGroup.getChildAt(0).getLayoutParams().width;
    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        if (animationType == ItemTouchHelper.DOWN) {
            if (animateDx < 0) {
                listener.onItemLeftSwipe(viewHolder.itemView, viewHolder.getLayoutPosition());
                Log.d("----------", "onSelectedChanged: " + "左滑完成");
            } else if (animateDx > 0) {
                listener.onItemRightSwipe(viewHolder.itemView, viewHolder.getLayoutPosition());
                Log.d("----------", "onSelectedChanged: " + "右滑完成");

            }
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    //自定义监听回调
    public interface OnItemSwipeListener {
        void onItemLeftSwipe(View view, int position);

        void onItemRightSwipe(View view, int position);
    }
}
