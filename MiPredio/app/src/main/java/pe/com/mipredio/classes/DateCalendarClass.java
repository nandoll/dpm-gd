package pe.com.mipredio.classes;


import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import pe.com.mipredio.R;
import pe.com.mipredio.utils.Tools;

public class DateCalendarClass {

    private Integer _day;
    private Integer _month;
    private Integer _year;
    private String textMonthYear;
    private String textDay;
    private String _fecha;
    private Context context;
    private DatePickerDialog picker;

    public DateCalendarClass(Context context) {
        Calendar calendar = Calendar.getInstance();
        this.set_day(calendar.get(Calendar.DAY_OF_MONTH));
        this.set_month(calendar.get(Calendar.MONTH) + 1);
        this.set_year(calendar.get(Calendar.YEAR));
        this.setContext(context);
        setFormat();
    }

    public void setFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.get_year());
        calendar.set(Calendar.MONTH, this.get_month() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, this.get_day());
        long date_ship_millis = calendar.getTimeInMillis();
        this.setTextMonthYear(Tools.getFormatDate(date_ship_millis));
        this.setTextDay(Tools.getDateFullDayName(date_ship_millis));
        String fecha = this.get_year().toString().concat("-").concat(String.format("%02d", this.get_month()).concat("-").concat(String.format("%02d", this.get_day())));
        this.set_fecha(fecha);
    }


    public void calendarPicker() {
        this.picker = new DatePickerDialog(context, R.style.DatePickerThemeLight, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //set_year(year);
                //set_month(month + 1);
                //set_day(dayOfMonth);
                //setFormat();
            }
        }, this.get_year(), this.get_month() - 1, this.get_day());

        this.picker.show();

    }



    public DatePickerDialog getPicker() {
        return picker;
    }

    public void setPicker(DatePickerDialog picker) {
        this.picker = picker;
    }

    public String get_fecha() {
        return _fecha;
    }

    public void set_fecha(String _fecha) {
        this._fecha = _fecha;
    }

    public String getTextMonthYear() {
        return textMonthYear;
    }

    public void setTextMonthYear(String textMonthYear) {
        this.textMonthYear = textMonthYear;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Integer get_day() {
        return _day;
    }

    public void set_day(Integer _day) {
        this._day = _day;
    }

    public Integer get_month() {
        return _month;
    }

    public void set_month(Integer _month) {
        this._month = _month;
    }

    public Integer get_year() {
        return _year;
    }

    public void set_year(Integer _year) {
        this._year = _year;
    }

}