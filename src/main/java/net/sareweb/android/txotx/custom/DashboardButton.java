package net.sareweb.android.txotx.custom;

import net.sareweb.android.txotx.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.dashboard_button)
public class DashboardButton extends RelativeLayout {

	private String TAG ="DasboardButton";
	private Context context;
	@ViewById
	ImageView imgButton;
	@ViewById
	TextView txButtonText;
	@ViewById
	TextView txButtonNum;
	Drawable image;
	String buttonText="";
	String buttonNum="";
	String buttonNum2="";

	public DashboardButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		TypedArray attrsArray = context.obtainStyledAttributes(attrs,
                R.styleable.DashboardButton);
		image  = attrsArray.getDrawable(R.styleable.DashboardButton_imgResId); 
		buttonText = attrsArray.getString(R.styleable.DashboardButton_buttonText);
		buttonNum = attrsArray.getString(R.styleable.DashboardButton_buttonNum);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		txButtonText.setText(buttonText);
		imgButton.setImageDrawable(image);
		if(buttonNum==null || buttonNum.equals("")){
			txButtonNum.setVisibility(GONE);
		}
		else{
			txButtonNum.setText(buttonNum);	
		}
	}
	
	public void setButtonNum(String buttonNum){
		this.buttonNum = buttonNum;
	}
}
