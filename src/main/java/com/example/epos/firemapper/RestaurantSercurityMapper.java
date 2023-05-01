package com.example.epos.firemapper;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

public class RestaurantSercurityMapper {
    public void changePassword(String uid, String password) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("sellers").document(uid).update("password", password);
    }
    public void findEmail(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("sellers").whereEqualTo("sellerEmail", email);
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        if (future.get().isEmpty()) {
            System.out.println("No such email");
        } else {
            for (DocumentSnapshot document : future.get().getDocuments()) {
                System.out.println(document.getId() + " => " + document.getData());
            }
        }


    }
}
