package com.mlp.test.service;

import java.util.*;

/**
 * @Author Bill Tu
 * @Time 2021-06-25
 */
public class BidService {
    public static int[] getUnallottedUsers(int[][] bids, int totalShares) {
        List<Integer> unAllottedUserList = new ArrayList<>();

        List<Bidder> allBidders = new ArrayList<>();
        //<UserId,no.OfShares>
        Map<Integer, Integer> userOriginalSharesMap = new HashMap<>();

        for (int[] bid : bids) {
            Bidder bidder = new Bidder(bid[0], bid[1], bid[2], bid[3]);
            allBidders.add(bidder);
            userOriginalSharesMap.put(bidder.getUserId(), bidder.getNoOfShares());
        }

        //Sort by price, if price equals, then sort by timestamp in desc
        Collections.sort(allBidders);


        for (Bidder bidder : allBidders) {
            if (totalShares <= 0) {
                break;
            } else {
                //Allocate shares.
                int allottedShares = bidder.getNoOfShares() <= totalShares ? bidder.getNoOfShares() : totalShares;
                bidder.setNoOfShares(bidder.getNoOfShares() - allottedShares);
                totalShares -= allottedShares;
            }
        }

        for (Bidder bidder : allBidders) {
            if (bidder.getNoOfShares() == userOriginalSharesMap.get(bidder.getUserId())) {
                //If the no. of shares is not changed, means it's unAllotted.
                unAllottedUserList.add(bidder.getUserId());
            }
        }
        int totalUnAllottedUserIds = unAllottedUserList.size();
        int[] userIds = new int[totalUnAllottedUserIds];
        for (int i = 0; i < totalUnAllottedUserIds; i++) {
            userIds[i] = unAllottedUserList.get(i);
        }

        //Free up memory
        userOriginalSharesMap.clear();
        unAllottedUserList.clear();
        allBidders.clear();
        return userIds;

    }

    static class Bidder implements Comparable<Bidder> {
        private final int userId;
        private final int price;
        private final int timestamp;
        private int noOfShares;

        public Bidder(int userId, int noOfShares, int price, int timestamp) {
            this.userId = userId;
            this.noOfShares = noOfShares;
            this.price = price;
            this.timestamp = timestamp;
        }

        public int getUserId() {
            return userId;
        }

        public int getNoOfShares() {
            return noOfShares;
        }

        public void setNoOfShares(int noOfShares) {
            this.noOfShares = noOfShares;
        }

        public int getPrice() {
            return price;
        }

        public int getTimestamp() {
            return timestamp;
        }


        @Override
        public int compareTo(Bidder o) {
            if (this.getPrice() != o.getPrice()) {
                return o.getPrice() - this.getPrice();
            } else {
                return this.getTimestamp() - o.getTimestamp();
            }
        }
    }

}
