package org.voiddog.android.lib.base.recycler.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * ┏┛ ┻━━━━━┛ ┻┓
 * ┃　　　　　　 ┃
 * ┃　　　━　　　┃
 * ┃　┳┛　  ┗┳　┃
 * ┃　　　　　　 ┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　 ┃
 * ┗━┓　　　┏━━━┛
 * * ┃　　　┃   神兽保佑
 * * ┃　　　┃   代码无BUG！
 * * ┃　　　┗━━━━━━━━━┓
 * * ┃　　　　　　　    ┣┓
 * * ┃　　　　         ┏┛
 * * ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
 * * * ┃ ┫ ┫   ┃ ┫ ┫
 * * * ┗━┻━┛   ┗━┻━┛
 *
 * 列表类型的多类型 adapter
 *
 * @author qigengxin
 * @since 2018-07-24 11:40
 */
public class ListMultiTypeBindAdapter<T> extends MultiTypeBindAdapter<T> {

    @Nullable
    private List<T> dataList;

    @NonNull
    public List<T> getDataList() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        return dataList;
    }

    public void add(@NonNull T data) {
        int oldPosition = getDataList().size();
        getDataList().add(data);
        notifyItemInserted(oldPosition);
    }

    public void add(List<T> dataList) {
        add(getDataList().size(), dataList);
    }

    public void add(int index, List<T> dataList) {
        if (dataList == null) {
            return;
        }
        getDataList().addAll(dataList);
        notifyItemRangeInserted(index, dataList.size());
    }

    public void set(List<T> dataList) {
        getDataList().clear();
        if (dataList != null) {
            getDataList().addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void remove(T data) {
        int position = getDataList().indexOf(data);
        remove(position);
    }

    public void remove(int index) {
        if (index >= 0 && index < getDataList().size()) {
            getDataList().remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public T getItemAtIndex(int position) {
        return getDataList().get(position);
    }

    @Override
    public int getItemCount() {
        return getDataList().size();
    }
}
