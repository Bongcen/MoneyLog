package uph.student.edu.MoneyLog;

public class Records {
    public int     records_ID;
    public String  record_Username;
    public String  category;
    public boolean positive;
    public int     amount;
    public String  description;
    public String  date;

    public Records() {

    }

    public Records(int records_ID, String record_Username, String category, boolean positive, int amount, String description, String date) {
        this.records_ID = records_ID;
        this.record_Username = record_Username;
        this.category = category;
        this.positive = positive;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Records(String record_Username, String category, boolean positive, int amount, String description, String date) {
        this.record_Username = record_Username;
        this.category = category;
        this.positive = positive;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getRecords_ID() {
        return records_ID;
    }

    public void setRecords_ID(int records_ID) {
        this.records_ID = records_ID;
    }

    public String getRecord_Username() {
        return record_Username;
    }

    public void setRecord_Username(String record_Username) {
        this.record_Username = record_Username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Records{" +
                "records_ID=" + records_ID +
                ", record_Username='" + record_Username + '\'' +
                ", category='" + category + '\'' +
                ", positive=" + positive +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
