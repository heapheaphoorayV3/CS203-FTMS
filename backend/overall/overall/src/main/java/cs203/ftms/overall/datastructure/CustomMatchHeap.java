package cs203.ftms.overall.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;

/**
 * Custom data structure that implements a max-heap for managing matches.
 * Matches are organized based on their ID, and the structure provides level-based access and custom sorting.
 */
public class CustomMatchHeap {
    private List<Match> heap;

    /**
     * Constructs an empty CustomMatchHeap.
     */
    public CustomMatchHeap() {
        heap = new ArrayList<>();
    }

    /**
     * Gets the index of the parent of the current node.
     *
     * @param i Index of the current node.
     * @return Index of the parent node.
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Gets the index of the left child of the current node.
     *
     * @param i Index of the current node.
     * @return Index of the left child.
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Gets the index of the right child of the current node.
     *
     * @param i Index of the current node.
     * @return Index of the right child.
     */
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    /**
     * Heapifies up from a given index to maintain the max-heap property.
     *
     * @param i Index to start the heapify-up process.
     */
    private void heapifyUp(int i) {
        while (i != 0 && heap.get(parent(i)).getId() < heap.get(i).getId()) {
            Collections.swap(heap, i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Calculates the depth level of a node in the heap.
     *
     * @param index Index of the node.
     * @return Depth level of the node.
     */
    public int depth(int index) {
        return (int)(Math.log(index + 1) / Math.log(2));
    }

    /**
     * Inserts a new match into the heap, maintaining the max-heap property.
     * Updates match attributes based on its position in the heap.
     *
     * @param match The match to insert.
     * @return The inserted match.
     */
    public Match insert(Match match) {
        heap.add(match);
        int index = heap.size() - 1;
        heapifyUp(index);
        DirectEliminationMatch deMatch = (DirectEliminationMatch) match;
        if (heap.size() > 1) {
            deMatch.setNextMatchId(heap.get(parent(index)).getId());
        }
        deMatch.setRoundOf((int) Math.pow(2, depth(index)+1));
        return match;
    }

    /**
     * Returns the size of the heap.
     *
     * @return The number of elements in the heap.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Prints the heap in level order, displaying each level on a new line.
     */
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

    /**
     * Retrieves a range of matches from the heap.
     *
     * @param start Start index (inclusive).
     * @param end End index (exclusive).
     * @return List of matches within the specified range.
     */
    public List<Match> getRange(int start, int end) {
        return new ArrayList<>(heap.subList(start, end));
    }

    /**
     * Retrieves all matches at a specified depth level in the heap.
     *
     * @param level The depth level to retrieve.
     * @return List of matches at the specified level.
     */
    public List<Match> getLevel(int level) {
        int levelStart = (int)Math.pow(2, level) - 1;
        int levelEnd = Math.min((int)Math.pow(2, level + 1) - 2, heap.size() - 1);
        return new ArrayList<>(heap.subList(levelStart, levelEnd + 1));
    }
}
