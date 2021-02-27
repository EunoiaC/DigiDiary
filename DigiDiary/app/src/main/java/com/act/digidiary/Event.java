package com.act.digidiary;

import android.provider.BaseColumns;

public class Event {

    private Event(){}

    public static final class EventEntry implements BaseColumns{
        public static final String TABLE_NAME = "eventlist";
        public static final String COLUMN_EVENT = "event";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";


    }
}
