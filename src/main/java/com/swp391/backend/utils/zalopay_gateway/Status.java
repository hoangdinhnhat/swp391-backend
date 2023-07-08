package com.swp391.backend.utils.zalopay_gateway;

public class Status {
    private int return_code;
    private String return_message;
    private String sub_return_message;
    private boolean is_processing;
    private long amount;
    private long zp_trans_id;

    public Status() {
    }

    public int getReturn_code() {
        return return_code;
    }

    public void setReturn_code(int return_code) {
        this.return_code = return_code;
    }

    public String getReturn_message() {
        return return_message;
    }

    public void setReturn_message(String return_message) {
        this.return_message = return_message;
    }

    public String getSub_return_message() {
        return sub_return_message;
    }

    public void setSub_return_message(String sub_return_message) {
        this.sub_return_message = sub_return_message;
    }

    public boolean isIs_processing() {
        return is_processing;
    }

    public void setIs_processing(boolean is_processing) {
        this.is_processing = is_processing;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getZp_trans_id() {
        return zp_trans_id;
    }

    public void setZp_trans_id(long zp_trans_id) {
        this.zp_trans_id = zp_trans_id;
    }
}