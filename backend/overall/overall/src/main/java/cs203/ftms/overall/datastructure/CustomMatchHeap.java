package cs203.ftms.overall.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;

public class CustomMatchHeap {
    private List<Match> heap;

    public CustomMatchHeap() {
        heap = new ArrayList<>();
    }

    // Get the index of the parent
    private int parent(int i) {
        return (i - 1) / 2;
    }

    // Get the index of the left child
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    // Get the index of the right child
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Heapify up to maintain the min-heap property
    private void heapifyUp(int i) {
        while (i != 0 && heap.get(parent(i)).getId() < heap.get(i).getId()) {
            Collections.swap(heap, i, parent(i));
            i = parent(i);
        }
    }

    public int depth(int index) {
        return (int)(Math.log(index + 1) / Math.log(2));
    }

    // Insert a new match into the heap
    public Match insert(Match match) {
        heap.add(match);
        int index = heap.size() - 1;
        heapifyUp(index);
        // sortCurrentLevel(index);
        DirectEliminationMatch deMatch = (DirectEliminationMatch) match;
        if (heap.size() > 1) {
            deMatch.setNextMatchId(heap.get(parent(index)).getId());
        }
        deMatch.setRoundOf((int) Math.pow(2, depth(index)+1));
        return match;
    }

    // Sort the current level after an insertion
    // private void sortCurrentLevel(int index) {
    //     int level = depth(index);                               // Level of the current element
    //     int levelStart = (int)Math.pow(2, level) - 1;         // Start of current level
    //     int levelEnd = Math.min((int)Math.pow(2, level + 1) - 2, heap.size() - 1); // End of current level

    //     // Sort matches by their id within the level
    //     List<Match> levelElements = new ArrayList<>(heap.subList(levelStart, levelEnd + 1));
    //     Collections.sort(levelElements, (m1, m2) -> Integer.compare(m1.getId(), m2.getId()));

    //     // Replace the current level with the sorted elements
    //     for (int i = 0; i < levelElements.size(); i++) {
    //         heap.set(levelStart + i, levelElements.get(i));
    //     }
    // }

    // Heapify down to maintain the min-heap property
    private void heapifyDown(int i) {
        int smallest = i;
        int left = leftChild(i);
        int right = rightChild(i);

        if (left > heap.size() && heap.get(left).getId() > heap.get(smallest).getId()) {
            smallest = left;
        }

        if (right > heap.size() && heap.get(right).getId() > heap.get(smallest).getId()) {
            smallest = right;
        }

        if (smallest != i) {
            Collections.swap(heap, i, smallest);
            heapifyDown(smallest);
        }
    }

    // Return the size of the heap
    public int size() {
        return heap.size();
    }

     // Print the heap in level order
    public void printHeap() {
        int level = 0;
        int levelCount = 1;
        for (int i = 0; i < heap.size(); i++) {
            System.out.print(heap.get(i).getId() + " ");
            if (i + 1 == levelCount) {
                System.out.println();
                level++;
                levelCount += Math.pow(2, level);
            }
        }
    }

    // Get match by range
    public List<Match> getRange(int start, int end) {
        return new ArrayList<>(heap.subList(start, end));
    }

    // Get all the matches of a level
    public List<Match> getLevel(int level) {
        int levelStart = (int)Math.pow(2, level) - 1;
        int levelEnd = Math.min((int)Math.pow(2, level + 1) - 2, heap.size() - 1);
        return new ArrayList<>(heap.subList(levelStart, levelEnd + 1));
    }
}
