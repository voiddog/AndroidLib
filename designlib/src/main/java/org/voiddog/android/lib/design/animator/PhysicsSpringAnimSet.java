package org.voiddog.android.lib.design.animator;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.NonNull;
import android.view.View;

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
 * @since 2017-08-30 17:30
 */

public class PhysicsSpringAnimSet {

    public static final int TRANSLATION_X = 0;
    public static final int TRANSLATION_Y = 1;
    public static final int TRANSLATION_Z = 2;
    public static final int SCALE_X = 3;
    public static final int SCALE_Y = 4;
    public static final int ROTATION = 5;
    public static final int ROTATION_X = 6;
    public static final int ROTATION_Y = 7;
    public static final int X = 8;
    public static final int Y = 9;
    public static final int Z = 10;
    public static final int ALPHA = 11;
    public static final int SCROLL_X = 12;
    public static final int SCROLL_Y = 13;
    public static PhysicsSpringAnimSet getPhysicsAnimFromView(View view){
        Object o = view.getTag(R.id.void_design_view_tag_physics_spring_anim);
        if (o instanceof PhysicsSpringAnimSet){
            return (PhysicsSpringAnimSet) o;
        }
        return null;
    }

    /**
     * if the view contain PhysicsSpringAnimSet return it, otherwise create new and set it to view's tag
     * @param view
     * @return
     */
    public static PhysicsSpringAnimSet from(View view){
        PhysicsSpringAnimSet res = getPhysicsAnimFromView(view);
        if (res == null){
            res = new PhysicsSpringAnimSet(view);
        }
        return res;
    }

    /**
     * get the current spring anim in set if null ? return a new spring
     * @param index the anim index in animations {@link #TRANSLATION_X}
     * @return
     */
    @NonNull
    public SpringAnimation getAnimationByIndex(int index){
        if (index < 0 || index >= animations.length){
            throw new IllegalArgumentException("index out of animations' bounds");
        }
        SpringAnimation animation = animations[index];
        if (animation == null){
            animation = createNewSpringAnimation(index);
            animations[index] = animation;
        }
        return animation;
    }

    /**
     * apply damping ratio to all spring force
     * @param dampingRatio
     * @return
     */
    public PhysicsSpringAnimSet setDampingRatio(float dampingRatio){
        defaultDampingRatio = dampingRatio;
        for (SpringAnimation springAnimation : animations){
            if (springAnimation != null){
                SpringForce sf = springAnimation.getSpring();
                if (sf == null){
                    sf = new SpringForce();
                    springAnimation.setSpring(sf);
                }
                sf.setDampingRatio(dampingRatio);
            }
        }
        return this;
    }

    /**
     * apply damping stiffness to all spring force
     * @param stiffness
     * @return
     */
    public PhysicsSpringAnimSet setStiffness(float stiffness){
        defaultStiffness = stiffness;
        for (SpringAnimation springAnimation : animations){
            if (springAnimation != null){
                SpringForce sf = springAnimation.getSpring();
                if (sf == null){
                    sf = new SpringForce();
                    springAnimation.setSpring(sf);
                }
                sf.setStiffness(stiffness);
            }
        }
        return this;
    }

    /**
     * apply damping ratio to spring animation that the index in animations is index
     * @param index
     * @param damping
     * @return
     */
    public PhysicsSpringAnimSet setDampingRatio(int index, float damping){
        SpringAnimation springAnimation = getAnimationByIndex(index);
        SpringForce springForce = springAnimation.getSpring();
        if (springForce == null){
            springForce = new SpringForce();
            springAnimation.setSpring(springForce);
        }
        springForce.setDampingRatio(damping);
        return this;
    }

    /**
     * apply damping stiffness to spring animation that the index in animations is index
     * @param index
     * @param stiffness
     * @return
     */
    public PhysicsSpringAnimSet setStiffness(int index, float stiffness){
        SpringAnimation springAnimation = getAnimationByIndex(index);
        SpringForce springForce = springAnimation.getSpring();
        if (springForce == null){
            springForce = new SpringForce();
            springAnimation.setSpring(springForce);
        }
        springForce.setStiffness(stiffness);
        return this;
    }

    public PhysicsSpringAnimSet translationX(float endX){
        getAnimationByIndex(TRANSLATION_X).animateToFinalPosition(endX);
        return this;
    }

    public PhysicsSpringAnimSet translationY(float endY){
        getAnimationByIndex(TRANSLATION_Y).animateToFinalPosition(endY);
        return this;
    }

