package com.example.lab5;

import android.util.Log;

public class Sorts {
    public static int partition(int[] arr, int low, int high, SortListener listener, CancelCheck cancelCheck) {

        if (cancelCheck.isCancelled()) return high;

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (arr[j] < pivot) {

                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                updateUI(listener, cancelCheck);
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        updateUI(listener, cancelCheck);

        return i + 1;
    }

    public static void quickSort(int[] arr, int low, int high,
                                 SortListener listener,
                                 CancelCheck cancelCheck) {

        if (cancelCheck.isCancelled()) return;

        if (low < high) {

            int pi = partition(arr, low, high, listener, cancelCheck);

            quickSort(arr, low, pi - 1, listener, cancelCheck);
            quickSort(arr, pi + 1, high, listener, cancelCheck);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, SortListener listener, CancelCheck cancelCheck) {

        if (cancelCheck.isCancelled()) return;

        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, left, L, 0, n1);

        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }

            k++;

            updateUI(listener, cancelCheck);
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
            updateUI(listener, cancelCheck);
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
            updateUI(listener, cancelCheck);
        }
    }

    public static void mergeSort(int[] arr, int left, int right, SortListener listener, CancelCheck cancelCheck) {

        if (cancelCheck.isCancelled()) return;

        if (left < right) {

            int mid = (left + right) / 2;

            mergeSort(arr, left, mid, listener, cancelCheck);
            mergeSort(arr, mid + 1, right, listener, cancelCheck);

            merge(arr, left, mid, right, listener, cancelCheck);

            updateUI(listener, cancelCheck);
        }
    }

    private static void updateUI(SortListener listener, CancelCheck cancelCheck) {

        if (cancelCheck.isCancelled()) return;
        listener.onUpdate();

        try {
            Thread.sleep(MainActivity.sortDelay);
        } catch (InterruptedException e) {
            Log.e("SortUpdateInterrupted", e.getMessage());
        }
    }

}
