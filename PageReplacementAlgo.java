
package simulation;
import java.util.*;

public class PageReplacementAlgo {

    // FIFO Algorithm
    public static int fifoPageReplacement(int[] pages, int capacity) {
        int pageFaults = 0;
        int pointer = 0;
        int[] memory = new int[capacity];
        Arrays.fill(memory, -1);

        for (int page : pages) {
            boolean isFault = !isHere(memory, page);
            if (isFault) {
                memory[pointer] = page;
                pointer = (pointer + 1) % capacity;
                pageFaults++;
            }
            PageReplacementGUI.displayCycle("FIFO", page, memory, isFault);
        }
 
        return pageFaults;
    }

    // LRU Algorithm
    public static int lruPageReplacement(int[] pages, int capacity) {
        int pageFaults = 0;
        int pointer = 0;
        int[] memory = new int[capacity];
        Arrays.fill(memory, -1);

        Queue<Integer> queue = new LinkedList<>();
        for (int page : pages) {
            boolean isFault = !isHere(memory, page);
            if (isFault) {
                if (queue.size() == capacity) {
                    int lru = queue.poll();
                    memory[indexFinder(memory, lru)] = page;
                } else {
                    memory[pointer] = page;
                    pointer++;
                }
                queue.add(page);
                pageFaults++;
            } else {
                queue.remove(page);
                queue.add(page);
            }
            PageReplacementGUI.displayCycle("LRU", page, memory, isFault);
        }

        return pageFaults;
    }

    // OPTIMAL Algorithm
    public static int optPageReplacement(int[] pages, int capacity) {
        int pageFaults = 0;
        int cycle = 0;
        int pointer = 0;

        int[] memory = new int[capacity];
        Arrays.fill(memory, -1);

        for (int page : pages) {
            boolean isFault = !isHere(memory, page);
            if (isFault) {
                if (pointer >= capacity) {
                    memory[victimIndex(memory, pages, cycle)] = page;
                } else {
                    memory[pointer] = page;
                    pointer++;
                }
                pageFaults++;
            }
            PageReplacementGUI.displayCycle("OPT", page, memory, isFault);
            cycle++;
        }

        return pageFaults;
    }

    // Helper methods
    public static int indexFinder(int[] memory, int target) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isHere(int[] memory, int page) {
        for (int pages : memory) {
            if (pages == page) {
                return true;
            }
        }
        return false;
    }

    public static int victimIndex(int[] memory, int[] page, int cycle) {
        int index = -1;
        int farthest = -1;

        for (int i = 0; i < memory.length; i++) {
            int nextUse = Integer.MAX_VALUE;
            for (int j = cycle + 1; j < page.length; j++) {
                if (page[j] == memory[i]) {
                    nextUse = j;
                    break;
                }
            }

            if (nextUse > farthest) {
                farthest = nextUse;
                index = i;
            }
        }

        return index;
    }
}