    public PhysicsSpringAnimSet translationZ(float endZ){
        getAnimationByIndex(TRANSLATION_Z).animateToFinalPosition(endZ);
        return this;
    }

    public PhysicsSpringAnimSet scale(float endScale){
        scaleX(endScale);
        scaleY(endScale);
        return this;
    }

    public PhysicsSpringAnimSet scaleX(float endX){
        getAnimationByIndex(SCALE_X).animateToFinalPosition(endX);
        return this;
    }

    public PhysicsSpringAnimSet scaleY(float endY){
        getAnimationByIndex(SCALE_Y).animateToFinalPosition(endY);
        return this;
    }

    public PhysicsSpringAnimSet rotation(float rotation){
        getAnimationByIndex(ROTATION).animateToFinalPosition(rotation);
        return this;
    }

    public PhysicsSpringAnimSet rotationX(float rotationX){
        getAnimationByIndex(ROTATION_X).animateToFinalPosition(rotationX);
        return this;
    }

    public PhysicsSpringAnimSet rotationY(float rotationY){
        getAnimationByIndex(ROTATION_Y).animateToFinalPosition(rotationY);
        return this;
    }

    public PhysicsSpringAnimSet x(float x){
        getAnimationByIndex(X).animateToFinalPosition(x);
        return this;
    }

    public PhysicsSpringAnimSet y(float y){
        getAnimationByIndex(Y).animateToFinalPosition(y);
        return this;
    }

    public PhysicsSpringAnimSet z(float z){
        getAnimationByIndex(Z).animateToFinalPosition(z);
        return this;
    }

    public PhysicsSpringAnimSet alpha(float alpha){
        getAnimationByIndex(ALPHA).animateToFinalPosition(alpha);
        return this;
    }

    public PhysicsSpringAnimSet scrollX(float scrollX){
        getAnimationByIndex(SCROLL_X).animateToFinalPosition(scrollX);
        return this;
    }

    public PhysicsSpringAnimSet scrollY(float scrollY){
        getAnimationByIndex(SCROLL_Y).animateToFinalPosition(scrollY);
        return this;
    }

    public PhysicsSpringAnimSet skipToEndAll(){
        for (SpringAnimation springAnimation : animations){
            if (springAnimation != null){
                springAnimation.skipToEnd();
            }
        }
        return this;
    }

    public PhysicsSpringAnimSet cancelAll(){
        for (SpringAnimation springAnimation : animations){
            if (springAnimation != null){
                springAnimation.cancel();
            }
        }
        return this;
    }

    private final View targetView;
    private final SpringAnimation[] animations = new SpringAnimation[14];
    // default spring force value
    private float defaultDampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY;
    private float defaultStiffness = SpringForce.STIFFNESS_MEDIUM;

    private PhysicsSpringAnimSet(View targetView) {
        this.targetView = targetView;
        targetView.setTag(R.id.void_design_view_tag_physics_spring_anim, this);
    }

    private SpringAnimation createNewSpringAnimation(int index){
        SpringAnimation res = new SpringAnimation(targetView, getValueHolderByIndex(index));
        SpringForce springForce = res.getSpring();
        if (springForce == null){
            springForce = new SpringForce();
            res.setSpring(springForce);
        }
        springForce.setDampingRatio(defaultDampingRatio);
        springForce.setStiffness(defaultStiffness);
        return res;
    }

    /**
     * @param index {@link #TRANSLATION_X}
     * @return
     */
    private DynamicAnimation.ViewProperty getValueHolderByIndex(int index){
        switch (index){
            case TRANSLATION_X:
                return DynamicAnimation.TRANSLATION_X;
            case TRANSLATION_Y:
                return DynamicAnimation.TRANSLATION_Y;
            case TRANSLATION_Z:
                return DynamicAnimation.TRANSLATION_Z;
            case SCALE_X:
                return DynamicAnimation.SCALE_X;
            case SCALE_Y:
                return DynamicAnimation.SCALE_Y;
            case ROTATION:
                return DynamicAnimation.ROTATION;
            case ROTATION_X:
                return DynamicAnimation.ROTATION_X;
            case ROTATION_Y:
                return DynamicAnimation.ROTATION_Y;
            case X:
                return DynamicAnimation.X;
            case Y:
                return DynamicAnimation.Y;
            case Z:
                return DynamicAnimation.Z;
            case ALPHA:
                return DynamicAnimation.ALPHA;
            case SCROLL_X:
                return DynamicAnimation.SCROLL_X;
            case SCROLL_Y:
                return DynamicAnimation.SCROLL_Y;
        }
        return null;
    }
}
