package com.example.comlier.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liangjianhua on 2018/1/8.
 */

public class IDUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {

        while (true) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the
            // range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) {
                // Roll over to 1, not 0.
                newValue = 1;
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }

    }
}
