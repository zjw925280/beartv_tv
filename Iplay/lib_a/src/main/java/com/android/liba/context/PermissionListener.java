package com.android.liba.context;

import java.util.List;

public interface PermissionListener {
    void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList);
}
