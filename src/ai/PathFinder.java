package ai;

import entity.Entity;
import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes() {

        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            node[col][row] = new Node(col,row);

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void resetNodes() {

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // Reset open, checked and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        resetNodes();

        // Set Start and Goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // SET SOLID NODE
            // CHECK TILES
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if(gp.tileM.tile[tileNum].collision) {

                node[col][row].solid = true; // set tiles with collision true as solid objects
            }
            // CHECK INTERACTIVE TILES
            for(int i = 0; i < gp.iTile[1].length; i++) {
                if(gp.iTile[gp.currentMap][i]!= null && gp.iTile[gp.currentMap][i].destructible) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX/gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY/gp.tileSize;
                    node[itCol][itRow].solid = true;
                }
            }
            // SET COST
            getCost(node[col][row]);

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
            }
        }
    }
    public void getCost(Node node) {
        // GET G COST OF NODE
        int xDistance = Math.abs(node.col - startNode.col); // x distance from start node to current
        int yDistance = Math.abs(node.row - startNode.row); // y distance from start node to current
        node.gCost = xDistance + yDistance;

        // GET H COST OF NODE
        xDistance = Math.abs(node.col - goalNode.col); // x distance from start node to current
        yDistance = Math.abs(node.row - goalNode.row); // y distance from start node to current
        node.hCost = xDistance + yDistance;

        // GET F COST
        node.fCost = node.gCost + node.hCost;
    }
    public boolean search() {

        while(!goalReached && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                // OPEN THE UP NODE
                openNode(node[col][row - 1]);
            }
            if (col - 1 >= 0) {
                // OPEN THE LEFT NODE
                openNode(node[col - 1][row]);
            }
            if (row + 1 < gp.maxWorldRow) {
                // OPEN THE DOWN NODE
                openNode(node[col][row + 1]);
            }
            if (col + 1 < gp.maxWorldCol) {
                // OPEN THE RIGHT NODE
                openNode(node[col + 1][row]);
            }

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {

                //Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //If F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            // If there s no node in the openList, end the loop
            if(openList.size() == 0) {
                break;
            }

            // After the loop, we get the best node which is our next step
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }
    private void openNode(Node node) {

        if(!node.open && !node.checked && !node.solid) {

            // If the node is not opened yet, add it to the open list
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    private void trackThePath() {

        // Backtrack and draw the best path
        Node current = goalNode;

        while(current != startNode) {

            pathList.add(0,current);
            current = current.parent;
        }
    }
}

