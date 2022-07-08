package com.example.stockscreener;

public class Ticker {

    private String ticker;
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;
    private double closeAdj;
    private double closeUnAdj;
    private String lastUpdated;

    public Ticker(String ticker, String date, double open, double high ,double low, double close,
                  int volume, double closeAdj, double closeUnAdj, String lastUpdated){

        this.ticker =  ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.closeAdj = closeAdj;
        this.closeUnAdj = closeUnAdj;
        this.lastUpdated = lastUpdated;
    }

    public void setTicker(String ticker){this.ticker = ticker;}

    public String getTicker(){return ticker;}

    public void  setDate(String date){this.date = date;}

    public String getDate(){return date;}

    public void setOpen(double open){ this.open = open; }

    public double getOpen(){ return open;}

    public void setHigh(double high){ this.high = high; }

    public double getHigh(){ return this.high; }

    public void setLow(double low){this.low = low; }

    public double getLow(){ return low; }

    public void setClose(double close) { this.close = close; }

    public double getClose() { return close;}

    public void setVolume(int volume) { this.volume = volume; }

    public double getVolume() { return volume; }

    public void setCloseAdj(double closeAdj) { this.closeAdj = closeAdj; }

    public double getCloseAdj() { return closeAdj; }

    public void setCloseUnAdj(double closeUnAdj) { this.closeUnAdj = closeUnAdj; }

    public double getCloseUnAdj() { return closeUnAdj; }

    public void setLastUpdated (String lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getLastUpdated() { return lastUpdated; }
}
