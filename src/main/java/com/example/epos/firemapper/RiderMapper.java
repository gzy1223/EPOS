package com.example.epos.firemapper;

import com.example.epos.common.BaseContext;
import com.example.epos.dto.RiderBelongDto;
import com.example.epos.entity.Rider;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.epos.firemapper.RestaurantMapper.RestaurantStatus;

public class RiderMapper {
    public static ArrayList findRiderByName(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("riders");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        ArrayList<RiderBelongDto> riderArrayList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            RiderBelongDto riderBelongDto = new RiderBelongDto();
            riderBelongDto.setRiderName(document.get("riderName").toString());
            BigDecimal bigDecimal = new BigDecimal(document.get("earnings").toString());
            riderBelongDto.setEarnings(bigDecimal);
            riderBelongDto.setStatus(RestaurantStatus(document.get("status").toString()));
            riderBelongDto.setRiderEmail(document.get("riderEmail").toString());
            riderBelongDto.setRiderAvatarUrl (document.get("riderAvatarUrl").toString());
            //find seller name based on seller uid
            String sellerUID = document.get("belonging").toString();
            if (sellerUID.equals("null")) {
                riderBelongDto.setBelonging("null");
            }else {
                String sellerName = db.collection("sellers").document(sellerUID).get().get().get("sellerName").toString();
                riderBelongDto.setBelonging(sellerName);
            }
            if (name == null)
                riderArrayList.add(riderBelongDto);
            else if (BaseContext.searchSubString(riderBelongDto.getRiderName(),name)!=0){
                riderArrayList.add(riderBelongDto);
            }
        }
        return riderArrayList;
    }
    public static int RiderStatus(String status){
        char s = status.charAt(0);
        if(s == 'a'){
            return 1;
        }else {
            return 0;
        }
    }
    public static void updateBelonging(String riderName,String sellerUID) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        //find which riders document riderName is
        Query uidQuery =db.collection("riders").whereEqualTo("riderName",riderName);
        uidQuery.get().get().getDocuments().forEach(documentSnapshot -> {
            String riderUID = documentSnapshot.getId();
            db.collection("riders").document(riderUID).update("belonging",sellerUID);
        });

    }
}
