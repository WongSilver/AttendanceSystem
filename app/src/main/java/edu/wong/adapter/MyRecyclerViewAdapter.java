package edu.wong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.wong.activity.R;
import edu.wong.entity.Leave;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> implements ItemTouchHelperInterface {
    private Context context;
    private List<Leave> list;
    private View inflate;

    //构造方法传递参数
    public MyRecyclerViewAdapter(Context context, List<Leave> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //创建ViewHolder，返回每一项的布局
        inflate = LayoutInflater.from(context).inflate(R.layout.home_list, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Leave leave = list.get(i);
        //数据与控件绑定
        myViewHolder.tvName.setText(leave.getPs());
        myViewHolder.tvStart.setText(leave.getStart());
        myViewHolder.tvEnd.setText(leave.getEnd());
        myViewHolder.tvType.setText(leave.getType());
        myViewHolder.tvStatus.setText(String.valueOf(leave.getStatus()));
        myViewHolder.tvRemark.setText(leave.getRemark());
        myViewHolder.tvID.setText(String.valueOf(leave.getId()));
    }


    @Override
    public int getItemCount() {
        //返回控件总数
        return list.size();
    }


    /**
     * 下面两个方法实现接口
     *
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {

//        交换位置
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //删除数据
        list.remove(position);
        notifyItemRemoved(position);
    }


    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRemark;
        TextView tvStatus;
        TextView tvType;
        TextView tvEnd;
        TextView tvStart;
        TextView tvAccept;
        TextView tvRemove;
        TextView tvID;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.leave_tvName);
            tvStart = itemView.findViewById(R.id.leave_tvStart);
            tvEnd = itemView.findViewById(R.id.leave_tvEnd);
            tvType = itemView.findViewById(R.id.leave_tvType);
            tvStatus = itemView.findViewById(R.id.leave_tvStatus);
            tvRemark = itemView.findViewById(R.id.leave_tvRemark);
            tvAccept = itemView.findViewById(R.id.leave_tvAccept);
            tvRemove = itemView.findViewById(R.id.leave_tvRemove);
            tvID = itemView.findViewById(R.id.leave_tvID);
        }
    }

}
