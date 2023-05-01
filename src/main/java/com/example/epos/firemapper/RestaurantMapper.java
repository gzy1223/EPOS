package com.example.epos.firemapper;

import com.example.epos.common.BaseContext;
import com.example.epos.dto.FirebaseRestaurantDto;
import com.example.epos.dto.RestaurantDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RestaurantMapper {
    public void RestaurantLogin() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("sellers").get();
        QuerySnapshot querySnapshot = future.get();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            System.out.println(document.getId() + " => " + document.getData());
        }
    }
    //get the restaurant entity from the firebase
    public static ArrayList getRestaurant(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("sellers");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        ArrayList<RestaurantDto> restaurantDtoArrayList = new ArrayList<>();
        long i = 0;
        for (DocumentSnapshot document : future.get().getDocuments()) {
            RestaurantDto restaurantDto = new RestaurantDto();
            restaurantDto.setName(document.get("sellerName").toString());
            BigDecimal bigDecimal = new BigDecimal(document.get("earnings").toString());
            restaurantDto.setPrice(bigDecimal);
            restaurantDto.setStatus(RestaurantStatus(document.get("status").toString()));
            restaurantDto.setCategoryName(document.get("sellerEmail").toString());
            restaurantDto.setImage(document.get("sellerAvatarUrl").toString());
            restaurantDto.setId(i);
            i++;
            if (name == null)
                restaurantDtoArrayList.add(restaurantDto);
            else if (BaseContext.searchSubString(restaurantDto.getName(),name)!=0){
                restaurantDtoArrayList.add(restaurantDto);
            }
        }
        return restaurantDtoArrayList;
    }
    public static void addRestaurant(RestaurantDto restaurantDto) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        //change RestaurantDto to firebase;
        FirebaseRestaurantDto firebaseRestaurantDto = new FirebaseRestaurantDto();
        firebaseRestaurantDto.setSellerName(restaurantDto.getName());
        firebaseRestaurantDto.setSellerEmail(restaurantDto.getCategoryName());
        firebaseRestaurantDto.setSellerAvatarUrl(restaurantDto.getImage());
        BigDecimal bigDecimal = new BigDecimal("0");
        firebaseRestaurantDto.setEarnings(bigDecimal);
        firebaseRestaurantDto.setStatus("approved");
        UUID sellerUID = UUID.randomUUID();
        firebaseRestaurantDto.setSellerUID(sellerUID.toString());
        db.collection("sellers").document(firebaseRestaurantDto.getSellerUID()).set(firebaseRestaurantDto);
    }
    //change the status of the restaurant
    public static void changeRestaurantStatus(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("sellers");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            if (document.get("sellerName").toString().equals(name)){
                if (document.get("status").toString().equals("approved")){
                    db.collection("sellers").document(document.getId()).update("status","pending");
                }else {
                    db.collection("sellers").document(document.getId()).update("status","approved");
                }
            }
        }
    }
    public static int RestaurantStatus(String status){
        char s = status.charAt(0);
        if(s == 'a'){
            return 1;
        }else {
            return 0;
        }
    }
    public static ArrayList getRestaurantUId(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("sellers");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        ArrayList<String> restaurantDtoArrayList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            restaurantDtoArrayList.add(document.get("sellerUID").toString());
        }
        return restaurantDtoArrayList;
    }
}
