package utot.utot.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

import utot.utot.R;

/**
 * Created by elysi on 12/14/2016.
 */

public class EdittextPlus extends AppCompatEditText {


    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public EdittextPlus(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EdittextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public EdittextPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        this.defStyle = defStyle;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.edittext_font));
        this.setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.edittext_font));
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.edittext_font));
        super.setTypeface(tf);
    }
}