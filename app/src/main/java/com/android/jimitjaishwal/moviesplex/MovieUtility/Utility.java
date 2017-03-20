package com.android.jimitjaishwal.moviesplex.MovieUtility;

import android.content.Context;
import android.util.DisplayMetrics;

import com.android.jimitjaishwal.moviesplex.R;
import com.android.jimitjaishwal.moviesplex.models.Genre;
import com.android.jimitjaishwal.moviesplex.models.MovieDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by JIMIT JAISHWAL on 01-03-2017.
 */

public class Utility {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static String humanReadableDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String fullDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        Date dates = null;
        try {
            dates = format.parse(date);
            long millis = dates.getTime();

            Date date1 = new Date(millis);
            fullDate = dateFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fullDate;
    }

    public static String getGenres(MovieDetail movieDetail) {
        String genreType = "";
        StringBuilder stringBuilder = null;
        ArrayList<String> genreList = new ArrayList<>();

        for (Genre genre : movieDetail.getGenres()) {
            genreType = genre.getName();
            genreList.add(genreType);
        }
        stringBuilder = new StringBuilder(genreList.toString());
        stringBuilder.deleteCharAt(genreList.toString().length() - 1);
        stringBuilder.deleteCharAt(0);
        return stringBuilder.toString();
    }

    public static int colors[] = {
            R.color.red,
            R.color.orange,
            R.color.green,
            R.color.blue
    };
}
