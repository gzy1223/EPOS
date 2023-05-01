package com.example.epos.firemapper;

import com.example.epos.common.BaseContext;
import com.example.epos.dto.Staff;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
// 0 is not found
// 1 is found
// 2 is wrong password
public class staffMapper {
    public int staffLogin(Staff staff) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("staff").whereEqualTo("username", staff.getUsername());
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        if (future.get().isEmpty()) {
            return 0;
        } else if (future.get().getDocuments().get(0).get("password").equals(staff.getPassword())){
            return 1;
        } else if (!future.get().getDocuments().get(0).get("password").equals(staff.getPassword())) {
            return 2;
        }
        return 3;
    }
    public static ArrayList findStaffByName(String username) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        Query uidQuery =db.collection("staff");
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        ArrayList<Staff> staffArrayList = new ArrayList<>();
        int size  = future.get().size();
        for (int i = 0; i < size; i++) {
            Staff staff = new Staff();
            staff.setUsername(future.get().getDocuments().get(i).get("username").toString());

            staff.setPassword(future.get().getDocuments().get(i).get("password").toString());
            staff.setUid(Integer.parseInt(future.get().getDocuments().get(i).get("uid").toString()));
            staff.setEmail(future.get().getDocuments().get(i).get("email").toString());
            if (username==null) {
                staffArrayList.add(staff);
            }else {
                if (BaseContext.searchSubString(staff.getUsername(),username)!=0)
                    staffArrayList.add(staff);
            }
        }
        return staffArrayList;
    }
    public static void addStaff(Staff staff) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("staff").document(staff.getUsername()).set(staff);
    }
    //delete staff by uid
    public static int deleteStaff(long uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ArrayList<Staff> staffArrayList = findStaffByName(null);
        for (int i = 0; i < staffArrayList.size(); i++) {
            if (staffArrayList.get(i).getUid()==uid) {
                db.collection("staff").document(staffArrayList.get(i).getUsername()).delete();
                return 1;
            }
        }
        return 0;
    }

    public Staff findStaffById(Long id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query uidQuery =db.collection("staff").whereEqualTo("uid", id);
        ApiFuture<QuerySnapshot> future = uidQuery.get();
        if (future.isDone()) {
            Staff staff = new Staff();
            staff.setUsername(future.get().getDocuments().get(0).get("username").toString());
            staff.setPassword(future.get().getDocuments().get(0).get("password").toString());
            staff.setUid(Integer.parseInt(future.get().getDocuments().get(0).get("uid").toString()));
            staff.setEmail(future.get().getDocuments().get(0).get("email").toString());
            return staff;
        }
        return null;
    }
}
