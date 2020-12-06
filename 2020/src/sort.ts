/**
 * Note picking bad pivots leads you to approach O(n^2) 
 */
export function quickSort(arr: number[], left = 0, right = arr.length - 1): number[] {
    // Base case is that the left and right pointers don't overlap, after which we'll be left with an array of 1 item
    if (left < right) {
        const pivotIndex = partition(arr, left, right);
      
        // For left subarray, which is everything to the left of the pivot element
        quickSort(arr, left, pivotIndex - 1);
      
        // For the right sub array, which is everything to the right of the pivot element
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        quickSort(arr, pivotIndex + 1, right);
    }
    // Return the array, when it's of length 1 i.e, left === right
    return arr;
}

// This is the step in quickSort that returns the index for the pivot element:
function partition(arr: number[], start = 0, end = arr.length - 1): number {
    // Let's choose the pivot to be the arr[start] element
    // Note that "pivot" simply notes the item that we want to find the position of, arbitrarily
    const pivot = arr[start];
    let swapIdx = start;

    for (let i = start + 1; i <= end; i++) {
        if (arr[i] < pivot) {
            swapIdx++;
            // Swap current element with the element at the new pivot index
            [arr[i], arr[swapIdx]] = [arr[swapIdx], arr[i]];
        }
    }
  
    // Swap the pivot element with the element at the pivotIndex index
    [arr[swapIdx], arr[start]] = [arr[start], arr[swapIdx]];
  
    // Return the index of the pivot element after swapping
    return swapIdx;
}