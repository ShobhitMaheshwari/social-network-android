package com.example.com.socialnetwork.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.com.socialnetwork.R;

/**
 * Created by luca on 2/2/2016.
 */
public class TemplateTextView extends TextView {

    protected String template;

    public TemplateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TemplateTextView);
        template = attributes.getString(R.styleable.TemplateTextView_template);
        if (template == null || !template.contains("%s")) {
            template = "%s";
        }
        attributes.recycle();
    }

}
