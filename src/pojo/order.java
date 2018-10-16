package pojo;

public class order {

    private String cusName;
    private String TableNum;
    private String FoodName;
    private String BeverageName;
    private String orderId;
    private String orderStatus;

    @Override
    public String toString() {
        return "order{" +
                "cusName='" + cusName + '\'' +
                ", TableNum='" + TableNum + '\'' +
                ", FoodName='" + FoodName + '\'' +
                ", BeverageName='" + BeverageName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }

    public order() {
    }

    public order(String cusName, String tableNum, String foodName, String beverageName, String orderId, String orderStatus) {
        this.cusName = cusName;
        TableNum = tableNum;
        FoodName = foodName;
        BeverageName = beverageName;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getTableNum() {
        return TableNum;
    }

    public void setTableNum(String tableNum) {
        TableNum = tableNum;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getBeverageName() {
        return BeverageName;
    }

    public void setBeverageName(String beverageName) {
        BeverageName = beverageName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}

