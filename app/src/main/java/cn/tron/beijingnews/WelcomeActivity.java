package cn.tron.beijingnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import cn.tron.beijingnews.activity.GuideActivity;
import cn.tron.beijingnews.activity.MainActivity;
import cn.tron.beijingnews.utils.CacheUtils;

import static cn.tron.beijingnews.R.layout.activity_welcome;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout rl_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_welcome);

        initView();
    }

    // 初始化View
    private void initView() {
        rl_welcome = (RelativeLayout) findViewById(R.id.rl_welcome);

        setAnimation();
    }

    // 设置动画
    private void setAnimation() {
        // 三个动画:旋转动画，渐变动画，缩放动画
        // 旋转动画：旋转中心，页面的中心，旋转度数：0~360
        RotateAnimation ra = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true); //设置停留在旋转后的状态

        // 渐变动画：透明度0~1变大
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        // 缩放动画：大小从0~1变大,缩放中心：界面中心
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        // 创建动画集合，参数表示他的子动画是否共用一个插值器Interpolator
        AnimationSet animationSet = new AnimationSet(false);

        // 添加动画:没有先后顺序
        animationSet.addAnimation(ra);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(sa);

        // 设置插件播放动画
        rl_welcome.startAnimation(animationSet);

        // 监听动画播放完成
        animationSet.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener {

        // 当动画开始播放的时候回调
        @Override
        public void onAnimationStart(Animation animation) {

        }

        // 当动画播放完成的时候回调
        @Override
        public void onAnimationEnd(Animation animation) {
            boolean isOpenMain = CacheUtils.getBoolean(WelcomeActivity.this, "isOpenMain");
            Intent intent = null;
            if (isOpenMain) {
                // 进入主界面
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                // 进入引导界面
                intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            }
            startActivity(intent);
            finish();
        }

        // 当动画重复播放的时候回调
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
