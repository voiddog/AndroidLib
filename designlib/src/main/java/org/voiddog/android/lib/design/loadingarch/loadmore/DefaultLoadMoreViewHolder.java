package org.voiddog.android.lib.design.loadingarch.loadmore;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Test;

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
    private ImageView imgIcon;
    private Animation loadingAnimation;

    public DefaultLoadMoreViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.void_design_layout_default_load_more, parent, false));
        txtLoadMore = itemView.findViewById(R.id.txt_load_more);
        imgIcon = itemView.findViewById(R.id.img_icon);
        loadingAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.void_design_loading_rotation);
        loadingAnimation.setInterpolator(new LinearInterpolator());
        loadingAnimation.setRepeatCount(-1);
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
                imgIcon.clearAnimation();
                imgIcon.setVisibility(View.GONE);
                desc = TextUtils.isEmpty(desc) ? itemView.getResources().getString(R.string.void_design_default_load_more_complete) : desc;
                break;
            case State.ERROR:
                imgIcon.clearAnimation();
                imgIcon.setVisibility(View.GONE);
                desc = TextUtils.isEmpty(desc) ? itemView.getResources().getString(R.string.void_design_default_load_more_error) : desc;
                break;
            case State.NORMAL:
            case State.LOADING:
                imgIcon.setVisibility(View.VISIBLE);
                imgIcon.startAnimation(loadingAnimation);
                desc = itemView.getResources().getString(R.string.void_design_default_load_more_loading);
                break;
        }
        txtLoadMore.setText(desc);
    }
}
