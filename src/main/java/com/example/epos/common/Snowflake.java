package com.example.epos.common;
public class Snowflake {

    private static final long EPOCH = 1682290197L; // Sunday, April 23, 2023 11:49:57 PM

    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final int MAX_NODE_ID = (1 << NODE_ID_BITS) - 1;
    private static final int MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;

    private static int nodeId = 0;
    private final int datacenterId;

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;

    public Snowflake(int nodeId, int datacenterId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + MAX_NODE_ID);
        }

        this.nodeId = nodeId;
        this.datacenterId = datacenterId;
    }

    public static synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Time move reverse");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << (NODE_ID_BITS + SEQUENCE_BITS))
                | (nodeId << SEQUENCE_BITS)
                | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}

