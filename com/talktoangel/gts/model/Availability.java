package com.talktoangel.gts.model;

public class Availability {
    private String available_date;
    private String e_end_time;
    private String e_start_time;
    private String m_end_time;
    private String m_start_time;

    public String getAvailable_date() {
        return this.available_date;
    }

    public void setAvailable_date(String available_date) {
        this.available_date = available_date;
    }

    public String getM_start_time() {
        return this.m_start_time;
    }

    public void setM_start_time(String m_start_time) {
        this.m_start_time = m_start_time;
    }

    public String getM_end_time() {
        return this.m_end_time;
    }

    public void setM_end_time(String m_end_time) {
        this.m_end_time = m_end_time;
    }

    public String getE_start_time() {
        return this.e_start_time;
    }

    public void setE_start_time(String e_start_time) {
        this.e_start_time = e_start_time;
    }

    public String getE_end_time() {
        return this.e_end_time;
    }

    public void setE_end_time(String e_end_time) {
        this.e_end_time = e_end_time;
    }
}
