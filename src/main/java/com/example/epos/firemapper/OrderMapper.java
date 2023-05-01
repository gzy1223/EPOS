package com.example.epos.firemapper;

import com.example.epos.common.BaseContext;
import com.example.epos.dto.OrderDto;
import com.example.epos.dto.Staff;
import com.example.epos.entity.Bill;
import com.example.epos.entity.Orders;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class OrderMapper {
    //fetch order from firebase
    public static ArrayList getOrders(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("orders");
        ApiFuture<QuerySnapshot> future = uidQuery.get();

        ArrayList<Orders> orderArrayList = new ArrayList<>();
        //control query speed;
        int size  = future.get().size();
        if (size>10){
            size = 6;
        }
        for (int i = 0;i<size;i++){
            Orders orders = new Orders();
            String orderTimeStamp = future.get().getDocuments().get(i).get("orderTime").toString();
            orders.setOrderTime(BaseContext.Timestamp2LocalDateTime(orderTimeStamp));
            orders.setAmount(BigDecimal.valueOf(Double.parseDouble(future.get().getDocuments().get(i).get("totalAmount").toString())));
            orders.setNumber(future.get().getDocuments().get(i).get("orderId").toString());
            orders.setAddress(getSellerName(future.get().getDocuments().get(i).get("sellerUID").toString()));
            String riderUID = future.get().getDocuments().get(i).get("riderUID").toString();
            if (riderUID.equals("")) {
                orders.setPhone("Not Assigned");
            }else {
                orders.setPhone(getRiderName(future.get().getDocuments().get(i).get("riderUID").toString()));
            }
            orders.setUserName(getUserName(future.get().getDocuments().get(i).get("orderBy").toString()));
            orders.setStatus(getOrderStatus(future.get().getDocuments().get(i).get("paymentDetails").toString()));
            if (id == null) {
                orderArrayList.add(orders);
            }else{
                if(BaseContext.searchSubString(orders.getNumber(),id)!=0){
                    orderArrayList.add(orders);
                }
            }
        }
        return orderArrayList;
    }
    //fetch the seller name from the order seller user name
    public static String getSellerName(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("sellers").whereEqualTo("sellerUID",uid);
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        String sellerName = future.get().getDocuments().get(0).get("sellerName").toString();
        return sellerName;
    }
    //fetch the rider name from the order riderUID
    public static String getRiderName(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("riders").whereEqualTo("riderUID",uid);
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        String riderName = future.get().getDocuments().get(0).get("riderName").toString();
        return riderName;
    }
    //fetch the user name from the order orderby
    public static String getUserName(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("users").whereEqualTo("uid",uid);
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        String userName = future.get().getDocuments().get(0).get("name").toString();
        return userName;
    }
    // return the order status 0 or 1 should put in service layer
    public static int getOrderStatus(String payment) {
        char c = payment.charAt(0);
        if (c == 'C'){
            return 1;
        }else{
            return 2;
        }
    }
    public static void updateOrderStatus(Orders orders) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("orders").whereEqualTo("orderId",orders.getNumber());
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        String orderID = future.get().getDocuments().get(0).getId();
        if (orders.getStatus()==1){
            db.collection("orders").document(orderID).update("paymentDetails","Online Payment");
        }else {
            db.collection("orders").document(orderID).update("paymentDetails", "Cash on Delivery");
        }
    }
    public static ArrayList getOrderwithinOneweek() throws ExecutionException, InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusDays(60);// change to billing to one week
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("orders");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        ArrayList<Bill> BillArrayList = new ArrayList<>();
        int size  = future.get().size();
        for (int i = 0;i<size;i++){
            Bill bill = new Bill();
            String orderTimeStamp = future.get().getDocuments().get(i).get("orderTime").toString();
            LocalDateTime orderTime = BaseContext.Timestamp2LocalDateTime(orderTimeStamp);
            if (orderTime.isAfter(oneWeekAgo)&& orderTime.isBefore(now)){
                bill.setOrderTime(String.valueOf(orderTime));
                Long totalAmount = 5L;
                bill.setTotalAmount(totalAmount);
                bill.setTitle(future.get().getDocuments().get(i).get("orderId").toString());
                bill.setSubtitle("Invoice");
                bill.setPaymentDetails(future.get().getDocuments().get(i).get("paymentDetails").toString());
                ArrayList<String> productNames = new ArrayList<>();
                productNames = (ArrayList<String>) future.get().getDocuments().get(i).get("productIDs");
                bill.setProductNames(productNames);
                bill.setRestaurantName(getSellerName(future.get().getDocuments().get(i).get("sellerUID").toString()));
                BillArrayList.add(bill);
            }else {
                break;
            }

        }
        return BillArrayList;
    }
}