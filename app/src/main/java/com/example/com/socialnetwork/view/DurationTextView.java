package com.example.com.socialnetwork.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luca on 2/2/2016.
 */
public class DurationTextView extends TemplateTextView {

    public DurationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Updates the text of the view with the new duration, properly formatted
     *
     * @param dateString
     *            The duration in seconds
     */
    public void setDuration(Integer dateString) {

	    Date date = new Date((dateString)*1000L);

	    Date dateNow = new Date();
	    float duration = (dateNow.getTime()-date.getTime())/1000;
        int durationInMinutes = Math.round(duration / 60);
	    int durationInHours = Math.round(durationInMinutes / 60);
	    int days = durationInHours/(24);
        int hours = durationInHours - days*24;
        int minutes = durationInMinutes - hours*60 - days*24*60;


        String hourText = "";
        String minuteText = "";
	    String dayText = "";

		if(days > 0){
			dayText = days + (days == 1 ? " day " : " days ");
		}
        if (hours > 0) {
            hourText = hours + (hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            minuteText = minutes + (minutes == 1 ? " minute" : " minutes");
        }
        if (hours == 0 && minutes == 0 && days ==0) {
            minuteText = "less than 1 minute";
        }
		String temp = dayText.isEmpty() ? hourText+minuteText : dayText+hourText;
        String durationText = String.format(template, temp + " ago");
        setText(Html.fromHtml(durationText), TextView.BufferType.SPANNABLE);
    }
}
