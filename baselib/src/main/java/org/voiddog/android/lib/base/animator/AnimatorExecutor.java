package org.voiddog.android.lib.base.animator;

import android.animation.Animator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数值动画执行器
 *
 * @author qigengxin
 * @since 2017-03-02 14:04
 */


public class AnimatorExecutor implements Animator.AnimatorListener{

    private BlockingQueue<Animator> animatorList = new LinkedBlockingQueue<>();
    private Animator currentAnimator;

    public void pushAnimator(Animator animator){
        animatorList.offer(animator);
        playNext(false, false);
    }

    /**
     * 播放下一个动画
     * @param force 是否暴力执行
     * @param cancelBefore 如果是暴力执行，是否取消前一个动画
     * @return 是否成功播放下一个动画
     */
    public boolean playNext(boolean force, boolean cancelBefore){
        Animator nextAnimator = null;
        if(!force && currentAnimator == null){
            nextAnimator = animatorList.poll();
        } else if (force){
            if(cancelBefore && currentAnimator != null){
                currentAnimator.cancel();
            }
            nextAnimator = animatorList.poll();
        }

        if(nextAnimator != null){
            nextAnimator.start();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        currentAnimator = animation;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(currentAnimator == animation){
            currentAnimator = null;
            playNext(false, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if(currentAnimator == animation){
            currentAnimator = null;
            playNext(false, false);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {}
}
