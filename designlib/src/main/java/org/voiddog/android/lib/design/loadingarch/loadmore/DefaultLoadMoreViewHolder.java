package org.voiddog.android.lib.design.loadingarch.loadmore;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.voiddog.android.lib.design.R;

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
 * @author qigengxin
 * @since 2018-05-12 20:01
 */
public class DefaultLoadMoreViewHolder extends LoadMoreWrapper.LoadMoreViewHolder {

    private TextView txtLoadMore;

    public DefaultLoadMoreViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.void_design_layout_default_load_more, parent, false));
        txtLoadMore = itemView.findViewById(R.id.txt_content);
    }

    @Override
    public void setOnErrorViewClickListener(final View.OnClickListener clickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getState().status == State.ERROR) {
                    clickListener.onClick(v);
                }
            }
        });
    }

    @Override
    protected void updateState(@NonNull State oldState, @NonNull State newState) {
        CharSequence desc = newState.content;
        switch (newState.status) {
            case State.COMPLETE:
                desc = desc == null ? itemView.getResources().getString(R.string.void_design_default_load_more_complete) : desc;
                break;
            case State.ERROR:
                desc = desc == null ? itemView.getResources().getString(R.string.void_design_default_load_more_error) : desc;
                break;
            case State.LOADING:
                desc = itemView.getResources().getString(R.string.void_design_default_load_more_loading);
        }
        txtLoadMore.setText(desc);
    }
}
