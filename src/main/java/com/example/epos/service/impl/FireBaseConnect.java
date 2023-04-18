package com.example.epos.service.impl;

import com.example.epos.entity.Orders;
import com.example.epos.service.OrderService;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FireBaseConnect {
    private static final String MYSQL_CONNECTION_URL = "jdbc:mysql://localhost:3306/epos";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PASSWORD = "123456";
    @Autowired
    private OrderService orderService;
    @PostConstruct
    public void initialize() throws IOException, ExecutionException, InterruptedException {
            FileInputStream serviceAccount = new FileInputStream("C:/Users/zhanyu guo/Desktop/Sendout/EPOS/src/main/java/com/example/epos/service/impl/serviceAccount.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);
    }
    public void test() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection("riders").document("lotfiidiot");
// Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
//asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
// ...
// result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

    public void fetchData() throws ExecutionException, InterruptedException {
            Firestore db = FirestoreClient.getFirestore();
            // asynchronously retrieve all users
            ApiFuture<QuerySnapshot> query = db.collection("orders").get();
// ...
// query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            Orders orders = new Orders();
            for (QueryDocumentSnapshot document : documents) {
                orders.setUserId(Long.parseLong(document.getId()));
                orders.setOrderTime(LocalDateTime.now());
                orders.setAddressBookId(Long.parseLong(document.getString("addressID")));
                orders.setCheckoutTime(LocalDateTime.now());
                orders.setAmount(BigDecimal.valueOf(document.getLong("totalAmount")));
                orderService.save(orders);
                System.out.println("Orders " + (String)(document.getId()));
                System.out.println("Orders " + document.getCreateTime());
                System.out.println("Order time: " + document.getString("orderTime"));
                System.out.println("Order Id: " + document.getString("orderId"));
                System.out.println("PaidAmount: " + document.getLong("totalAmount"));
                System.out.println("Status" + document.getString("status"));
                System.out.println("riderUID" + document.getString("riderUID"));
            }
        }
//    public void transferData()
//    {
//        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Connection mysqlConnection = null;
//                try {
//                    mysqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_URL,MYSQL_USERNAME,MYSQL_PASSWORD);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//                for (DataSnapshot childSnapshot:dataSnapshot.getChildren())
//                {
//                    String key = childSnapshot.getKey();
//                    String value = childSnapshot.getValue().toString();
//                    try {
//                        String sql = "INSERT INTO firebase_data (key, value) VALUESS (?,?)";
//                        PreparedStatement statement = mysqlConnection.prepareStatement(sql);
//                        statement.setString(1,key);
//                        statement.setString(2,value);
//                        statement.executeUpdate();
//                    }catch (SQLException e)
//                    {
//                        System.out.println("Error");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("Error");
//            }
//        });
//    }

}
