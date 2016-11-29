package com.boopi.dynamiclayout;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shanmuga-3787 on 28/11/16.
 */

public class DynamicLayout extends RelativeLayout {

    private static final String TAG = DynamicLayout.class.getName();

    private final String TAG_INTERNET_OFF 	 =  "INTERNET_OFF";
    private final String TAG_LOADING_CONTENT =  "LOADING_CONTENT";
    private final String TAG_OTHER_EXCEPTION =  "OTHER_EXCEPTION";

    private final RelativeLayout mContainer;
    private Context mContext;
    private LayoutInflater mInflater;

    private RelativeLayout targetContainer;
    private View targetView;
    private OnClickListener mClickListener;

    private ArrayList<View> mDefaultViews = new ArrayList<>();
    private ArrayList<View> mCustomViews = new ArrayList<>();

    public DynamicLayout(Context context) {
        this(context, null);
    }

    public DynamicLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DynamicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        mInflater = LayoutInflater.from(context);

        mContainer 	= new RelativeLayout(mContext);
        targetContainer = new RelativeLayout(mContext);

        initLayout();
    }

    private void initLayout() {
        setDefaultViews();

        mContainer.addView(targetContainer, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

        if (childCount != 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        targetView = getChildAt(0);
        if (!(targetView.equals(mContainer))) {
            int index = 0;
            ViewGroup parent = (ViewGroup) targetView.getParent();
            if (parent != null) {
                index = parent.indexOfChild(targetView);
                parent.removeView(targetView);
            }

            targetContainer.addView(targetView);

            if (parent != null) {
                parent.addView(mContainer, index);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void setDefaultViews() {
        View mLayoutInternetOff = initView(com.boopi.dynamiclayout.R.layout.exception_no_internet,TAG_INTERNET_OFF);
        View mLayoutLoadingContent = initView(com.boopi.dynamiclayout.R.layout.exception_loading_content,TAG_LOADING_CONTENT);
        View mLayoutOther = initView(com.boopi.dynamiclayout.R.layout.exception_failure,TAG_OTHER_EXCEPTION);

        mDefaultViews.add(0,mLayoutInternetOff);
        mDefaultViews.add(1,mLayoutLoadingContent);
        mDefaultViews.add(2,mLayoutOther);

        // Hide all layouts at first initialization
        mLayoutInternetOff.setVisibility(View.GONE);
        mLayoutLoadingContent.setVisibility(View.GONE);
        mLayoutOther.setVisibility(View.GONE);

        // init Layout params
        LayoutParams containerParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        containerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        containerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        // init new RelativeLayout Wrapper
        mContainer.setLayoutParams(containerParams);

        mContainer.addView(mLayoutInternetOff);
        mContainer.addView(mLayoutLoadingContent);
        mContainer.addView(mLayoutOther);
    }

    /**
     * Return a view based on layout id
     * @param layout Layout Id
     * @param tag Layout Tag
     * @return View
     */
    private View initView(int layout, String tag){
        View view = mInflater.inflate(layout, null,false);

        view.setTag(tag);
        view.setVisibility(View.GONE);

        View buttonView = view.findViewById(R.id.exception_button);
        if(buttonView!=null)
            buttonView.setOnClickListener(this.mClickListener);

        return view;
    }

    public void showLoadingLayout(boolean hideTargetContainer){
        show(TAG_LOADING_CONTENT, hideTargetContainer);
    }

    public void showInternetOffLayout(boolean hideTargetContainer){
        show(TAG_INTERNET_OFF, hideTargetContainer);
    }

    public void showExceptionLayout(boolean hideTargetContainer){
        show(TAG_OTHER_EXCEPTION, hideTargetContainer);
    }

    public void showCustomView(String tag, boolean hideTargetContainer){
        show(tag, hideTargetContainer);
    }

    public void showInternetOffLayout(String message, boolean hideTargetContainer) {
        show(TAG_INTERNET_OFF, message, hideTargetContainer);
    }

    public void addCustomView(View customView,String tag){
        customView.setTag(tag);
        customView.setVisibility(View.GONE);
        mCustomViews.add(customView);
        mContainer.addView(customView);
    }

    public void hideAll(){
        ArrayList<View> views =  new ArrayList<>(mDefaultViews);
        views.addAll(mCustomViews);
        for(View view : views){
            view.setVisibility(View.GONE);
        }
        targetContainer.setVisibility(View.VISIBLE);
    }

    private void show(String tag, boolean hideTargetContainer) {
        show(tag, null, hideTargetContainer);
    }

    private void show(String tag, String message, boolean hideTargetContainer){
        ArrayList<View> views =  new ArrayList<>(mDefaultViews);
        views.addAll(mCustomViews);
        for(View view : views){
            if(view.getTag()!=null && view.getTag().toString().equals(tag)){
                if (!TextUtils.isEmpty(message)) {
                    TextView textView = (TextView) view.findViewById(R.id.exception_message);
                    if (textView != null) {
                        textView.setText(message);
                    }
                }
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }
        if (hideTargetContainer) {
            targetContainer.setVisibility(View.GONE);
        }
    }

    public void setClickListener(OnClickListener clickListener){

        this.mClickListener = clickListener;

        for(View view : mDefaultViews){
            View buttonView = view.findViewById(R.id.exception_button);
            if(buttonView!=null)
                buttonView.setOnClickListener(this.mClickListener);
        }
    }
}
