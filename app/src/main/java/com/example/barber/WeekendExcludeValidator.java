package com.example.barber;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.Calendar;
import java.util.TimeZone;

public class WeekendExcludeValidator implements CalendarConstraints.DateValidator {

    public static final Parcelable.Creator<WeekendExcludeValidator> CREATOR = new Parcelable.Creator<WeekendExcludeValidator>() {
        public WeekendExcludeValidator createFromParcel(Parcel source) {
            return new WeekendExcludeValidator();
        }

        public WeekendExcludeValidator[] newArray(int size) {
            return new WeekendExcludeValidator[size];
        }
    };

    @Override
    public boolean isValid(long date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek != Calendar.SUNDAY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // no-op
    }
}
